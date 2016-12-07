package io.rebolt.cache.loaders;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.integration.CacheLoader;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Inmemory 캐시 로더
 *
 * @since 1.0
 */
public final class LocalCacheLoader extends CacheLoader<Long, Object> {
  private final Callable<?> superMethod;
  private final Cache<Long, Object> cache;

  /**
   * @param superMethod 호출하고자 했던 메소드
   * @param max 최대 캐싱수
   * @param duration 캐싱 유효기간 (단위: 초)
   */
  public LocalCacheLoader(final Callable<?> superMethod, int max, long duration) {
    this.superMethod = superMethod;
    this.cache = new Cache2kBuilder<Long, Object>() {}
        .entryCapacity(max)
        .expireAfterWrite(duration, TimeUnit.SECONDS)
        .loader(this)
        .build();
  }

  @Override
  public Object load(Long key) throws Exception {
    return superMethod.call();
  }

  public Object getCache(Long key) {
    return cache.get(key);
  }
}
