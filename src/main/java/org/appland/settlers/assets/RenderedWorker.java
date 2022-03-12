package org.appland.settlers.assets;

import java.util.HashMap;
import java.util.Map;

public class RenderedWorker {
    private final JobType job;
    private final Map<AnimationKey, StackedBitmaps[]> animationMap;

    public RenderedWorker(JobType job) {
        this.job = job;
        animationMap = new HashMap<>();
    }

    public void setAnimationStep(Nation nation, Direction direction, StackedBitmaps bitmaps, int animationStep) {
        AnimationKey animationKey = new AnimationKey(nation, direction);

        if (!animationMap.containsKey(animationKey)) {
            animationMap.put(animationKey, new StackedBitmaps[8]);
        }

        StackedBitmaps[] animation = animationMap.get(animationKey);

        animation[animationStep] = bitmaps;
    }

    public StackedBitmaps[] getAnimation(Nation nation, Direction direction) {
        return animationMap.get(new AnimationKey(nation, direction));
    }

    @Override
    public String toString() {
        return "RenderedWorker{" +
                "job=" + job +
                ", animationMap=" + animationMap +
                '}';
    }
}
