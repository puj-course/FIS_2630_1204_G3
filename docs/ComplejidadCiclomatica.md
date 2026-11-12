

## 2. Estado de la medición

| Dato | Registro |
|---|---|
| Tipo de medición | Conteo manual del código fuente |
| Clase analizada | `RecomendadorDestinos` |
| Fecha de registro | 10 de noviembre de 2026 |

La medición se realizó mediante la lectura del código fuente.

## 3. Alcance

Las clases se seleccionan por su responsabilidad en el flujo de recomendaciones.

| Clase | Justificación | Estado |
|---|---|---|
| `RecomendadorDestinos` | Implementa la validación, la puntuación y el ordenamiento de destinos. | Conteo manual completado. |
| `RecomendacionControlador` | Es candidata por su papel como punto de entrada del módulo. | Pendiente revisar sus métodos y llamadas. |
| `LlenarAtributosCiudad` | Completa información utilizada en las recomendaciones. | Pendiente confirmar su participación en el flujo. |
| `ProxyLugares` | Proporciona información sobre lugares. | Pendiente confirmar su participación en el flujo. |

En las clases seleccionadas se registrarán los métodos públicos y privados dentro del alcance, incluidos los de complejidad baja. Los constructores se registrarán por separado. La inclusión de cada clase se justificará por su participación en el flujo.

## 4. Criterio de medición manual

La complejidad ciclomática representa el número de caminos independientes del flujo de control. Para el conteo manual se aplica la siguiente fórmula.

**Complejidad ciclomática = 1 + decisiones identificadas**

| Elemento | Aporte |
|---|---:|
| Base de cada método o constructor | 1 |
| Cada `if` o `else if` | +1 |
| Cada `for`, `while` o `do-while` | +1 |
| Cada operador ternario (`? :`) | +1 |
| Cada operador `&&` o `\|\|` | +1 |
| `else` | 0 |
| Asignaciones y operaciones aritméticas | 0 |
| Comparaciones por sí solas, como `==` o `>` | 0 |
| `return` | 0 |
| Llamadas a otros métodos | 0 |
| `continue` | 0 |

### Convenciones adicionales

- Cada bucle suma un punto, independientemente del número de iteraciones.
- Cada decisión se cuenta una vez, independientemente de su nivel de anidamiento.
- Las decisiones se cuentan en el método donde están escritas. Las decisiones de una lambda se atribuyen al método que la contiene, con una sola base para ese método.
- Las llamadas a `stream()`, `anyMatch()` y `sort()` aportan cero puntos por sí mismas.
- En `validarCiudades`, se cuenta el `if` que determina si se ejecuta `continue`.
- Los comentarios quedan fuera del conteo.

Estas convenciones se aplicarán también en la medición posterior. Al contrastar el conteo manual con un analizador, se documentará cómo trata los operadores booleanos y las lambdas, así como cualquier diferencia con el criterio utilizado aquí.

## 5. Línea base de `RecomendadorDestinos`

Archivo analizado en el repositorio. [RecomendadorDestinos.java](../app/Entrega1/wisetrip_entrega1/src/main/java/com/wisetrip/servicio/RecomendadorDestinos.java)

| Método | Justificación del cálculo | Complejidad manual |
|---|---|---:|
| `calcularPuntajePresupuesto` | 1 de base + 2 `if` | 3 |
| `calcularCoincidenciaPonderada` | 1 de base + 2 `for` + 3 decisiones `if`/`else if` + 2 operadores `&&` + 1 ternario | 9 |
| `calcularPuntajeTotal` | 1 de base + 2 operadores `&&` dentro de la lambda | 3 |
| `diferenciaPresupuesto` | 1 de base. Contiene cálculos y llamadas. | 1 |
| `excedePresupuesto` | 1 de base + 3 operadores `&&` | 4 |
| `validarCiudades` | 1 de base + 2 `for` + 3 decisiones `if`/`else if` + 2 operadores `\|\|` | 8 |
| `puntuarCiudadesValidas` | 1 de base + 1 `for` + 1 operador `\|\|` + 1 `if` | 4 |
| `todosMismoPuntaje` | 1 de base + 2 `if` + 1 `for` | 4 |

