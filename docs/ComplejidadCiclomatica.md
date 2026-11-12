# HU 142 — Complejidad ciclomática y claridad de RecomendadorDestinos

## Objetivo y alcance

Medir manualmente la complejidad por método de `RecomendadorDestinos` y mejorar su legibilidad conservando su comportamiento. La refactorización busca que las condiciones y los cálculos sean fáciles de explicar; no pretende reducir artificialmente la métrica.

Archivo: [RecomendadorDestinos.java](../app/Entrega1/wisetrip_entrega1/src/main/java/com/wisetrip/servicio/RecomendadorDestinos.java).

Fecha de cierre de esta serie: 11 de noviembre de 2026. Versión anterior a los seis cambios: `d33041b6430497d466e5110605dfbf467bcf70f6`. El sexto cambio está preparado en el directorio de trabajo, pendiente del commit de la autora.

Esta evaluación abarca ocho métodos y registra el constructor aparte. No representa una medición del módulo completo. Queda pendiente revisar `RecomendacionControlador` y confirmar la inclusión de `LlenarAtributosCiudad` y `ProxyLugares` según su responsabilidad en el flujo.

## Criterio de conteo manual

**Complejidad = 1 inicial + decisiones identificadas.**

- Se suma 1 por cada `if` o `else if`, bucle, ternario, operador `&&` y operador `||`.
- No se suma por `else`, `return`, `break`, `continue`, asignaciones, comparaciones aisladas, operaciones aritméticas ni llamadas a métodos.
- Las decisiones de una lambda se atribuyen al método que la contiene, sin otra base para la lambda.
- `stream()`, `anyMatch()` y `sort()` no suman por sí mismos. Sus implementaciones internas no se incorporan al conteo del método llamador.
- `|=` no es un operador con cortocircuito y no suma bajo este criterio.
- El anidamiento y el número de iteraciones no añaden puntos adicionales.

Es un conteo manual con convenciones explícitas, no un reporte de Sonar o JaCoCo. La validación automática de la métrica sigue pendiente y deberá registrar herramienta, versión y posibles diferencias de criterio, especialmente para lambdas.

## Línea base y resultados finales

El conteo inicial de la conversación se realizó sobre una versión anterior. Antes de iniciar los seis cambios, `validarCiudades` ya había sido reescrito con condiciones explícitas y su valor era 11, no 8. Esta diferencia previa se registra para no atribuirla a la serie actual.

| Método | Conteo inicial de la conversación | Antes de los seis cambios | Después | Justificación del valor final |
|---|---:|---:|---:|---|
| `calcularPuntajePresupuesto` | 3 | 3 | 3 | 1 base + 2 if |
| `calcularCoincidenciaPonderada` | 9 | 9 | 9 | 1 base + 2 for + 4 if/else if + 2 && |
| `calcularPuntajeTotal` | 3 | 3 | 5 | 1 base + 1 for + 3 if |
| `diferenciaPresupuesto` | 1 | 1 | 1 | Solo cálculos y llamadas |
| `excedePresupuesto` | 4 | 4 | 4 | 1 base + 3 if |
| `validarCiudades` | 8 | 11 | 11 | 1 base + 2 for + 8 if/else if |
| `puntuarCiudadesValidas` | 4 | 4 | 4 | 1 base + 1 for + 1 operador OR con cortocircuito + 1 if |
| `todosMismoPuntaje` | 4 | 4 | 4 | 1 base + 1 for + 2 if |

Constructor `RecomendadorDestinos`: **1 → 1**, sin decisiones. Se excluye de los agregados siguientes.

| Indicador de los ocho métodos | Antes de la serie | Después |
|---|---:|---:|
| Suma de complejidades | 39 | 41 |
| Promedio | 4,875 | 5,125 |
| Máximo | 11 | 11 |
| Mínimo | 1 | 1 |

La suma es un agregado de valores por método, no el número de caminos de toda la clase. El promedio inicial de la conversación era 4,5, pero no corresponde a la versión inmediatamente anterior a esta serie.

## Los seis cambios y su justificación

