# Retrospectiva Starfish — M1: Módulo de Registro y Gestión del Usuario

| Dato | Valor |
|---|---|
| Milestone | M1 - Módulo de Registro y Gestión del Usuario |
| Periodo | 19 de agosto al 12 de noviembre de 2026 (fecha límite: 20 de noviembre) |
| Sprints relacionados | Sprint 3 (pantallas de sesión), Sprint 5 (HU-48), Sprint 8 (HU-76, HU-77), Sprint 10 (HU-96), Sprints 13–14 (pruebas, SMS) |
| Estado | 48 de 48 issues cerrados (45 issues + 3 PR) |
| Etiquetas principales | Back (21), Front (14), Diseño (12), Base de Datos (7), Despliegue y Documentación (7), Pruebas (5), bug (2) |
| Equipo | Valeria Cortes, Santiago Clavijo, María Alejandra Rodríguez, Gabriela Melo, Isabella Posada |

## 1. Descripción del milestone
Módulo que permite al viajero **crear una cuenta e iniciar sesión** para guardar
sus viajes, preferencias y presupuestos. Con el tiempo absorbió también las
notificaciones al usuario y gran parte del trabajo de calidad (pruebas y SonarQube).

## 2. Lo que se planeó en la definición inicial
- **5.3 Registro de usuarios:** crear una cuenta para tener asociados al perfil los
  viajes, preferencias, presupuestos e itinerarios.
- **5.6 Alertas (parte de canales):** enviar alertas por correo o Telegram según
  el canal que elija el usuario.

## 3. Planeado vs. resultado final
| Planeado | Resultado | Estado |
|---|---|---|
| Registro con cuenta propia | Registro con correo y contraseña, validaciones (correo, contraseña de 6+ caracteres, documento, edad) | Cumplido |
| Información asociada al perfil | Viajes, preferencias, presupuesto y gastos guardados por usuario en PostgreSQL (Neon); pantalla *Mis viajes* | Cumplido |
| Alertas por correo o Telegram, según el canal elegido | Correo de bienvenida, vinculación con bot de Telegram y **SMS con Twilio** (no estaba planeado), pero solo para registro e inicio de sesión | Parcial |
| Usuario elige su canal de alertas | Se envía por todos los canales configurados; no hay preferencia de canal | Cambió |

## 4. Qué se entregó
- Pantallas de registro, inicio de sesión y bienvenida con la identidad de WiseTrip.
- `Usuario` con validaciones propias y `UsuarioDAO` sobre PostgreSQL (Neon).
- **Patrones GoF:** Builder (`UsuarioBuilder`, `DirectorUsuario`), Strategy
  (`INotificador` con correo, Telegram y SMS), Factory Method (`CreadorNotificacion`
  para bienvenida e inicio de sesión) y Facade (`UsuarioSesionFacade`).
- Plan de pruebas (CP48–CP57) y pruebas con JUnit + Mockito. `Usuario`,
  `UsuarioServicio` y los notificadores al 100% de cobertura.
- Diagramas de clases, análisis y componentes.

## 5. Métricas
| Métrica | Valor |
|---|---|
| Issues cerrados | 48 / 48 (100%) |
| Bugs reportados | 2 |
| Reparto de issues | Valeria 22 · María Alejandra 15 · Gabriela 12 · Isabella 11 · Santiago 10 |
| Calidad (SonarCloud, todo el proyecto) | 0 bugs, 0 vulnerabilidades, calificación A, 97.7% de cobertura |

## 6. Lo que dijeron las Reviews y Retrospectivas
- **Retro del 30/08:** María Alejandra hizo las pantallas de inicio, registro e
  inicio de sesión. Santiago contó que "unos lo hicimos en JavaFX y otros en
  JSP", y hubo que rehacer pantallas. Valeria señaló la falta de comunicación
  sobre qué tecnología usar en el Front.
- **Retro del 06/09:** tras la pre-entrega, el profesor y el monitor corrigieron
  el repositorio: historias con formato INVEST, DoD y commits asociados a los
  issues. Gabriela dijo que habían entendido "50% del proyecto" cuando era "50% de
  dos HU". Isabella pidió leer mejor la rúbrica.
