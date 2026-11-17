# WiseTrip — Reporte final de proyecto y Postmortem

| Dato | Valor |
|---|---|
| Proyecto | WiseTrip — planificación de viajes por Latinoamérica según presupuesto e intereses |
| Repositorio | `puj-course/FIS_2630_1204_G3` |
| Periodo | 13 de agosto al 16 de noviembre de 2026 (14 sprints semanales) |
| Equipo | Valeria Cortes (`valeriacortess`), Santiago Clavijo (`Santiago-Clavijo`), María Alejandra Rodríguez (`MalejaRodri`), Gabriela Melo (`GabrielaMeloG`), Isabella Posada (`isaposada`) |
| Corte de los datos | 16/11/2026, rama `origin/main` y API de GitHub |

## 0. Metodología y fuentes
- **Commits, ramas y merges:** `git log` sobre `origin/main`. Los commits de merge se cuentan aparte. Los autores con varios nombres se unificaron (por ejemplo, `isa` e `Isabella Posada`).
- **Issues, HU, milestones y PR:** API pública de GitHub. Se cuentan como **HU** los issues cuyo título empieza por "HU" o "Como…". Los PR se excluyen.
- **Sprint de una HU:** el sprint en el que se cerró (`closed_at`, hora de Bogotá).
- **HU trazable:** su número (`#N`) aparece en al menos un commit o PR.
- **Sprints:** semanas de lunes a domingo según el CHANGELOG. S1 = 13–16/08 y S14 = 09–20/11.

## 1. Resumen ejecutivo
| Indicador | Valor |
|---|---|
| Milestones | 6 (M0 cerrado; M1–M5 completos, abiertos hasta el 20/11) |
| Issues totales (sin PR) | 339 |
| Historias de usuario | 149 → **146 cerradas (98%)**, 3 abiertas |
| HU cerradas y trazables | **127 de 146 (87%)** |
| Commits en `main` (sin merges) | **1.278** |
| Commits de merge | 205 |
| Pull requests | 138 (**134 fusionados**, 4 cerrados sin fusionar) |
| Daily Sprint registrados como issues | 84 |
| Bugs reportados / cerrados | 10 / 10 |
| Tiempo de ciclo de una HU (creación → cierre) | mediana 6 días · promedio 11,2 · máximo 78 |
| Calidad (SonarCloud) | Quality Gate OK · cobertura 97,7% · 508 pruebas · 0 bugs · 0 vulnerabilidades · calificación A |
| Funcionalidades de la definición inicial | 3 de 8 completas, 3 parciales o cambiadas, 2 sin implementar |

## 2. Milestones
| Milestone | Issues cerrados / total | HU cerradas / total | HU trazables | Estado en GitHub | Cumplimiento frente a la definición |
|---|---|---|---|---|---|
| M0 – Planeación y base | 19 / 19 | 0 (tareas de organización) | — | Cerrado (19/08, fecha límite 30/08) | Cumplido: Definición, backlog y repositorio |
| M1 – Registro y gestión del usuario | 48 / 48 | 31 / 31 | 27 (87%) | Abierto, 100% | Cumplido: Registro · Parcial: alertas solo de cuenta |
| M2 – Recomendación personalizada | 66 / 69 | 48 / 51 | 40 (83%) | Abierto, 3 HU pendientes | Cumplido: Recomendaciones por intereses y presupuesto |
| M3 – Planificación e itinerario | 51 / 51 | 34 / 34 | 29 (85%) | Abierto, 100% | Parcial: Itinerario manual · No implementado: alojamiento |
| M4 – Presupuesto y gastos | 19 / 19 | 9 / 9 | 9 (100%) | Abierto, 100% | Cumplido: Presupuesto y gastos · No implementado: pagos |
| M5 – Información y acompañamiento | 30 / 30 | 17 / 17 | 15 (88%) | Abierto, 100% | No implementado: Clima y soporte · Parcial: identidad visual |
| Sin milestone | — | 7 / 7 | 7 (100%) | — | — |
| **Total** | — | **146 / 149** | **127 (87%)** | | |

**HU abiertas:** #479 (HU-142, complejidad ciclomática), #480 (HU-143, métricas orientadas a objetos) y #474 (HU-144, aviso de Telegram al equipo). Las tres son de M2.

**HU cerradas sin trazabilidad (19):** #49, #147, #170, #173, #182, #183, #192, #217, #221, #224, #276, #345, #389, #391, #392, #417, #418, #419, #484.

