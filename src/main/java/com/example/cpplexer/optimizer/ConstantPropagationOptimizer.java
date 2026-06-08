package com.example.cpplexer.optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.cpplexer.ir.IntermediateCode;

public class ConstantPropagationOptimizer implements Optimizer {
    @Override
    public String getName() {
        return "propagacion de constantes";
    }

    @Override
    public IntermediateCode optimize(IntermediateCode input) {
        Map<String, String> constants = new HashMap<>();
        List<String> optimized = new ArrayList<>();

        for (String instruction : input.getInstructions()) {
            String rewritten = rewriteInstruction(instruction, constants);
            optimized.add(rewritten);
            updateConstants(rewritten, constants);
        }

        return new IntermediateCode(optimized);
    }

    private String rewriteInstruction(String instruction, Map<String, String> constants) {
        int assignIndex = instruction.indexOf(" = ");
        if (assignIndex < 0 || instruction.contains("call ")) {
            return instruction;
        }

        String target = instruction.substring(0, assignIndex);
        String expression = instruction.substring(assignIndex + 3);
        String[] parts = expression.split(" ");

        if (parts.length == 1) {
            return target + " = " + constants.getOrDefault(parts[0], parts[0]);
        }

        if (parts.length == 3) {
            String left = constants.getOrDefault(parts[0], parts[0]);
            String right = constants.getOrDefault(parts[2], parts[2]);
            return target + " = " + left + " " + parts[1] + " " + right;
        }

        return instruction;
    }

    private void updateConstants(String instruction, Map<String, String> constants) {
        int assignIndex = instruction.indexOf(" = ");
        if (assignIndex < 0) {
            return;
        }

        String target = instruction.substring(0, assignIndex);
        String expression = instruction.substring(assignIndex + 3);

        if (!isSimpleIdentifier(target)) {
            return;
        }

        if (isConstant(expression)) {
            constants.put(target, expression);
        } else {
            constants.remove(target);
        }
    }

    private boolean isConstant(String value) {
        return value.matches("-?[0-9]+")
                || value.matches("-?[0-9]+\\.[0-9]+")
                || value.matches("'[^']*'")
                || value.matches("\"[^\"]*\"")
                || "true".equals(value)
                || "false".equals(value)
                || "TRUE".equals(value)
                || "FALSE".equals(value);
    }

    private boolean isSimpleIdentifier(String value) {
        return value.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }
}
