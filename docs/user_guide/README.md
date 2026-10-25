# Manual de usuario – WiseTrip
 
*Planificador de viajes por Latinoamérica con control de presupuesto · Proyecto FIS_2630_1204_G3*
 
## 1. ¿Qué es WiseTrip?
 
WiseTrip es una aplicación web que te ayuda a planear un viaje por Latinoamérica sin gastar de más. Tú le cuentas de dónde sales, qué te gusta, cuándo viajas y cuánto dinero tienes; WiseTrip te recomienda **tres destinos** que encajan con tus preferencias y que sí puedes pagar, y después te ayuda a **repartir tu presupuesto** entre los gastos del viaje.
 
Datos clave de la aplicación:
 
- Cubre **20 países** de Latinoamérica.
- Acepta **19 monedas** (USD, COP, MXN, PEN, ARS, CLP, BRL y más; ver sección 9).
- Tu perfil de viajero se construye con **41 preguntas**.
- Te entrega **3 destinos** a tu medida.

## 2. Antes de empezar
 
### 2.1 Qué necesitas
 
- Un navegador web actualizado (Chrome, Edge, Firefox o Safari).
- Un correo electrónico válido.
- Tener 18 años o más (es requisito para crear la cuenta).
- Un documento de identidad (cédula de ciudadanía, cédula de extranjería, tarjeta de identidad o pasaporte).
### 2.2 Cómo abrir la aplicación
 
La aplicación se ejecuta en el equipo donde está instalada y se abre desde el navegador en la dirección **http://localhost:8090** (si tu grupo usa otro puerto, la dirección cambia en consecuencia).
 
Para quien instala el proyecto (requiere JDK 21 o superior; no hace falta instalar Maven ni Tomcat):
 
- Windows: `mvnw.cmd spring-boot:run`
- Linux / macOS: `./mvnw spring-boot:run`
- Alternativa con el archivo empaquetado: `java -jar wisetrip-0.0.1-SNAPSHOT.war`
