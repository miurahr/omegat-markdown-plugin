# Fenced code test

Samples from seafile project docs.

## Modify Seafile.conf

Edit `seafile.conf`, add the following lines:

```
[commit_object_backend]
name = s3
# bucket name can only use lowercase characters, numbers, periods and dashes. Period cannot be used in Frankfurt region.
bucket = my-commit-objects
key_id = your-key-id
key = your-secret-key
memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100

[fs_object_backend]
name = s3
# bucket name can only use lowercase characters, numbers, periods and dashes. Period cannot be used in Frankfurt region.
bucket = my-fs-objects
key_id = your-key-id
key = your-secret-key
memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100

[block_backend]
name = s3
# bucket name can only use lowercase characters, numbers, periods and dashes. Period cannot be used in Frankfurt region.
bucket = my-block-objects
key_id = your-key-id
key = your-secret-key
memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100
```

### Use HTTPS connections to S3

Since Pro 5.0.4, you can use HTTPS connections to S3. Add the following options to seafile.conf:

```
[commit_object_backend]
name = s3
......
use_https = true

[fs_object_backend]
name = s3
......
use_https = true

[block_backend]
name = s3
......
use_https = true
```


## Use S3-compatible Object Storage

Many object storage systems are now compatible with the S3 API, such as OpenStack Swift and Ceph's RADOS Gateway. You can use these S3-compatible storage systems as backend for Seafile. Here is an example config:

```
[commit_object_backend]
name = s3
bucket = my-commit-objects
key_id = your-key-id
key = your-secret-key
host = 192.168.1.123:8080
path_style_request = true
memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100

[fs_object_backend]
name = s3
bucket = my-fs-objects
key_id = your-key-id
key = your-secret-key
host = 192.168.1.123:8080
path_style_request = true
memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100

[block_backend]
name = s3
bucket = my-block-objects
key_id = your-key-id
key = your-secret-key
host = 192.168.1.123:8080
path_style_request = true
memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100
```
