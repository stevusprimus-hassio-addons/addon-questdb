stream {
    include /etc/nginx/includes/ssl_params.conf;

    log_format mqtt '$remote_addr [$time_local] $protocol $status $bytes_received ' 
                '$bytes_sent $upstream_addr';
                
    server {
        listen              {{ .port }} ssl;
        server_name         default_server;
        ssl_certificate /ssl/{{ .certfile }};
        ssl_certificate_key /ssl/{{ .keyfile }};

        proxy_pass questdb_influxdb;
    }

}