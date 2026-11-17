# Retrospectiva Starfish — M2: Módulo de Recomendación Personalizada de Destinos

| Dato | Valor |
|---|---|
| Milestone | M2 - Módulo de Recomendación Personalizada de Destinos |
| Periodo | 19 de agosto al 11 de noviembre de 2026 (fecha límite: 20 de noviembre) |
| Sprints relacionados | Sprint 3 (Geoapify, modelo de datos), Sprint 5 (HU-47, HU-50, HU-53), Sprint 6 (HU-63), Sprint 8 (HU-80 a HU-85), Sprint 9 (HU-93 a HU-95), Sprint 10 (HU-102, HU-103, HU-105), Sprint 14 (métricas) |
| Estado | 66 cerrados, 3 abiertos (68 issues + 1 PR) |
| Etiquetas principales | Back (42), Base de Datos (20), Pruebas (14), Front (13), Organización (8), bug (7), Diseño (7), Arquitectura (6) |
| Equipo | Valeria Cortes, Santiago Clavijo, María Alejandra Rodríguez, Gabriela Melo, Isabella Posada |

## 1. Descripción del milestone
Es el **núcleo de WiseTrip** y la parte de la propuesta de valor que más se
cumplió. El viajero ingresa su presupuesto, responde un cuestionario sobre sus
intereses y WiseTrip le recomienda hasta 3 ciudades de Latinoamérica con mayor
afinidad que caben en su presupuesto.

## 2. Lo que se planeó en la definición inicial
- **5.7 Recomendaciones según intereses:** después del presupuesto, preguntar por
  los gustos en 9 categorías (aventura, cultura, gastronomía, naturaleza, vida
  nocturna, familiares, temperatura, compras, bajo costo) y recomendar
  **destinos, lugares, actividades y planes**.
- **5.4 (parte):** si el usuario cambia el presupuesto, recalcular las recomendaciones.
- **Propuesta de valor:** mostrar destinos asequibles "que no son muy turísticos".

## 3. Planeado vs. resultado final
| Planeado | Resultado | Estado |
|---|---|---|
| Cuestionario de intereses | 26 preguntas Sí/No por categoría, con imágenes, barra de progreso y nivel de importancia | Cumplido |
| 9 categorías originales | Se reorganizaron (paisaje y clima, tipo de destino, ritmo, estilo, compañía, gastronomía, actividades, cultura, características) | Cambió |
| Recomendar destinos por afinidad y presupuesto | `RecomendadorDestinos` + `SelectorDestinos`: puntaje ponderado, requisitos obligatorios, filtro por costo real y máximo 3 destinos | Cumplido |
| Recomendar también lugares y actividades | Lugares reales de Geoapify por ciudad, que alimentan el itinerario (M3) | Cumplido |
| Recalcular al cambiar el presupuesto | El viajero puede modificar presupuesto y respuestas antes de confirmar (HU-95) | Cumplido |
| Destinos poco turísticos | 44 ciudades de Latinoamérica con atributos | Cumplido |

## 4. Qué se entregó
- Cuestionario visual y pantalla de carga mientras se calcula.
- Algoritmo de afinidad con ponderación de intereses (HU-80) para que una ciudad no
  gane solo por acumular gustos secundarios.
- `LlenarAtributosCiudad` completa los atributos con los conteos de Geoapify y
  distingue atributos manuales de los de la API.
- **Patrones GoF:** Proxy (`ProxyLugares` usa primero la caché en base de datos)
  y Decorator (`PresupuestoConMargenDecorador` agrega un 5% de imprevistos).
- SonarCloud + JaCoCo en el pipeline; `RecomendadorDestinos` al 100% de cobertura.

## 5. Métricas
| Métrica | Valor |
|---|---|
| Issues cerrados / abiertos | 66 / 3 |
| Bugs reportados | 7 (el milestone con más bugs) |
| Reparto de issues | Isabella 34 · Santiago 16 · Gabriela 9 · Valeria 8 · María Alejandra 5 |
| Issues abiertos | HU#142 complejidad ciclomática, HU#143 métricas orientadas a objetos, HU#144 aviso de Telegram al equipo |

## 6. Lo que dijeron las Reviews y Retrospectivas
- **Retro del 23/08:** Isabella pidió definir pronto los **rangos de presupuesto por
  ciudad**, "algo muy importante del algoritmo".
- **Review del 30/08:** se ajustó el modelo de datos (ciudades, coordenadas,
  atributos) porque el inicial no servía para el algoritmo, y se integró
  Geoapify. Isabella logró traer y filtrar las categorías de las 44 ciudades.
