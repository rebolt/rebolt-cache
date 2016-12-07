package io.rebolt.cache.annotations;

import io.rebolt.cache.enums.CacheType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 캐시기능을 사용할 메소드에 선언해서 사용한다.
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheableMethod {
  int max() default 1024;
  int duration() default 60; // 초
  CacheType type() default CacheType.Inmemory;
}
