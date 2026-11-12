


Se seleccionaron nueve clases del flujo de recomendaciones: el controlador, el recomendador, el selector, los servicios que completan información y los objetos que contienen los resultados. Es un alcance seleccionado del módulo, no una medición de todo WiseTrip. Las interfaces y las clases privadas internas usadas para leer JSON quedan fuera de la tabla.

## Qué significa cada métrica

- **NPM:** número de métodos públicos escritos explícitamente en la clase. Incluye getters, métodos estáticos públicos y métodos que implementan interfaces.
- No incluye constructores, métodos privados/protegidos, heredados ni generados automáticamente. Cada sobrecarga contaría por separado.
- **NOC:** número de clases hijas directas que usan `extends` para heredar de la clase analizada. Usar una clase como atributo o implementar la misma interfaz no crea esa relación.

El NPM se verificó leyendo cada clase. Para NOC se revisaron las declaraciones de herencia de los **107 archivos Java en `app/Entrega1/wisetrip_entrega1/src/main/java`**. Se excluyeron pruebas, archivos compilados y copias temporales. No se encontraron hijas directas de las nueve clases seleccionadas.

## Resultados

| Clase | NPM explícito | NOC | Métodos públicos contados |
|---|---:|---:|---|
| RecomendacionControlador | 1 | 0 | mostrarRecomendaciones |
| RecomendadorDestinos | 7 | 0 | calcularPuntajePresupuesto, calcularCoincidenciaPonderada, calcularPuntajeTotal, diferenciaPresupuesto, excedePresupuesto, validarCiudades, puntuarCiudadesValidas |
| SelectorDestinos | 1 | 0 | seleccionarMejoresDestinos |
| LlenarAtributosCiudad | 1 | 0 | enriquecer |
| ProxyLugares | 1 | 0 | contarLugares |
| ServicioGeoapify | 2 | 0 | coordenadas, contarLugares |
| ResultadoRecomendacion | 6 | 0 | getCiudad, getPuntajeTotal, getPuntajePresupuesto, getPuntajePreferencias, isPreferenciasPuntuables, isCoincidenciaProvisional |
| ValidacionCiudades | 0 | 0 | Ninguno escrito explícitamente; su constructor no cuenta |
| SeleccionDestinos | 3 | 0 | getDestinos, getMensaje, isVacio |

### Caso especial: ValidacionCiudades

Es un `record`. Java genera seis métodos públicos: `validas()`, `descartadas()`, `pendientes()`, `equals(Object)`, `hashCode()` y `toString()`.

Por eso su **NPM explícito es 0**, pero **sería 6 si se incluyeran los métodos generados**. No significa que el objeto no tenga métodos utilizables. Es importante mantener esta misma convención en futuras mediciones. Los records son finales, así que no pueden tener clases hijas.

## Relaciones encontradas

| Clase | Relación declarada |
|---|---|
| RecomendadorDestinos | Implementa Recomendador |
| SelectorDestinos | Implementa SeleRecomendaciones |
| ProxyLugares | Implementa ProovedorLugares |
| ServicioGeoapify | Implementa ProovedorLugares |
| ResultadoRecomendacion y SeleccionDestinos | Implementan Serializable |
| ValidacionCiudades | Hereda implícitamente de java.lang.Record |
| RecomendacionControlador y LlenarAtributosCiudad | Sin extends explícito; heredan de Object |

Las demás clases ordinarias de la tabla también heredan de Object al no declarar extends. Implementar una interfaz no aumenta el NOC de esas clases. Por ejemplo, ProxyLugares utiliza un ProovedorLugares y delega consultas al servicio real: no hereda de ServicioGeoapify.

## Qué nos dicen los números

**RecomendadorDestinos tiene el NPM más alto de la selección: 7.** Es la primera candidata para revisar si todos sus métodos necesitan ser públicos. La interfaz Recomendador declara cuatro de ellos; los otros tres son calcularPuntajePresupuesto, calcularCoincidenciaPonderada y diferenciaPresupuesto. Antes de reducir su visibilidad se deben revisar sus usos en la aplicación y las pruebas. En esta HU solo se identifica la oportunidad; no se cambia la visibilidad.

**ResultadoRecomendacion tiene 6 métodos públicos explícitos**, pero todos consultan datos del resultado. Su cantidad no implica por sí sola demasiadas responsabilidades ni justifica eliminar getters.

No se fijó un límite universal para considerar un NPM excesivo. Se identificaron los valores más altos dentro del alcance y se revisó qué representan, sin calificarlos automáticamente como defectos.

**Todas las clases tienen NOC = 0.** No hay ramificaciones de herencia hacia clases hijas dentro del código de aplicación revisado. En este grupo predominan el uso de objetos colaboradores y las interfaces. Un cero no demuestra por sí solo buena o mala calidad.

