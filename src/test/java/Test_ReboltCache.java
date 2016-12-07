import io.rebolt.cache.ReboltCache;
import io.rebolt.cache.annotations.CacheableMethod;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertTrue;

public final class Test_ReboltCache {

  public static class Cacheable {
    private final LocalDateTime time;

    public Cacheable() {
      this.time = LocalDateTime.now();
    }

    @CacheableMethod(duration = 10)
    public LocalDateTime getTime() {
      return time;
    }
  }

  public static class Cacheable2 {
    private final LocalDateTime time;

    public Cacheable2() {
      this.time = LocalDateTime.now();
    }

    @CacheableMethod(duration = 10)
    public LocalDateTime getTime() {
      return time;
    }
  }

  @Test
  public void test_default() {
    Cacheable cacheable = ReboltCache.getInstance(Cacheable.class);
    Cacheable2 cacheable2 = ReboltCache.newInstance(Cacheable2.class);
    int sames = 1, sames2 = 1;
    LocalDateTime before = cacheable.getTime();
    LocalDateTime before2 = cacheable2.getTime();
    LocalDateTime current, current2;
    for (int i = 0; i < 1000; i++) {
      current = cacheable.getTime();
      current2 = cacheable2.getTime();
      if (!before.equals(current)) {
        --sames;
      }
      if (!before2.equals(current2)) {
        --sames2;
      }
    }
    assertTrue(sames >= 0);
    assertTrue(sames2 >= 0);
  }

}
