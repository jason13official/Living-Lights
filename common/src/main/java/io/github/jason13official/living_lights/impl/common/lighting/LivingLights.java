package io.github.jason13official.living_lights.impl.common.lighting;

import io.github.jason13official.living_lights.api.common.lighting.EmissionProvider;
import io.github.jason13official.living_lights.api.common.lighting.LightEmission;
import io.github.jason13official.living_lights.api.common.lighting.LightEmitter;
import io.github.jason13official.living_lights.api.common.lighting.Source;
import io.github.jason13official.living_lights.platform.Services;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public final class LivingLights {

  /// tracked lights in a level (overworld, nether, end)
  private static final Map<BlockGetter, LevelLights> LEVELS = new ConcurrentHashMap<>();

  /// get our emitted light level at a given position in a level
  public static int getEmission(BlockGetter level, long blockNode) {

    LevelLights lights = LEVELS.get(level);
    return lights == null ? 0 : lights.getEmission(blockNode);
  }

  /// update the light from emitters; servers compute derived light and sync it, clients read the synced value
  public static void tick(Entity entity) {

    if (entity instanceof LightEmitter emitter) {
      update(entity, emitter.getLightEmission());
      return;
    }

    if (entity.level().isClientSide()) {
      update(entity, Services.emission().getSyncedEmission(entity));
      return;
    }

    int emission = entity instanceof LivingEntity living ? getEmission(living) : 0;
    if (emission != Services.emission().getSyncedEmission(entity)) {
      Services.emission().setSyncedEmission(entity, emission);
    }
    update(entity, emission);
  }

  /// remove emitters `<= 0`, or put new emission level and update the block light engine
  public static void update(Entity entity, int emission) {

    Level level = entity.level();
    LevelLights lights = LEVELS.get(level);

    Source current = lights == null ? null : lights.get(entity); // may be null if not a tracked emitter yet

    if (emission <= 0) {
      if (current != null) {
        // stop tracking and update light engine
        lights.remove(entity);
        checkBlock(level, current.pos());
      }
      return;
    }

    long pos = entity.blockPosition().asLong();
    if (current != null && current.pos() == pos && current.emission() == emission) {
      return; // the source is unchanged, do nothing.
    }

    if (lights == null) {
      lights = LEVELS.computeIfAbsent(level, key -> new LevelLights());
    }

    lights.put(entity, new Source(pos, emission)); // track a new light source

    if (current != null && current.pos() != pos) {
      checkBlock(level, current.pos()); // entity's position does not match tracked source, update light at source
    }

    checkBlock(level, pos); // update light at entity position
  }

  /// set an entity's light emission to 0, which removes them from emission mappings entirely
  public static void remove(Entity entity) {

    update(entity, 0);
  }

  /// de-reference levels to avoid leaks
  public static void unload(BlockGetter level) {

    LEVELS.remove(level);
  }

  /// get light emission from entities that don't directly implement `LightEmitter`; the brightest source wins
  private static int getEmission(LivingEntity entity) {

    if (entity.typeHolder().is(LightEmission.LUMINOUS)) {
      return LightEmission.MAX;
    }

    int emission = 0;
    for (EmissionProvider provider : LightEmission.providers()) {
      emission = Math.max(emission, provider.getEmission(entity));
    }
    for (EquipmentSlot slot : EquipmentSlot.VALUES) {
      emission = Math.max(emission, LightEmission.of(entity.getItemBySlot(slot), LightEmission.LUMINOUS_WHEN_EQUIPPED));
    }
    if (entity instanceof Player player) {
      for (ItemStack stack : player.getInventory()) {
        if (emission >= LightEmission.MAX) {
          break;
        }
        if (stack.is(LightEmission.LUMINOUS_IN_INVENTORY)) {
          emission = Math.max(emission, LightEmission.of(stack, LightEmission.LUMINOUS_IN_INVENTORY));
        }
      }
    }
    return Math.min(emission, LightEmission.MAX);
  }

  /// update the light engine for chunks/blocks at a given position in a level/dimension
  private static void checkBlock(Level level, long pos) {

    level.getChunkSource().getLightEngine().checkBlock(BlockPos.of(pos));
  }
}
