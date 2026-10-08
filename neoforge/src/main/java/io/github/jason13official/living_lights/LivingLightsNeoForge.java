package io.github.jason13official.living_lights;


import io.github.jason13official.living_lights.impl.common.lighting.LivingLights;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@Mod(Constants.MOD_ID)
public class LivingLightsNeoForge {

  public LivingLightsNeoForge(IEventBus modEventBus) {

    LivingLightsMod.init();

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
}