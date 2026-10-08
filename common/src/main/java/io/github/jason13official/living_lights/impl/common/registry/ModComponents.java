package io.github.jason13official.living_lights.impl.common.registry;

import io.github.jason13official.living_lights.LivingLightsMod;
import io.github.jason13official.living_lights.api.common.lighting.LightEmission;
import java.util.function.BiConsumer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class ModComponents {

  public static final Identifier LIGHT_EMISSION_ID = LivingLightsMod.id("light_emission");

  /// exact light level for a single stack; overrides the item tags, and applies while held or worn
  public static DataComponentType<Integer> LIGHT_EMISSION;

  public static void register(BiConsumer<DataComponentType<?>, Identifier>  consumer) {

    LIGHT_EMISSION = DataComponentType.<Integer>builder() // format
        .persistent(ExtraCodecs.intRange(0, LightEmission.MAX)) // format
        .networkSynchronized(ByteBufCodecs.VAR_INT) // format
        .build(); // format

    consumer.accept(LIGHT_EMISSION, LIGHT_EMISSION_ID);
  }
}
