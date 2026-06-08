# Informe Tecnico

## Portada

**Trabajo Final:** Compilador para un subconjunto de C++  
**Materia:** Tecnicas de Compilacion  
**Herramientas:** Java, Maven, ANTLR4  
**Repositorio:** https://github.com/TheNerd3/TC-A

## Introduccion

El proyecto implementa un compilador para un subconjunto de C++ usando ANTLR4 para las fases lexica y sintactica, y Java para el analisis semantico, la generacion de codigo intermedio y la optimizacion.

El objetivo es demostrar el pipeline completo de compilacion:

```text
Fuente C++
  -> analisis lexico
  -> analisis sintactico
  -> analisis semantico
  -> codigo intermedio
  -> optimizacion
  -> archivos de salida
```

## Subconjunto Implementado

Tipos soportados:

- `int`
- `char`
- `double`
- `void`
- extensiones: `float`, `bool`, `string`, `date`

Estructuras soportadas:

- declaraciones de variables
- arreglos con indice
- declaraciones y definiciones de funciones
- llamadas a funciones
- asignaciones
- expresiones aritmeticas
- expresiones relacionales y logicas
- `if` / `else`
- `while`
- `for`
- `break`
- `continue`
- `return`

## Arquitectura

El codigo fuente principal esta organizado en:

- `src/main/antlr4/com/example/cpplexer/CppSubsetLexer.g4`: reglas lexicas.
- `src/main/antlr4/com/example/cpplexer/CppSubsetParser.g4`: reglas sintacticas.
- `src/main/java/com/example/cpplexer/LexerMain.java`: pipeline principal.
- `src/main/java/com/example/cpplexer/SemanticVisitor.java`: analisis semantico.
- `src/main/java/com/example/cpplexer/SymbolTable.java`: tabla de simbolos con ambitos.
- `src/main/java/com/example/cpplexer/ir`: codigo intermedio.
- `src/main/java/com/example/cpplexer/optimizer`: optimizaciones.

## Analisis Lexico

El analizador lexico reconoce palabras reservadas, tipos, identificadores, literales, operadores, separadores y comentarios.

Tambien detecta caracteres no reconocidos mediante la regla `INVALID` y los reporta como errores lexicos. La salida incluye una tabla de tokens con tipo, lexema, linea y columna.

## Analisis Sintactico

El parser parte de la regla raiz `program`. La gramatica contempla declaraciones globales, funciones, bloques, sentencias y expresiones con precedencia.

Si ANTLR detecta errores sintacticos, el pipeline se detiene antes del analisis semantico.

La visualizacion actual usa `tree.toStringTree(parser)`, por lo que representa el arbol sintactico generado por ANTLR.

## Analisis Semantico

El analisis semantico se implementa con `SemanticVisitor`.

Validaciones principales:

- variables no declaradas
- redeclaracion en el mismo ambito
- variables de tipo `void`
- asignaciones incompatibles
- uso potencial de variables no inicializadas
- arreglos con indice no entero
- uso incorrecto de arreglos
- funciones declaradas y definidas
- cantidad y tipo de argumentos
- retorno compatible con el tipo de funcion
- `if`, `while` y `for` con condicion booleana
- `break` y `continue` solo dentro de bucles

El sistema distingue entre:

- errores criticos: abortan el pipeline
- warnings: se reportan, pero no impiden continuar

## Tabla de Simbolos

`SymbolTable` maneja una pila de ambitos. Cada simbolo almacena:

- nombre
- tipo
- si es arreglo
- tamanio de arreglo
- estado de inicializacion
- cantidad de usos

Esto permite validar alcance, redeclaraciones, uso de identificadores y warnings por variables no usadas.

## Codigo Intermedio

La generacion de codigo intermedio se implementa en `IntermediateCodeGenerator`.

El codigo usa una representacion textual de tres direcciones con:

- temporales: `t1`, `t2`, `t3`
- etiquetas: `L1`, `L2`, `L3`
- saltos condicionales: `ifFalse condicion goto Lx`
- saltos incondicionales: `goto Lx`
- llamadas: `arg valor` y `tN = call funcion, cantidad`

El resultado se escribe en:

- `output/intermediate_code.txt`

## Optimizacion

Las optimizaciones se aplican sobre el codigo intermedio mediante `OptimizerPipeline`.

Tecnicas implementadas:

1. Propagacion de constantes.
   - Reemplaza identificadores por constantes conocidas en instrucciones lineales.
   - Limpia el contexto al cruzar etiquetas, saltos y limites de funcion para evitar propagaciones inseguras.

2. Simplificacion de expresiones.
   - Aplica reglas como `x + 0 -> x`, `x * 1 -> x`, `x * 0 -> 0`.
   - Evalua operaciones enteras constantes como `2 + 3 -> 5`.

3. Eliminacion de temporales muertos.
   - Elimina asignaciones a temporales `tN` que no se usan posteriormente.

El resultado se escribe en:

- `output/optimized_code.txt`

## Pruebas

Casos validos:

- `examples/valid/ejemplo1.cpp`
- `examples/valid/ejemplo2.cpp`
- `examples/valid/optimizacion.cpp`
- `examples/final/ejemploCorrecto.cpp`

Casos invalidos:

- `examples/invalid/error_lexico1.cpp`
- `examples/invalid/error_lexico2.cpp`
- `examples/invalid/error_semantico1.cpp`
- `examples/invalid/error_semantico2.cpp`
- `examples/final/ejemploErrores.cpp`

Criterios verificados:

- Los ejemplos validos pasan las fases lexica, sintactica y semantica.
- Los errores lexicos se reportan antes de ejecutar el parser.
- Los errores semanticos abortan antes de generar codigo intermedio.
- Los ejemplos validos generan archivos de codigo intermedio y optimizado.

## Dificultades y Soluciones

- **Ambitos anidados:** se resolvio con una pila de mapas en `SymbolTable`.
- **Funciones y variables con nombres repetidos:** se valida la reutilizacion incompatible de identificadores.
- **Control de flujo en codigo intermedio:** se resolvio con etiquetas y pila de labels para `break` y `continue`.
- **Optimizacion conservadora:** la propagacion de constantes se limita en fronteras de control para evitar resultados incorrectos.

## Conclusiones

El proyecto implementa un pipeline completo de compilacion para un subconjunto de C++: analisis lexico, sintactico, semantico, generacion de codigo intermedio, optimizacion y generacion de archivos de salida.

La implementacion permite demostrar los conceptos centrales de la materia y deja una base extensible para agregar nuevas reglas semanticas, optimizaciones mas avanzadas o generacion de codigo final.

## Referencias

- ANTLR4 Documentation: https://www.antlr.org/
- The Java Language Specification: https://docs.oracle.com/javase/specs/
- Aho, Lam, Sethi, Ullman. Compilers: Principles, Techniques, and Tools.
