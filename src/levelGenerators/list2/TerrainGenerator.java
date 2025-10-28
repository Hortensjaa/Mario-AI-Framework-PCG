package levelGenerators.list2;

import java.util.*;

public class TerrainGenerator {

    private record Rule(String expansion, double probability) {}

    private final Map<String, List<Rule>> grammarRules = new HashMap<>();
    private final Random random = new Random();

    public TerrainGenerator() {
        // <s> ::= <c><s> | <c>
        grammarRules.put("s", List.of(
                new Rule("cs", 0.99) // recursive expansion (continue)
//                new Rule("c", 0.01)    // stop expansion
        ));

        // <c> ::= G | J | T | P | B
        grammarRules.put("c", List.of(
                new Rule("G", 0.3),   // ground
                new Rule("J", 0.3),   // jump
                new Rule("T", 0.15),   // tube
                new Rule("P", 0.1),   // pit
                new Rule("B", 0.05)    // bullet bill
        ));
    }

    /**
     * Generate a random string starting from given symbol.
     * Expands recursively until all nonterminals are replaced or maxDepth reached.
     */
    public String generate(int maxDepth) {
        StringBuilder result = new StringBuilder();
        expand("s", result, 0, maxDepth);
        return result.toString();
    }

    /** Recursively expand one symbol */
    private void expand(String symbol, StringBuilder out, int depth, int maxDepth) {
        if (depth > maxDepth) return; // safety guard
        List<Rule> rules = grammarRules.get(symbol);

        if (rules == null) {
            // terminal — just append to output
            out.append(symbol);
            return;
        }

        // Choose rule weighted by probability
        String expansion = weightedPick(rules);

        // For each character in expansion, expand again if nonterminal
        for (char ch : expansion.toCharArray()) {
            expand(String.valueOf(ch), out, depth + 1, maxDepth);
        }
    }

    private String weightedPick(List<Rule> rules) {
        double total = rules.stream().mapToDouble(Rule::probability).sum();
        double r = random.nextDouble() * total;
        double cumulative = 0;

        for (Rule rule : rules) {
            cumulative += rule.probability();
            if (r <= cumulative) {
                return rule.expansion();
            }
        }
        return rules.get(rules.size() - 1).expansion(); // fallback
    }
}
