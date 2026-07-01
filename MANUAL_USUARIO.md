# Manual de Usuario

## 1. Descripción General

Este proyecto implementa un compilador educativo para un subconjunto de C++.
El flujo principal del sistema es:

1. análisis léxico
2. análisis sintáctico
3. construcción de AST
4. análisis semántico
5. generación de código intermedio
6. optimización
7. generación de reportes

El ejecutable principal es [src/main/java/com/example/cpplexer/LexerMain.java](src/main/java/com/example/cpplexer/LexerMain.java).

## 2. Requisitos

- JDK 17 o superior
- Maven 3.6 o superior
- Terminal compatible con ANSI para ver colores en la salida

## 3. Compilación

Desde la raíz del proyecto:

```bash
mvn clean package
```

Si el proyecto ya fue compilado, también puedes ejecutar directamente el comando de `exec:java` sin repetir el empaquetado completo.

## 4. Ejecución

Comando recomendado:

```bash
mvn -q exec:java -Dexec.mainClass="com.example.cpplexer.LexerMain" -Dexec.args="examples/final/ejemploCorrecto.cpp"
```

El argumento final debe ser la ruta del archivo fuente a analizar.

### Ejecución con AST en ventana gráfica

```bash
mvn -q exec:java -Dexec.mainClass="com.example.cpplexer.LexerMain" -Dexec.args="examples/final/ejemploCorrecto.cpp --gui"
```

## 5. Qué hace el compilador

### 5.1 Análisis léxico

- Identifica tokens del subconjunto soportado.
- Reporta caracteres no reconocidos como errores léxicos.
- Imprime una tabla con tipo, lexema, línea y columna.

### 5.2 Análisis sintáctico

- Usa ANTLR4 con regla raíz `program`.
- Construye el árbol sintáctico.
- Detiene el pipeline si hay errores sintácticos.

### 5.3 AST

- Construye un AST propio con `AstBuilder`.
- Puede visualizarse con `--gui`.

### 5.4 Análisis semántico

- Verifica variables declaradas y uso correcto de ámbitos.
- Detecta redeclaraciones.
- Revisa compatibilidad de tipos.
- Detecta variables potencialmente no inicializadas como warning.
- Valida arreglos, llamadas a funciones y retornos.
- Controla condiciones de `if`, `while` y `for`.
- Verifica que `break` y `continue` solo se usen dentro de bucles.

### 5.5 Generación de código intermedio

- Genera código de tres direcciones.
- Usa temporales `tN` y etiquetas `Lx`.
- Traduce asignaciones, expresiones, arreglos, llamadas, retornos y control de flujo.

### 5.6 Optimización

El pipeline actual aplica estas pasadas:

- propagación de constantes
- simplificación de expresiones
- propagación de copias
- eliminación de código muerto
- eliminación de código muerto con ML

## 6. Optimizaciones

### Propagación de constantes

Reemplaza valores conocidos por constantes cuando es seguro hacerlo.

### Simplificación de expresiones

Aplica reglas como:

- `x + 0 -> x`
- `x * 1 -> x`
- `x * 0 -> 0`
- plegado de constantes como `2 + 3 -> 5`

### Propagación de copias

Propaga asignaciones equivalentes para exponer redundancias y habilitar eliminaciones posteriores.

### Eliminación de código muerto

Elimina asignaciones y declaraciones de variables que no se usan en el programa.

### Eliminación con ML

Usa un árbol de decisión entrenado con Smile para clasificar asignaciones candidatas a eliminación.

Las señales del modelo incluyen:

- cantidad de asignaciones
- cantidad de lecturas
- si está dentro de un loop
- si está dentro de un `if`
- si es temporal
- si el lado derecho es simple
- si contiene llamadas
- si la expresión es constante

## 7. Archivos generados

Si la compilación no tiene errores críticos, se generan:

- `output/intermediate_code.txt`: código intermedio antes de optimizar
- `output/optimized_code.txt`: código intermedio luego de optimizar
- `output/ai_report.txt`: reporte semántico y explicaciones de optimización

## 8. Interpretación de la salida

### Colores

- verde: fase completada correctamente
- amarillo: inicio de fase o warnings no críticos
- rojo: error crítico que detiene el pipeline

### Warnings

Los warnings no detienen la compilación. Por ejemplo, una variable declarada pero no usada se informa como warning.

### Errores críticos

Los errores críticos detienen el proceso antes de generar código intermedio. Ejemplos:

- carácter no reconocido
- error sintáctico
- variable no declarada
- tipos incompatibles
- `break` o `continue` fuera de un bucle
- retorno incompatible con el tipo declarado de la función

## 9. Casos de prueba recomendados

### Válidos

- `examples/valid/ejemplo1.cpp`
- `examples/valid/ejemplo2.cpp`
- `examples/valid/optimizacion.cpp`
- `examples/valid/optimizacion2.cpp`
- `examples/final/ejemploCorrecto.cpp`

### Con errores léxicos

- `examples/invalid/error_lexico1.cpp`
- `examples/invalid/error_lexico2.cpp`

### Con errores semánticos

- `examples/invalid/error_semantico1.cpp`
- `examples/invalid/error_semantico2.cpp`
- `examples/final/ejemploErrores.cpp`

## 10. Códigos de salida

- `0`: ejecución sin errores críticos
- `1`: error general o sintáctico
- `2`: errores léxicos
- `3`: errores semánticos

## 11. Limpieza de salidas

La carpeta `output/` contiene archivos generados por la ejecución del compilador. Puede eliminarse sin afectar el código fuente.

## 12. Notas útiles

- La carpeta `target/` se genera con Maven.
- La carpeta `gen/` contiene código generado por ANTLR.
- Si ejecutas con `--gui`, se abre una ventana con el AST.

## 13. Documentación complementaria

- `README.md`: resumen general del proyecto.
- `INFORME_TECNICO.md`: arquitectura, decisiones técnicas y pruebas.
