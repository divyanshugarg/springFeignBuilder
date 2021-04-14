package com.vads.springFeignBuilder.configuration;

import com.vads.springFeignBuilder.annotations.FilterHeaders;
import com.vads.springFeignBuilder.properties.FeignProperties;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.ContentType;
import feign.micrometer.MicrometerCapability;
import feign.okhttp.OkHttpClient;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*

 */
@Slf4j
@Configuration
@ConditionalOnClass(Feign.class)
//@EnableFeignClients(value = "com.vads.springFeignBuilder")
@EnableConfigurationProperties({FeignProperties.class})
public class FeignBasicConfiguration {

  private static final Log LOG = LogFactory.getLog(FeignBasicConfiguration.class);
  private final ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;
  @Resource
  FeignProperties feignProperties;
  private okhttp3.OkHttpClient okHttpClient;

  /*
  @Autowired
  private final AccountContext<AccountEntity> accountContext;
  @Autowired
  private final UserContext userContext;
*/

  @Bean
  @ConditionalOnProperty(value = "spring.common.lib.feign.loggerLevel")
  public Logger.Level level() {
    LOG.debug("Set Feign Builder Logger Level as {" + feignProperties.getLoggerLevel() + "} to complete FeignBasicConfiguration request.");
    return (feignProperties.getLoggerLevel());
  }

  /**
   * @return Feign.Builder with Decode404 response as 200 if 'spring.common.lib.feign.decode404' property set as true
   */
  @Bean
  public Feign.Builder feignBuilder() {
    LOG.debug("Create Feign builder to complete Feign Basic Configuration request.");
    Feign.Builder builder = Feign.builder();
    return feignProperties.isDecode404() ? builder.decode404() : builder;
  }

  /**
   * @return Encoder
   */
  @Bean
  @ConditionalOnProperty(value = "spring.common.lib.feign.springEncoder.enabled", havingValue = "true")
  Encoder feignEncoder() {
    LOG.debug("Create SpringEncoder to complete Feign Basic Configuration request.");
    return new SpringEncoder(messageConverters);
  }

  /**
   * @return
   */
  @Bean
  @ConditionalOnProperty(value = "spring.common.lib.feign.springDecoder.enabled", havingValue = "true")
  Decoder feignDecoder() {
    LOG.debug("Create SpringDecoder bean to complete Feign Basic Configuration request.");
    return new SpringDecoder(messageConverters);
  }

  @Bean
  @ConditionalOnMissingBean(ConnectionPool.class)
  @ConditionalOnProperty(value = "spring.common.lib.feign.ok.client.enabled", matchIfMissing = true)
  public ConnectionPool httpClientConnectionPool(OkHttpClientConnectionPoolFactory connectionPoolFactory) {
    LOG.debug("Create Connection Pool to complete Feign Basic Configuration request.");
    int maxTotalConnections = feignProperties.getHttpClient().getMaxConnections();
    long timeToLive = feignProperties.getHttpClient().getTimeToLive();
    TimeUnit ttlUnit = feignProperties.getHttpClient().getTimeToLiveUnit();
    return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
  }

  /**
   * To get default/customized OkHttpClient
   * Refer com.vads.springFeignBuilder.properties.FeignProperties.HttpWebClientProperties class to get default value
   * Override WebClient properties if need using application property file. Example: spring.common.lib.feign.httpClient.connectionTimeout =5
   *
   * @return
   */
  @Bean
  @ConditionalOnProperty(value = "spring.common.lib.feign.ok.client.enabled", matchIfMissing = true)
  public okhttp3.OkHttpClient client(OkHttpClientFactory httpClientFactory, ConnectionPool connectionPool) {
    LOG.debug("Create Feign builder Okhttp Client to complete Feign Basic Configuration request.");
    boolean followRedirects = feignProperties.getHttpClient().isFollowRedirects();
    int connectTimeout = feignProperties.getHttpClient().getConnectionTimeout();
    int readTimeout = feignProperties.getHttpClient().getReadTimeout();
    boolean disableSslValidation = feignProperties.getHttpClient().isDisableSslValidation();
    this.okHttpClient = httpClientFactory.createBuilder(disableSslValidation)
        .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS).followRedirects(followRedirects).readTimeout(readTimeout, TimeUnit.MILLISECONDS)
        .connectionPool(connectionPool).build();
    return this.okHttpClient;
  }

  /**
   * destroy/Clean-up connection from ConnectionPool
   *
   * @return
   */
  @PreDestroy
  @ConditionalOnProperty(value = "spring.common.lib.feign.ok.client.enabled", matchIfMissing = true)
  public void destroy() {
    if (this.okHttpClient != null) {
      this.okHttpClient.dispatcher().executorService().shutdown();
      this.okHttpClient.connectionPool().evictAll();
    }
  }

  /**
   * Set feignClient as OkhttpClient
   * Set spring.common.lib.feign.ok.client.enabled as false if don't want to use OkhttpClient as per web client to perform web request
   *
   * @param client
   * @return
   */
  @Bean
  @ConditionalOnMissingBean(Client.class)
  @ConditionalOnProperty(value = "spring.common.lib.feign.ok.client.enabled", havingValue = "true")
  public feign.Client feignClient(okhttp3.OkHttpClient client) {
    LOG.debug("Configure Okhttp as Feign client to complete Feign Basic Configuration request.");
    return new OkHttpClient(client);
  }

  /**
   * Feign Micrometer Capability to get different metrics generated/supported by MicrometerCapability.class
   *
   * @param meterRegistry
   * @return MicrometerCapability object
   */
  @Bean
  @ConditionalOnProperty(value = "spring.common.lib.feign.metrics.enabled", matchIfMissing = true)
  public MicrometerCapability requestMetricCollector(MeterRegistry meterRegistry) {
    return new MicrometerCapability(meterRegistry);
  }

  /**
   * @return RequestInterceptor to default Header set in each web request.
   * Set property as False if don't want to include in each request
   */
  @Bean
  @ConditionalOnProperty(value = "spring.common.lib.feign.add.default.headers", matchIfMissing = true)
  public RequestInterceptor requestInterceptor() {
    LOG.debug("Configure default header in request served by Feign client to complete Feign Basic Configuration request.");
    return requestTemplate -> {
      requestTemplate.header("user", "username");
      requestTemplate.header("password", "password");
      requestTemplate.header("x-acc-id", "12345667789");
      requestTemplate.header("x-api-id", UUID.randomUUID().toString());
      requestTemplate.header("x-org-id", "8657836585618563");
      requestTemplate.header("Accept", ContentType.MULTIPART.getHeader());
      FilterHeaders filterHeaders = requestTemplate.methodMetadata().method().getAnnotation(FilterHeaders.class);
      if (filterHeaders != null && filterHeaders.exclude().length > 0) {
        Arrays.stream(filterHeaders.exclude()).forEach(requestTemplate::removeHeader);
      }
      requestTemplate.decodeSlash(true);
    };

  }

  /**
   * Enable this Bean if want to retry the web request in case of failure.
   * Provide Retry configuration accordingly.
   * Refer @class com.vads.springFeignBuilder.properties.FeignProperties.RetryConfig for default value
   *
   * @return
   */
  @Bean
  @ConditionalOnProperty(value = "spring.common.lib.feign.retry.enabled", havingValue = "true")
  public Retryer retryer() {
    LOG.debug("Create Feign builder Retryer bean to complete FeignBasicConfiguration request.");
    return new Retryer.Default(feignProperties.getRetry().getPeriod(), feignProperties.getRetry().getMaxPeriod(), feignProperties.getRetry().getMaxAttempts());
  }

}