### Definición inicial vs. producto final
| # | Funcionalidad | Estado |
|---|---|---|
| 5.1 | Itinerario personalizado | Parcial: Lo arma el usuario arrastrando actividades y se guarda en el navegador |
| 5.2 | Alojamiento | Parcial: Solo lugares de Geoapify; sin reservas |
| 5.3 | Registro de usuarios | Cumplido: Más SMS con Twilio |
| 5.4 | Control del presupuesto | Cumplido: Reparto, gastos y alerta al 90%; sin pagos |
| 5.5 | Clima | No implementado |
| 5.6 | Alertas | Parcial: Correo, Telegram y SMS solo al registrarse e iniciar sesión |
| 5.7 | Recomendaciones por intereses | Cumplido |
| 5.8 | Soporte y retroalimentación | No implementado (reemplazado por el manual de usuario) |

## 3. HU por sprint
| Sprint | Fechas | HU creadas | HU cerradas | Issues cerrados (todos) | PR fusionados | Commits |
|---|---|---|---|---|---|---|
| S1 | 13–16/08 | 0 | 0 | 1 | 0 | 21 |
| S2 | 17–23/08 | 14 | 0 | 0 | 3 | 28 |
| S3 | 24–30/08 | 15 | 0 | 0 | 10 | 30 |
| S4 | 31/08–06/09 | 18 | **23** | **97** | 9 | **154** |
| S5 | 07–13/09 | 12 | **26** | 39 | 10 | 36 |
| S6 | 14–20/09 (receso) | 10 | 7 | 13 | 7 | 54 |
| S7 | 21–27/09 | 10 | 13 | 23 | 13 | 129 |
| S8 | 28/09–04/10 | 10 | 7 | 26 | 12 | 142 |
| S9 | 05–11/10 | 10 | 4 | 7 | 10 | 119 |
| S10 | 12–18/10 | 11 | 19 | 40 | 16 | 155 |
| S11 | 19–25/10 (parciales) | 10 | 5 | 13 | 4 | 116 |
| S12 | 26/10–01/11 (parciales) | 10 | 5 | 11 | 4 | 80 |
| S13 | 02–08/11 | 10 | 10 | 19 | 17 | 122 |
| S14 | 09–16/11 | 9 | **27** | 37 | 19 | 91 |
| **Total** | | **149** | **146** | **326** | **134** | **1.278** |

**Lectura de los datos**
- **S2 y S3 no cerraron ninguna HU**, aunque las Reviews del 23/08 y del 30/08 marcan todo como demostrado. Las historias se cerraron en bloque en S4, tras la reorganización con el monitor (97 issues cerrados el 05–06/09, cuando se rehicieron los milestones).
- Desde S5 el equipo planificó **unas 10 HU por sprint**, pero cerró entre 4 y 27. La velocidad no fue estable.
- Hubo caídas claras en **S6 (receso)** y en **S11–S12 (parciales)**: 5 HU por sprint. Coinciden con lo que dijeron las retros.
- En **S14 se cerraron 27 HU**, muchas de ellas pendientes de sprints anteriores, antes de la entrega.

## 4. Commits
| Indicador | Valor |
|---|---|
| Commits en `main` (sin merges) | 1.278 |
| Commits de merge | 205 |
| Commits que citan un issue (`#N`) | 882 (**69%**) |
| Commits con formato convencional (`feat:`, `fix:`, `docs:`, `test:`…) | 920 (**72%**) |
| Merges con conflicto resuelto | 10 |
| Reverts | 4 |
| Sprints con más commits | S10 (155), S4 (154), S8 (142) |
| Sprints con menos commits | S1 (21), S2 (28), S3 (30), S5 (36) |

### Commits por integrante y sprint
| Sprint | Valeria | Gabriela | Santiago | María Alejandra | Isabella | Externos* |
|---|---|---|---|---|---|---|
| S1 | 13 | 7 | 0 | 0 | 0 | 1 |
| S2 | 9 | 16 | 1 | 1 | 1 | 0 |
| S3 | 8 | 6 | 4 | 9 | 3 | 0 |
| S4 | 45 | 23 | 17 | 43 | 26 | 0 |
| S5 | 7 | 7 | 9 | 9 | 4 | 0 |
| S6 | 11 | 7 | 8 | 22 | 6 | 0 |
| S7 | 12 | 21 | 25 | 47 | 24 | 0 |
| S8 | 37 | 35 | 21 | 22 | 26 | 1 |
| S9 | 27 | 28 | 23 | 25 | 16 | 0 |
| S10 | 34 | 26 | 33 | 34 | 28 | 0 |
| S11 | 41 | 14 | 24 | 5 | 32 | 0 |
| S12 | 16 | 4 | 14 | 37 | 3 | 6 |
| S13 | 19 | 24 | 29 | 32 | 18 | 0 |
| S14 | 3 | 30 | 13 | 16 | 29 | 0 |

