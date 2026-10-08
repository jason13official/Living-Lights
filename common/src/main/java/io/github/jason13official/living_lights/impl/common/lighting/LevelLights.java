package io.github.jason13official.living_lights.impl.common.lighting;

import io.github.jason13official.living_lights.api.common.lighting.Source;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;

public final class LevelLights {

  /// entities mapped to their tracked light source
  private final Map<Entity, Source> sources = new HashMap<>();

  /// track many light sources in a single position
  private final Map<Long, Map<Entity, Integer>> byPos = new HashMap<>();

  /// track the amount of light we're emitting at a given position
  private final Map<Long, Integer> emissions = new ConcurrentHashMap<>();

  /// @return the light level we're emitting at the given position, `pos`
  public int getEmission(long pos) {

    return this.emissions.getOrDefault(pos, 0);
  }

  /// @return the light source for the entity (if any), or `null` if no light source is tracked for that entity
  public @Nullable Source get(Entity entity) {

    // TODO should we be able to track many light sources (with offsets) for a single entity?
    return this.sources.get(entity);
  }

  /// map an entity to a light source (remove previous if existing)
  public void put(Entity entity, Source source) {

    Source previous = this.sources.put(entity, source);
    if (previous != null) {
      this.detach(entity, previous.pos());
    }
    this.byPos.computeIfAbsent(source.pos(), pos -> new HashMap<>()).put(entity, source.emission());
    this.recompute(source.pos());
  }

  /// remove from quick map to it's light source and detach from emissions entirely
  public @Nullable Source remove(Entity entity) {

    Source previous = this.sources.remove(entity);
    if (previous != null) {
      this.detach(entity, previous.pos());
    }
    return previous;
  }

  /// stop tracking emitter at given position
  private void detach(Entity entity, long pos) {

    // get other emitting entities at this position, with their light level
    Map<Entity, Integer> atPos = this.byPos.get(pos);
    if (atPos != null) {

      // remove from tracked light levels in this position
      atPos.remove(entity);

      if (atPos.isEmpty()) {

        // remove light levels map if nothing is being tracked
        this.byPos.remove(pos);
      }
    }

    this.recompute(pos);
  }

  /// gather emitters at the given position, check max is over 0, then add max or remove emission
  private void recompute(long pos) {

    Map<Entity, Integer> atPos = this.byPos.get(pos);
    int max = 0;
    if (atPos != null) {
      for (int emission : atPos.values()) {
        max = Math.max(max, emission);
      }
    }

    if (max > 0) {
      this.emissions.put(pos, max);
    } else {
      this.emissions.remove(pos);
    }
  }
}
