package com.example.cpplexer.optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.cpplexer.ir.IntermediateCode;

public class CopyPropagationOptimizer implements Optimizer {
    @Override
    public String getName() {
        return "propagacion de copias";
    }

    @Override
    public IntermediateCode optimize(IntermediateCode input) {
        Map<String, String> copies = new HashMap<>();
        List<String> optimized = new ArrayList<>();

        for (String instruction : input.getInstructions()) {
            if (isControlBoundary(instruction)) {
                // Control boundaries invalidate copy knowledge
                copies.clear();
                optimized.add(instruction);
                continue;
            }

            int assignIndex = instruction.indexOf(" = ");
            if (assignIndex >= 0 && !instruction.contains("call ")) {
                // Don't rewrite the left-hand side (target). Only rewrite the RHS using known copies.
                String target = instruction.substring(0, assignIndex).trim();
                String rhs = instruction.substring(assignIndex + 3).trim();

                String rewrittenRhs = rewriteUsingCopies(rhs, copies);
                String rewritten = target + " = " + rewrittenRhs;

                // Update copy map based on rewritten RHS
                if (isSimpleIdentifier(rewrittenRhs) || isConstant(rewrittenRhs)) {
                    copies.put(target, resolveCopy(rewrittenRhs, copies));
                } else {
                    copies.remove(target);
                }

                optimized.add(rewritten);
            } else {
                // Non-assignment: rewrite whole instruction
                String rewritten = rewriteUsingCopies(instruction, copies);
                optimized.add(rewritten);
            }
        }

        return new IntermediateCode(optimized);
    }

    private String rewriteUsingCopies(String instruction, Map<String, String> copies) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < instruction.length()) {
            char c = instruction.charAt(i);
            if (Character.isLetter(c) || c == '_') {
                int j = i + 1;
                while (j < instruction.length() && (Character.isLetterOrDigit(instruction.charAt(j)) || instruction.charAt(j) == '_')) j++;
                String token = instruction.substring(i, j);
                String replacement = resolveCopy(token, copies);
                out.append(replacement);
                i = j;
            } else {
                out.append(c);
                i++;
            }
        }
        return out.toString();
    }

    private String resolveCopy(String token, Map<String, String> copies) {
        if (!isSimpleIdentifier(token)) {
            return token;
        }

        String current = token;
        Set<String> seen = new HashSet<>();
        while (copies.containsKey(current) && seen.add(current)) {
            String next = copies.get(current);
            if (!isSimpleIdentifier(next)) {
                return next;
            }
            current = next;
        }

        return current;
    }

    private boolean isControlBoundary(String instruction) {
        return instruction.endsWith(":")
                || instruction.startsWith("goto ")
                || instruction.startsWith("ifFalse ")
                || instruction.startsWith("func ")
                || instruction.startsWith("endfunc ");
    }

    private boolean isSimpleIdentifier(String value) {
        return value != null && value.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }

    private boolean isConstant(String value) {
        if (value == null) return false;
        return value.matches("-?[0-9]+") || value.matches("-?[0-9]+\\.[0-9]+") || value.matches("'[^']*'")
                || value.matches("\"[^\"]*\"") || "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }
}
