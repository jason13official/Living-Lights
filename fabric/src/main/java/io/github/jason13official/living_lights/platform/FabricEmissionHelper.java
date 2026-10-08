package io.github.jason13official.living_lights.platform;

import io.github.jason13official.living_lights.impl.common.registry.ModAttachmentsFabric;
import io.github.jason13official.living_lights.platform.services.IEmissionHelper;
import net.minecraft.world.entity.Entity;

public class FabricEmissionHelper implements IEmissionHelper {

  @Override
  public int getSyncedEmission(Entity entity) {

    return entity.getAttachedOrElse(ModAttachmentsFabric.EMISSION, 0);
  }

  @Override
  public void setSyncedEmission(Entity entity, int emission) {

    if (emission > 0) {
      entity.setAttached(ModAttachmentsFabric.EMISSION, emission);
    } else {
      entity.removeAttached(ModAttachmentsFabric.EMISSION);
    }
  }
}
