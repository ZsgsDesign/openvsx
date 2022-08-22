FROM gitpod/workspace-postgres:latest

# the following env variable is solely here to invalidate the docker image. We want to rebuild the image from time to time to get the latest base image (which is cached).
ENV DOCKER_BUMP=1

USER root

# Caddy proxy (https://caddyserver.com/)
RUN install-packages debian-keyring debian-archive-keyring apt-transport-https \
    && curl -1sSL 'https://dl.cloudsmith.io/public/caddy/stable/gpg.key' | sudo gpg --dearmor -o /usr/share/keyrings/caddy-stable-archive-keyring.gpg \
    && curl -1sSL 'https://dl.cloudsmith.io/public/caddy/stable/debian.deb.txt' | sudo tee /etc/apt/sources.list.d/caddy-stable.list \
    && apt update \
    && install-packages caddy

USER gitpod

RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh \
    # Install Java 18 for the Java extension to function properly
    && sdk install java 18.0.1.1-open \
    && sdk install java 11.0.2-open"

RUN curl https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.9.3-linux-x86_64.tar.gz --output elasticsearch-linux-x86_64.tar.gz \
    && tar -xzf elasticsearch-linux-x86_64.tar.gz \
    && rm elasticsearch-linux-x86_64.tar.gz
ENV ES_HOME="$HOME/elasticsearch-7.9.3"

