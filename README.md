# Living Lights

A library mod for Fabric and NeoForge that lets entities emit real block light as they move.

- Light is computed on the server and synced to every client tracking the entity.
- Installing the mod alone changes nothing; every tag ships empty.
- Required on both client and server.

## Gradle

```groovy
repositories {
    maven { url = "https://api.modrinth.com/maven" }
}

dependencies {
    // NeoForge
    implementation "maven.modrinth:living-lights:neoforge-<minecraft_version>-<mod_version>"
    // Fabric
    implementation "maven.modrinth:living-lights:fabric-<minecraft_version>-<mod_version>"
}
```

Then declare `living_lights` as a required dependency in your `neoforge.mods.toml` or `fabric.mod.json`.

## API

All entry points live in `io.github.jason13official.living_lights.api.common.lighting`.

### LightEmitter

Implement on your entity to emit light directly; checked first, computed on both sides.

```java
public class GlowingMob extends Monster implements LightEmitter {

  @Override
  public int getLightEmission() {
    return this.isAggressive() ? 15 : 7;
  }
}
```

### LightEmission.register

Add rules for any living entity; the brightest result wins.

```java
public class MyMod {

  private void onAnEvent() {
    LightEmission.register(entity -> entity.isOnFire() ? 15 : 0);
    LightEmission.register(entity -> entity.hasEffect(MobEffects.GLOWING), 8);
  }
}
```

### Tags

| Tag                                            | Effect                                               |
|------------------------------------------------|------------------------------------------------------|
| `#living_lights:luminous` (entity type)        | Always emits light level 15                          |
| `#living_lights:luminous_when_equipped` (item) | Emits light while held or worn in any equipment slot |
| `#living_lights:luminous_in_inventory` (item)  | Emits light from anywhere in a player's inventory    |

Tagged block items emit their block's light level (torch 14, soul lantern 10); other items emit 15.

### Data component

`living_lights:light_emission` (0-15) sets an exact light level for one stack while it's equipped, overriding the item tags.

```java
public class MyMod {
  
  private void onAnEvent() {
    stack.set(LightEmission.component(), 12);
  }
}
```

```
/give @s minecraft:blaze_rod[living_lights:light_emission=12]
```
