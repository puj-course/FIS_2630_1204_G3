# Pruebas funcionales - Notificaciones de Registro e Inicio de Sesión (WiseTrip)

*Fecha de ejecución:* _completar_ · *Responsable:* _completar_

## Objetivo y alcance

Verificar que el módulo de registro, inicio de sesión y las notificaciones por correo y Telegram funcionen correctamente de extremo a extremo en la aplicación WiseTrip.

Incluye: registro de cuenta nueva, envío de correo de bienvenida, vinculación de Telegram, inicio de sesión y envío de notificación de confirmación por Telegram.

## Entorno de pruebas

| Elemento | Detalle |
| --- | --- |
| Aplicación | WiseTrip, puerto 8090 (local) |
| Comando de arranque | `.\mvnw.cmd spring-boot:run` |
| Base de datos | PostgreSQL en Neon (nube) |
| Correo | Gmail SMTP (`spring.mail.*`) |
| Telegram | Bot `wisetrip_g3_notif_bot` |
| Navegador | _completar_ |
| Fecha de ejecución | _completar_ |

## Caso de prueba Inicio correcto de Sprintboot 

<img width="959" height="564" alt="image" src="https://github.com/user-attachments/assets/4bf6154b-36d6-4cd6-bc7a-81285ae028fb" />


## Casos de prueba: registro con correo

