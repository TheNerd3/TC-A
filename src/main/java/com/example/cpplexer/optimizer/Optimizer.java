package com.example.cpplexer.optimizer;

import com.example.cpplexer.ir.IntermediateCode;

public interface Optimizer {
    String getName();

    IntermediateCode optimize(IntermediateCode input);
}
