package levelGenerators.list3;

import engine.core.MarioLevelModel;
import levelGenerators.list3.structure.Decorator;
import levelGenerators.list3.structure.Terrain;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
public final class LevelRenderer {
    private static final int BASE_FLOOR_OFFSET = 2;

    public String getRenderedLevel(
            MarioLevelModel model,
            List<Terrain> terrains,
            List<Decorator> decorators
    ) {
        renderLevel(model, terrains, decorators);
        return model.getMap();
    }

    /**
     * Render the level from the structures and decorators lists
     */
    private void renderLevel(
            MarioLevelModel model,
            List<Terrain> terrains,
            List<Decorator> decorators
    ) {
        model.clearMap();

        int width = model.getWidth();
        int height = model.getHeight();
        int baseFloorLevel = height - BASE_FLOOR_OFFSET;

        // Place Mario at start
        int currentX = 0;
        model.setBlock(currentX, baseFloorLevel - 1, MarioLevelModel.MARIO_START);

        // Render terrain
        for (Terrain terrain : terrains) {
            terrain.generate(model, currentX, baseFloorLevel);
            currentX += terrain.getWidth();
        }

        currentX = 0;
        for (Decorator decorator : decorators) {
            int w = decorator.getWidth();
            int maxFloorY = getFloorY(currentX, model);
            for (int i = 1; i < w; i++) {
                maxFloorY = Math.min(maxFloorY, getFloorY(currentX + i, model));
            }
            decorator.generate(model, currentX, maxFloorY);
            currentX += decorator.getWidth();
        }

        model.setBlock(150, baseFloorLevel - 1, MarioLevelModel.MARIO_EXIT);
    }

    private int getFloorY(int x, MarioLevelModel model) {
        for (int y = 0; y < model.getHeight(); y++) {
            if (model.getBlock(x, y) != MarioLevelModel.EMPTY) {
                return y;
            }
        }
        return model.getHeight() - BASE_FLOOR_OFFSET;
    }
}
