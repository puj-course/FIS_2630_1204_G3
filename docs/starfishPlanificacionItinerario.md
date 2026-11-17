# Retrospectiva Starfish — M3: Módulo de Planificación e Itinerario del Viaje

| Dato | Valor |
|---|---|
| Milestone | M3 - Módulo de Planificación e Itinerario del Viaje |
| Periodo | 13 de agosto al 12 de noviembre de 2026 (fecha límite: 20 de noviembre) |
| Sprints relacionados | Sprint 5 (HU-49, 51, 52, 55), Sprint 6 (HU-60 a 65), Sprint 9 (HU-90, 91), Sprint 10 (HU-100, 101), Sprints 11–12 (refactorización, itinerario HU-119) |
| Estado | 51 de 51 issues cerrados (49 issues + 2 PR) |
| Etiquetas principales | Front (16), Back (16), Diseño (11), Base de Datos (9), Pruebas (7), Arquitectura (5) |
| Equipo | Valeria Cortes, Santiago Clavijo, María Alejandra Rodríguez, Gabriela Melo, Isabella Posada |

## 1. Descripción del milestone
Módulo que convierte una recomendación en un **viaje concreto**: ciudad de
origen, fechas en un calendario, presupuesto con moneda y un itinerario día a
día. Los datos del viaje se mantienen mientras el usuario navega entre pantallas.

## 2. Lo que se planeó en la definición inicial
- **5.1 Itinerario personalizado:** una vez elegido el destino, **la plataforma
  genera** un itinerario según las fechas, el presupuesto para actividades y los intereses.
- **5.2 Alojamiento:** consultar hoteles, hostales y otras opciones (ya "en pausa").
- **Problema:** "llegar al destino sin itinerario" y "no saber cómo moverse dentro del país".

## 3. Planeado vs. resultado final
| Planeado | Resultado | Estado |
|---|---|---|
| Itinerario generado automáticamente | El viajero **arrastra** actividades recomendadas (Geoapify) o propias a un calendario por días en *Mi viaje* | Cambió: manual, no automático |
| Itinerario según presupuesto e intereses | Las actividades vienen del destino elegido; el itinerario muestra el gasto y la próxima actividad | Parcial |
| Itinerario guardado en el perfil | Se guarda en el **navegador** (localStorage), no en la base de datos | Parcial |
| Fechas del viaje | Calendario visual de rango con validaciones (no fechas pasadas, fin después del inicio, duración) | Cumplido |
| Opciones de alojamiento | Solo los lugares de Geoapify; sin proveedor de hoteles ni reservas | No implementado (estaba en pausa) |
| Cómo moverse dentro del país | No se implementó | No implementado |
| No estaba planeado | Origen por país y ciudad (44 ciudades), moneda con bandera, sesión del viaje, *Mis viajes* | Extra |

## 4. Qué se entregó
- Pantallas de origen, fechas, presupuesto, resumen y *Mi viaje* con itinerario.
- `SesionViaje` guarda el viaje en curso y `ViajeDAO` lo persiste en PostgreSQL al confirmar.
- Clase `Viaje` que agrupa usuario, ciudad, fechas, presupuesto y preferencias, y
  se valida antes de guardar (HU-90).
- **Patrones GoF:** Chain of Responsibility (`ReglaValidacionFechas`) y Facade (`UsuarioSesionFacade`).
- Pruebas: `FechasServicioTest` (CP34–CP47), `ReglasFechasTest`,
  `SesionViajeTest` (CP58–CP67) y `PlanificacionControladorTest`.

## 5. Métricas
| Métrica | Valor |
|---|---|
| Issues cerrados | 51 / 51 (100%) |
| Reparto de issues | Gabriela 22 · María Alejandra 12 · Valeria 11 · Santiago 4 |
| Correcciones de SonarQube | `PlanificacionControlador` (12 issues), sesiones, reglas y base de datos |

## 6. Lo que dijeron las Reviews y Retrospectivas
- **Review del 13/09:** el ingreso manual de fechas se cambió por un calendario
  visual (HU-51); origen por país y ciudad (HU-49, HU-52); moneda con bandera (HU-55).
