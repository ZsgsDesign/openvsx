// @ts-check
/********************************************************************************
 * Copyright (c) 2022 Gitpod and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/

import { parseStringPromise } from 'xml2js';
// TODO(ak) get rid of it, use http(s) modules
import fetch from 'node-fetch';
import * as fs from 'fs';
import * as os from 'os';
import * as path from 'path';
import { Extension, Registry } from "./registry";
import { matchExtensionId } from './util';
import { download } from './get';


const SITEMAP_URL = 'https://open-vsx.org/sitemap.xml';
const VSIX_DOWNLOAD_DIR = path.join(os.tmpdir(), 'crawl-open-vsx');
const OVSX_CLI_ENV = {
    ...process.env,
    OVSX_REGISTRY_URL: 'http://localhost:8080',
    OVSX_PAT: 'super_token'
};

const upstream = new Registry({
    registryUrl: 'https://open-vsx.org'
});
const registry = new Registry({
    registryUrl: OVSX_CLI_ENV.OVSX_REGISTRY_URL,
    pat: OVSX_CLI_ENV.OVSX_PAT
});

async function timeout(millis: number) {
    return new Promise((resolve) => setTimeout(resolve, millis));
}

async function retry<T>(task: () => Promise<T>, delay: number, retries: number): Promise<T> {
    let lastError;

    for (let i = 0; i < retries; i++) {
        try {
            return await task();
        } catch (error) {
            lastError = error;

            await timeout(delay);
        }
    }

    throw lastError;
}

async function fetchSitemapContent(): Promise<string> {
    try {
        return retry(async () => {
            const controller = new AbortController();
            setTimeout(() => controller.abort(), 15000);
            const resp = await fetch(SITEMAP_URL, {
                signal: controller.signal
            });

            if (!resp.ok) {
                throw new Error(`Response with ${resp.status} ${resp.statusText}`);
            }

            return await resp.text();
        }, 1000, 3);
    } catch (e) {
        if (e.name == 'AbortError') {
            throw new Error(`Timeout while fetching ${SITEMAP_URL}: ${e}`);
            console.error();
        }
        throw new Error(`Failed to fetch ${SITEMAP_URL}: ${e}`);
    }
}

async function parseSitemapData(xmlData: Object): Promise<{ date: string; extId: string; }[]> {
    const URL_PREFIX = 'https://open-vsx.org/extension/';

    const parsedXml = await parseStringPromise(xmlData);
    return parsedXml.urlset.url.map((entry: any) => {
        const url = entry.loc[0];
        const date = entry.lastmod[0];
        const extId = url.substring(URL_PREFIX.length).replace('/', '.');
        return { date, extId };
    });
}

const extensions = new Set();
const namespaces = new Set();
async function fetchAndPublishExtension(extensionId: string) {
    if (extensions.has(extensionId)) {
        return;
    }
    extensions.add(extensionId);

    const match = matchExtensionId(extensionId);
    if (!match) {
        console.error(`❌ Failed to resolve extension ${extensionId}: The extension identifier must have the form 'namespace.extension'.`);
        return;
    }

    let extension: Extension;
    try {
        extension = await upstream.getMetadata(match[1], match[2]);
        if (extension.error) {
            throw new Error(extension.error);
        }
    } catch (e) {
        console.error(`❌ Failed to resolve extension ${extensionId}:`, e);
        return;
    }
    if (extension.dependencies) {
        await fetchAndPublishExtensions(extension.dependencies.map(dep => `${dep.namespace}.${dep.extension}`));
    }

    let published: Extension | undefined;
    try {
        published = await registry.getMetadata(match[1], match[2]);
        if (published.error) {
            throw new Error(published.error);
        }
    } catch (e) {
        console.error(`Failed to resolve extension ${extensionId} from ${registry.url}:`, e);
    }
    // TODO(ak) how to deal with removed extensions?
    for (const version of Object.keys(extension.allVersions).reverse()) {
        if (extension.versionAlias.includes(version)) {
            continue;
        }
        if (published?.allVersions[version]) {
            continue;
        }
        await fetchAndPublishExtensionVersion(extensionId, version, extension.allVersions[version], published);
    }
}

async function fetchAndPublishExtensionVersion(extensionId: string, version: string, versionUrl: string, published: Extension | undefined) {
    let extension: Extension;
    try {
        extension = await upstream.getJson(new URL(versionUrl));
        if (extension.error) {
            throw new Error(extension.error);
        }
    } catch (e) {
        console.error(`❌ Failed to resolve extension ${extensionId}@${version}:`, e);
        return;
    }
    // resolving alias i.e. preview
    version = extension.version;
    const downloadUrl = extension.files.download;
    if (!downloadUrl) {
        console.error(`❌ Failed to download extension ${extensionId}@${version}: download url is missing.`);
        return;
    }
    if (published?.allVersions[version]) {
        console.debug(`✅ Skipping, ${extensionId}@${version} already published.`);
        return;
    }
    try {
        let filePath;
        try {
            fs.mkdirSync(VSIX_DOWNLOAD_DIR, { recursive: true });
            filePath = await download(upstream, extension, VSIX_DOWNLOAD_DIR);
        } catch (e) {
            console.error(`❌ Failed to download extension ${extensionId}@${extension.version}:`, e);
            return;
        }
        if (!namespaces.has(extension.namespace)) {
            namespaces.add(extension.namespace);
            try {
                await registry.createNamespace(extension.namespace, OVSX_CLI_ENV.OVSX_PAT);
            } catch (e) {
                if (!/Namespace already exists:/g.test(String(e))) {
                    console.error(`Failed to create namespace ${extension.namespace}:`, e);
                }
            }
        }
        try {
            // TODO(ak) we cannot just publish extensions we need to respect correpsonding flags like pre-release or target
            // prepackaged extension does not allow to configure it, so we need to repackage?
            await registry.publish(filePath, OVSX_CLI_ENV.OVSX_PAT);
            console.log(`✅ Published extension ${extensionId}@${extension.version}`);
        } catch (e) {
            if (!/is already published/g.test(String(e))) {
                console.error(`❌ Failed to publish extension ${extensionId}@${extension.version}:`, e);
            }
            return;
        }
    } finally {
        try {
            fs.rmdirSync(VSIX_DOWNLOAD_DIR, { recursive: true });
        } catch (e) {
            console.error(`Failed to remove download dir:`, e);
        }
    }
}

/**
 * @param {string[]} extIds
 */
async function fetchAndPublishExtensions(extIds: string[]) {
    let queue = Promise.resolve();
    if (extIds.length) {
        for (const extId of extIds) {
            queue = queue.then(() => fetchAndPublishExtension(extId));
        }
    }
    await queue;
}


async function main() {
    // commont test
    await fetchAndPublishExtension("golang.Go");
    // TODO(ak) ignore pre-release and targeted at the beginning and rely on upstream for it?
    // TODO(ak) target test? -> what would it mean if upstream has a new attribute but we sync without respecting it?
    // pre-release test
    await fetchAndPublishExtension("GitHub.vscode-pull-request-github");
    /*const xmlData = await fetchSitemapContent();
    const activeExtensions = await parseSitemapData(xmlData);
    await fetchAndPublishExtensions(activeExtensions.map(({ extId }) => extId));*/
}

main().then(null, console.error);