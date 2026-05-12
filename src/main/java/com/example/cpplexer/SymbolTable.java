package com.example.cpplexer;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SymbolTable {
    // Una pila de diccionarios. Cada llave '{' crea un diccionario nuevo.
    private Stack<Map<String, Object>> scopes;

    public SymbolTable() {
        scopes = new Stack<>();
        enterScope(); // Creamos el ámbito Global al iniciar
    }

    // Entrar a una función o bloque {
    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    // Salir de una función o bloque }
    public void exitScope() {
        if (scopes.size() > 1) {
            scopes.pop();
        }
    }

    // Guardar una nueva variable
    public void define(String name, Object value) {
        if (scopes.peek().containsKey(name)) {
            throw new RuntimeException("ERROR SEMÁNTICO: La variable '" + name + "' ya fue declarada en este bloque.");
        }
        scopes.peek().put(name, value);
    }

    // Buscar el valor de una variable (desde el bloque local hasta el global)
    public Object resolve(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                return scopes.get(i).get(name);
            }
        }
        throw new RuntimeException("ERROR SEMÁNTICO: La variable '" + name + "' no está declarada.");
    }
}