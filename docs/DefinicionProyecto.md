## 1. Definir el objetivo del producto

Nuestro proyecto, **WiseTrip**, es una plataforma web que ayudará a centralizar la mayoría de la planificación de un viaje. Su idea principal es poder recomendar diferentes destinos y sus respectivos planes de acuerdo al presupuesto con el que el usuario cuente. El usuario contará con recomendación de destinos, reservas, itinerarios, además de información del clima de la ciudad destino.

El objetivo es poder simplificar y personalizar el proceso de planeación de viajes, generando itinerarios con respecto al presupuesto y a las preferencias de cada usuario (aventura, cultura, gastronomía, entre otras), de manera que se elimine la incertidumbre sobre si el dinero disponible alcanzará para las actividades o la idea de nunca contar con "suficiente" dinero para viajar.

Con esto se espera que el viajero pueda tener todo el control sobre su economía antes, durante y después del viaje sin imprevistos y que deje de abstenerse a darse el lujo de conocer otros países latinoamericanos.

## 2. Definir los usuarios objetivo

WiseTrip está especialmente pensada para tres tipos de viajeros entre los 18 y 40 años (aunque su interfaz es amigable para que cualquier persona de cualquier edad pueda utilizarla). Dentro de este grupo se encuentran los siguientes perfiles:

- **Estudiantes y jóvenes con presupuesto limitado:** muchas veces se limitan a viajar por pensar que el presupuesto que tienen no les alcanzará para viajar, lo cual puede ser completamente erróneo y se perderían de increíbles experiencias.
- **Viajeros frecuentes o "planificadores autónomos":** aquellos que prefieren planificar sus propios itinerarios sin ayuda de agencias de viajes y que actualmente requieren del uso de varias aplicaciones para gestionar la información de sus actividades.
- **Turistas que buscan experiencias personalizadas:** usuarios que tienen en mente viajar con un interés en particular, ya sea vivir aventuras desafiantes, conocer la cultura del país destino o poder probar cada plato típico entre muchas más opciones. Estos viajeros aprecian recibir recomendaciones ajustadas a sus gustos en lugar de itinerarios genéricos o improvisados.

En común, estos usuarios valoran el control sobre su presupuesto, la organización centralizada de su viaje y el ahorro de tiempo frente al uso de múltiples plataformas dispersas.

## 3. Definir el problema

Como equipo percibimos un problema a la hora de planificar un viaje y fue que esto implica recurrir a múltiples plataformas; una para hospedaje, otra para el clima, otra para calcular los gastos, y diversas para planificar el itinerario del día a día en el viaje, lo cual lo hace demasiado dispendioso y propenso a errores.

Detallamos ciertos errores a la hora de viajar que fueron los siguientes:

- Llegar al destino sin itinerario.
- No saber cómo moverse dentro del país.
- No tener claro si el presupuesto realmente alcanza para todo lo que se planea realizar.
- No tener conocimiento de destinos asequibles solo porque no son muy turísticos.

Estas dificultades evidencian la necesidad de una herramienta que integre presupuesto, reservas, clima e itinerario en un solo lugar, y que es precisamente lo que da origen al desarrollo de WiseTrip.

## 4. Definir la propuesta de valor

WiseTrip no es solo una página para planear viajes alrededor de Latinoamérica. La plataforma busca ofrecer al usuario una experiencia de planificación personalizada a partir de su presupuesto, las fechas del viaje y sus intereses.

A partir de esta información, WiseTrip permite identificar qué destinos, actividades y opciones se ajustan mejor a las posibilidades del usuario. De esta manera, facilita la toma de decisiones, reduce el tiempo dedicado a consultar diferentes plataformas y ayuda al viajero a mantener un mayor control sobre su presupuesto antes y durante el viaje.

## 5. Identificar las funcionalidades principales

