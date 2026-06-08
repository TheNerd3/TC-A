package com.example.cpplexer.optimizer;

import java.util.ArrayList;
import java.util.List;

import com.example.cpplexer.ir.IntermediateCode;

public class OptimizerPipeline {
    private final List<Optimizer> optimizers = new ArrayList<>();

    public OptimizerPipeline add(Optimizer optimizer) {
        optimizers.add(optimizer);
        return this;
    }

    public IntermediateCode optimize(IntermediateCode input) {
        IntermediateCode current = input;
        for (Optimizer optimizer : optimizers) {
            current = optimizer.optimize(current);
        }
        return current;
    }

    public List<String> getOptimizerNames() {
        List<String> names = new ArrayList<>();
        for (Optimizer optimizer : optimizers) {
            names.add(optimizer.getName());
        }
        return names;
    }
}
