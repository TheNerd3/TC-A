# Analizador Léxico — Subconjunto de C++ (ANTLR4 + Java)

Proyecto que implementa la primera fase (análisis léxico) del trabajo final de Técnicas de Compilación.

Resumen:
- Lexer construido con ANTLR4 (archivo `CppSubsetLexer.g4`).
- Runner en Java que genera una tabla de tokens e informa errores léxicos.

Estructura del proyecto
```
raiz-del-proyecto/
  README.md
  .gitignore
  pom.xml
  src/
    main/
      antlr4/
        CppSubsetLexer.g4
      java/
        com/example/cpplexer/
          LexerMain.java
          TokenInfo.java
  examples/
    valid/
      ejemplo1.cpp
      ejemplo2.cpp
    invalid/
      error_lexico1.cpp
      error_lexico2.cpp
  output/
    .gitkeep
```

Requisitos previos
- Java 17+ (JDK instalado)
- Maven 3.6+
- ANTLR4 se maneja desde el plugin Maven incluido en `pom.xml`.

Instalación y compilación

1. Clona o descarga el repositorio.
2. Compila con Maven (esto generará el código ANTLR y compilará el proyecto):

```bash
mvn clean package
```

Ejecución

Para ejecutar el analizador léxico sobre un archivo fuente:

```bash
mvn -q exec:java -Dexec.mainClass="com.example.cpplexer.LexerMain" -Dexec.args="examples/valid/ejemplo1.cpp"
```

O bien, puedes ejecutar el JAR generado (asegúrate de incluir las fuentes generadas por ANTLR en el classpath):

```bash
java -cp target/cpp-lexer-1.0-SNAPSHOT.jar;target/generated-sources/antlr4 com.example.cpplexer.LexerMain examples/valid/ejemplo1.cpp
```

Salida
- Imprime una tabla de tokens con columnas: `TYPE | LEXEMA | LINE | COL`.
- Si se detectan errores léxicos (tokens inválidos), se muestran en rojo con línea, columna y lexema problemático.
- Si no hay errores, se muestra un mensaje de éxito en verde.

Tokens reconocidos (resumen)
- Palabras reservadas: `int, char, double, void, if, else, for, while, break, continue, return`
- Identificadores: nombres que comienzan con letra o `_` y luego letras/dígitos/`_`.
- Literales: enteros, decimales (float), `char` y `string` (opcional pero soportado).
- Operadores: `+ - * / % = == != < <= > >= && || ! ++ --`
- Separadores: `; , ( ) { }`
- Comentarios: `//` (línea) y `/* ... */` (bloque) — ambos se ignoran.
- Espacios y saltos de línea son ignorados.
- Cualquier símbolo no reconocido produce un token `INVALID` y se reporta como error léxico.

Limitaciones
- Esta etapa implementa únicamente el análisis léxico. No hay parser ni análisis semántico.
- El lexer cubre el subconjunto solicitado; extensiones posteriores son sencillas de agregar en `CppSubsetLexer.g4`.

Ejemplos
- `examples/valid/ejemplo1.cpp`, `examples/valid/ejemplo2.cpp` — ejemplos válidos.
- `examples/invalid/error_lexico1.cpp`, `examples/invalid/error_lexico2.cpp` — ejemplos con errores léxicos para probar el reporting.

Contacto
Si quieres que implemente la fase siguiente (sintaxis), puedo continuar a partir de este punto.
# TC-A