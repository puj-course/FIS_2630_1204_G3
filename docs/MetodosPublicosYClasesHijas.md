Se seleccionaron nueve clases del flujo de recomendaciones de WiseTrip: el controlador, el recomendador, el selector, los servicios encargados de completar información y los objetos utilizados para almacenar los resultados. El análisis corresponde únicamente a este alcance del módulo de recomendaciones y no representa una medición completa de todo el sistema. Las interfaces y las clases privadas internas utilizadas para la lectura de archivos JSON no fueron incluidas en la tabla de resultados.

## Métricas utilizadas

- **NPM (Number of Public Methods):** corresponde al número de métodos públicos declarados explícitamente en cada clase. Para esta medición se incluyen getters, métodos `static` públicos y métodos públicos que implementan interfaces. No se incluyen constructores, métodos `private` o `protected`, métodos heredados ni métodos generados automáticamente. En caso de existir sobrecargas, cada método se cuenta de manera independiente.

- **NOC (Number of Children):** corresponde al número de clases hijas directas que heredan de la clase analizada mediante `extends`. La implementación de interfaces o el uso de una clase como atributo no incrementa este valor.

El NPM se obtuvo mediante la revisión directa de cada una de las clases seleccionadas. Para calcular el NOC se revisaron las declaraciones de herencia presentes en los **107 archivos Java ubicados en `app/Entrega1/wisetrip_entrega1/src/main/java`**. Se excluyeron archivos de pruebas

## Resultados

| Clase | NPM | NOC | Métodos públicos contados |
|---|---:|---:|---|
| RecomendacionControlador | 1 | 0 | mostrarRecomendaciones |
| RecomendadorDestinos | 7 | 0 | calcularPuntajePresupuesto, calcularCoincidenciaPonderada, calcularPuntajeTotal, diferenciaPresupuesto, excedePresupuesto, validarCiudades, puntuarCiudadesValidas |
| SelectorDestinos | 1 | 0 | seleccionarMejoresDestinos |
| LlenarAtributosCiudad | 1 | 0 | enriquecer |
| ProxyLugares | 1 | 0 | contarLugares |
| ServicioGeoapify | 2 | 0 | coordenadas, contarLugares |
| ResultadoRecomendacion | 6 | 0 | getCiudad, getPuntajeTotal, getPuntajePresupuesto, getPuntajePreferencias, isPreferenciasPuntuables, isCoincidenciaProvisional |
| ValidacionCiudades | 0 | 0 | Ninguno escrito explícitamente; el constructor no cuenta |
| SeleccionDestinos | 3 | 0 | getDestinos, getMensaje, isVacio |

## ValidacionCiudades

`ValidacionCiudades` está implementada como un `record`. Java genera automáticamente los métodos `validas()`, `descartadas()`, `pendientes()`, `equals(Object)`, `hashCode()` y `toString()`. Debido a que la convención utilizada en esta medición solo considera métodos escritos explícitamente en la clase, su valor de NPM es **0**. Si se incluyeran los métodos generados automáticamente, el valor sería 6.

Esta decisión no significa que la clase no disponga de métodos públicos, sino que se mantiene un criterio uniforme para todas las mediciones realizadas. Además, los `record` son finales, por lo que no pueden tener clases hijas y su NOC es 0.

## Relaciones identificadas

| Clase | Relación declarada |
|---|---|
| RecomendadorDestinos | Implementa Recomendador |
| SelectorDestinos | Implementa SeleRecomendaciones |
| ProxyLugares | Implementa ProovedorLugares |
| ServicioGeoapify | Implementa ProovedorLugares |
| ResultadoRecomendacion y SeleccionDestinos | Implementan Serializable |
| ValidacionCiudades | Hereda implícitamente de java.lang.Record |
| RecomendacionControlador y LlenarAtributosCiudad | Sin `extends` explícito; heredan de Object |

Las demás clases ordinarias analizadas también heredan de `Object` al no declarar una relación `extends`. La implementación de interfaces no incrementa el NOC. Por esta razón, relaciones como la existente entre `ProxyLugares` y `ProovedorLugares` no representan herencia entre clases.

## Interpretación de resultados

`RecomendadorDestinos` presenta el valor de NPM más alto del grupo analizado, con **7 métodos públicos**. Este resultado permite identificarla como una de las principales candidatas para revisar durante la refactorización y determinar si todos sus métodos necesitan mantener visibilidad pública. La interfaz `Recomendador` define cuatro de estos métodos, mientras que `calcularPuntajePresupuesto`, `calcularCoincidenciaPonderada` y `diferenciaPresupuesto` corresponden a métodos adicionales de la implementación.

`ResultadoRecomendacion` presenta un NPM de **6**. Sin embargo, la mayoría de estos métodos están orientados a consultar la información almacenada en el resultado, por lo que este valor no implica por sí solo un problema de diseño.

No se estableció un valor límite universal para determinar cuándo un NPM es excesivo. En este análisis se compararon los valores obtenidos dentro del mismo módulo y se revisó el propósito de los métodos antes de considerar una posible mejora.

En cuanto al NOC, todas las clases analizadas presentan un valor de **0**, lo que indica que no existen clases hijas directas dentro del código revisado. En este grupo del módulo predominan las relaciones mediante interfaces y colaboración entre objetos en lugar de estructuras de herencia entre clases.

## Conclusión

Las métricas NPM y NOC permitieron identificar características de la estructura orientada a objetos del módulo de recomendaciones. El valor más relevante corresponde a `RecomendadorDestinos`, que presenta la mayor cantidad de métodos públicos dentro del alcance analizado y puede ser revisada durante la refactorización. Por otro lado, el valor NOC igual a 0 en todas las clases evidencia que este grupo del módulo no utiliza jerarquías de herencia con clases hijas directas.
