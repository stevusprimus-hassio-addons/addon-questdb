
ARG BUILD_FROM=ghcr.io/hassio-addons/debian-base:7.8.2
# hadolint ignore=DL3006
FROM $BUILD_FROM AS base

# Set shell
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Setup NGINX
RUN \
    apt-get update \
    && apt-get install -y --no-install-recommends \
        nginx=1.22.1-9+deb12u1 \
        libnginx-mod-stream=1.22.1-9+deb12u1 \
    \
    && rm -fr \
        /tmp/* \
        /etc/nginx \
        /var/{cache,log}/* \
        /var/lib/apt/lists/* \
    \
    && mkdir -p /var/log/nginx \
    && touch /var/log/nginx/error.log

# Setup QuestDB
# Copy everything over
FROM questdb/questdb:8.3.0 AS builder
FROM base
COPY --from=builder /app /app
COPY --from=builder /usr/local/bin/gosu /usr/local/bin/gosu
COPY --from=builder /docker-entrypoint.sh /

RUN chmod +x /docker-entrypoint.sh

WORKDIR /app

# Create questdb user and group
RUN groupadd -g 10001 questdb && \
    useradd -u 10001 -g 10001 -d /var/lib/questdb -M -s /sbin/nologin questdb && \
    mkdir -p /var/lib/questdb && \
    chown -R questdb:questdb /var/lib/questdb

WORKDIR /var/lib/questdb

# Copy root filesystem
COPY rootfs /

# Health check
HEALTHCHECK \
    CMD curl --fail http://127.0.0.1:9000 || exit 1

# Build arguments
ARG BUILD_ARCH
ARG BUILD_DATE
ARG BUILD_DESCRIPTION
ARG BUILD_NAME
ARG BUILD_REF
ARG BUILD_REPOSITORY
ARG BUILD_VERSION

# Labels
LABEL \
    io.hass.name="${BUILD_NAME}" \
    io.hass.description="${BUILD_DESCRIPTION}" \
    io.hass.arch="${BUILD_ARCH}" \
    io.hass.type="addon" \
    io.hass.version=${BUILD_VERSION} \
    maintainer="StevusPrimus <dev@stevusprimus.com>" \
    org.opencontainers.image.title="${BUILD_NAME}" \
    org.opencontainers.image.description="${BUILD_DESCRIPTION}" \
    org.opencontainers.image.vendor="Home Assistant Add-ons by StevusPrimus" \
    org.opencontainers.image.authors="StevusPrimus <dev@stevusprimus.com>" \
    org.opencontainers.image.licenses="MIT" \
    #org.opencontainers.image.url="https://addons.community" \
    org.opencontainers.image.source="https://github.com/${BUILD_REPOSITORY}" \
    org.opencontainers.image.documentation="https://github.com/${BUILD_REPOSITORY}/blob/main/README.md" \
    org.opencontainers.image.created=${BUILD_DATE} \
    org.opencontainers.image.revision=${BUILD_REF} \
    org.opencontainers.image.version=${BUILD_VERSION}
