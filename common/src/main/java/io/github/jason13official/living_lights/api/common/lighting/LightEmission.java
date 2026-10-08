package io.github.jason13official.living_lights.api.common.lighting;

import io.github.jason13official.living_lights.LivingLightsMod;
import io.github.jason13official.living_lights.impl.common.lighting.LivingLights;
import io.github.jason13official.living_lights.impl.common.registry.ModComponents;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/// built-in ways to make living entities emit light without implementing `LightEmitter`
public final class LightEmission {

  public static final int MAX = 15;

  /// entity types that always emit `MAX` light
  public static final TagKey<EntityType<?>> LUMINOUS = TagKey.create(Registries.ENTITY_TYPE, LivingLightsMod.id("luminous"));

  /// items that emit light while held or worn in any equipment slot TODO empty the tag before release -> no gameplay should be modified by installing
  public static final TagKey<Item> LUMINOUS_WHEN_EQUIPPED = TagKey.create(Registries.ITEM, LivingLightsMod.id("luminous_when_equipped"));

  /// items that emit light from anywhere in a player's inventory
  public static final TagKey<Item> LUMINOUS_IN_INVENTORY = TagKey.create(Registries.ITEM, LivingLightsMod.id("luminous_in_inventory"));

  private static final List<EmissionProvider> PROVIDERS = new CopyOnWriteArrayList<>();

  /// the `living_lights:light_emission` component (0-15); exact light for one stack while equipped, overrides the item tags
  public static DataComponentType<Integer> component() {

    return ModComponents.LIGHT_EMISSION;
  }

  /// light an entity that doesn't tick (e.g. part entities); call on both sides, ticking entities are recomputed every tick
  public static void set(Entity entity, int emission) {

    LivingLights.update(entity, emission);
  }

  /// remove light set with `set`
  public static void clear(Entity entity) {

    LivingLights.remove(entity);
  }

  /// add custom logic for any living entity
  public static void register(EmissionProvider provider) {

    PROVIDERS.add(provider);
  }

  /// emit a fixed light level whenever the predicate matches
  public static void register(Predicate<? super LivingEntity> predicate, int emission) {

    register(EmissionProvider.when(predicate, emission));
  }

  public static List<EmissionProvider> providers() {

    return PROVIDERS;
  }

  /// the component value if present, else the light of a tagged item's block (or `MAX` for non-block items), else `0`
  public static int of(ItemStack stack, TagKey<Item> tag) {

    if (stack.isEmpty()) {
      return 0;
    }
    Integer component = stack.get(component());
    if (component != null) {
      return component;
    }
    if (!stack.is(tag)) {
      return 0;
    }
    int blockLight = stack.getItem() instanceof BlockItem blockItem ? blockItem.getBlock().defaultBlockState().getLightEmission() : 0;
    return blockLight > 0 ? blockLight : MAX;
  }
}
