server {
    listen              {{ .port }} ssl;
    server_name         default_server;
    ssl_certificate /ssl/{{ .certfile }};
    ssl_certificate_key /ssl/{{ .keyfile }};

    proxy_pass questdb_influxdb;
}
