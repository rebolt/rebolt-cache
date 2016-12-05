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

  @Test
  public void test_default() {
    Cacheable cacheable = ReboltCache.getInstance(Cacheable.class);

    int sames = 1;
    LocalDateTime before = cacheable.getTime();
    LocalDateTime current;
    for (int i = 0; i < 1000; i++) {
      current = cacheable.getTime();
      if (!before.equals(current)) {
        --sames;
      }
    }

    assertTrue(sames >= 0);
  }
}
