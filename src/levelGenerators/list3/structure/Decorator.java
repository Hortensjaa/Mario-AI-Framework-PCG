package levelGenerators.list3.structure;

import engine.core.MarioLevelModel;
import lombok.Data;

@Data
public abstract class Decorator implements Mutable {
    protected int width;

    public abstract void mutate();
    public abstract int generate(MarioLevelModel model, int x, int floorLevel);
}
