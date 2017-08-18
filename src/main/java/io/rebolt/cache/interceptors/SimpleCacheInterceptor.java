package io.rebolt.cache.interceptors;

import com.google.common.collect.Maps;
import io.rebolt.cache.annotations.CacheableMethod;
import io.rebolt.cache.enums.CacheType;
import io.rebolt.cache.loaders.LocalCacheLoader;
import io.rebolt.core.exceptions.NotImplementedException;
import io.rebolt.core.utils.HashUtil;
import io.rebolt.core.utils.ProxyUtil.AbstractIterceptor;
import io.rebolt.core.utils.StringUtil;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;

import static io.rebolt.cache.enums.CacheType.Inmemory;

/**
 * 가장 단순한 CacheInterceptor를 제공한다
 *
 * @since 1.0
 */
public class SimpleCacheInterceptor extends AbstractIterceptor {
  private Map<String, LocalCacheLoader> localCacheLoaderMap = Maps.newConcurrentMap();

  /**
   * 인터셉트 메소드. 타겟 클래스내 메소드가 호출되기 전에 먼저 호출된다.
   *
   * @param superMethod 원래 호출하고자 했던 메소드 (ByteBuddy용)
   * @param method 원래 호출하고 했던 메소드 (Reflection {@link Method})
   * @param args 메소드 인자
   * @return 메소드 실행 결과값
   */
  @RuntimeType
  @Override
  public Object intercept(@SuperCall Callable<?> superMethod, @Origin Method method, @AllArguments Object[] args) throws Exception {
    if (method.isAnnotationPresent(CacheableMethod.class)) {
      CacheableMethod cacheableMethod = method.getAnnotation(CacheableMethod.class);
      int max = cacheableMethod.max();
      long duration = cacheableMethod.duration();
      CacheType cacheType = cacheableMethod.type();
      String name = cacheableMethod.name();
      if (StringUtil.isNullOrEmpty(name)) {
        name = method.getName();
      }
      Object value;
      if (cacheType == Inmemory) {
        value = getLocalCacheLoader(superMethod, name, max, duration).getCache(HashUtil.deepHash(args));
      } else {
        // TODO: Remote Cache 추가
        throw new NotImplementedException("Not yet");
      }
      return value;
    }
    return superMethod.call();
  }

  /**
   * 로컬캐시로더 조회. 런타임시에 호출가능하다. 첫번째 호출시 초기화한다.
   *
   * @param superMethod 호출하고자 했던 메소드
   * @param name 캐시명 (unique)
   * @param max 최대 캐싱수
   * @param duration 캐싱 유효시간 (단위: 초)
   * @return {@link LocalCacheLoader}
   */
  private LocalCacheLoader getLocalCacheLoader(Callable<?> superMethod, String name, int max, long duration) {
    LocalCacheLoader localCacheLoader = localCacheLoaderMap.get(name);
    if (localCacheLoader == null) {
      localCacheLoader = new LocalCacheLoader(superMethod, max, duration);
      localCacheLoaderMap.putIfAbsent(name, new LocalCacheLoader(superMethod, max, duration));
    }
    return localCacheLoader;
  }
}
