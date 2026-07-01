package com.example.cpplexer.optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.cpplexer.ir.IntermediateCode;

import smile.classification.DecisionTree;
import smile.base.cart.SplitRule;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.data.vector.IntVector;

public class MLDeadCodeOptimizer implements Optimizer {
    private static final String LABEL_COLUMN = "dead_code";
    private static final String[] FEATURE_NAMES = {
            "asignaciones",
            "lecturas",
            "dentroDeLoop",
        "dentroDeIf",
        "esTemporal",
        "rhsSimple",
        "tieneCall",
        "esConstante"
    };

    private static final int[][] TRAINING_X = {
        {1, 0, 0, 0, 1, 0, 0, 0},
        {2, 0, 0, 0, 0, 1, 0, 1},
        {1, 0, 1, 0, 1, 1, 0, 1},
        {1, 0, 0, 1, 0, 1, 0, 1},
        {1, 1, 0, 0, 1, 1, 0, 1},
        {1, 1, 0, 0, 0, 0, 1, 0},
        {1, 1, 0, 0, 0, 1, 0, 1},
        {3, 0, 0, 0, 0, 1, 0, 1},
        {1, 2, 0, 0, 0, 1, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0},
        {1, 0, 0, 0, 1, 1, 0, 0},
        {1, 0, 0, 0, 0, 1, 1, 0}
    };

    private static final int[] TRAINING_Y = {1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0};

    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");
    private static final Pattern GOTO_TARGET_PATTERN = Pattern.compile("\\bgoto\\s+([A-Za-z_][A-Za-z0-9_]*)");
    private static final Set<String> KEYWORDS = Set.of(
            "if",
            "ifFalse",
            "goto",
            "return",
            "call",
            "func",
            "endfunc",
            "while",
            "for",
            "do",
            "else",
            "then",
            "true",
            "false"
    );

    private final DecisionTree model;

    public MLDeadCodeOptimizer() {
        this.model = trainModel();
    }

    @Override
    public String getName() {
        return "eliminacion de codigo muerto con ML";
    }

    @Override
    public IntermediateCode optimize(IntermediateCode input) {
        if (input == null) {
            throw new IllegalArgumentException("IntermediateCode input cannot be null");
        }

        List<String> instructions = input.getInstructions();
        if (instructions.isEmpty() || model == null) {
            return new IntermediateCode(new ArrayList<>(instructions));
        }

        Map<String, Integer> assignmentCounts = collectAssignmentCounts(instructions);
        Map<String, Integer> readCounts = collectReadCounts(instructions);
        Map<String, Integer> labelPositions = collectLabelPositions(instructions);
        List<JumpContext> jumps = collectJumpContexts(instructions, labelPositions);

        List<String> optimized = new ArrayList<>();
        for (int index = 0; index < instructions.size(); index++) {
            String instruction = instructions.get(index);
            Assignment assignment = parseAssignment(instruction);
            if (assignment == null) {
                optimized.add(instruction);
                continue;
            }

            int[] features = buildFeatures(
                    assignment.target(),
                    assignment.expression(),
                    assignmentCounts,
                    readCounts,
                    index,
                    jumps);

            if (shouldRemove(features)) {
                continue;
            }

            optimized.add(instruction);
        }

        return new IntermediateCode(optimized);
    }

    private DecisionTree trainModel() {
        try {
            DataFrame trainingData = DataFrame.of(
                    IntVector.of(FEATURE_NAMES[0], extractColumn(0)),
                    IntVector.of(FEATURE_NAMES[1], extractColumn(1)),
                    IntVector.of(FEATURE_NAMES[2], extractColumn(2)),
                    IntVector.of(FEATURE_NAMES[3], extractColumn(3)),
                    IntVector.of(LABEL_COLUMN, TRAINING_Y));

            // The model is trained once at construction time so optimize() can make a
            // lightweight prediction per assignment candidate.
            return DecisionTree.fit(Formula.lhs(LABEL_COLUMN), trainingData, SplitRule.GINI, 20, 5, 1);
        } catch (RuntimeException exception) {
            System.err.println("No se pudo entrenar MLDeadCodeOptimizer: " + exception.getMessage());
            return null;
        }
    }

