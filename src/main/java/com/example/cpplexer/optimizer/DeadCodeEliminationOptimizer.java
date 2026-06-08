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
        Set<String> usedTemporaries = collectUsedTemporaries(input.getInstructions());
        List<String> optimized = new ArrayList<>();

        for (String instruction : input.getInstructions()) {
            String assignedTemporary = assignedTemporary(instruction);
            if (assignedTemporary != null && !usedTemporaries.contains(assignedTemporary)) {
                continue;
            }
            optimized.add(instruction);
        }

        return new IntermediateCode(optimized);
    }

    private Set<String> collectUsedTemporaries(List<String> instructions) {
        Set<String> used = new HashSet<>();

        for (String instruction : instructions) {
            int assignIndex = instruction.indexOf(" = ");
            String rightSide = assignIndex >= 0 ? instruction.substring(assignIndex + 3) : instruction;
            for (String token : rightSide.split("[^a-zA-Z0-9_]+")) {
                if (token.matches("t[0-9]+")) {
                    used.add(token);
                }
            }
        }

        return used;
    }

    private String assignedTemporary(String instruction) {
        int assignIndex = instruction.indexOf(" = ");
        if (assignIndex < 0) {
            return null;
        }

        String target = instruction.substring(0, assignIndex);
        if (target.matches("t[0-9]+")) {
            return target;
        }

        return null;
    }
}