Como equipo, se decidió reducir algunas de las funcionalidades planteadas inicialmente debido al tiempo disponible, la dificultad técnica y la capacidad del grupo para desarrollar objetivos con un alto nivel de complejidad. Por esta razón, se seleccionaron las funcionalidades consideradas más pertinentes para el alcance actual de WiseTrip.

### 5.1. Generación de un itinerario personalizado

Una vez el usuario haya definido su presupuesto y seleccionado uno de los destinos de Latinoamérica recomendados por WiseTrip, la plataforma generará un itinerario teniendo en cuenta las fechas del viaje, el presupuesto disponible para actividades y los intereses seleccionados por el usuario.

El objetivo es mantener una planificación organizada y clara de las actividades que se realizarán durante el viaje.

### 5.2. Consulta e integración de opciones de alojamiento

La plataforma permitirá al usuario consultar diferentes tipos de alojamiento disponibles en la zona del destino, como hoteles, hostales y otras opciones de hospedaje.

Esta funcionalidad se encuentra en revisión, ya que requiere un análisis más detallado sobre las fuentes de información y los servicios externos que se utilizarán para mostrar estas opciones. Por esta razón, se mantiene temporalmente en pausa.

### 5.3. Registro de usuarios

WiseTrip permitirá que los usuarios creen una cuenta para acceder a las funcionalidades de la plataforma y mantener asociada a su perfil la información relacionada con sus viajes, preferencias, presupuestos e itinerarios.

### 5.4. Control del presupuesto disponible

El control del presupuesto constituye una funcionalidad fundamental dentro de WiseTrip.

Cuando el usuario ingresa el presupuesto disponible para su viaje, la plataforma presenta una serie de preguntas relacionadas con sus preferencias e intereses. Esta información permite filtrar y recomendar diferentes destinos que se ajusten tanto a sus posibilidades económicas como a sus gustos.

El usuario podrá modificar su presupuesto en diferentes situaciones, por ejemplo:

1. Si considera que el presupuesto inicial es insuficiente y desea aumentar la cantidad de dinero disponible.
2. Si encuentra una opción de viaje más económica y desea reducir el presupuesto.
3. Si desea agregar más actividades, noches de hospedaje u otras opciones.
4. Si cambia la cantidad de dinero que realmente tiene disponible.
5. Si modifica las fechas del viaje y esto genera cambios en los costos.

Cuando se produzca alguno de estos cambios, la plataforma deberá recalcular los resultados de acuerdo con las nuevas condiciones.

Si el usuario modifica únicamente el presupuesto y desea mantener el mismo destino, WiseTrip actualizará las recomendaciones según el nuevo valor disponible. Si desea explorar nuevos destinos manteniendo sus intereses, podrá realizar una nueva búsqueda con el presupuesto actualizado.

Además, durante el viaje el usuario podrá registrar los gastos que vaya realizando, como restaurantes, alojamiento u otros gastos adicionales. El sistema actualizará el dinero utilizado y el presupuesto restante, permitiendo mantener un mayor control financiero durante el viaje.

### 5.5. Consulta de información climática

WiseTrip permitirá al usuario consultar las condiciones climáticas del destino antes y durante su viaje, mostrando información como la temperatura y el estado general del clima.

Entre tres y cinco días antes del inicio del viaje, la plataforma podrá realizar seguimiento a posibles cambios importantes en las condiciones climáticas. Si se presenta alguna variación relevante, como lluvias fuertes o temperaturas extremas, WiseTrip podrá enviar una alerta al usuario mediante el canal de comunicación seleccionado.

Estas alertas podrán mantenerse activas durante el desarrollo del viaje.

Además, si las condiciones climáticas pueden afectar alguna actividad incluida en el itinerario, la plataforma podrá informar al usuario y ofrecer recomendaciones alternativas que se ajusten a sus intereses y presupuesto.

Por ejemplo, si se pronostica lluvia y el usuario tiene programada una actividad al aire libre, WiseTrip podrá sugerir actividades bajo techo.

