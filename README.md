# Compilador de Subconjunto C++ (ANTLR4 + Java)

Proyecto de Tecnicas de Compilacion con implementacion hasta la fase 3:

1. Analisis lexico
2. Analisis sintactico (AST)
3. Analisis semantico

## Funcionalidades implementadas

### 1) Analisis lexico
- Reconocimiento de tokens del subconjunto definido.
- Deteccion de errores lexicos.
- Tabla de tokens con tipo, lexema, linea y columna.

### 2) Analisis sintactico
- Parser ANTLR4 con regla raiz `program`.
- Construccion e impresion del arbol sintactico.
- Deteccion de errores sintacticos y corte de pipeline.

### 3) Analisis semantico
- Tabla de simbolos con ambitos anidados.
- Registro de funciones (declaraciones/definiciones y firma).
- Validacion de variables:
  - redeclaracion en el mismo ambito
  - uso de identificadores no declarados
  - chequeo de asignaciones por tipo
  - deteccion de uso potencial de variables no inicializadas (warning)
- Validacion de arreglos:
  - declaracion con tamano
  - acceso y asignacion por indice
  - chequeo de tipo del indice
- Validacion de funciones:
  - llamadas a funciones declaradas
  - cantidad y tipo de argumentos
  - retorno compatible con el tipo declarado
- Validacion de control de flujo:
  - condicion de `if`, `while` y `for` debe ser `bool`
  - `break` y `continue` solo dentro de bucles
- Reporte separado de errores (criticos) y warnings (no criticos).

## Requisitos
- JDK 17+
- Maven 3.6+

## Compilacion

```bash
mvn clean package
```

## Ejecucion

```bash
mvn -q exec:java -Dexec.mainClass="com.example.cpplexer.LexerMain" -Dexec.args="examples/valid/ejemploFinal.cpp"
```

## Casos de prueba sugeridos

### Validos
- `examples/valid/ejemplo1.cpp`
- `examples/valid/ejemplo2.cpp`
- `examples/valid/ejemploFinal.cpp`

### Invalidos (lexico)
- `examples/invalid/error_lexico1.cpp`
- `examples/invalid/error_lexico2.cpp`

### Invalidos (semantico)
- `examples/invalid/error_semantico1.cpp`
- `examples/invalid/error_semantico2.cpp`

## Codigos de salida
- `0`: sin errores criticos
- `1`: error general / sintactico
- `2`: errores lexicos
- `3`: errores semanticos
