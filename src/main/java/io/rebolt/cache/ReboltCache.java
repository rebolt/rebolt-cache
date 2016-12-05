package io.rebolt.cache;

import io.rebolt.cache.annotations.CacheableMethod;
import io.rebolt.cache.interceptors.SimpleCacheInterceptor;
import io.rebolt.core.utils.ClassUtil;
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
    Class<? extends T> proxyClass = ProxyUtil.getInterceptorClass(SimpleCacheInterceptor.class, clazz);
    return ClassUtil.getSingleton(proxyClass);
  }
}
