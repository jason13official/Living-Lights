package io.github.jason13official.living_lights.api.common.lighting;

import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;

/// computes extra light emission for living entities that don't implement `LightEmitter`
@FunctionalInterface
public interface EmissionProvider {

  int getEmission(LivingEntity entity);

  /// emit a fixed light level whenever the predicate matches
  static EmissionProvider when(Predicate<? super LivingEntity> predicate, int emission) {

    return entity -> predicate.test(entity) ? emission : 0;
  }
}
