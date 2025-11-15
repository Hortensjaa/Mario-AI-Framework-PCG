package levelGenerators.list3.generator;

import engine.core.MarioLevelModel;
import lombok.Data;

@Data
public abstract class Structure {
    protected static final int MAX_WIDTH = 4;
    protected static final int MIN_WIDTH = 1;
    protected static final int MAX_HEIGHT = 4;
    protected static final int MIN_HEIGHT = 1;

    protected int width;
    protected int height;

    public abstract void mutate();

    public abstract int generate(MarioLevelModel model, int x, int floorLevel);
}
