package com.example.cpplexer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    public static class Symbol {
        private final String name;
        private final String type;
        private final boolean array;
        private final int arraySize;
        private boolean initialized;
        private int usages;

        public Symbol(String name, String type, boolean array, int arraySize, boolean initialized) {
            this.name = name;
            this.type = type;
            this.array = array;
            this.arraySize = arraySize;
            this.initialized = initialized;
            this.usages = 0;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public boolean isArray() {
            return array;
        }

        public int getArraySize() {
            return arraySize;
        }

        public boolean isInitialized() {
            return initialized;
        }

        public void setInitialized(boolean initialized) {
            this.initialized = initialized;
        }

        public void markUsed() {
            usages++;
        }

        public int getUsages() {
            return usages;
        }
    }

    private final Deque<Map<String, Symbol>> scopes;

    public SymbolTable() {
        scopes = new ArrayDeque<>();
        enterScope();
    }

    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        if (scopes.size() > 1) {
            scopes.pop();
        }
    }

    public boolean existsInCurrentScope(String name) {
        return scopes.peek().containsKey(name);
    }

    public Symbol defineVariable(String name, String type, boolean initialized) {
        Symbol symbol = new Symbol(name, type, false, 0, initialized);
        scopes.peek().put(name, symbol);
        return symbol;
    }

    public Symbol defineArray(String name, String type, int size, boolean initialized) {
        Symbol symbol = new Symbol(name, type, true, size, initialized);
        scopes.peek().put(name, symbol);
        return symbol;
    }

    public Symbol resolve(String name) {
        for (Map<String, Symbol> scope : scopes) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        return null;
    }
}