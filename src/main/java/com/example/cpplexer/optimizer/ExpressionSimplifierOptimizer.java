package com.example.cpplexer.optimizer;

import java.util.ArrayList;
import java.util.List;

import com.example.cpplexer.ir.IntermediateCode;

public class ExpressionSimplifierOptimizer {
    public IntermediateCode optimize(IntermediateCode input) {
        List<String> optimized = new ArrayList<>();

        for (String instruction : input.getInstructions()) {
            optimized.add(simplifyInstruction(instruction));
        }

        return new IntermediateCode(optimized);
    }

    private String simplifyInstruction(String instruction) {
        int assignIndex = instruction.indexOf(" = ");
        if (assignIndex < 0) {
            return instruction;
        }

        String target = instruction.substring(0, assignIndex);
        String expression = instruction.substring(assignIndex + 3);
        String[] parts = expression.split(" ");

        if (parts.length != 3) {
            return instruction;
        }

        String left = parts[0];
        String operator = parts[1];
        String right = parts[2];
        String simplified = simplifyExpression(left, operator, right);
        return target + " = " + simplified;
    }

    private String simplifyExpression(String left, String operator, String right) {
        if (isInteger(left) && isInteger(right)) {
            return foldIntegers(Integer.parseInt(left), operator, Integer.parseInt(right));
        }

        if ("+".equals(operator)) {
            if ("0".equals(right)) {
                return left;
            }
            if ("0".equals(left)) {
                return right;
            }
        }

        if ("-".equals(operator) && "0".equals(right)) {
            return left;
        }

        if ("*".equals(operator)) {
            if ("0".equals(left) || "0".equals(right)) {
                return "0";
            }
            if ("1".equals(right)) {
                return left;
            }
            if ("1".equals(left)) {
                return right;
            }
        }

        if ("/".equals(operator) && "1".equals(right)) {
            return left;
        }

        return left + " " + operator + " " + right;
    }

    private String foldIntegers(int left, String operator, int right) {
        return switch (operator) {
            case "+" -> Integer.toString(left + right);
            case "-" -> Integer.toString(left - right);
            case "*" -> Integer.toString(left * right);
            case "/" -> right == 0 ? left + " / " + right : Integer.toString(left / right);
            case "%" -> right == 0 ? left + " % " + right : Integer.toString(left % right);
            default -> left + " " + operator + " " + right;
        };
    }

    private boolean isInteger(String value) {
        return value.matches("-?[0-9]+");
    }
}
