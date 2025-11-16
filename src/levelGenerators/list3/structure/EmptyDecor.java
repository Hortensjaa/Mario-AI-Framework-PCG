package levelGenerators.list3.structure;

import engine.core.MarioLevelModel;
import lombok.AllArgsConstructor;

import java.util.Random;


public class EmptyDecor extends Decorator {
    final static int MAX_WIDTH = 10;

    public EmptyDecor() {
        width = new Random().nextInt(MAX_WIDTH) + 1;
    }

    @Override
    public void mutate() {}

    @Override
    public int generate(MarioLevelModel model, int x, int floorLevel) {
        return this.width;
    }
}
