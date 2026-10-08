package io.github.jason13official.living_lights;

import io.github.jason13official.living_lights.impl.common.registry.ModAttachmentsFabric;
import io.github.jason13official.living_lights.impl.common.registry.ModComponents;
import io.github.jason13official.living_lights.impl.common.lighting.LivingLights;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class LivingLightsFabric implements ModInitializer {

  @Override
  public void onInitialize() {

    LivingLightsMod.init();

    bind(BuiltInRegistries.DATA_COMPONENT_TYPE, ModComponents::register);
    ModAttachmentsFabric.register();

    // ServerEntityEvents.ENTITY_UNLOAD
    ServerEntityEvents.ENTITY_UNLOAD.register((entity, level) -> LivingLights.remove(entity));

    // ServerLevelEvents.UNLOAD
    ServerLevelEvents.UNLOAD.register((server, level) -> LivingLights.unload(level));
  }

  public <T> void bind(Registry<T> registry, Consumer<BiConsumer<T, Identifier>> source) {

    source.accept((t, id) -> Registry.register(registry, id, t));
  }
}
