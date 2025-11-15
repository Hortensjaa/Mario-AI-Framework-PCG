package levelGenerators.list3.generator;

import engine.core.MarioLevelModel;
import lombok.Data;

@Data
public abstract class Decorator {
    protected int width;

    public abstract void mutate();
    public abstract int generate(MarioLevelModel model, int x, int floorLevel);
}
