package io.rebolt.cache.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 캐시타입
 *
 * @since 1.0
 */
@ToString
public enum CacheType {

  Local(0),
  InmemoryGrid(1);

  private final @Getter long type;

  CacheType(long type) {
    this.type = type;
  }

  public boolean equals(CacheType cacheType) {
    return type == cacheType.type;
  }
}
