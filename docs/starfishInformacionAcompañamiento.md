# Retrospectiva Starfish — M5: Módulo de Información y Acompañamiento del Viaje

| Dato | Valor |
|---|---|
| Milestone | M5 - Módulo de Información y Acompañamiento del Viaje |
| Periodo | 19 de agosto al 25 de octubre de 2026 (fecha límite: 20 de noviembre) |
| Sprints relacionados | Sprint 2 (investigación del clima), Sprint 6 (HU-56 a 59), Sprint 7 (HU-66 a 68), Sprint 8 (HU-76, HU-78), Sprint 11 (manual de usuario) |
| Estado | 30 de 30 issues cerrados (28 issues + 2 PR) |
| Etiquetas principales | Back (13), Front (11), Diseño (11), Despliegue y Documentación (5), Documentación (2), Pruebas (2) |
| Equipo | Valeria Cortes, Santiago Clavijo, María Alejandra Rodríguez, Gabriela Melo, Isabella Posada |

## 1. Descripción del milestone
Módulo que **acompaña al viajero** con información y comunicación: clima del
destino, notificaciones, elegir el destino final, resumen del viaje, checklist de
equipaje, identidad visual y manual de usuario.

## 2. Lo que se planeó en la definición inicial
- **5.5 Clima:** temperatura y estado del destino antes y durante el viaje;
  seguimiento 3–5 días antes; alertas por lluvias o temperaturas extremas; y
  sugerir actividades bajo techo si el clima afecta el itinerario.
- **5.6 Alertas:** por clima, presupuesto, gastos o nuevas recomendaciones, por
  correo o Telegram.
- **5.8 Soporte:** chat o correo antes, durante y después del viaje, y
  retroalimentación al final del viaje.
- **6 Identidad visual:** paleta (azul profundo, turquesa, coral, arena),
  tipografías (Fraunces, Instrument Sans, IBM Plex Mono) y logo 4.

## 3. Planeado vs. resultado final
| Planeado | Resultado | Estado |
|---|---|---|
| Clima del destino y pronóstico | Solo un consejo fijo de "Clima" en *Mi viaje*; sin API ni pronóstico | No implementado |
| Alertas por clima y actividades alternativas | No se implementó | No implementado |
| Alertas por correo o Telegram | Funcionan para registro e inicio de sesión (HU-66, HU-67, HU-76) | Parcial |
| Soporte por chat o correo | No se implementó; se reemplazó por el manual de usuario | No implementado |
| Retroalimentación al final del viaje | No se implementó | No implementado |
| Identidad visual | Logo 4, Instrument Sans e IBM Plex Mono se mantuvieron. Fraunces → Archivo Black y paleta ajustada | Cambió |
| No estaba planeado | Pantalla de bienvenida, checklist de equipaje, imágenes por destino, consejos por destino, selección de 1 de 3 destinos | Extra |

## 4. Qué se entregó
- Correo de bienvenida y vinculación con bot de Telegram (`NotificadorCorreo`,
  `NotificadorTelegram`, `TelegramControlador`).
- Selección del destino entre los tres recomendados (HU-68) y resumen editable.
- Pantalla de bienvenida (HU-56) y checklist de equipaje (HU-57).
- Identidad visual uniforme (HU-58, HU-59) con imágenes distintas por destino (HU-78).
- Manual de usuario (`docs/user_guide`).
- **Clima:** se investigó y se eligió una API (Santiago, Sprint 2), pero no se integró.

