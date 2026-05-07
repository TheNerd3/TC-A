package com.example.cpplexer;

import java.util.ArrayList;
import java.util.List;

/**
 * Entrada de la tabla de símbolos.
 * Guarda metadatos de variables y funciones, no valores de ejecución.
 */
public class SymbolTable {

    public enum Kind { VARIABLE, ARRAY, FUNCTION }

    public static class Symbol {
        public final String name;
        public final String type;       // "int", "double", "char", "bool", "void"
        public final Kind kind;

        // Solo para arrays
        public final int arraySize;

        // Solo para funciones
        public final List<String> paramTypes; // tipos de parámetros en orden

        /** Constructor para variable simple */
        public Symbol(String name, String type) {
            this.name = name;
            this.type = type;
            this.kind = Kind.VARIABLE;
            this.arraySize = -1;
            this.paramTypes = null;
        }

        /** Constructor para array */
        public Symbol(String name, String type, int arraySize) {
            this.name = name;
            this.type = type;
            this.kind = Kind.ARRAY;
            this.arraySize = arraySize;
            this.paramTypes = null;
        }

        /** Constructor para función (declaración o definición) */
        public Symbol(String name, String returnType, List<String> paramTypes) {
            this.name = name;
            this.type = returnType;
            this.kind = Kind.FUNCTION;
            this.arraySize = -1;
            this.paramTypes = new ArrayList<>(paramTypes);
        }

        @Override
        public String toString() {
            if (kind == Kind.FUNCTION) {
                return "FUNC " + type + " " + name + "(" + String.join(", ", paramTypes) + ")";
            } else if (kind == Kind.ARRAY) {
                return "ARRAY " + type + " " + name + "[" + arraySize + "]";
            } else {
                return "VAR " + type + " " + name;
            }
        }
    }
}
