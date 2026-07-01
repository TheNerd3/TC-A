package com.example.cpplexer.optimizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.cpplexer.ir.IntermediateCode;

public class DeadCodeEliminationOptimizer implements Optimizer {
    @Override
    public String getName() {
        return "eliminacion de temporales muertos";
    }

    @Override
    public IntermediateCode optimize(IntermediateCode input) {
        Set<String> usedIdentifiers = collectUsedIdentifiers(input.getInstructions());
        List<String> optimized = new ArrayList<>();

        for (String instruction : input.getInstructions()) {
            String trimmed = instruction.trim();

            // Handle declarations: remove if the declared identifier is never used.
            if (trimmed.startsWith("declare ")) {
                String[] parts = trimmed.split("\\s+");
                if (parts.length >= 2) {
                    String maybeIdent = parts[parts.length - 1];
                    if (isSimpleIdentifier(maybeIdent) && !usedIdentifiers.contains(maybeIdent)) {
                        continue; // drop unused declaration
                    }
                }
                optimized.add(instruction);
                continue;
            }

            int assignIndex = instruction.indexOf(" = ");
            if (assignIndex < 0) {
                optimized.add(instruction);
                continue;
            }

            String target = instruction.substring(0, assignIndex).trim();
            // If the target is a simple identifier and it's never used anywhere, remove the assignment.
            if (isSimpleIdentifier(target) && !usedIdentifiers.contains(target)) {
                continue;
            }

            optimized.add(instruction);
        }

        return new IntermediateCode(optimized);
    }
    private Set<String> collectUsedIdentifiers(List<String> instructions) {
        Set<String> used = new HashSet<>();

        for (String instruction : instructions) {
            // Skip declarations like "declare int x" — they are not uses.
            String trimmed = instruction.trim();
            if (trimmed.startsWith("declare ")) {
                continue;
            }

            int assignIndex = instruction.indexOf(" = ");
            String rightSide = assignIndex >= 0 ? instruction.substring(assignIndex + 3) : instruction;
            for (String token : rightSide.split("[^a-zA-Z0-9_]+")) {
                if (token.matches("[A-Za-z_][A-Za-z0-9_]*")) {
                    used.add(token);
                }
            }
        }

        return used;
    }

    private boolean isSimpleIdentifier(String value) {
        return value != null && value.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }
}
