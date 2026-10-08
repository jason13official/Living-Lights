package io.github.jason13official.living_lights;

import net.minecraft.resources.Identifier;

public class LivingLightsMod {

  public static void init() {
  }

  public static Identifier id(String path) {

    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}