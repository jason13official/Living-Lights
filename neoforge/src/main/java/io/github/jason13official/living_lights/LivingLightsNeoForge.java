package io.github.jason13official.living_lights;

import io.github.jason13official.living_lights.impl.common.registry.ModComponents;
import io.github.jason13official.living_lights.impl.common.lighting.LivingLights;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class LivingLightsNeoForge {

  public static IEventBus EVENT_BUS;

  public LivingLightsNeoForge(IEventBus modEventBus) {

    EVENT_BUS = modEventBus;

    LivingLightsMod.init();

    bind(Registries.DATA_COMPONENT_TYPE, ModComponents::register);

    // EntityTickEvent.Post
    NeoForge.EVENT_BUS.addListener(LivingLightsNeoForge::onTick);

    // EntityLeaveLevelEvent
    NeoForge.EVENT_BUS.addListener(LivingLightsNeoForge::onLeaveLevel);

    // LevelEvent.Unload
    NeoForge.EVENT_BUS.addListener((LevelEvent.Unload event) -> LivingLights.unload(event.getLevel()));
  }

  private static void onTick(EntityTickEvent.Post event) {

    Entity entity = event.getEntity();
    LivingLights.tick(entity);
  }

  private static void onLeaveLevel(EntityLeaveLevelEvent event) {

    LivingLights.remove(event.getEntity());
  }

  public <T> void bind(ResourceKey<Registry<T>> registryKey, Consumer<BiConsumer<T, Identifier>> source) {

    EVENT_BUS.addListener((Consumer<RegisterEvent>) event -> {
      if (registryKey.equals(event.getRegistryKey())) {
        source.accept((t, id) -> event.register(registryKey, id, () -> t));
      }
    });
  }
}