    private int[] extractColumn(int columnIndex) {
        int[] column = new int[TRAINING_X.length];
        for (int row = 0; row < TRAINING_X.length; row++) {
            column[row] = TRAINING_X[row][columnIndex];
        }
        return column;
    }

    private boolean shouldRemove(int[] features) {
        if (model == null) {
            return false;
        }

        try {
            double[][] data = {toDoubleArray(features)};
            DataFrame predictionFrame = DataFrame.of(data, FEATURE_NAMES);
            return model.predict(predictionFrame.stream().findFirst().orElseThrow()) == 1;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private int[] buildFeatures(String target, String expression, Map<String, Integer> assignmentCounts, Map<String, Integer> readCounts,
            int instructionIndex, List<JumpContext> jumps) {
        int assignments = assignmentCounts.getOrDefault(target, 0);
        int reads = readCounts.getOrDefault(target, 0);
        int dentroDeLoop = isInsideLoop(instructionIndex, jumps) ? 1 : 0;
        int dentroDeIf = isInsideIf(instructionIndex, jumps) ? 1 : 0;
        int esTemporal = isTemporalIdentifier(target) ? 1 : 0;
        int rhsSimple = isSimpleValue(expression) ? 1 : 0;
        int tieneCall = expression.contains("call ") ? 1 : 0;
        int esConstante = isConstant(expression) ? 1 : 0;
        return new int[] {assignments, reads, dentroDeLoop, dentroDeIf, esTemporal, rhsSimple, tieneCall, esConstante};
    }

    private Map<String, Integer> collectAssignmentCounts(List<String> instructions) {
        Map<String, Integer> counts = new HashMap<>();
        for (String instruction : instructions) {
            Assignment assignment = parseAssignment(instruction);
            if (assignment != null) {
                counts.merge(assignment.target(), 1, Integer::sum);
            }
        }
        return counts;
    }

    private Map<String, Integer> collectReadCounts(List<String> instructions) {
        Map<String, Integer> counts = new HashMap<>();
        for (String instruction : instructions) {
            Assignment assignment = parseAssignment(instruction);
            if (assignment != null) {
                countIdentifiers(assignment.expression(), counts);
                continue;
            }

            countConditionalJumpOperands(instruction, counts);

            if (isLabelDefinition(instruction) || isGoto(instruction)) {
                continue;
            }

            countIdentifiers(instruction, counts);
        }
        return counts;
    }

    private Map<String, Integer> collectLabelPositions(List<String> instructions) {
        Map<String, Integer> positions = new HashMap<>();
        for (int index = 0; index < instructions.size(); index++) {
            String instruction = instructions.get(index).trim();
            if (instruction.endsWith(":")) {
                positions.put(instruction.substring(0, instruction.length() - 1), index);
            }
        }
        return positions;
    }

    private List<JumpContext> collectJumpContexts(List<String> instructions, Map<String, Integer> labelPositions) {
        List<JumpContext> jumps = new ArrayList<>();
        for (int index = 0; index < instructions.size(); index++) {
            String instruction = instructions.get(index).trim();
            String targetLabel = jumpTarget(instruction);
            if (targetLabel == null) {
                continue;
            }

            Integer targetIndex = labelPositions.get(targetLabel);
            if (targetIndex != null) {
                jumps.add(new JumpContext(index, targetIndex, isConditionalJump(instruction)));
            }
        }
        return jumps;
    }

    private boolean isInsideIf(int instructionIndex, List<JumpContext> jumps) {
        for (JumpContext jump : jumps) {
            if (jump.conditional() && jump.jumpIndex() < instructionIndex && instructionIndex < jump.targetIndex()) {
                return true;
            }
        }
        return false;
    }

    private boolean isInsideLoop(int instructionIndex, List<JumpContext> jumps) {
        for (JumpContext jump : jumps) {
            if (jump.targetIndex() < jump.jumpIndex() && jump.targetIndex() < instructionIndex
                    && instructionIndex < jump.jumpIndex()) {
                return true;
            }
        }
        return false;
    }

    private Assignment parseAssignment(String instruction) {
        int assignIndex = instruction.indexOf(" = ");
        if (assignIndex < 0) {
            return null;
        }

        String target = instruction.substring(0, assignIndex).trim();
        String expression = instruction.substring(assignIndex + 3).trim();
        if (!isSimpleIdentifier(target) || expression.isEmpty()) {
            return null;
        }

        return new Assignment(target, expression);
    }

    private void countIdentifiers(String text, Map<String, Integer> counts) {
        Matcher matcher = IDENTIFIER_PATTERN.matcher(text);
        while (matcher.find()) {
            String token = matcher.group();
            if (isReadableIdentifier(token)) {
                counts.merge(token, 1, Integer::sum);
            }
        }
    }

    private boolean isReadableIdentifier(String token) {
        return !KEYWORDS.contains(token) && !isNumeric(token) && !looksLikeLabel(token);
    }

    private boolean isTemporalIdentifier(String value) {
        return value.matches("t[0-9]+");
    }

    private boolean isSimpleValue(String value) {
        return isSimpleIdentifier(value) || isConstant(value);
    }

    private boolean isConstant(String value) {
        return isNumeric(value)
                || value.matches("'[^']*'")
                || value.matches("\"[^\"]*\"")
                || "true".equalsIgnoreCase(value)
                || "false".equalsIgnoreCase(value)
                || "true".equals(value)
                || "false".equals(value)
                || "TRUE".equals(value)
                || "FALSE".equals(value);
    }

    private boolean isConditionalJump(String instruction) {
        return instruction.startsWith("ifFalse ") || instruction.startsWith("if ");
    }

    private void countConditionalJumpOperands(String instruction, Map<String, Integer> counts) {
        if (!isConditionalJump(instruction)) {
            return;
        }

        String trimmed = instruction.trim();
        String expression = trimmed.startsWith("ifFalse ")
                ? trimmed.substring("ifFalse ".length())
                : trimmed.substring("if ".length());

        int gotoIndex = expression.indexOf(" goto ");
        if (gotoIndex >= 0) {
            expression = expression.substring(0, gotoIndex).trim();
        }

        if (!expression.isEmpty()) {
            countIdentifiers(expression, counts);
        }
    }

    private boolean isGoto(String instruction) {
        return instruction.startsWith("goto ") || instruction.startsWith("ifFalse ") || instruction.startsWith("if ");
    }

    private boolean isLabelDefinition(String instruction) {
        return instruction.trim().endsWith(":");
    }

    private String jumpTarget(String instruction) {
        if (!isGoto(instruction)) {
            return null;
        }

        Matcher matcher = GOTO_TARGET_PATTERN.matcher(instruction);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private boolean isSimpleIdentifier(String value) {
        return value.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }

    private boolean isNumeric(String value) {
        return value.matches("-?[0-9]+") || value.matches("-?[0-9]+\\.[0-9]+") || value.matches("0x[0-9a-fA-F]+");
    }

    private boolean looksLikeLabel(String value) {
        return value.matches("L[0-9]+");
    }

    private double[] toDoubleArray(int[] values) {
        double[] converted = new double[values.length];
        for (int index = 0; index < values.length; index++) {
            converted[index] = values[index];
        }
        return converted;
    }

    private record Assignment(String target, String expression) {
    }

    private record JumpContext(int jumpIndex, int targetIndex, boolean conditional) {
    }
}