- **Review del 13/09:** HU-48 unificó el estilo de registro e inicio de sesión.
- **Retro del 27/09:** María Alejandra hizo commits en `main` con un correo que no
  era del proyecto y tuvo que reescribirlos, lo que causó conflictos al hacer merge.
- **Review del 04/10:** Telegram (HU-76) y organización de los datos del usuario (HU-77).
- **Review del 18/10:** HU-96 dejó un inicio de sesión "más simple y estable".
- **Retro del 25/10:** Valeria no pudo empezar el envío de mensajes porque no
  sabían si el profesor lo quería como autenticador. Al final se resolvió con SMS
  de aviso (HU-140, HU-141, Sprint 14).
- **Review del 08/11:** se hicieron las pruebas de servicios y modelos (HU-128 a
  HU-133). Los DAO no se probaron "para que no se dañe nada" (HU-134 parcial).

## 7. Análisis
**Lo que funcionó**
- Es el módulo **más completo**: el flujo de cuenta funciona de punta a punta y
  cumple lo que pedía la definición.
- Separar el canal (Strategy) del mensaje (Factory Method) permitió **agregar SMS
  sin tocar** correo ni Telegram, aunque el SMS no estaba en la definición.
- Mockito permitió probar los envíos sin mandar mensajes reales.

**Lo que se pudo hacer mejor**
- Las alertas quedaron **limitadas a eventos de cuenta**. La definición las
  pensaba para clima, presupuesto y gastos, que es donde aportan más valor al viajero.
- El usuario no puede elegir su canal, como prometía la definición.
- Las **credenciales** quedaron en `application.properties` y se subieron al repositorio.
- Las pruebas con la fecha de "hoy" fallaban en GitHub Actions por la diferencia
  entre UTC y la hora de Bogotá.
- Al principio no había acuerdo sobre la tecnología del Front (JavaFX vs. JSP).
- El milestone duró casi todo el semestre porque siguió recibiendo historias de calidad.

**Decisiones clave**
- Base de datos en la nube (Neon) para que todo el equipo use los mismos datos.
- Todas las notificaciones detrás de una sola interfaz (`INotificador`).
- No probar los DAO para no romper la conexión con la base de datos.

## 8. Retrospectiva Starfish
| Sección | Aportes |
|---|---|
| **Más de** | Pruebas de casos borde en las validaciones · Revisar SonarQube antes de cada PR · Preguntar al profesor antes de implementar algo ambiguo |
| **Menos de** | Credenciales escritas en el código · Pruebas que dependen de la hora del sistema · Commits en `main` o con la cuenta equivocada |
| **Comenzar a hacer** | Usar variables de entorno para las claves · Usar las alertas para presupuesto y gastos, no solo para la cuenta · Dejar que el usuario elija su canal |
| **Dejar de hacer** | Subir secretos al repositorio · Empezar pantallas sin acordar la tecnología (JavaFX vs. JSP) |
| **Seguir haciendo** | Patrones GoF para separar responsabilidades (Builder, Strategy) · Probar con Mockito los servicios externos · Mantener el inicio de sesión simple |

## 9. Acciones de mejora
| Acción | Responsable | Para |
|---|---|---|
| Mover las credenciales a variables de entorno y rotarlas | Equipo de Back | Antes de la entrega final |
| Calcular "hoy" con la zona horaria de la app en todas las pruebas | Quien escriba pruebas con fechas | Hecho (#435) |
| Reutilizar `INotificador` para avisar cuando el gasto pase del 90% | Equipo de Back | Trabajo futuro |

## 10. Conclusión
M1 cumplió lo que prometía la definición sobre el registro y superó lo previsto en
canales (SMS). Lo que quedó corto fue el **uso** de esas notificaciones: se
construyó el mecanismo, pero no las alertas de viaje que daban valor al usuario.
El aprendizaje principal fue de **seguridad y calidad**: los secretos no van en el
código y las pruebas deben ser repetibles en cualquier entorno.


<img width="2000" height="1414" alt="starfishRegistroygestiondelusuario" src="https://github.com/user-attachments/assets/8a022560-7565-4bcb-8a18-f6337d67e1a0" />
