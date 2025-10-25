package levelGenerators.list1;

import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

public class Task4Generator extends List1Generator {
    private final int LEVEL_HEIGHT = 20;
    private final int singleFallHeight = 6;

    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
        model.clearMap();

        int currentX = 0;
        model.setBlock(0, 3, MarioLevelModel.MARIO_START);

        for (int i = 0; i < 3; i++) {
            int OffsetY = -2 - i * 3;
            int ceilingY = -1 - OffsetY;
            // (flat + ceiling) x2
            model.setBlock(currentX, 4 - OffsetY, MarioLevelModel.GROUND);
            model.setBlock(currentX, 1 - OffsetY, MarioLevelModel.SPECIAL_BRICK);
            model.setBlock(currentX, ceilingY, MarioLevelModel.GROUND);
            currentX++;
            model.setBlock(currentX, 4 - OffsetY, MarioLevelModel.GROUND);
            model.setBlock(currentX, 1 - OffsetY, MarioLevelModel.SPECIAL_BRICK);
            model.setBlock(currentX, ceilingY, MarioLevelModel.GROUND);
            currentX++;
            // stair up
            model.setBlock(currentX, 4 - OffsetY, MarioLevelModel.GROUND);
            model.setBlock(currentX, 3 - OffsetY, MarioLevelModel.GROUND);
            model.setBlock(currentX, 1 - OffsetY, MarioLevelModel.SPECIAL_BRICK);
            model.setBlock(currentX, ceilingY, MarioLevelModel.GROUND);
            currentX++;

            // left side of fall
            for (int y = 4; y < 4 + singleFallHeight; y++) {
                model.setBlock(currentX - 1, y - OffsetY, MarioLevelModel.GROUND);
            }
            model.setBlock(currentX, 4 - OffsetY, MarioLevelModel.GROUND);
            int floorHeight = 4 + singleFallHeight - OffsetY;
            model.setBlock(currentX, floorHeight, MarioLevelModel.GROUND);
            model.setBlock(currentX, ceilingY, MarioLevelModel.GROUND);
            currentX++;
            model.setBlock(currentX,  floorHeight, MarioLevelModel.GROUND);
            model.setBlock(currentX, ceilingY, MarioLevelModel.GROUND);
            currentX++;

            for (int y = -2; y < singleFallHeight + 2; y++) {
                model.setBlock(currentX, y - OffsetY, MarioLevelModel.GROUND);
            }
            model.setBlock(currentX,  floorHeight, MarioLevelModel.GROUND);
            currentX++;

            model.setBlock(currentX, floorHeight, MarioLevelModel.GROUND);
            model.setBlock(currentX, floorHeight - 3, MarioLevelModel.NORMAL_BRICK);
            model.setBlock(currentX, floorHeight - 6, MarioLevelModel.GROUND);
            currentX++;

                model.setBlock(currentX,  floorHeight, MarioLevelModel.GROUND);
                model.setBlock(currentX,  floorHeight - 1, MarioLevelModel.GROUND);
                model.setBlock(currentX,  floorHeight - 2, MarioLevelModel.GROUND);
                model.setBlock(currentX,  floorHeight - 5, MarioLevelModel.SPIKY);
                model.setBlock(currentX,  floorHeight - 6, MarioLevelModel.GROUND);
                currentX++;

                if (i < 2) {
                    model.setBlock(currentX,  floorHeight - 2, MarioLevelModel.GROUND);
                    model.setBlock(currentX,  floorHeight - 6, MarioLevelModel.GROUND);
                    currentX++;

                    model.setBlock(currentX,  floorHeight - 2, MarioLevelModel.GROUND);
                    model.setBlock(currentX,  floorHeight - 3, MarioLevelModel.GROUND);
                    model.setBlock(currentX,  floorHeight - 4, MarioLevelModel.GROUND);
                    model.setBlock(currentX,  floorHeight - 6, MarioLevelModel.GROUND);
                    currentX++;
                    model.setBlock(currentX,  floorHeight - 6, MarioLevelModel.GROUND);
            }
        }

        model.setBlock(currentX, 20, MarioLevelModel.MARIO_EXIT);

        return model.getMap();
    }

    @Override
    public String getGeneratorName() {
        return "Task4Generator";
    }

    public int getLevelWidth() {
        return 150;
    }
}
