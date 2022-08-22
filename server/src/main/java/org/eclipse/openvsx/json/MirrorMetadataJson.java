/********************************************************************************
 * Copyright (c) 2019 TypeFox and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package org.eclipse.openvsx.json;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;;

@ApiModel(
    value = "MirrorEntry",
    description = "Summary of metadata of an extension"
)
@JsonInclude(Include.NON_NULL)
public class MirrorMetadataJson {

    @ApiModelProperty("Name of the extension")
    @NotNull
    public String name;

    @ApiModelProperty("Namespace of the extension")
    @NotNull
    public String namespace;

    @ApiModelProperty(value = "Average rating", allowableValues = "range[0,5]")
    public Double averageRating;

    @ApiModelProperty("Number of downloads of the extension package")
    @Min(0)
    public int downloadCount;
    
    @ApiModelProperty("Essential metadata of all available versions")
    public List<VersionMirror> allVersions;
    
    @ApiModel(
        value = "VersionMirror",
        description = "Essential metadata of an extension version"
    )
    public static class VersionMirror {

        @NotNull
        public String version;
        
        @ApiModelProperty("Date and time when this version was published (ISO-8601)")
        @NotNull
        public String timestamp;
    }

}