package io.github.jason13official.living_lights.impl.common.registry;

import io.github.jason13official.living_lights.LivingLightsMod;
import java.util.function.BiConsumer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.attachment.AttachmentType;

public class ModAttachmentsNeoForge {

  public static final Identifier EMISSION_ID = LivingLightsMod.id("emission");

  /// server-computed light level, synced to tracking clients
  public static AttachmentType<Integer> EMISSION;

  public static void register(BiConsumer<AttachmentType<?>, Identifier> consumer) {

    EMISSION = AttachmentType.builder(() -> 0).sync(ByteBufCodecs.VAR_INT).build();

    consumer.accept(EMISSION, EMISSION_ID);
  }
}
