# springFeignBuilder
An example of springFeignBuilder to customised as per different micro-services need.

# Why Feign:
Feign is a Java to HTTP client binder. Feign works by processing annotations into a templatized request. Arguments are applied to these templates in a straightforward fashion before output. Refer [OpenFeign](https://github.com/OpenFeign/feign) to know more about it. 
Here, we are using "[spring-cloud-starter-openfeign](https://github.com/spring-cloud/spring-cloud-openfeign)" implementation to make sure its compatibility with Springboot 2.X applications.

# Feign Builder:
Feign provide the features to override/configure its behaviour. Means, you can configure to decide which **client** (e.g. Apache HC5, Http2, OkHttp, Google HTTP etc), **contract** (e.g. Spring-boot, SOAP, JAX-RS 2 etc.), **encoder/decoder** (GSON, Jackson 1/2 etc.), **metric** (Dropwizard, Micrometer etc.) and in the last but not least **circuit-breaker** (Hystrix etc) used while sending a web request and receiving its response. You can easily enable/disable/override any capabilities at entire project level or at a particular feign client (i.e. method) level.
In this example, I used following libraries to configure our Feign builder. Remember, it is not limit features or constaint to use these libraries only.
```
  client: OkHttpClient
  encoder: SpringEncoder
  decoder: SpringDecoder
  metric: Micrometer
```
Apart from it, we also override Feign build client to add below capabilities:
```
  1- Add some default set of Headers while sending any request with the control to override (add, remove, update) at each method/request level
  2- Decode Slash at each request
  3- Retry incase of any failure with maximum attempts and max period
  4- Decode 404 response code
```
# Override/Enable/Disable Basic features: 
Although we created Feign basic configuration as per industry best practice. It is also true that one size can't be fit for all. Considering it, I provided the option to override/enable/disable any configuration/features via passing corresponding property value in your service application property file. Refer [com.vads.springFeignBuilder.properties.FeignProperties](https://github.com/divyanshugarg/springFeignBuilder/blob/main/src/main/java/com/vads/springFeignBuilder/properties/FeignProperties.java) and [com.vads.springFeignBuilder.configuration.FeignBasicConfiguration](https://github.com/divyanshugarg/springFeignBuilder/blob/main/src/main/java/com/vads/springFeignBuilder/configuration/FeignBasicConfiguration.java) class to get more insight. 
As an example, if you don't want to use OkHttp as Feign default client then you can disable it via setting `spring.common.lib.feign.ok.client.enabled` property value as `false`. Another, suppose you want to set your http client connection timeout as 5 seconds, then you can define it via setting `spring.common.lib.feign.httpClient.connectionTimeout` property value as `5000`.

# How to give a try:
It is a common ask. Developers want to try themselves. For you, I already created an [Restcontroller](https://github.com/divyanshugarg/springFeignBuilder/blob/main/src/main/java/com/vads/springFeignBuilder/controller/PostController.java) in the same repository. It has 3 rest endpoints. Each of these endpoints are using Feign client interface to call another service to complete the request. Observe the simplicity and clean code.

# Environment Setup:
It require Java 11 on your machine and Gradle tool setup. Rest of dependencies, it'll downdload, if you have active internet connection. 

  ### Clone repository:
  ```
    git clone https://github.com/divyanshugarg/springFeignBuilder.git
  ```
  ### Command to start:
  Go to terminal/cmd and run below command:
  ```
    gradle bootrun
  ```

  ### Sample request:
  ```
    curl --location --request DELETE 'http://localhost:8081/posts/1'
    curl --location --request GET 'http://localhost:8081/posts/1'
    curl --location --request GET 'http://localhost:8081/posts'
  ```

