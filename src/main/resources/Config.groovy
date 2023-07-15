server {
    host = '0.0.0.0'
    port = 8080
    ioWorkersCount = 2
    ttl = 5 * 60 * 1000l
    resources {
        mapping = '/'
        cache {
            enabled = true
        }
    }
    debugOutput = false
    multipart {
        enabled = true
    }
}
