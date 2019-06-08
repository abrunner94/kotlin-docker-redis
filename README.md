### Kotlin using Docker and Redis
This project demonstrates how to run a simple Kotlin app using Javalin and Redis in a containerized environment.

#### Get Started

In order to build, run and deploy your app, make sure you have Docker installed on your machine. 


##### Build and run
Once you have cloned the repository, in the root directory, run:
```bash
$ ./gradlew build
```
Next, build and run your containers using `docker-compose`:
```bash
$ docker-compose build --pull
$ docker-compose up
```

Your Kotlin app, which is a Javalin API, is now accessible on your host machine.
 ```bash
 kotlin-docker-redis  $  curl -v -k http://localhost:7000/users
 *   Trying ::1...
 * TCP_NODELAY set
 * Connected to localhost (::1) port 7000 (#0)
 > GET /users HTTP/1.1
 > Host: localhost:7000
 > User-Agent: curl/7.54.0
 > Accept: */*
 > 
 < HTTP/1.1 200 OK
 < Date: Sat, 08 Jun 2019 09:33:05 GMT
 < Server: Javalin
 < Content-Type: text/plain
 < Content-Length: 0
 < 
 * Connection #0 to host localhost left intact
 ```
The following API endpoints are now available:
````
GET http://localhost:7000/users/
GET http://localhost:7000/user/name
````

##### Using Redis for our APIs
The main idea is to link our Redis container with our Kotlin app container, such that we can make use of the Redis KV store by displaying specific data using our Javalin API endpoints.
However, our Redis container is a new image, which means we have to ingest data first.
We can do this by tunneling into our Redis container and add a new key-value.
```bash
kotlin-docker-redis  $  docker ps
CONTAINER ID        IMAGE                          COMMAND                  CREATED             STATUS              PORTS                    NAMES
613c1d7bc6e2        kotlin-docker-redis_api   "/bin/sh -c 'java -j…"   37 minutes ago      Up 37 minutes       0.0.0.0:7000->7000/tcp   kotlin-api
459976112ba8        redis              "docker-entrypoint.s…"   37 minutes ago      Up 37 minutes       6379/tcp                 cache
```

```bash
kotlin-docker-redis  $  docker exec -it 459976112ba8 /bin/sh
# redis-cli
127.0.0.1:6379> set Bob 23
OK
127.0.0.1:6379> 
```

We can verify that our Javalin API now has data to display:

```bash
kotlin-docker-redis  $  curl -v -k http://localhost:7000/user/Bob
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 7000 (#0)
> GET /user/Bob HTTP/1.1
> Host: localhost:7000
> User-Agent: curl/7.54.0
> Accept: */*
> 
< HTTP/1.1 200 OK
< Date: Sat, 08 Jun 2019 09:47:26 GMT
< Server: Javalin
< Content-Type: application/json
< Content-Length: 29
< 
* Connection #0 to host localhost left intact
{"name":"Bob","downloads":23}
```