\* Externos: 9 commits de personas que no son del equipo, como la plantilla del curso y las correcciones del monitor.

## 5. Participación del equipo
| Integrante | Commits | % commits | Líneas + / − | PR abiertos | PR fusionados | HU cerradas asignadas | Issues asignados (todos) |
|---|---|---|---|---|---|---|---|
| María Alejandra | 302 | 23,6% | 36.753 / 20.743 | 28 | 28 | 31 | 154 |
| Valeria | 282 | 22,1% | 14.908 / 8.434 | 30 | 30 | 29 | 151 |
| Gabriela | 248 | 19,4% | 5.781 / 1.664 | 36 | 34 | 32 | 158 |
| Santiago | 221 | 17,3% | 7.582 / 5.513 | 35 | 33 | 29 | 142 |
| Isabella | 216 | 16,9% | 17.374 / 8.923 | 9 | 9 | 29 | 146 |
| Externos | 9 | 0,7% | 486 / 7 | — | — | — | — |

**Reparto por milestone** (issues por persona, incluidas las tareas)
| Milestone | Distribución |
|---|---|
| M0 | Valeria 5 · Isabella 4 · Santiago 4 · María Alejandra 3 · Gabriela 3 |
| M1 | Valeria 22 · María Alejandra 15 · Gabriela 12 · Isabella 11 · Santiago 10 |
| M2 | Isabella 34 · Santiago 16 · Gabriela 9 · Valeria 8 · María Alejandra 5 |
| M3 | Gabriela 22 · María Alejandra 12 · Valeria 11 · Santiago 4 |
| M4 | Gabriela 14 · María Alejandra 4 · Santiago 1 |
| M5 | María Alejandra 13 · Santiago 13 · Valeria 8 · Isabella 3 · Gabriela 3 |

**Lectura de los datos**
- **En el total, la participación fue equilibrada.** Cada integrante tiene entre el 17% y el 24% de los commits y entre 29 y 32 HU cerradas asignadas.
- **Por módulo, el trabajo estuvo concentrado.** Isabella llevó la mitad de M2, Gabriela el 74% de M4 y casi la mitad de M3, y M4 no tuvo participación de Valeria ni de Isabella.
- Isabella abrió solo 9 PR porque fusionaba su trabajo a través de `develop`.
- El número de líneas es solo indicativo, porque incluye imágenes, documentación y archivos generados.

## 6. Control de versiones
| Aspecto | Resultado |
|---|---|
| Estrategia | Gitflow simplificado: `main` ← `develop` ← una rama por integrante, más ramas `feature/*` para Docker |
| Ramas remotas | `main`, `develop`, 5 ramas de integrante, `feature/docker-deploy`, `feature/docker-compose-red`, `1raEntregaJueves` |
| PR por rama destino | 90 a `main` · 48 a `develop` |
| PR por rama origen | `features-santiago` 35 · `features_Gabriela` 25 · `develop` 25 · `features-valeria` 23 · `feature_MariaAlejandra` 21 · `features-isabella` 5 · `feature/*` 3 · `sync-develop-main` 1 |
| Tasa de fusión de PR | 134 / 138 (97%) |
| PR que citan un issue | 71 / 138 (51%) |
| Integración continua | GitHub Actions: pruebas + informe (`pruebas.yml`), SonarCloud (`SonarQube.yml`), Docker build + compose + publicación en DockerHub (`docker.yml`), avisos por Telegram |
| Etiquetas y releases | **Ninguna** (no hay tags `v*`) |

**Problemas registrados en las retros:** commits directos a `main` o `develop` (Retros del 27/09), commits con una cuenta de correo ajena al proyecto (27/09), commits duplicados (04/10), imágenes que solo se veían en un equipo por IDs viejos (27/09), y conflictos al hacer merge "porque no sabemos qué cambios hacemos como grupo" (27/09). La refactorización del Sprint 11 se hizo desde una sola cuenta para evitar conflictos (25/10).

## 7. Postmortem

### 7.1 Qué salió bien
1. **Se entregó el núcleo del producto:** recomendar destinos dentro del presupuesto y controlar los gastos.
2. **La calidad final es alta:** 97,7% de cobertura, 0 bugs y 0 vulnerabilidades en SonarCloud, con 8 patrones GoF aplicados.
3. **El 98% de las HU se cerró** y el 87% es trazable a commits o PR.
4. **La participación total fue pareja** y el equipo mejoró mucho su coordinación después de la retroalimentación del 06/09.
5. **Hay automatización completa:** pruebas, análisis de calidad, contenedores y notificaciones en cada push.