| Cambio | Método | Antes y después | Motivo | Complejidad |
|---|---|---|---|---|
| 1 | `calcularCoincidenciaPonderada` | Ternario final sustituido por un if y la variable `proporcionLograda`. | Separar el caso sin peso del cálculo de la proporción. Se mantienen la conversión a double y el mínimo de cero. | 9 → 9 |
| 2 | `calcularPuntajePresupuesto` | Eliminación del else posterior al return; cálculo dividido en `montoExcedido`, `proporcionExcedida` y `puntajePenalizado`. | Mostrar la penalización paso a paso, conservando las operaciones y su orden. | 3 → 3 |
| 3 | `calcularPuntajeTotal` | `stream().anyMatch()` sustituido por for, condiciones explícitas y break al encontrar el primer dato puntuable desconocido. | Mostrar el recorrido, la comprobación de null y la condición de provisionalidad. | 3 → 5 |
| 4 | `excedePresupuesto` | Cadena de tres && sustituida por tres if con retorno temprano. | Explicar por separado presupuesto opcional, costo no finito y costo no positivo. Se conserva el orden de evaluación. | 4 → 4 |
| 5 | `validarCiudades` | Variables con nombres descriptivos, tipos explícitos en lugar de var y comentarios de las reglas. | Distinguir incumplimientos confirmados de datos faltantes y explicar la prioridad del descarte. | 11 → 11 |
| 6 | `puntuarCiudadesValidas` y `todosMismoPuntaje` | Comparadores separados en `porCercaniaAlPresupuesto`, `porMayorPuntaje` y `porPuntajeYDesempate`; `primero` renombrado a `puntajeDeReferencia` y extracción de `puntajeActual`. | Explicar orden principal, desempate y comparación con el puntaje de referencia. Se conservan Comparator y Double.compare para mantener sus reglas de comparación. | 4 → 4 en ambos |

Se añadieron comentarios HU142 junto a los cambios principales. No se dejaron copias de las implementaciones anteriores comentadas dentro de los métodos.

### Referencias de los commits

Los commits los realiza la autora del proyecto:

1. `0098c5c`: cálculo final de coincidencia.
2. `32dc7ba`: puntaje de presupuesto.
3. `97fa811`: detección de coincidencia provisional.
4. `0154124`: validación de exceso de presupuesto.
5. `de3cba7`: nombres y reglas de clasificación.
6. Pendiente de commit: ordenamiento, desempates y este informe final.

## Verificación

Se revisaron los diffs y se ejecutó la suite existente `RecomendadorDestinosTest` sobre el resultado de los seis cambios:

**20 pruebas ejecutadas, 0 fallos, 0 errores, 0 omitidas. BUILD SUCCESS.**

Se utilizó Maven con JDK 26.0.1, compilación configurada para Java 25 y estas opciones:

```text
mvn -o -Dmaven.repo.local=C:/Users/XPG/.m2/repository -Dmaven.compiler.fork=true -Dtest=RecomendadorDestinosTest test
```

Los primeros intentos fallaron por acceso denegado a la dependencia local `commons-logging-1.3.6.jar`. La ejecución con permisos ampliados permitió completar las pruebas. No se modificó el pom ni se añadieron tests para esta serie.

Las pruebas existentes comprueban puntuación, datos faltantes, presupuesto, clasificación y ordenamiento. Su aprobación aporta evidencia de conservación del comportamiento, pero no demuestra cobertura exhaustiva de todos los caminos. No se ejecutó la suite completa del proyecto ni se obtuvo una medición automática de complejidad.

## Interpretación para la HU

La complejidad máxima permanece en 11, en `validarCiudades`, seguida de 9 en `calcularCoincidenciaPonderada`. Estos valores orientan la revisión de decisiones y pruebas; no representan errores ni una nota global de calidad.

La complejidad agregada aumenta de 39 a 41 porque el recorrido antes encapsulado en `anyMatch` queda explícito en `calcularPuntajeTotal`. El conteo utilizado mide decisiones visibles en el método y no incorpora el flujo interno de bibliotecas. Por ello, el incremento no demuestra por sí solo un empeoramiento del comportamiento o de la mantenibilidad.

La mejora buscada es cualitativa: nombres descriptivos, cálculos separados y reglas explicadas. No se afirma una reducción de complejidad ni una mejora cuantitativa de mantenibilidad. La comprensión de la nueva versión debe confirmarse mediante revisión por quienes mantienen el código.

## Pendientes

1. Registrar el commit del sexto cambio cuando la autora lo cree.
2. Contrastar el conteo con un analizador manteniendo documentadas las convenciones.
3. Completar el alcance del módulo si la HU requiere evaluar otras clases.
4. Revisar si las pruebas cubren los caminos relevantes de los métodos con mayor complejidad.
