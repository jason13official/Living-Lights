package io.github.jason13official.living_lights.platform.services;

import net.minecraft.world.entity.Entity;

public interface IEmissionHelper {

  /// light level the server last synced for this entity, `0` if none
  int getSyncedEmission(Entity entity);

  /// store an entity's light level and sync it to tracking clients; `0` clears it
  void setSyncedEmission(Entity entity, int emission);
}
