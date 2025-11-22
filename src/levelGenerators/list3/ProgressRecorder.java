package levelGenerators.list3;

import levelGenerators.list3.structure.LevelStructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProgressRecorder {

    record IterationProgress(int iteration, float bestHeuristicScore, float averageSimulationScore) { }

    private final List<IterationProgress> records = new ArrayList<>();

    public void record(Map<LevelStructure, Float> populationScores, int iteration) {
        float bestHeuristicScore = populationScores.values()
                .stream()
                .max(Float::compare)
                .orElse(0f);

        float averageSimulationScore = (float) populationScores.values()
                .stream()
                .mapToDouble(v -> v)
                .average()
                .orElse(0.0);

        records.add(new IterationProgress(iteration, bestHeuristicScore, averageSimulationScore));
    }

    public void clear() {
        records.clear();
    }

    public void saveToCSV(String generatorName, int num) {
        try {
            String dirPath = "results/" + generatorName;
            Files.createDirectories(Paths.get(dirPath));

            String filePath = dirPath + "/res-" + num + ".csv";

            // convert records → CSV lines
            List<String> lines = new ArrayList<>();
            lines.add("iteration,best_heuristic,avg_simulation"); // header

            for (IterationProgress r : records) {
                lines.add(r.iteration + "," + r.bestHeuristicScore + "," + r.averageSimulationScore);
            }

            Files.write(Paths.get(filePath), lines);
            System.out.println("Saved CSV: " + filePath);

        } catch (IOException e) {
            System.out.println("Failed to save results: " + e.getMessage());
        }
    }
}
