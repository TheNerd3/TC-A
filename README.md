# Compilador de Subconjunto C++

Proyecto académico de Técnicas de Compilación implementado en Java con ANTLR4. El sistema procesa un subconjunto de C++ y ejecuta un pipeline completo: análisis léxico, sintáctico, semántico, generación de código intermedio, optimización y reporte.

## Tabla de Contenidos

1. [Objetivo](#objetivo)
2. [Tecnologías](#tecnologías)
3. [Características Principales](#características-principales)
4. [Subconjunto del Lenguaje Soportado](#subconjunto-del-lenguaje-soportado)
5. [Arquitectura del Proyecto](#arquitectura-del-proyecto)
6. [Pipeline de Compilación](#pipeline-de-compilación)
7. [Optimizaciones Implementadas](#optimizaciones-implementadas)
8. [IA y Reportes](#ia-y-reportes)
9. [Estructura de Carpetas](#estructura-de-carpetas)
10. [Requisitos](#requisitos)
11. [Instalación y Compilación](#instalación-y-compilación)
12. [Ejecución](#ejecución)
13. [Archivos de Salida](#archivos-de-salida)
14. [Casos de Prueba Recomendados](#casos-de-prueba-recomendados)
15. [Códigos de Salida](#códigos-de-salida)
16. [Limitaciones Actuales](#limitaciones-actuales)
17. [Documentación Relacionada](#documentación-relacionada)

## Objetivo

Construir un compilador educativo para un subconjunto de C++ que permita demostrar de forma práctica las etapas clásicas de compilación y la integración de optimización tradicional + heurística basada en ML.

## Tecnologías

- Java 17
- Maven
- ANTLR4
- Smile 3.1.1 (árbol de decisión para optimización con ML)
- SLF4J Simple (binding de logging)

## Características Principales

- Lexer y parser generados con ANTLR4.
- AST propio para visualización y depuración.
- Análisis semántico con tabla de símbolos y ámbitos anidados.
- Generación de código intermedio de tres direcciones.
- Pipeline de optimización con múltiples pasadas.
- Reporte AI de errores/warnings y explicación por etapa de optimización.
- Salida textual de artefactos en carpeta `output`.

## Subconjunto del Lenguaje Soportado

### Tipos

- `int`, `float`, `double`, `char`, `bool`, `void`, `string`, `date`

### Construcciones

- Declaraciones de variables (con/sin inicialización)
- Arreglos con índice
- Declaración y definición de funciones
- Llamadas a funciones con argumentos
- Asignaciones
- Expresiones aritméticas, relacionales y lógicas
- `if` / `else`
- `while`
- `for`
- `break` / `continue`
- `return`

## Arquitectura del Proyecto

### Núcleo de compilación

- `src/main/java/com/example/cpplexer/LexerMain.java`
  Orquesta todo el pipeline, manejo de errores, archivos de salida y reporte AI.

- `src/main/java/com/example/cpplexer/SemanticVisitor.java`
  Ejecuta validaciones semánticas sobre el árbol parseado.

- `src/main/java/com/example/cpplexer/SymbolTable.java`
  Gestiona ámbitos y metadatos de símbolos (tipo, inicialización, uso, etc.).

### Gramáticas ANTLR

- `src/main/antlr4/com/example/cpplexer/CppSubsetLexer.g4`
- `src/main/antlr4/com/example/cpplexer/CppSubsetParser.g4`

### Código intermedio

- `src/main/java/com/example/cpplexer/ir/IntermediateCodeGenerator.java`
- `src/main/java/com/example/cpplexer/ir/IntermediateCode.java`

### Optimizadores

Ubicados en `src/main/java/com/example/cpplexer/optimizer`:

- `ConstantPropagationOptimizer`
- `ExpressionSimplifierOptimizer`
- `CopyPropagationOptimizer`
- `DeadCodeEliminationOptimizer`
- `MLDeadCodeOptimizer`

### IA de soporte y reportes

- `src/main/java/com/example/cpplexer/ai/AIRecommendationService.java`

## Pipeline de Compilación

1. **Análisis léxico**
   - Tokeniza la entrada.
   - Registra tipo, lexema, línea y columna.
   - Si hay errores léxicos, termina con código `2`.

2. **Análisis sintáctico**
   - Parsea usando la regla raíz `program`.
   - Si hay errores sintácticos, termina con código `1`.

3. **Construcción de AST**
   - Construye AST propio (`AstBuilder`).
   - Puede mostrarlo en GUI con `--gui`.

4. **Análisis semántico**
   - Variables no declaradas/redeclaradas.
   - Compatibilidad de tipos en asignaciones/retornos.
   - Uso potencial de variables no inicializadas (warning).
   - Validación de arreglos, llamadas y control de flujo.
   - Si hay errores críticos, termina con código `3`.

5. **Generación de código intermedio**
   - Formato de tres direcciones (`tN`, `Lx`, `goto`, `ifFalse`, `arg`, `call`).
   - Escribe `output/intermediate_code.txt`.

6. **Optimización**
   - Ejecuta secuencia de optimizadores.
   - Escribe `output/optimized_code.txt`.

7. **Reporte AI**
   - Consolida warnings/errores y explicación de pasadas.
   - Escribe `output/ai_report.txt`.

## Optimizaciones Implementadas

### 1) Propagación de constantes

- Reemplaza identificadores por constantes conocidas.
- Limpia estado al cruzar fronteras de control (labels/saltos/funciones) para mantener seguridad semántica.

### 2) Simplificación de expresiones

- Simplifica reglas algebraicas (`x + 0`, `x * 1`, `x * 0`, etc.).
- Pliega constantes enteras (`2 + 3 -> 5`).

### 3) Propagación de copias

- Propaga equivalencias de asignaciones simples (`a = b`, `t1 = 50`).
- Resuelve cadenas de copias transitivas cuando es seguro.
- Facilita eliminaciones posteriores.

### 4) Eliminación de código muerto

- Elimina asignaciones a identificadores no usados.
- Elimina declaraciones huérfanas de variables/temporales no usados.

### 5) Eliminación con ML (Decision Tree)

- Entrena un árbol de decisión con Smile (`smile-core`).
- Clasifica asignaciones candidatas a “dead code”.
- Features actuales:
  - `asignaciones`
  - `lecturas`
  - `dentroDeLoop`
  - `dentroDeIf`
  - `esTemporal`
  - `rhsSimple`
  - `tieneCall`
  - `esConstante`

## IA y Reportes

El archivo `output/ai_report.txt` incluye:

- Resumen del archivo analizado
- Cantidad de errores y warnings semánticos
- Explicación de cada pasada de optimización

Nota: el reporte ahora diferencia entre:

- cambios en cantidad de instrucciones,
- cambios de contenido con igual cantidad,
- ausencia real de cambios.

## Estructura de Carpetas

```text
.
├─ examples/                 # Casos de entrada
├─ output/                   # Artefactos generados
├─ src/main/antlr4/          # Gramáticas ANTLR
├─ src/main/java/            # Código Java
├─ target/                   # Build de Maven
├─ MANUAL_USUARIO.md
├─ INFORME_TECNICO.md
├─ README.md
└─ pom.xml
```

## Requisitos

- JDK 17 o superior
- Maven 3.6 o superior

## Instalación y Compilación

```bash
mvn clean package
```

## Ejecución

### Ejecución básica

```bash
mvn -q exec:java -Dexec.mainClass="com.example.cpplexer.LexerMain" -Dexec.args="examples/final/ejemploCorrecto.cpp"
```

### Ejecución con visualización de AST

```bash
mvn -q exec:java -Dexec.mainClass="com.example.cpplexer.LexerMain" -Dexec.args="examples/final/ejemploCorrecto.cpp --gui"
```

## Archivos de Salida

- `output/intermediate_code.txt`: código de tres direcciones antes de optimizar.
- `output/optimized_code.txt`: código luego de optimizaciones.
- `output/ai_report.txt`: resumen semántico y explicaciones de optimización.

## Casos de Prueba Recomendados

### Válidos

- `examples/valid/ejemplo1.cpp`
- `examples/valid/ejemplo2.cpp`
- `examples/valid/optimizacion.cpp`
- `examples/valid/optimizacion2.cpp`
- `examples/final/ejemploCorrecto.cpp`

### Inválidos (léxico)

- `examples/invalid/error_lexico1.cpp`
- `examples/invalid/error_lexico2.cpp`

### Inválidos (semántico)

- `examples/invalid/error_semantico1.cpp`
- `examples/invalid/error_semantico2.cpp`
- `examples/final/ejemploErrores.cpp`

## Códigos de Salida

- `0`: ejecución sin errores críticos
- `1`: error general/sintáctico
- `2`: error léxico
- `3`: error semántico

## Limitaciones Actuales

- Las optimizaciones priorizan seguridad y legibilidad sobre agresividad.
- La optimización por ML es heurística y depende del dataset de entrenamiento.
- No hay backend a código máquina/bytecode: la salida final es IR textual optimizado.

## Documentación Relacionada

- `MANUAL_USUARIO.md`: guía rápida de uso.
- `INFORME_TECNICO.md`: detalles técnicos, decisiones de diseño y fundamentos.
