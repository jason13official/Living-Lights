package io.github.jason13official.living_lights.mixin;

import io.github.jason13official.living_lights.impl.common.lighting.LivingLights;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class FabricMinecraftMixin {

  @Shadow
  public @Nullable ClientLevel level;

  @Inject(method = "setLevel", at = @At("HEAD"))
  private void living_lights$setLevel(ClientLevel level, CallbackInfo ci) {

    this.living_lights$unloadLevel();
  }

  @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V", at = @At("HEAD"))
  private void living_lights$disconnect(Screen screen, boolean keepResourcePacks, boolean stopSound, CallbackInfo ci) {

    this.living_lights$unloadLevel();
  }

  @Unique
  private void living_lights$unloadLevel() {

    if (this.level != null) {
      LivingLights.unload(this.level);
    }
  }
}
