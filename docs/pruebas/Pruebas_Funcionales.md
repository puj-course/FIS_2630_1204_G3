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


## Casos de prueba: Inicio de la aplicacion correcatmente 
Esta pantalla corresponde a la vista pública de bienvenida de la aplicación WiseTrip. Presenta la propuesta de valor del sistema, métricas clave (20 países, 19 monedas, 41 preguntas de perfil y 3 recomendaciones de destino), un catálogo de destinos sugeridos con filtros por temporada y un desglose explicativo del flujo de recomendación en cuatro pasos. A nivel funcional, expone los puntos de entrada para la navegación hacia el catálogo general (Destinos, Cómo funciona), la gestión de accesos (Iniciar sesión, Crear cuenta) y el inicio directo del cuestionario de presupuesto (Empezar a planear).

<img width="942" height="468" alt="image" src="https://github.com/user-attachments/assets/c1e4aea5-6c7c-45f9-beee-4f6846fccf56" />

## Caso de prueba: Opción destinos en la pantalla 

En la barra de navegación superior se encuentra la opción Destinos. Al hacer clic en este botón, el sistema redirige correctamente a la sección del catálogo de destinos de la aplicación

<img width="946" height="469" alt="image" src="https://github.com/user-attachments/assets/5a8ac77d-2b6e-4d3e-9394-a12fe00f2b5e" />

## Caso de prueba: Opción Como funciona 

En la barra de navegación superior se encuentra la opción Cómo funciona. Al hacer clic en este enlace, el sistema realiza un desplazamiento suave (scroll) o redirige a la sección explicativa donde se detalla el flujo de recomendación en cuatro pasos de la aplicación.

<img width="944" height="468" alt="image" src="https://github.com/user-attachments/assets/4508b130-f5d9-4ea1-8b99-95dae72c0dbf" />

## Caso de prueba: Registro con correo

En la barra de navegación superior se encuentra la opción **Crear cuenta**. Al hacer clic en este botón, el sistema despliega el formulario de registro de usuario. Tras ingresar un nombre, correo electrónico válido y contraseña, y pulsar en **Registrarse**, el sistema crea la cuenta en la base de datos, envía un correo de bienvenida mediante el servicio SMTP y redirige al usuario a la vista correspondiente.

<img width="957" height="469" alt="image" src="https://github.com/user-attachments/assets/f1f28b69-8e3f-4140-82d7-53736ca3e27c" />

<img width="959" height="469" alt="image" src="https://github.com/user-attachments/assets/b96bde63-4eaa-4c3d-b299-0de9cc3c2bdf" />

<img width="959" height="468" alt="image" src="https://github.com/user-attachments/assets/8e3044ae-6fe6-414b-ba57-94437e12bb2e" />

Se puede observar como quedo registrado el usuario dentro de la base de datos 

<img width="959" height="452" alt="image" src="https://github.com/user-attachments/assets/7f8643c8-6707-4f3c-b253-759c907e861f" />

## Caso de prueba: Intento de registro con correo ya existente

En la vista de registro (**Crear cuenta**), se ingresan datos de un usuario utilizando una dirección de correo electrónico previamente registrada en la base de datos. Al presionar el botón **Registrarse**, el sistema detecta la duplicidad del identificador, bloquea la creación del registro y muestra un mensaje de validación indicando que el correo ya se encuentra en uso, evitando registros duplicados y sin emitir correos adicionales.

<img width="943" height="466" alt="image" src="https://github.com/user-attachments/assets/205fa90f-c328-4e30-8a2b-e1dfa2f93d19" />

## Caso de prueba: Intento de registro con campos incompletos

En la vista de registro (**Crear cuenta**), se intenta enviar el formulario omitiendo uno o más campos obligatorios (nombre, correo electrónico o contraseña). Al presionar el botón **Registrarse**, el sistema interrumpe el flujo, no crea ningún registro en la base de datos ni despacha correos, y resalta los campos vacíos con mensajes de validación que solicitan completar la información requerida.

<img width="945" height="471" alt="image" src="https://github.com/user-attachments/assets/e8fd9e92-7105-4e93-b82a-d62608b97a81" />

## Caso de prueba: Recepción de correo de bienvenida tras el registro

Una vez completado satisfactoriamente el registro de una cuenta nueva, el servicio SMTP de la aplicación despacha una notificación por correo electrónico a la dirección registrada. Al consultar la bandeja de entrada del usuario, se valida la recepción oportuna del mensaje de bienvenida de WiseTrip, verificando que contenga el remitente configurado, asunto adecuado y la información inicial de bienvenida al servicio.

<img width="959" height="468" alt="image" src="https://github.com/user-attachments/assets/8e3044ae-6fe6-414b-ba57-94437e12bb2e" />
<img width="959" height="305" alt="image" src="https://github.com/user-attachments/assets/629bc4ea-9c6f-40cf-9aaf-cb0a3318da3d" />





