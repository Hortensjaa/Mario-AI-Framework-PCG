package levelGenerators.list1;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;


public class Task1Generator extends List1Generator {
    private final int GROUND_Y_LOCATION = 13;
    private final int LOOPS_NUMBER = 5;

    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
        model.clearMap();

        int currentX = 0;

        for (int i = 0; i < LOOPS_NUMBER; i++) {
            // some flat blocks at the start
            for (int x = currentX; x < currentX + 10; x++) {
                model.setBlock(x, GROUND_Y_LOCATION, MarioLevelModel.GROUND);
                model.setBlock(x, GROUND_Y_LOCATION + 1, MarioLevelModel.GROUND);
                model.setBlock(x, GROUND_Y_LOCATION + 2, MarioLevelModel.GROUND);
            }
            currentX += 10;

            // stairs 1
            for (int step = 0; step < 5; step++) {
                for (int x = currentX; x < currentX + 1; x++) {
                    for (int y = GROUND_Y_LOCATION; y >= GROUND_Y_LOCATION - step; y--) {
                        model.setBlock(x, y, MarioLevelModel.PYRAMID_BLOCK);
                    }

                    for (int y = GROUND_Y_LOCATION + 1; y < model.getHeight(); y++) {
                        model.setBlock(x, y, MarioLevelModel.PYRAMID_BLOCK);
                    }
                }
                currentX += 1;
            }

            // pit
            currentX += 2;

            // some flat blocks 2
            for (int x = currentX; x < currentX + 3; x++) {
                model.setBlock(x, GROUND_Y_LOCATION, MarioLevelModel.GROUND);
                model.setBlock(x, GROUND_Y_LOCATION + 1, MarioLevelModel.GROUND);
                model.setBlock(x, GROUND_Y_LOCATION + 2, MarioLevelModel.GROUND);
            }
            currentX += 3;

            // stairs 2
            for (int step = 0; step < 3; step++) {
                for (int x = currentX; x < currentX + 1; x++) {
                    for (int y = GROUND_Y_LOCATION; y >= GROUND_Y_LOCATION - step; y--) {
                        model.setBlock(x, y, MarioLevelModel.PYRAMID_BLOCK);
                    }

                    for (int y = GROUND_Y_LOCATION + 1; y < model.getHeight(); y++) {
                        model.setBlock(x, y, MarioLevelModel.PYRAMID_BLOCK);
                    }
                }
                currentX += 1;
            }

            // enemy
            model.setBlock(currentX, GROUND_Y_LOCATION, MarioLevelModel.GROUND);
            model.setBlock(currentX, GROUND_Y_LOCATION + 1, MarioLevelModel.GROUND);
            model.setBlock(currentX, GROUND_Y_LOCATION + 2, MarioLevelModel.GROUND);
            model.setBlock(currentX, GROUND_Y_LOCATION - 1, MarioLevelModel.GOOMBA);
            model.setBlock(currentX + 1, GROUND_Y_LOCATION - 1, MarioLevelModel.GOOMBA);
            currentX += 1;
        }

        for (int x = currentX; x < currentX + 10; x++) {
            model.setBlock(x, GROUND_Y_LOCATION, MarioLevelModel.GROUND);
            model.setBlock(x, GROUND_Y_LOCATION + 1, MarioLevelModel.GROUND);
            model.setBlock(x, GROUND_Y_LOCATION + 2, MarioLevelModel.GROUND);
        }
        currentX += 10;


        model.setBlock(2, GROUND_Y_LOCATION - 1, MarioLevelModel.MARIO_START);

        model.setBlock(currentX - 1, GROUND_Y_LOCATION - 1, MarioLevelModel.MARIO_EXIT);

        return model.getMap();
    }

    @Override
    public String getGeneratorName() {
        return "Task1Generator";
    }

    public int getLevelWidth() {
        return LOOPS_NUMBER * (10 + 5 + 2 + 3 + 1 + 1) + 10 + 1;
    }
}