## 5. Métricas
| Métrica | Valor |
|---|---|
| Issues cerrados | 30 / 30 (100%) |
| Reparto de issues | María Alejandra 13 · Santiago 13 · Valeria 8 · Isabella 3 · Gabriela 3 |
| Funcionalidades de la definición sin implementar | Clima (HU#10–12), soporte y retroalimentación |

## 6. Lo que dijeron las Reviews y Retrospectivas
- **Retro del 23/08:** Santiago documentó la investigación de la API del clima.
- **Review del 30/08:** el módulo climático se dejó "para etapas posteriores".
- **Review del 20/09:** bienvenida, checklist, identidad visual y recursos gráficos.
- **Retro del 20/09 (semana de receso):** el trabajo bajó. Valeria criticó los
  dailys sin llenar y Santiago respondió que "parte de la metodología es ser
  honestos con el trabajo que se hizo". Isabella pidió mejorar cómo se toman las
  decisiones en grupo.
- **Review del 27/09:** notificaciones por correo y Telegram y selección del destino.
- **Retro del 27/09:** el equipo sintió que la retro anterior "valió la pena" y la
  comunicación mejoró. Valeria propuso fijar una hora para las reuniones.
  Isabella advirtió que el diseño iba muy bien, pero "nos falta trabajar un poco
  más en el back".
- **Review del 04/10:** integración con Telegram (HU-76) e imágenes por destino (HU-78).
- **Retro del 25/10:** Valeria hizo el manual de usuario.

## 7. Análisis
**Lo que funcionó**
- La **identidad visual** quedó consistente y es de lo que más destacaron las retros.
- Las notificaciones reutilizaron `INotificador` (M1) sin código duplicado.
- Se agregaron herramientas útiles que no estaban planeadas (checklist, consejos, bienvenida).

**Lo que se pudo hacer mejor**
- Es el milestone **más alejado de la definición**: de clima, alertas y soporte,
  solo quedaron parcialmente las alertas. Era justo lo que hacía de WiseTrip un
  acompañante "antes y durante el viaje".
- El clima se aplazó en agosto, pero sus historias se cerraron sin implementarse
  y sin decir que se descartaron.
- El milestone **mezcló temas**: diseño, notificaciones, selección de destino
  (M2) y reparto de presupuesto (M4).
- Se priorizó el diseño sobre el Back, como advirtió Isabella el 27/09.
- La identidad visual cambió (tipografía y colores) sin actualizar la definición.
- Pocas pruebas propias del módulo (2 issues).

**Decisiones clave**
- Priorizar la experiencia visual y la comunicación sobre la integración del clima.
- Reemplazar el soporte por chat con un manual de usuario.

## 8. Retrospectiva Starfish
| Sección | Aportes |
|---|---|
| **Más de** | Funcionalidades de acompañamiento real (alertas útiles durante el viaje) · Equilibrio entre diseño y Back · Pruebas de notificaciones y selección de destino |
| **Menos de** | Mezclar historias de otros módulos en el milestone · Cerrar historias sin aclarar su estado · Cambiar la identidad visual sin documentarlo |
| **Comenzar a hacer** | Integrar el clima con la API que ya se investigó · Indicar en el issue si algo se descarta y por qué · Fijar una hora para las reuniones |
| **Dejar de hacer** | Dejar los dailys sin llenar · Dejar el manual y los diagramas para el final |
| **Seguir haciendo** | Reutilizar interfaces existentes (`INotificador`) · Mantener una sola identidad visual · Agregar detalles que ayudan al viajero (checklist, consejos) |

## 9. Acciones de mejora
| Acción | Responsable | Para |
|---|---|---|
| Documentar clima, soporte y retroalimentación como trabajo futuro, con la API ya elegida | Equipo de Back | Entrega final |
| Actualizar la sección de identidad visual con la tipografía y paleta reales | Equipo de Diseño | Entrega final |
| Asignar cada historia al milestone del módulo que le corresponde | Scrum Master | Próximos proyectos |

## 10. Conclusión
M5 dejó una experiencia visual coherente y canales de notificación funcionando,
pero se quedó lejos de la definición: el clima, las alertas de viaje y el soporte
eran lo que convertía a WiseTrip en un acompañante del viajero. El aprendizaje
principal fue **equilibrar diseño y funcionalidad**, y dejar explícito qué se
descarta para que el avance se pueda medir.

<img width="2000" height="1414" alt="starfishInformacionyAcompañamiento" src="https://github.com/user-attachments/assets/08a33fe4-25b5-4ea9-a32e-bbc47738938d" />

