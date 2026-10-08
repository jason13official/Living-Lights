package io.github.jason13official.living_lights.impl.common.lighting;

import io.github.jason13official.living_lights.api.common.lighting.LightEmitter;
import io.github.jason13official.living_lights.api.common.lighting.Source;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

  /// update the light from emitters
  public static void tick(Entity entity) {

    if (entity instanceof LightEmitter emitter) {
      update(entity, emitter.getLightEmission());
    } else if (entity instanceof LivingEntity living) {
      update(entity, getEmission(living));
    }
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

  /// get light emission from entities that don't directly implement `LightEmitter`
  private static int getEmission(LivingEntity entity) {

    // TODO check against tags, data component(s), items or general Predicate
//    int fromHead = PumpkinHeads.isLit(entity) ? LANTERN_EMISSION : 0;
//    return Math.max(fromHead, Math.max(PumpkinMaceItem.getLight(entity.getMainHandItem()), PumpkinMaceItem.getLight(entity.getOffhandItem())));

    return 15;
  }

  /// update the light engine for chunks/blocks at a given position in a level/dimension
  private static void checkBlock(Level level, long pos) {

    level.getChunkSource().getLightEngine().checkBlock(BlockPos.of(pos));
  }
}
