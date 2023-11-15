# Description

Spring Boot application to showcase how to handle timeout exceptions while using `WebClient` and `RestTemplate` and return `504 Gateway Timeout` response to the client.

Works in conjunction with [Echo](https://github.com/sandipchitale/echo).

## How to use

- Run Echo application on port 9090. This sleeps for 2 seconds before returning a response.
- Try the following commands using httpie

The following two commands will return `504 Gateway Timeout` response to the client.

```shell
http.exe localhost:8080/resttemplate X-TIMEOUT-MILLIS:1000
http.exe localhost:8080/webclient X-TIMEOUT-MILLIS:1000
```

The following two commands will return `200` response to the client.

```shell
http.exe localhost:8080/resttemplate X-TIMEOUT-MILLIS:3000
http.exe localhost:8080/webclient X-TIMEOUT-MILLIS:3000
```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.5/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.5/gradle-plugin/reference/html/#build-image)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.1.5/reference/htmlsingle/index.html#web.reactive)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

