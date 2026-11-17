# Retrospectiva Starfish — M4: Módulo de Control de Presupuesto y Gastos

| Dato | Valor |
|---|---|
| Milestone | M4 - Módulo de Control de Presupuesto y Gastos |
| Periodo | 19 de agosto al 6 de octubre de 2026 (fecha límite: 20 de noviembre) |
| Sprints relacionados | Sprint 2 (investigación de pagos), Sprint 7 (HU-69), Sprint 8 (HU-79, HU-84), Sprint 9 (HU-88, HU-89), Sprint 10 (HU-98, HU-99) |
| Estado | 19 de 19 issues cerrados |
| Etiquetas principales | Pruebas (8), Front (8), Back (6), Diseño (5), Documentación (3) |
| Equipo | Valeria Cortes, Santiago Clavijo, María Alejandra Rodríguez, Gabriela Melo, Isabella Posada |

## 1. Descripción del milestone
Módulo financiero del viaje. El viajero registra su presupuesto y su moneda, lo
reparte entre categorías (hospedaje, alimentación, transporte, actividades,
imprevistos) y **registra sus gastos reales** para saber cuánto le queda.

## 2. Lo que se planeó en la definición inicial
- **5.4 Control del presupuesto**, descrita como "una funcionalidad fundamental":
  - Modificar el presupuesto en 5 situaciones (subirlo, bajarlo, agregar
    actividades, cambiar el dinero disponible, cambiar las fechas) y **recalcular**.
  - Registrar gastos durante el viaje y actualizar el dinero usado y el restante.
- **Objetivo:** "que el viajero tenga todo el control sobre su economía antes,
  durante y después del viaje".