- **Review del 13/09:** las respuestas Sí/No ya filtran destinos según el peso de
  cada atributo. Se pidió "revisar con más pruebas la recomendación final".
- **Review del 20/09:** HU-63 quedó en seguimiento porque las recomendaciones aún
  no coincidían bien con los intereses.
- **Retro del 27/09:** Gabriela subió las imágenes del cuestionario, pero solo se
  veían en su equipo porque quedaron con IDs viejos.
- **Review del 04/10:** se corrigió la comparación entre presupuesto y costo real
  (HU-85): había destinos mal marcados como "accesibles".
- **Review del 11/10:** destinos ordenados por afinidad y mensaje claro cuando
  ninguno cabe en el presupuesto (HU-94).
- **Retro del 18/10:** Isabella implementó el Proxy. Contó que **dos compañeras
  hicieron el mismo patrón**, que una clase principal del recomendador estaba
  **desconectada** sin que nadie lo notara y que quedó pendiente pasar la caché
  de memoria a una tabla en la base de datos (se hizo después).
- **Retro del 08/11:** los issues de SonarQube se repartieron por paquetes.

## 7. Análisis
**Lo que funcionó**
- Es la funcionalidad de la definición que **mejor se cumplió**. El algoritmo
  maneja empates, costos inválidos, preferencias nulas y ciudades indispensables.
- El **Proxy con caché** protegió los créditos de Geoapify.
- Se iteró con base en las Reviews: HU-63 → HU-80 → HU-82 → HU-85 → HU-94
  mejoraron la precisión sprint a sprint.

**Lo que se pudo hacer mejor**
- La carga quedó **concentrada en Isabella** (34 de 68 issues).
- Es el módulo con **más bugs (7)**, sobre todo de imágenes y de la pantalla de preferencias.
- El modelo de datos se tuvo que rehacer porque no se diseñó pensando en el algoritmo.
- Hubo confusión entre atributos manuales y de la API, y se siguió consultando la
  API aunque ya existía la caché.
- La falta de coordinación hizo que se duplicara un patrón y que una clase quedara desconectada.
- Quedan 3 historias abiertas.

## 8. Retrospectiva Starfish (tablero del equipo)
| Sección | Aportes del equipo |
|---|---|
| **Más de** | Escucha activa y concentración en las reuniones · Más pruebas sobre el cálculo de puntajes y la selección de destinos · Seguir revisando la actividad de Geoapify para no sobrepasar los créditos |
| **Menos de** | Confundir los atributos manuales con los de la API · Depender de la API cuando ya existe la caché en la base de datos · Hacer cambios o push a `main` apresurados cerca de la entrega |
| **Comenzar a hacer** | Más pruebas funcionales usando atributos manuales y de la API · Mejorar los diagramas para que representen el módulo · Probar más casos con diferentes presupuestos e intereses · Pulir el código para que todo el equipo lo entienda |
| **Dejar de hacer** | No poner en `.gitignore` la clave de la API · Ir inmediatamente a la IA ante cualquier duda sin entender lo que hace · Modificar clases del módulo sin actualizar después los diagramas |
| **Seguir haciendo** | Corregir issues y hacer pruebas según SonarQube · Consultar primero la caché antes de llamar a Geoapify · Seguir usando los diferentes tipos de atributos |

## 9. Acciones de mejora
| Acción | Responsable | Para |
|---|---|---|
| Repartir las tareas de Back del módulo entre más integrantes | Equipo | Siguiente sprint |
| Cerrar HU#142 y HU#143 (complejidad ciclomática y métricas OO) | Isabella | Antes del 20/11 |
| Revisar en el daily quién implementa cada patrón para no duplicar trabajo | Scrum Master | Desde ya |
| Proteger la clave de Geoapify con variables de entorno | Equipo de Back | Entrega final |

## 10. Conclusión
M2 entregó lo que diferencia a WiseTrip y cumplió casi por completo la
funcionalidad 5.7 de la definición: recomendaciones personalizadas según
intereses y presupuesto, con datos reales y un uso controlado de la API. Los
aprendizajes fueron **repartir mejor el trabajo**, **coordinar quién hace qué** y
**entender lo que se programa** en vez de depender de la IA.

<img width="2000" height="1414" alt="starfishretrospective pdf (3)" src="https://github.com/user-attachments/assets/3e34f06a-fd52-4f59-a650-a8793d89aadd" />

