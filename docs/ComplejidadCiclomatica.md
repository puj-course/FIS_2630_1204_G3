## Complejidad ciclomática de recomendaciones

## ¿Qué medimos?

Medimos manualmente las decisiones de **11 métodos en tres clases** de WiseTrip. Esto permite identificar qué métodos tienen más caminos que revisar y probar.

- `RecomendadorDestinos`: valida, puntúa y ordena ciudades.
- `RecomendacionControlador`: recibe la solicitud y coordina las recomendaciones.
- `LlenarAtributosCiudad`: completa los atributos necesarios para evaluar ciudades.

`ProxyLugares` quedó fuera del alcance. Los resultados corresponden al código local revisado el **11 de noviembre de 2026**.

## ¿Cómo contamos?

Cada método empieza en **1**. Sumamos **1** por cada:

- `if` o `else if`.
- Bucle, como `for` o `while`.
- Operador `&&` o `||`.
- Ternario (`condicion ? valor1 : valor2`).

No sumamos por `else`, `return`, `break`, `continue`, asignaciones ni llamadas a otros métodos. Las decisiones dentro de una lambda se cuentan en el método que la contiene; las decisiones internas de una biblioteca no se incorporan al método que la llama.

**Ejemplo:** un método con dos `if` tiene complejidad **1 + 2 = 3**. Un `for` suma 1 aunque recorra cien ciudades.

## Resultados actuales

En los cálculos, la cantidad de `if` incluye los `else if`.

### RecomendadorDestinos

| Método | Cálculo | Complejidad |
|---|---|---:|
| `calcularPuntajePresupuesto` | 1 base + 2 if | 3 |
| `calcularCoincidenciaPonderada` | 1 base + 2 for + 4 if + 2 AND | 9 |
| `calcularPuntajeTotal` | 1 base + 1 for + 3 if | 5 |
| `diferenciaPresupuesto` | 1 base, sin decisiones | 1 |
| `excedePresupuesto` | 1 base + 3 if | 4 |
| `validarCiudades` | 1 base + 2 for + 8 if | 11 |
| `puntuarCiudadesValidas` | 1 base + 1 for + 1 if + 1 OR | 4 |
| `todosMismoPuntaje` | 1 base + 1 for + 2 if | 4 |

AND corresponde a `&&` y OR a `||`.

### RecomendacionControlador

| Método | Cálculo | Complejidad |
|---|---|---:|
| `mostrarRecomendaciones` | 1 base + 7 if + 1 for + 1 OR | 10 |

### LlenarAtributosCiudad

| Método | Cálculo | Complejidad |
|---|---|---:|
| `enriquecer` | 1 base + 5 if + 1 for | 7 |
| `incumpleIndispensableManual` | 1 base + 1 for + 1 if + 3 AND | 6 |

Los tres constructores tienen complejidad **1** cada uno porque solo asignan dependencias. Se registran aparte y no se incluyen en los promedios.

## Resumen

| Clase | Métodos | Promedio | Máximo |
|---|---:|---:|---:|
| RecomendadorDestinos | 8 | 5,125 | 11 |
| RecomendacionControlador | 1 | 10 | 10 |
| LlenarAtributosCiudad | 2 | 6,5 | 7 |

El promedio de los **11 métodos** es **5,82**, redondeado a dos decimales. No es una nota de calidad; es un resumen de las decisiones por método.

Los métodos que conviene revisar primero son `validarCiudades` (11), `mostrarRecomendaciones` (10) y `calcularCoincidenciaPonderada` (9), porque concentran más decisiones. Esto no significa que tengan errores.

## ¿Qué cambiamos y por qué?

En `RecomendadorDestinos` se separaron cálculos largos, se usaron nombres más claros, se eliminó un else innecesario y se explicó el ordenamiento. También se cambió `anyMatch` por un recorrido explícito para facilitar su comprensión.

En `RecomendacionControlador` solo se añadieron comentarios con la medición; no se refactorizó su lógica.

En `LlenarAtributosCiudad` se aclararon el nombre del proveedor y el radio de búsqueda. Además, la versión final consulta las preferencias directamente en el mapa y usa condiciones explícitas para guardar los estados de los atributos.

| Método afectado | Antes | Después | Explicación |
|---|---:|---:|---|
| `calcularPuntajeTotal` | 3 | 5 | El recorrido antes encapsulado en anyMatch ahora aparece como for y condiciones explícitas. |
| `enriquecer` | 7 | 7 | Cambió la forma de escribir las decisiones, pero el total se mantuvo. |
| Los demás métodos durante esta serie | Sin variación | Sin variación | Los cambios de nombres, comentarios y estructura no alteraron su conteo. |

**Aclaración de la línea base:** en el primer conteo de la conversación, `validarCiudades` daba 8. Antes de comenzar la serie de seis cambios del recomendador, ya había sido modificado y daba 11. Para esa serie, la comparación correcta es **11 → 11**. El promedio del recomendador pasó de **4,875 a 5,125** durante la serie.

La mejora buscada fue que el código fuera más fácil de leer y explicar. **No se obtuvo una reducción de complejidad**, y no se presenta como si hubiera ocurrido.

## Verificación y conclusión

Después de los seis cambios de `RecomendadorDestinos`, sus **20 pruebas existentes pasaron sin fallos**. Ese resultado corresponde a esa clase y a ese momento; no es una ejecución de pruebas de las tres clases después de todos los cambios posteriores.

La medición de este documento es **manual**. No se validó con un analizador automático.

Se completó la medición de las tres clases seleccionadas y se identificaron los métodos con más decisiones. Los resultados sirven como referencia para futuras revisiones y para priorizar las pruebas. La complejidad por sí sola no demuestra que el código sea bueno o malo, ni indica el número exacto de pruebas necesarias.