- **5.6 Alertas por variaciones del presupuesto o gastos realizados**, por correo o Telegram.
- **Reservas y pagos:** en el objetivo ("el usuario contará con … reservas") y en el backlog (HU#4–6).

## 3. Planeado vs. resultado final
| Planeado | Resultado | Estado |
|---|---|---|
| Ingresar presupuesto | Presupuesto con moneda local o USD y bandera | Cumplido |
| Repartir el presupuesto | Reparto por categoría con porcentajes ajustables (HU-69, HU-79) | Cumplido |
| Registrar gastos y ver el restante | Gastos en *Mi viaje* con barra de uso por categoría | Cumplido |
| Recalcular al modificar presupuesto o fechas | Se puede ajustar antes de confirmar; el recálculo durante el viaje es limitado | Parcial |
| Alertas de presupuesto por correo o Telegram | Alerta **dentro de la página** (roja al 90% del gasto), no por correo ni Telegram | Cambió |
| Pagos y reservas dentro de la app | Investigados y probados, pero no implementados | No implementado |

## 4. Qué se entregó
- `RepartoServicio`, `RepartoPresupuesto`, `GastosServicio`, `EstadoCategoriaGasto` y `GastoDAO`.
- Clases de dominio con métodos de negocio (HU-88) e interfaces del módulo (HU-89).
- **Patrón Factory Method:** `ControlCategoriaCreador` con `CreadorControlEstricto`
  y `CreadorControlFlexible`. Las categorías no pueden pasarse del cupo, excepto
  imprevistos, que solo muestra una advertencia.
- **Patrón Decorator (relacionado):** `PresupuestoConMargenDecorador` (5% de imprevistos).
- Plan de pruebas CP01–CP33; `GastosServicio` al 100% de líneas.

## 5. Métricas
| Métrica | Valor |
|---|---|
| Issues cerrados | 19 / 19 (100%) |
| Reparto de issues | Gabriela 14 · María Alejandra 4 · Santiago 1 |
| Historias que no llegaron al producto | 3 (pagos: HU#4, HU#5, HU#6) |

## 6. Lo que dijeron las Reviews y Retrospectivas
- **Retro del 23/08:** Gabriela hizo "una investigación profunda de las APIs de
  pasarela de pagos" y eligió la más indicada.
- **Review del 30/08:** se decidió dejar **"para etapas posteriores el módulo
  climático y la pasarela de pagos"**. Es la primera vez que se registra el recorte.
- **Retro del 30/08:** Gabriela pidió "no adelantarnos tanto y querer abarcar mucho
  del proyecto cuando no nos va a dar el tiempo".
- **Review del 27/09:** distribución del presupuesto entre categorías (HU-69).
- **Review del 04/10:** porcentajes ajustables (HU-79) y bandera de moneda en el resumen (HU-84).
- **Review del 11/10:** el dominio del presupuesto dejó de ser solo getters y
  setters (HU-88) y se crearon sus interfaces (HU-89).
- **Review del 18/10:** gestión del presupuesto (HU-98) y registro de gastos (HU-99).
- **Retro del 25/10:** Gabriela advirtió que "tenemos que tener en cuenta lo que
  nos propusimos lograr al principio del proyecto".

## 7. Análisis
**Lo que funcionó**
- Es la parte de la propuesta de valor ("control sobre su economía") que llegó
  al producto: presupuesto, reparto, gastos y restante.
- El **Factory Method** dejó la regla de negocio clara y extensible.
- Es el módulo con **más peso de pruebas** (8 de 19 issues), y se terminó primero (6 de octubre).

**Lo que se pudo hacer mejor**
- El trabajo quedó **muy concentrado**: Gabriela tuvo 14 de 19 issues, y dos
  integrantes no participaron.
- **Pagos y reservas** se cerraron en GitHub sin implementarse, aunque la Review
  del 30/08 ya los había aplazado. No quedó escrito que se descartaron.
- Las alertas de presupuesto quedaron solo dentro de la página. La definición las
  quería por correo o Telegram, y ese canal ya existía (M1).
- No se cubren todas las situaciones de recálculo que describía la definición.
- Las reglas de moneda se definieron tarde y obligaron a ajustar pantallas.

**Decisiones clave**
- Dos políticas de control: **estricta** y **flexible** (para imprevistos).
- No integrar una pasarela de pagos en esta versión.

## 8. Retrospectiva Starfish
| Sección | Aportes |
|---|---|
| **Más de** | Pruebas sobre cálculos con dinero · Repartir el trabajo entre más integrantes · Reutilizar lo que ya existe (avisar por Telegram los gastos) |
| **Menos de** | Querer abarcar demasiado en una semana · Historias muy grandes como "pagar dentro de la app" · Concentrar un módulo en una sola persona |
| **Comenzar a hacer** | Marcar como "Descartado" lo que se aplaza, con su motivo · Revisar la definición inicial al cerrar cada módulo · Definir las reglas de moneda desde el diseño |
| **Dejar de hacer** | Cerrar historias sin aclarar si se implementaron · Prometer integraciones sin revisar su costo |
| **Seguir haciendo** | Factory Method para las reglas de cada categoría · Probar cada caso del plan (CP) · Mostrar el avance del gasto de forma visual |

## 9. Acciones de mejora
| Acción | Responsable | Para |
|---|---|---|
| Etiquetar HU#4–6 como "Descartado / trabajo futuro" con el motivo | Gabriela | Entrega final |
| Enviar la alerta del 90% también por `INotificador` | Equipo de Back | Trabajo futuro |
| Rotar responsables por módulo | Todo el equipo | Próximos proyectos |

## 10. Conclusión
M4 entregó un control de gastos útil y bien probado, que es el corazón de la
propuesta de valor de WiseTrip. Lo que quedó fuera (pagos, reservas, alertas por
correo) se aplazó a tiempo, pero no se documentó. El aprendizaje principal fue de
**gestión del alcance**: decidir pronto qué no se hará y dejarlo escrito.

<img width="2000" height="1414" alt="starfishControldePresupuestoYGastos" src="https://github.com/user-attachments/assets/4046a50a-8a2b-4b9e-b22e-7af08667d393" />

