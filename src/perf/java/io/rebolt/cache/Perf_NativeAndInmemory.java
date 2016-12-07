package io.rebolt.cache;

import io.rebolt.cache.annotations.CacheableMethod;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

@State(Scope.Thread)
@Fork(1)
@Measurement(iterations = 3)
@Warmup(iterations = 3)
public class Perf_NativeAndInmemory {

  public static class Inner {
    @CacheableMethod
    LocalDateTime getCache() {
      return LocalDateTime.now();
    }

    LocalDateTime getNow() {
      return LocalDateTime.now();
    }
  }

  private static Inner inner = ReboltCache.newInstance(Inner.class);
  private static Inner inner2 = new Inner();

  @Benchmark
  public void test_native() {
    inner2.getNow();
  }

  @Benchmark
  public void test_inmemory() {
    inner.getCache();
  }

  public static void main(String[] args) throws RunnerException, IOException {
    Options opt = new OptionsBuilder()
        .include(Perf_NativeAndInmemory.class.getSimpleName())
        .build();

    new Runner(opt).run();
  }

}

/*
Benchmark                              Mode  Cnt        Score          Error  Units
Perf_NativeAndInmemory.test_inmemory  thrpt    3  9784594.289 ± 14126642.766  ops/s
Perf_NativeAndInmemory.test_native    thrpt    3  8570383.731 ±   467530.460  ops/s
 */