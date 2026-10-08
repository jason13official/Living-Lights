package io.github.jason13official.living_lights.api.common.lighting;

/// A given packed BlockPos as a `long`, paired to the amount of light to emit at that position, as an `int`.
public record Source(long pos, int emission) {

}
