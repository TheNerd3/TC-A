package com.example.cpplexer.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntermediateCode {
    private final List<String> instructions = new ArrayList<>();

    public IntermediateCode() {
    }

    public IntermediateCode(List<String> instructions) {
        this.instructions.addAll(instructions);
    }

    public void add(String instruction) {
        instructions.add(instruction);
    }

    public List<String> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    public String asText() {
        return String.join(System.lineSeparator(), instructions) + System.lineSeparator();
    }
}
