
ARG BUILD_FROM=ghcr.io/hassio-addons/debian-base:7.8.2
# hadolint ignore=DL3006
FROM $BUILD_FROM AS base

# Set shell
SHELL ["/bin/bash", "-o", "pipefail", "-c"]


# Setup base system
ARG BUILD_ARCH=amd64

FROM questdb/questdb:8.3.0 AS questdb_image

WORKDIR /app

COPY --from=questdb_image /app .
COPY --from=questdb_image /usr/local/bin/gosu /usr/local/bin/gosu

COPY --from=questdb_image /docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

# Create questdb user and group
RUN groupadd -g 10001 questdb && \
    useradd -u 10001 -g 10001 -d /var/lib/questdb -M -s /sbin/nologin questdb && \
    mkdir -p /var/lib/questdb && \
    chown -R questdb:questdb /var/lib/questdb

WORKDIR /var/lib/questdb

# Make port 9000 available to the world outside this container
EXPOSE 9000/tcp
EXPOSE 8812/tcp
EXPOSE 9009/tcp

# Copy root filesystem
COPY rootfs /

# Health check
HEALTHCHECK --start-period=5m \
    CMD curl --insecure --fail http://localhost:9000 || exit 1

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