### 5.6. Generación de alertas

WiseTrip podrá generar alertas relacionadas con cambios importantes en el clima, variaciones en el presupuesto, gastos realizados o nuevas recomendaciones.

Estas alertas podrán enviarse mediante correo electrónico o Telegram, de acuerdo con el canal seleccionado previamente por el usuario.

El objetivo es permitir que el viajero reciba información relevante antes y durante su viaje sin necesidad de ingresar constantemente a la plataforma.

### 5.7. Recomendaciones según los intereses del usuario

Después de que el usuario ingrese su presupuesto, la plataforma solicitará información sobre sus gustos e intereses personales.

Entre las categorías consideradas se encuentran:

- Aventura.
- Cultura.
- Gastronomía.
- Naturaleza.
- Vida nocturna.
- Actividades familiares.
- Preferencias de temperatura.
- Compras.
- Actividades gratuitas o de bajo costo.

Estas preferencias serán utilizadas como criterios para filtrar y recomendar destinos, lugares, actividades y planes que presenten mayor afinidad con el usuario.

### 5.8. Soporte al usuario mediante chat o correo electrónico

WiseTrip permitirá que los usuarios reciban asistencia antes, durante y después de su viaje para resolver dudas relacionadas con el uso de la plataforma o con la planificación de su viaje.

El usuario podrá comunicarse con el soporte mediante chat o correo electrónico, dependiendo del tipo de consulta o de su preferencia.

Además, al finalizar el viaje, la plataforma podrá solicitar retroalimentación sobre la experiencia, permitiendo que el usuario indique si el itinerario y las recomendaciones fueron útiles, qué aspectos fueron satisfactorios y qué elementos podrían mejorarse.

Esta información podrá utilizarse para identificar oportunidades de mejora y desarrollar recomendaciones cada vez más ajustadas a las necesidades de los viajeros.


## 6. Identidad visual de WiseTrip
La entidad visual es el conjunto de elementos graficos que permiten reconocer y diferenciar a una marca. Dentro de estos elementos se encuentran el color, la tipografia, las formas, simbolos y estilo general. En el caso de nuestra marca buscamos representar la pagina web relacionada con la planificacion inteligente de viajes. La marca debe transmitir conceptos de confianza, exploracion, tecnologia y personalizacion. Ya que nuestra plataforma recomendara destinos de acuerdo a las preferencias, fechas y presupuesto de cada usuario.

Por esta razon la identidad visual de WiseTrip se planteara con un estilo moderno, evitando representar al turismo tradicionalmente. En lugar de basarnos en los tipicos simbolos como aviones o maletas, buscaremos representar la experienicia de la toma de decisiones y encontrar un viaje adecuado. 

Como parte fundamental del branding, decidimos usar una paleta de colores como azul profundo, turquesa, coral y colores arena. Creando una identidad entre tecnologia y viajes. 

- tinta: #0E3A53;
- tinta-honda: #0A2A3E;
- turquesa: #1BA3A0;
- turquesa-hondo: #128784;
- turquesa-suave: #E4F5F1;
- coral: #E8623D;
- coral-hondo: #A83A1C;
- arena: #F6F1E6;
- arena-honda: #EDE4D3;
- linea: #D8CFBC;
- gris: #5B6B75;
- gris-claro: #93A3AC;

## Tipografia 

La tipografia no se considera como un elemento decorativo. Dentro del logo e interfaces graficas, influyen en la legibilidad, personalidad y experiencia del usuario. Por ello en WiseTrip usaremos tres tipografias principales: 

- Fraunces
- Instrument Sans
- IBM Plex Mono, para etiquetas

Estos tres tipos de tipografias se usaron para diferentes aspectos; La tipografia de Fraunces es usada principalmente en titulos y elementos destacados. Instrument Sans para el contenido general y la interfaz. Y para finalizar IBM Plex Mono Para datos, etiquetas y elementos estructurados. 

