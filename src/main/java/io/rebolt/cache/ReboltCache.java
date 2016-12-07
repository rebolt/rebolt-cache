package io.rebolt.cache;

import io.rebolt.cache.annotations.CacheableMethod;
import io.rebolt.cache.interceptors.SimpleCacheInterceptor;
import io.rebolt.core.utils.ProxyUtil;

/**
 * ReboltCache를 통해 프로젝트에 캐시를 적용할 수 있다.
 *
 * @since 1.0
 */
public final class ReboltCache {

  /**
   * Cache기능을 사용할 클래스를 싱글턴으로 생성한다.
   * 클래스내 메소드에 {@link CacheableMethod} 어노테이션을 정의해 캐시를 활성화 한다.
   *
   * @param clazz 타켓 글래스, final로 정의하면 안된다.
   * @since 1.0
   */
  public static <T> T getInstance(Class<T> clazz) {
    return ProxyUtil.getInterceptorClass(SimpleCacheInterceptor.class, clazz);
  }

  /**
   * Cache기능을 사용할 클래스 인스턴스를 생성한다.
   * 클래스내 메소드에 {@link CacheableMethod} 어노테이션을 정의해 캐시를 활성화 한다.
   *
   * @param clazz 타겟 클래스, final로 정의하면 안된다.
   * @return 생성된 인스턴스는 자체적으로 캐싱해 사용한다. (예: 스프링빈)
   * @since 1.0
   */
  public static <T> T newInstance(Class<T> clazz) {
    return ProxyUtil.newInterceptorClass(SimpleCacheInterceptor.class, clazz);
  }

}
