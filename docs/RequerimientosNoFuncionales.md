## Requisitos No Funcionales (RNF)

### RNF01 - Compatibilidad multiplataforma
La plataforma debe funcionar correctamente en computadores, tabletas y dispositivos móviles, manteniendo una resolución mínima soportada de 360px de ancho (móvil) hasta 1920px (escritorio). En todos los tamaños de pantalla, el usuario debe poder acceder al 100% de las funcionalidades core del sistema (creación de viajes, gestión de presupuesto, itinerarios y reservas), aunque la disposición visual de los elementos pueda adaptarse a cada dispositivo.

### RNF02 - Usabilidad
Un usuario sin experiencia previa en la plataforma debe poder completar el flujo de planificación de un viaje (presupuesto, fechas, preferencias) sin asistencia externa, requiriendo no más de 3 intentos fallidos por pantalla. Esto debe verificarse mediante pruebas de usabilidad con al menos 10 usuarios representativos, midiendo la tasa de finalización del flujo y el número de intentos por pantalla.

### RNF03 - Seguridad de la información
La plataforma debe proteger la información personal, presupuestos, reservas e intereses de los usuarios mediante cifrado de datos en tránsito (HTTPS/TLS) y en reposo, además de control de acceso basado en autenticación de usuario.

### RNF04 - Disponibilidad de datos
La plataforma debe garantizar que la información del viaje esté disponible y accesible durante el 99% del tiempo de uso activo del usuario en el proceso de planificación y gestión.

### RNF05 - Tiempo de respuesta
La plataforma debe responder a las acciones principales del usuario (navegación entre pantallas, guardado de datos, consultas de recomendaciones) en un tiempo no mayor a 3 segundos bajo condiciones normales de operación.

### RNF06 - Estabilidad ante concurrencia
La plataforma debe soportar al menos 100 usuarios concurrentes sin degradar su rendimiento por debajo de los tiempos de respuesta establecidos en el RNF05.

### RNF07 - Compatibilidad con navegadores
La plataforma debe ser compatible con las últimas dos versiones estables de los navegadores Google Chrome, Safari, Mozilla Firefox y Microsoft Edge.

### RNF08 - Integridad de datos
La plataforma debe garantizar que, ante cualquier modificación de presupuestos, gastos, reservas e itinerarios, los datos resultantes sean consistentes y no se pierda ni corrompa información, verificable mediante un 100% de coincidencia entre los datos ingresados y los datos almacenados tras cada actualización.

### RNF09 - Integración con servicios externos
La plataforma debe integrarse con servicios externos de clima, movilidad, reservas y otros, de manera que una falla o demora en dichos servicios no afecte la disponibilidad ni el rendimiento general del sistema (tiempo de respuesta interno no superior a lo definido en el RNF05).

### RNF10 - Mantenibilidad
La plataforma debe estar diseñada de forma modular, de manera que la incorporación de nuevas funcionalidades o la corrección de errores no afecte el funcionamiento de los módulos existentes, verificable mediante pruebas de regresión antes de cada despliegue.

### RNF11 - Accesibilidad
La plataforma debe cumplir con el nivel AA de las pautas WCAG 2.1, garantizando que usuarios con discapacidad visual, auditiva o motriz puedan navegar y completar el flujo de planificación de viajes mediante lectores de pantalla, navegación por teclado y contrastes de color adecuados, verificable mediante herramientas automatizadas de auditoría con un puntaje mínimo de 90/100.

## RNF12 - Recuperación ante fallos (backup y restauración)
Ante una falla del sistema (caída de base de datos, corrupción de almacenamiento), la plataforma debe permitir restaurar la información de los usuarios (viajes, presupuestos, reservas) con una pérdida máxima de datos de 1 hora y un tiempo de recuperación del servicio no mayor a 4 horas, mediante respaldos automáticos periódicos.
