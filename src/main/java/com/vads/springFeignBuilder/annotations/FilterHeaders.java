package com.vads.springFeignBuilder.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * FilterHeaders Annotation to filter header(s) from the default set before sending the web request.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterHeaders {
  /**
   * The selectors for headers to be exclude while sending any request.
   * The selectors should be in the form {@code "header1,header2"} format.
   */
  String[] exclude() default "";

}
