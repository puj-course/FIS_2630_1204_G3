# Arquitectura del Sistema WiseTrip

Como podemos observar en el gráfico de arquitectura, el sistema se divide en **tres módulos principales**:

1. **Frontend (Cliente)**
2. **Backend (Servidores / API REST)**
3. **Servicios externos (APIs de terceros)**

Esta arquitectura permite separar las responsabilidades de cada componente y facilita el desarrollo, mantenimiento y escalabilidad de la plataforma.

---

## 1. Frontend (Cliente)

Para el frontend se propone el desarrollo de una página web o interfaz de usuario utilizando tecnologías como:

- HTML
- CSS
- JavaScript

Este módulo será el encargado de mostrar al usuario la información de una manera clara, comprensible y visualmente organizada. Además, será responsable de la interacción directa entre el usuario y la plataforma.

Dentro de las funcionalidades principales del frontend se encuentran:

- Registro e inicio de sesión de usuarios.
- Consulta de destinos.
- Selección de preferencias de viaje.
- Generación de viajes personalizados.
- Visualización de itinerarios.
- Visualización de mapas y trayectos.
- Visualización de alertas e información relevante para el viaje.
- Gestión de información relacionada con los viajes del usuario.

La generación de viajes será una de las funcionalidades principales de la plataforma. El usuario podrá ingresar sus preferencias y, a partir de ellas, el sistema generará un itinerario personalizado.

---

# API, REST y API REST

Para comprender la comunicación entre los diferentes módulos del sistema, es importante conocer los conceptos de **API**, **REST** y **API REST**.

## API

Una **API (Application Programming Interface)** es un conjunto de protocolos y reglas utilizados para desarrollar e integrar sistemas de software.

Una API permite que diferentes aplicaciones o sistemas interactúen entre sí para solicitar, enviar o recibir información. Puede entenderse como un contrato que establece la forma en que un sistema puede comunicarse con otro y acceder a determinados servicios o datos.

> Una API permite la interacción entre diferentes sistemas para intercambiar información. [1]

---

## REST

**REST (Representational State Transfer)** es un conjunto de principios y lineamientos relacionados con la arquitectura de sistemas distribuidos.

REST define una forma de organizar la comunicación entre aplicaciones, principalmente mediante servicios web, permitiendo que diferentes sistemas se comuniquen de manera independiente y estructurada. [1]

---

## API REST

Una **API REST** es una interfaz que utiliza los principios de REST para permitir la comunicación entre diferentes componentes de un sistema a través de internet.

En el caso de WiseTrip, la API REST permitirá la comunicación entre el **frontend y el backend** mediante el protocolo **HTTP**.

El funcionamiento general será el siguiente:

1. El frontend envía una solicitud al backend.
2. La API REST recibe y gestiona la solicitud.
3. El backend procesa la información.
4. El backend consulta o almacena información en la base de datos.
5. Si es necesario, el backend consulta servicios externos.
6. Finalmente, el backend devuelve una respuesta al frontend.

El uso de una API REST permite que el frontend y el backend puedan desarrollarse y mantenerse de manera independiente, aunque permanezcan conectados mediante solicitudes HTTP.

Además, la API debe garantizar que la información enviada desde el frontend sea correctamente procesada y almacenada en la base de datos cuando sea necesario. [2]

---

# 2. Backend (Servidores / API REST)

Para el backend se propone el desarrollo de una **API REST**, la cual será la encargada de gestionar la lógica de negocio de la plataforma.

Dentro de sus principales responsabilidades se encuentran:

- Gestión de usuarios.
- Autenticación y autorización.
- Manejo de la información almacenada en la base de datos.
- Gestión de viajes.
- Generación de itinerarios personalizados.
- Gestión de preferencias de viaje.
- Control de presupuestos y gastos.
- Comunicación con servicios externos.
- Procesamiento de solicitudes realizadas desde el frontend.

El backend tendrá una conexión directa con la **base de datos** y con las **APIs de servicios externos**.

Por esta razón, el backend funcionará como el componente central de procesamiento y gestión de la información dentro de la arquitectura de WiseTrip.

---

# 3. Servicios Externos (APIs de Terceros)

El tercer módulo corresponde a la integración de servicios externos mediante APIs de terceros.

Estas integraciones permitirán complementar la información y funcionalidades ofrecidas por la plataforma.

Los principales servicios considerados son:

## API del Clima

La API del clima permitirá obtener información actualizada sobre las condiciones climáticas de los destinos seleccionados por el usuario.

Esta información podrá utilizarse para:

- Informar al usuario sobre las condiciones climáticas.
- Generar alertas relacionadas con el clima.
- Ayudar al usuario en la planificación de actividades.

---

## API de Mapas

La API de mapas permitirá visualizar:

- Destinos.
- Ubicaciones.
- Trayectos.
- Rutas relacionadas con el viaje.

Esto facilitará la representación visual del itinerario generado por la plataforma.

---

## API de Mensajería

La API de mensajería permitirá enviar información relacionada con las actividades realizadas dentro de la plataforma.

Por ejemplo:

- Confirmaciones.
- Notificaciones.
- Alertas.
- Información relacionada con itinerarios.
- Actualizaciones sobre actividades o reservas.

---

## Integración Simulada de Servicios Turísticos

La integración con servicios de:

- Vuelos.
- Hospedajes.
- Actividades.

será realizada de manera simulada.

La integración directa con plataformas comerciales reales, como servicios de reservas o agencias de viaje, requeriría convenios, permisos y acuerdos comerciales con dichas plataformas.

Por esta razón, para el alcance actual del proyecto, estas funcionalidades serán simuladas dentro de la plataforma.

---

# Flujo General del Sistema

El flujo principal de la arquitectura será el siguiente:

```text
Usuario
   │
   ▼
Frontend (Cliente)
   │
   │ Solicitudes HTTP
   ▼
Backend / API REST
   │
   ├──────────────► Base de Datos
   │
   └──────────────► APIs de Servicios Externos
                         │
                         ▼
                  Información Procesada
                         │
                         ▼
                    Backend / API
                         │
                         ▼
                      Frontend
                         │
                         ▼
                       Usuario
