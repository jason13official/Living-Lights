package io.github.jason13official.living_lights.impl.common.registry;

import io.github.jason13official.living_lights.LivingLightsMod;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;

public class ModAttachmentsFabric {

  public static final Identifier EMISSION_ID = LivingLightsMod.id("emission");

  /// server-computed light level, synced to tracking clients
  public static AttachmentType<Integer> EMISSION;

  public static void register() {

    EMISSION = AttachmentRegistry.create(EMISSION_ID, builder -> builder.syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all()));
  }
}
