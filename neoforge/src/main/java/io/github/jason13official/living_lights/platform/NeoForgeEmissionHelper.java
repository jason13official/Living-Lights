package io.github.jason13official.living_lights.platform;

import io.github.jason13official.living_lights.impl.common.registry.ModAttachmentsNeoForge;
import io.github.jason13official.living_lights.platform.services.IEmissionHelper;
import net.minecraft.world.entity.Entity;

public class NeoForgeEmissionHelper implements IEmissionHelper {

  @Override
  public int getSyncedEmission(Entity entity) {

    Integer emission = entity.getExistingDataOrNull(ModAttachmentsNeoForge.EMISSION);
    return emission == null ? 0 : emission;
  }

  @Override
  public void setSyncedEmission(Entity entity, int emission) {

    if (emission > 0) {
      entity.setData(ModAttachmentsNeoForge.EMISSION, emission);
    } else {
      entity.removeData(ModAttachmentsNeoForge.EMISSION);
    }
  }
}