## Proceso de creacion logo WiseTrip 

El objetivo fue buscar una identidad que pueda representar la idea de viajar de forma personalizada e inteligente. Entre las diferentes posibilidades encontramos: 

- Iniciales de la marca
- Simbolos
- Referencias a rutas y trayectorias
- Elementos relacionados con exploracion
- Formas asociadas con la direccion
- Representaciones de viaje

Para ello se propusieron los siguientes diseños 

1. Logo 1 
<img width="344" height="342" alt="image" src="https://github.com/user-attachments/assets/98742a4e-156e-4191-b414-23f5ebe6ad5f" />

2. Logo 2 
<img width="342" height="343" alt="image" src="https://github.com/user-attachments/assets/53f66d12-7fbf-4568-b47d-fbe4718f90a1" />

3. Logo 3
<img width="341" height="344" alt="image" src="https://github.com/user-attachments/assets/56c449fc-5876-45ea-b943-d6fad93a0931" />

4. Logo 4
<img width="341" height="340" alt="image" src="https://github.com/user-attachments/assets/46b7925d-0e6f-4b81-bc5c-c28ef18ed308" />

## Analisis Logo

Para realizar la elección del logo WiseTrip nos basamos en 5 criterios principales

1. Simplicidad.
2. Memorabilidad.
3. Versatilidad.
4. Escalabilidad.
4. Relación con WiseTrip.

| Logo       | Criterio                 | Valeria | Isabella | María Alejandra | Santiago | Gabriela |
| ---------- | ------------------------ | ------: | -------: | --------------: | -------: | -------: |
| **Logo 1** | Simplicidad              |       3 |        4 |               3 |        3 |        3 |
|            | Memorabilidad            |       4 |        4 |               4 |        3 |        4 |
|            | Versatilidad             |       3 |        3 |               3 |        4 |        3 |
|            | Escalabilidad            |       4 |        3 |               3 |        3 |        3 |
|            | Pertinencia con la marca |       5 |        4 |               4 |        4 |        5 |
| **Logo 2** | Simplicidad              |       4 |        4 |               2 |        2 |        3 |
|            | Memorabilidad            |       4 |        3 |               4 |        3 |        3 |
|            | Versatilidad             |       4 |        3 |               4 |        2 |        1 |
|            | Escalabilidad            |       3 |        3 |               3 |        2 |        2 |
|            | Pertinencia con la marca |       1 |        1 |               1 |        1 |        2 |
| **Logo 3** | Simplicidad              |       4 |        4 |               4 |        3 |        3 |
|            | Memorabilidad            |       4 |        3 |               3 |        3 |        3 |
|            | Versatilidad             |       2 |        3 |               3 |        2 |        2 |
|            | Escalabilidad            |       2 |        4 |               3 |        4 |        2 |
|            | Pertinencia con la marca |       4 |        3 |               4 |        4 |        3 |
| **Logo 4** | Simplicidad              |       5 |        3 |               4 |        3 |        4 |
|            | Memorabilidad            |       3 |        3 |               3 |        4 |        4 |
|            | Versatilidad             |       4 |        4 |               4 |        4 |        3 |
|            | Escalabilidad            |       4 |        4 |               4 |        3 |        3 |
|            | Pertinencia con la marca |       5 |        5 |               5 |        5 |        5 |

## Tabla Final de Eleccion 

| Logo       | Valeria | Isabella | María Alejandra | Santiago | Gabriela | **Total** |
| ---------- | ------: | -------: | --------------: | -------: | -------: | --------: |
| **Logo 1** |      19 |       18 |              17 |       17 |       18 |    **89** |
| **Logo 2** |      16 |       14 |              14 |       10 |       11 |    **65** |
| **Logo 3** |      16 |       17 |              17 |       16 |       13 |    **79** |
| **Logo 4** |      21 |       19 |              20 |       19 |       19 |    **98** |