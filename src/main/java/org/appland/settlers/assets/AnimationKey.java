package org.appland.settlers.assets;

import java.util.Objects;

public class AnimationKey {
    private final Nation nation;
    private final Direction direction;

    public AnimationKey(Nation nation, Direction direction) {
        this.nation = nation;
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimationKey that = (AnimationKey) o;
        return nation == that.nation && direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nation, direction);
    }

    @Override
    public String toString() {
        return "AnimationKey{" +
                "nation=" + nation +
                ", direction=" + direction +
                '}';
    }
}
