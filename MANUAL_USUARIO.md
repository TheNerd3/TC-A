# Manual de Usuario

## Requisitos

- JDK 17 o superior.
- Maven 3.6 o superior.
- Terminal con soporte ANSI para ver colores en errores y warnings.

## Compilacion

Desde la raiz del proyecto:

```bash
mvn clean package
```

Si Maven no esta disponible, el proyecto tambien puede compilarse manualmente usando el JAR de ANTLR incluido y las fuentes generadas en `target/generated-sources`.

## Ejecucion

Comando recomendado:

```bash
mvn -q exec:java -Dexec.mainClass="com.example.cpplexer.LexerMain" -Dexec.args="examples/final/ejemploCorrecto.cpp"
```

El argumento final debe ser la ruta al archivo fuente C++ a analizar.

## Salidas

El compilador muestra por consola:

1. Tabla de tokens.
2. Resultado del analisis lexico.
3. Arbol sintactico generado por ANTLR.
4. Warnings semanticos, si existen.
5. Errores semanticos, si existen.
6. Confirmacion de generacion de codigo intermedio y optimizado.

Si no hay errores criticos, tambien genera:

- `output/intermediate_code.txt`: codigo de tres direcciones.
- `output/optimized_code.txt`: codigo intermedio luego de aplicar optimizaciones.

## Codigos de Salida

- `0`: ejecucion sin errores criticos.
- `1`: error general o error sintactico.
- `2`: errores lexicos.
- `3`: errores semanticos.

## Interpretacion de Mensajes

- Verde: fase completada correctamente.
- Amarillo: inicio de fase o warnings no criticos.
- Rojo: errores criticos que abortan el pipeline.

Los warnings no detienen la compilacion. Por ejemplo, una variable declarada pero no usada se informa como warning.

Los errores criticos detienen el proceso antes de generar codigo intermedio. Por ejemplo:

- caracter no reconocido
- error sintactico
- variable no declarada
- tipos incompatibles
- `break` o `continue` fuera de un bucle
- retorno incompatible con el tipo declarado de la funcion

## Ejemplos Recomendados

Archivos validos:

- `examples/valid/ejemplo1.cpp`
- `examples/valid/ejemplo2.cpp`
- `examples/valid/optimizacion.cpp`
- `examples/final/ejemploCorrecto.cpp`

Archivos con errores:

- `examples/invalid/error_lexico1.cpp`
- `examples/invalid/error_lexico2.cpp`
- `examples/invalid/error_semantico1.cpp`
- `examples/invalid/error_semantico2.cpp`
- `examples/final/ejemploErrores.cpp`

## Ejemplo de Uso Completo

```bash
mvn -q exec:java -Dexec.mainClass="com.example.cpplexer.LexerMain" -Dexec.args="examples/valid/optimizacion.cpp"
```

Resultado esperado:

- Analisis lexico correcto.
- Analisis sintactico correcto.
- Analisis semantico sin errores criticos.
- Generacion de `output/intermediate_code.txt`.
- Generacion de `output/optimized_code.txt`.

## Limpieza de Salidas

La carpeta `output/` contiene archivos generados y esta ignorada por Git. Puede borrarse sin afectar el codigo fuente.
