package levelGenerators.list3.structure;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LevelStructure {
    private List<Terrain> terrains;
    private List<Decorator> decorators;
}
