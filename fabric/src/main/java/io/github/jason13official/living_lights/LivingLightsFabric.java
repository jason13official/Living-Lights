package io.github.jason13official.living_lights;

import io.github.jason13official.living_lights.impl.common.lighting.LivingLights;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;

public class LivingLightsFabric implements ModInitializer {

  @Override
  public void onInitialize() {

    LivingLightsMod.init();

    // ServerEntityEvents.ENTITY_UNLOAD
    ServerEntityEvents.ENTITY_UNLOAD.register((entity, level) -> LivingLights.remove(entity));

    // ServerLevelEvents.UNLOAD
    ServerLevelEvents.UNLOAD.register((server, level) -> LivingLights.unload(level));
  }
}
