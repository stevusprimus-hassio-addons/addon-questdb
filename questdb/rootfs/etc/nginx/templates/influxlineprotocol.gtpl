server {
    listen              {{ .port }} ssl;
    ssl_certificate /ssl/{{ .certfile }};
    ssl_certificate_key /ssl/{{ .keyfile }};

    proxy_pass questdb_influxdb;
}
