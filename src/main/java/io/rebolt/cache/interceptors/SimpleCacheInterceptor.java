package io.rebolt.cache.interceptors;

import io.rebolt.cache.annotations.CacheableMethod;
import io.rebolt.cache.enums.CacheType;
import io.rebolt.cache.loaders.LocalCacheLoader;
import io.rebolt.core.exceptions.NotInitializedException;
import io.rebolt.core.utils.HashUtil;
import io.rebolt.core.utils.ProxyUtil.AbstractIterceptor;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 가장 단순한 CacheInterceptor를 제공한다
 *
 * @since 1.0
 */
public final class SimpleCacheInterceptor extends AbstractIterceptor {
  private volatile LocalCacheLoader localCacheLoader;
  private final Object _lock = new Object();

  @RuntimeType
  @Override
  public Object intercept(@SuperCall Callable<?> superMethod, @Origin Method method, @AllArguments Object[] args) throws Exception {
    if (method.isAnnotationPresent(CacheableMethod.class)) {
      CacheableMethod cacheableMethod = method.getAnnotation(CacheableMethod.class);
      int max = cacheableMethod.max();
      long duration = cacheableMethod.duration();
      CacheType cacheType = cacheableMethod.type();
      Object value;
      if (CacheType.Local.equals(cacheType)) {
        value = getCacheLoader(superMethod, max, duration).getCache(HashUtil.deepHash(args));
      } else {
        // TODO: Remote Cache 추가
        throw new NotInitializedException("not implement");
      }
      return value;
    }
    return superMethod.call();
  }

  private LocalCacheLoader getCacheLoader(Callable<?> superMethod, int max, long duration) {
    if (localCacheLoader == null) {
      synchronized (_lock) {
        if (localCacheLoader == null) {
          localCacheLoader = new LocalCacheLoader(superMethod, max, duration);
        }
      }
    }
    return localCacheLoader;
  }
}
