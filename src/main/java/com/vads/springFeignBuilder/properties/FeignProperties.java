package com.vads.springFeignBuilder.properties;

import feign.Logger;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Data
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "spring.common.lib.feign")
public class FeignProperties {

  private RetryConfig retry = new RetryConfig();

  private HttpWebClientProperties httpClient = new HttpWebClientProperties();

  @Value("${logger.level:NONE}")
  private Logger.Level loggerLevel;

  @Value("${decode404:false}")
  private boolean decode404;

  @Data
  @ConfigurationProperties(prefix = "spring.common.lib.feign")
  public static class RetryConfig {

    /**
     * Default value for max number of attempts.
     */
    public static final int DEFAULT_MAX_ATTEMPTS = 0;

    /**
     * Default value for period.
     */
    public static final int DEFAULT_PERIOD = 100;

    /**
     * Default value for max period for retry.
     */
    public static final long DEFAULT_MAX_PERIOD = 1L;

    /**
     * Max Attempts to retry
     */
    private int maxAttempts = DEFAULT_MAX_ATTEMPTS;
    /**
     * Min period
     */
    private long period = DEFAULT_PERIOD;
    /**
     * maximum period
     */
    private long maxPeriod = DEFAULT_MAX_PERIOD;
  }

  @Data
  public static class HttpWebClientProperties {

    /**
     * Default value for disabling SSL validation.
     */
    public static final boolean DEFAULT_DISABLE_SSL_VALIDATION = false;

    /**
     * Default value for max number od connections.
     */
    public static final int DEFAULT_MAX_CONNECTIONS = 200;

    /**
     * Default value for max number od connections per route.
     */
    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;

    /**
     * Default value for time to live.
     */
    public static final long DEFAULT_TIME_TO_LIVE = 900L;

    /**
     * Default time to live unit.
     */
    public static final TimeUnit DEFAULT_TIME_TO_LIVE_UNIT = TimeUnit.SECONDS;

    /**
     * Default value for following redirects.
     */
    public static final boolean DEFAULT_FOLLOW_REDIRECTS = true;

    /**
     * Default value for connection timeout.
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 2000;

    /**
     * Default value for read timeout.
     */
    public static final int DEFAULT_READ_TIMEOUT = 5000;

    /**
     * Default value for connection timer repeat.
     */
    public static final int DEFAULT_CONNECTION_TIMER_REPEAT = 3000;

    private boolean disableSslValidation = DEFAULT_DISABLE_SSL_VALIDATION;

    private int maxConnections = DEFAULT_MAX_CONNECTIONS;

    private int maxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;

    private long timeToLive = DEFAULT_TIME_TO_LIVE;

    private TimeUnit timeToLiveUnit = DEFAULT_TIME_TO_LIVE_UNIT;

    private boolean followRedirects = DEFAULT_FOLLOW_REDIRECTS;

    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;

    private int readTimeout = DEFAULT_READ_TIMEOUT;

    private int connectionTimerRepeat = DEFAULT_CONNECTION_TIMER_REPEAT;
  }

}
