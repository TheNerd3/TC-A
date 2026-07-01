package com.example.cpplexer.ai;

import java.util.List;
import java.util.StringJoiner;

import org.antlr.v4.runtime.ParserRuleContext;

public class AIRecommendationService {
    private AIRecommendationService() {
        // Utility class
    }

    public static String explainSemanticIssue(String message, ParserRuleContext ctx, List<String> sourceLines) {
        if (message == null) {
            return null;
        }

        String lower = message.toLowerCase();
        String suggestion = null;

        if (lower.contains("variable no declarada") || lower.contains("no declarada")) {
            suggestion = "Verifica si el identificador está declarado en el ámbito actual o si hay un error de ortografía en el nombre.";
        } else if (lower.contains("tipos incompatibles") || lower.contains("incompatible")) {
            suggestion = "Revisa el tipo de la expresión y el tipo de destino; puede ser necesario cambiar la variable o usar un valor compatible.";
        } else if (lower.contains("return fuera de una función")) {
            suggestion = "Asegúrate de que la instrucción return aparezca solo dentro de una función, no en el bloque global.";
        } else if (lower.contains("parámetro") && lower.contains("void")) {
            suggestion = "Los parámetros no pueden ser void; utiliza un tipo válido como int, double, char, bool, string o date.";
        } else if (lower.contains("identificador redeclarado") || lower.contains("redeclarado")) {
            suggestion = "Renombra una de las declaraciones o elimina la redeclaración en el mismo ámbito.";
        } else if (lower.contains("función declarada pero no definida")) {
            suggestion = "Define la función declarada o elimina su prototipo si no se va a usar.";
        } else if (lower.contains("la función 'main'")) {
            suggestion = "Verifica que exista una función main con el nombre correcto para empezar la ejecución.";
        } else if (lower.contains("debe retornar")) {
            suggestion = "Asegúrate de que todas las rutas de la función devuelvan el tipo correcto.";
        } else if (lower.contains("break fuera de un bucle") || lower.contains("continue fuera de un bucle")) {
            suggestion = "Solo use break y continue dentro de bucles como while o for.";
        }

        if (suggestion == null && ctx != null && sourceLines != null) {
            int line = ctx.getStart().getLine();
            if (line >= 1 && line <= sourceLines.size()) {
                String source = sourceLines.get(line - 1).trim();
                suggestion = "Revisa la línea " + line + ": '" + source + "'. Asegúrate de que la sintaxis y los tipos sean correctos.";
            }
        }

        if (suggestion == null) {
            suggestion = "Revisa el mensaje de error y verifica el código cercano a la ubicación indicada.";
        }

        return suggestion;
    }

    public static String explainOptimizationPass(String optimizerName, List<String> before, List<String> after) {
        int removed = before.size() - after.size();
        boolean contentChanged = !before.equals(after);
        StringJoiner report = new StringJoiner(" ");
        report.add("[IA] Optimización:").add(optimizerName + ".");

        if (removed > 0) {
            report.add("Se eliminaron").add(String.valueOf(removed)).add("instrucción(es) redundantes o simplificables.");
        } else if (removed < 0) {
            report.add("Se generaron").add(String.valueOf(-removed)).add("instrucción(es) adicionales para preservar el comportamiento durante la optimización.");
        } else if (contentChanged) {
            report.add("No cambió la cantidad de instrucciones, pero sí su contenido.");
        } else {
            report.add("No se encontraron cambios netos en esta etapa.");
        }

        if (optimizerName.toLowerCase().contains("const") || optimizerName.toLowerCase().contains("constante")) {
            report.add("Esta etapa reemplaza valores conocidos con constantes para simplificar expresiones posteriores.");
        } else if (optimizerName.toLowerCase().contains("simplific")) {
            report.add("Esta etapa reduce expresiones aritméticas triviales como x + 0 o x * 1.");
        } else if (optimizerName.toLowerCase().contains("copia")) {
            report.add("Esta etapa propaga valores equivalentes para exponer copias redundantes y habilitar otras eliminaciones.");
        } else if (optimizerName.toLowerCase().contains("muerto")) {
            report.add("Esta etapa elimina asignaciones y declaraciones de variables que no se usan en el programa.");
        }

        return report.toString();
    }

    public static String summarizeCompilation(String sourcePath, int tokenCount, boolean hasSyntaxError, int semanticErrors, int semanticWarnings, List<String> optimizerNames) {
        StringJoiner summary = new StringJoiner(System.lineSeparator());
        summary.add("Resumen de compilación AI para: " + sourcePath);
        summary.add("Tokens leídos: " + tokenCount);
        summary.add("Errores sintácticos detectados: " + (hasSyntaxError ? "sí" : "no"));
        summary.add("Errores semánticos: " + semanticErrors);
        summary.add("Warnings semánticos: " + semanticWarnings);
        summary.add("Optimizadores aplicados: " + String.join(", ", optimizerNames));

        if (semanticErrors > 0) {
            summary.add("El compilador encontró errores semánticos; es recomendable corregirlos antes de generar código intermedio.");
        } else if (hasSyntaxError) {
            summary.add("Hay errores sintácticos; revisa el árbol generado y ajusta la estructura del programa.");
        } else {
            summary.add("El flujo del compilador avanzó correctamente hasta optimización.");
        }

        summary.add("Si deseas acelerar la corrección, usa estas sugerencias como guía para corregir el código fuente.");
        return summary.toString();
    }

    public static String buildAiReport(String sourcePath, List<String> semanticErrors, List<String> semanticWarnings, List<String> optimizationExplanations) {
        StringJoiner report = new StringJoiner(System.lineSeparator());
        report.add("=== Reporte AI de Compilación ===");
        report.add("Archivo analizado: " + sourcePath);
        report.add("Errores semánticos: " + semanticErrors.size());
        for (String error : semanticErrors) {
            report.add(error);
        }
        report.add("Warnings semánticos: " + semanticWarnings.size());
        for (String warning : semanticWarnings) {
            report.add(warning);
        }
        report.add("Optimización: " + optimizationExplanations.size() + " explicaciones generadas.");
        for (String explanation : optimizationExplanations) {
            report.add(explanation);
        }
        report.add("=== Fin del reporte ===");
        return report.toString();
    }
}