- **Retro del 13/09:** Isabella explicó que dividieron las HU en pasos pequeños y
  validaron antes de integrar. Valeria propuso reunirse a probar en los
  computadores de todos.
- **Review del 20/09:** se mejoraron los textos e imágenes del cuestionario (HU-60,
  HU-61) y se corrigieron las banderas (HU-64, HU-65). Fue la **semana de receso**:
  en la retro varios dijeron que el trabajo bajó y hubo tensiones por los dailys
  que no se llenaban.
- **Review del 11/10:** se creó la clase `Viaje` y la interfaz de fechas para que
  los controladores dependan de interfaces (SOLID).
- **Review del 18/10:** HU-100 y HU-101 resolvieron que fechas, presupuesto y
  destino "no se mezclen ni se pierdan" tras la refactorización.
- **Retro del 25/10:** en la sustentación les dijeron que **los diagramas estaban mal**
  y no coincidían con el código. Gabriela hizo la refactorización desde una sola
  cuenta para evitar conflictos.
- **Review y Retro del 01/11:** se terminó la página del itinerario (HU-119).
  Todos coincidieron en que se puso mucho trabajo en **semana de parciales** y que
  quedaron tareas incompletas por mala estimación.

## 7. Análisis
**Lo que funcionó**
- La **cadena de reglas** hizo las validaciones de fechas fáciles de probar y ampliar.
- La **fachada de sesión** resolvió la pérdida de datos entre pantallas.
- El calendario y las banderas mejoraron mucho la experiencia, aunque no estaban en la definición.

**Lo que se pudo hacer mejor**
- El itinerario **no es automático** como prometía la definición: el usuario lo
  arma a mano y solo vive en su navegador. Si cambia de equipo, lo pierde.
- El itinerario llegó tarde (Sprint 12), cuando la carga académica era más alta.
- Alojamiento y transporte local, dos problemas de la definición, no se resolvieron.
- Hubo varias historias casi iguales sobre "que mis datos sigan guardados"
  (HU-91, HU-100, HU-130, HU-131): el problema se corrigió por partes.
- Las fechas con zona horaria generaron fallos (servidor en UTC, app en Bogotá).

**Decisiones clave**
- Guardar el viaje en la sesión y persistirlo solo al confirmar.
- Itinerario de arrastrar y soltar en lugar de generarlo con un algoritmo.
- Usar siempre `America/Bogota` para validar fechas.

## 8. Retrospectiva Starfish
| Sección | Aportes |
|---|---|
| **Más de** | Pruebas de fechas en los límites · Probar el flujo completo en los equipos de todos · Dividir las HU en pasos pequeños antes de programar |
| **Menos de** | Historias repetidas para el mismo problema · Funcionalidades grandes en semana de parciales · Controladores largos con mucha lógica |
| **Comenzar a hacer** | Guardar el itinerario en la base de datos · Sugerir actividades automáticamente según intereses y presupuesto · Diseñar el flujo de datos entre pantallas antes de programarlas |
| **Dejar de hacer** | Usar `LocalDate.now()` sin zona horaria · Hacer los diagramas después del código |
| **Seguir haciendo** | La cadena de reglas para validaciones · La fachada para la sesión · Cuidar lo visual (calendario, banderas) |

## 9. Acciones de mejora
| Acción | Responsable | Para |
|---|---|---|
| Usar `ReglaValidacionFechas.ZONA_HORARIA` en todo el código y las pruebas | Equipo de Back | Hecho |
| Persistir el itinerario en la base de datos | Equipo de Back | Trabajo futuro |
| Planear las HU grandes fuera de las semanas de parciales | Scrum Master | Próximos proyectos |

## 10. Conclusión
M3 dejó una planificación de viaje sólida y bien validada, pero el itinerario
cambió respecto a la definición: en lugar de **generarse** según presupuesto e
intereses, el viajero lo arma a mano. El aprendizaje principal fue **diseñar
primero cómo viajan los datos** y no dejar la funcionalidad más importante del
módulo para las semanas más cargadas del semestre.

<img width="2000" height="1414" alt="starfisthPlanificacioneItinerario" src="https://github.com/user-attachments/assets/8fb8564b-ce6e-4cb7-a423-fd9760b40ee8" />


 
