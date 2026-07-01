int main() {
    // ---------------------------------------------------------
    // 1. CASCADA DE PROPAGACIÓN Y PLEGADO DE CONSTANTES
    // ---------------------------------------------------------
    int x = 5;
    int y = 10;

    // ConstantPropagation ve "5 * 10". ExpressionSimplifier lo pliega a "50".
    int z = x * y;

    // ---------------------------------------------------------
    // 2. SIMPLIFICACIÓN ALGEBRAICA AVANZADA
    // ---------------------------------------------------------
    // 'z' ya es 50. Sumar 0 se simplifica a "a = 50".
    int a = z + 0;

    // 'a' es 50. Multiplicar por 1 se simplifica a "b = 50".
    int b = a * 1;

    // Multiplicar por 0 se absorbe completamente. Queda "nulo = 0".
    int nulo = b * 0;

    // ---------------------------------------------------------
    // 3. LA PRUEBA DE FUEGO PARA LA INTELIGENCIA ARTIFICIAL (ML)
    // ---------------------------------------------------------
    // 'peso_muerto' se declara y se reasigna, pero NUNCA se lee.
    // El modelo extraerá las features: [asignaciones=2, lecturas=0, loop=0, if=0].
    // Predicción del Árbol de Decisión: 1 (ELIMINAR).
    int peso_muerto = 100;
    peso_muerto = 200 + nulo;

    // Extensión del lenguaje (booleano) sin lecturas.
    // Features: [asignaciones=1, lecturas=0, loop=0, if=0] -> ELIMINAR.
    bool bandera_fantasma = FALSE;

    // ---------------------------------------------------------
    // 4. BARRERAS DE CONTROL (Demuestra la seguridad del compilador)
    // ---------------------------------------------------------
    int contador = 0;
    int limite = 3;

    // Al cruzar la etiqueta del 'while', el propagador de constantes
    // debe borrar su memoria para no romper la lógica del bucle.
    while (contador < limite) {

        contador = contador + 1;

        // Variable inútil declarada dentro de un bloque de control.
        // Features para el ML: [asignaciones=1, lecturas=0, loop=1, if=0].
        // Predicción: 1 (ELIMINAR).
        int basura_en_loop = 404;
    }

    // ---------------------------------------------------------
    // 5. VARIABLE VIVA
    // ---------------------------------------------------------
    // 'b' es retornada (leída).
    // Features ML: [asignaciones=1, lecturas=1, loop=0, if=0] -> CONSERVAR.
    return b;
}