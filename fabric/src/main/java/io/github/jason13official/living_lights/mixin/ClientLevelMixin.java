package io.github.jason13official.living_lights.mixin;

import io.github.jason13official.living_lights.impl.common.lighting.LivingLights;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

  @Inject(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = At.Shift.AFTER))
  private void living_lights$tickNonPassenger(Entity entity, CallbackInfo ci) {

    LivingLights.tick(entity);
  }
}
