package levelGenerators.list3.generator;

import engine.core.MarioLevelModel;

import java.util.Random;



public class EmptyDecor extends Decorator {
    final static int MAX_WIDTH = 10;

    EmptyDecor() {
        width = new Random().nextInt(MAX_WIDTH) + 1;
    }

    EmptyDecor(int width) {
        this.width = width;
    }

    @Override
    public void mutate() {
        width = new Random().nextInt(MAX_WIDTH) + 1;
    }

    @Override
    public int generate(MarioLevelModel model, int x, int floorLevel) {
        return this.width;
    }
}