### 7.2 Qué salió mal y por qué
| # | Problema (evidencia) | Causa raíz | Por qué ocurrió (5 porqués, resumido) |
|---|---|---|---|
| 1 | 5 de 8 funcionalidades de la definición no se completaron (clima, soporte, pagos, reservas, itinerario automático) | **Alcance sin validar** | Se escribió la definición antes de investigar las APIs (S2). Los costos y la complejidad aparecieron después, y el recorte (Review del 30/08) no se reflejó en la definición ni en los issues |
| 2 | 0 HU cerradas en S2–S3 y 97 issues cerrados en S4 | **Aún no se entendía Scrum ni GitHub** | Las retros del 15/08 y 18/08 dicen que apenas se entendía qué era un milestone. Las HU se rehicieron con formato INVEST y DoD después de la pre-entrega |
| 3 | Velocidad irregular (4 a 27 HU por sprint) y caídas en S6, S11 y S12 | **Mala estimación frente a la carga académica** | No se estimaron puntos (las HU de la planning quedaban "TBD"). Las HU grandes se pusieron en semanas de parciales y receso (Retros del 20/09, 25/10 y 01/11) |
| 4 | Módulos concentrados en una persona (M2: Isabella; M4: Gabriela) | **Reparto por especialidad sin rotación** | Cada persona siguió con "su" módulo, y por eso había un solo dueño del conocimiento (Retro del 18/10: "aprender el funcionamiento de las partes de nuestros compañeros") |
| 5 | 19 HU cerradas sin commit ni PR, y 49% de los PR sin issue | **La trazabilidad no era parte de la DoD** | Se asociaron commits a issues de forma retroactiva (06/09) y no se exigió `#N` en cada PR |
| 6 | Diagramas incorrectos en la sustentación, patrón duplicado, clase desconectada | **Poca revisión cruzada** | No había revisión de código entre compañeros antes de fusionar (Retro del 18/10) |
| 7 | Reviews sin feedback de stakeholders, fechas erróneas, una Review copiada y la última vacía | **Las ceremonias se llenaban como trámite** | Santiago lo reconoció el 18/10: "seguimos la metodología bien, exceptuando los reviews" |
| 8 | Credenciales en `application.properties`, M1–M5 sin cerrar, sin tags | **No había un checklist de cierre ni de seguridad** | Las prioridades estuvieron en las funcionalidades y la cobertura |

### 7.3 Acciones de mejora
| # | Acción | Causa que ataca | Indicador de éxito | Responsable |
|---|---|---|---|---|
| 1 | Hacer un *spike* de viabilidad (API, costo, tiempo) antes de aceptar una HU con servicios externos y mantener la definición del producto actualizada | 1 | 0 HU cerradas sin implementar | Product Owner + Back |
| 2 | Estimar en puntos y comparar con la capacidad real, reduciendo la capacidad en semanas de parciales | 3 | Variación de la velocidad < 30% entre sprints | Scrum Master |
| 3 | Incluir en la DoD: PR con `Closes #N` + Quality Gate OK + diagrama actualizado | 5, 6 | 100% de HU y PR trazables | Todo el equipo |
| 4 | Exigir 1 aprobación de otro integrante antes de fusionar a `main` | 6 | 0 patrones duplicados o clases desconectadas | Todo el equipo |
| 5 | Rotar a los responsables por módulo en cada sprint | 4 | Ningún integrante con > 40% de un módulo | Scrum Master |
| 6 | Registrar en cada Review el feedback real del profesor o monitor y convertirlo en issues | 7 | 100% de Reviews con feedback | Quien lidere la Review |
| 7 | Etiquetar como "Descartado / trabajo futuro" las HU de clima, pagos y soporte, con su motivo | 1, 8 | Backlog que refleja el producto real | Product Owner |
| 8 | Checklist de cierre: mover secretos a variables de entorno y rotarlos, cerrar M1–M5, crear el tag `v1.0.0` | 8 | Release publicado y sin secretos en el repositorio | DevOps (María Alejandra) |

### 7.4 Conclusión
WiseTrip cerró 146 de 149 historias en 14 sprints, con 1.278 commits repartidos de
forma pareja y una calidad técnica alta. El producto final es más pequeño que la
definición inicial. El postmortem muestra que eso no se debió a falta de esfuerzo,
sino a un **alcance que no se validó a tiempo**, a **estimaciones que no tuvieron en
cuenta la carga académica** y a ceremonias y trazabilidad que se llenaban como
trámite. Las acciones de mejora apuntan a esas causas: validar antes de
comprometer, estimar con la capacidad real y hacer de la trazabilidad y la
revisión cruzada parte de la *Definition of Done*.