### Constructor

| Constructor | Justificación del cálculo | Complejidad manual |
|---|---|---:|
| `RecomendadorDestinos` | 1 de base. Contiene asignaciones de atributos. | 1 |

El promedio se calcula sobre los ocho métodos de la tabla principal.

## 6. Ejemplo de cálculo

El método `calcularPuntajePresupuesto` contiene dos decisiones.

1. Comprueba si el presupuesto es menor o igual a cero.
2. Si el presupuesto es positivo, comprueba si el costo está dentro del presupuesto.

**Complejidad ciclomática = 1 + 2 = 3**

Los tres caminos son los siguientes.

1. El presupuesto es menor o igual a cero.
2. El presupuesto es positivo y el costo es menor o igual al presupuesto.
3. El presupuesto es positivo y el costo supera el presupuesto.

El `else`, los `return`, las operaciones aritméticas y la llamada a `Math.max()` aportan cero puntos al conteo del método.

## 7. Resumen de `RecomendadorDestinos`

| Indicador | Resultado |
|---|---:|
| Métodos analizados | 8 |
| Suma de las complejidades por método | 36 |
| Promedio por método | 4,5 |
| Complejidad máxima | 9 |
| Complejidad mínima | 1 |

La suma de 36 es un agregado de las mediciones individuales. Los métodos con mayor complejidad son `calcularCoincidenciaPonderada` (**9**) y `validarCiudades` (**8**). Se priorizarán en la revisión porque concentran la mayor cantidad de decisiones entre los métodos analizados.

## 8. Comparación posterior a la refactorización

| Clase | Método original | Antes (conteo manual) | Después | Observaciones |
|---|---|---:|---|---|
| `RecomendadorDestinos` | `calcularPuntajePresupuesto` | 3 | Pendiente | |
| `RecomendadorDestinos` | `calcularCoincidenciaPonderada` | 9 | Pendiente | |
| `RecomendadorDestinos` | `calcularPuntajeTotal` | 3 | Pendiente | |
| `RecomendadorDestinos` | `diferenciaPresupuesto` | 1 | Pendiente | |
| `RecomendadorDestinos` | `excedePresupuesto` | 4 | Pendiente | |
| `RecomendadorDestinos` | `validarCiudades` | 8 | Pendiente | |
| `RecomendadorDestinos` | `puntuarCiudadesValidas` | 4 | Pendiente | |
| `RecomendadorDestinos` | `todosMismoPuntaje` | 4 | Pendiente | |

Si un método se divide, se renombra o se elimina, se registrará qué pasó con su lógica. Los métodos nuevos también se medirán. Para evaluar la mantenibilidad, se revisarán los cambios en complejidad junto con la claridad del código, la distribución de responsabilidades y la conservación del comportamiento.

## 9. Relación con las pruebas

El conteo manual se obtiene al revisar el código fuente. La complejidad ayuda a identificar qué caminos conviene revisar. Las pruebas sirven para comprobar que la refactorización conserva los resultados esperados. Los casos de prueba se definen según el comportamiento que se quiere verificar.

## 10. Trabajo pendiente

- [ ] Revisar `RecomendacionControlador` y medir los métodos incluidos en el alcance.
- [ ] Confirmar la participación de `LlenarAtributosCiudad` y `ProxyLugares`.
- [ ] Completar la línea base de las clases seleccionadas.
- [ ] Contrastar los resultados con un analizador y documentar su versión y criterio.
- [ ] Guardar la referencia de la versión medida antes de refactorizar.
- [ ] Repetir la medición después de la refactorización y comparar los resultados.
