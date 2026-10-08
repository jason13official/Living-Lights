package io.github.jason13official.living_lights;

import io.github.jason13official.living_lights.impl.common.lighting.LivingLights;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;

public class LivingLightsClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    // ClientEntityEvents.ENTITY_UNLOAD
    ClientEntityEvents.ENTITY_UNLOAD.register((entity, level) -> LivingLights.remove(entity));
  }
}
