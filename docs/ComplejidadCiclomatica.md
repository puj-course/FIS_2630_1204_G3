## Complejidad ciclomática de recomendaciones

## Medir

Medimos manualmente las decisiones de **11 métodos en tres clases** de WiseTrip. Esto permite identificar qué métodos tienen más caminos que revisar y probar.

- `RecomendadorDestinos`: valida, puntúa y ordena ciudades.
- `RecomendacionControlador`: recibe la solicitud y coordina las recomendaciones.
- `LlenarAtributosCiudad`: completa los atributos necesarios para evaluar ciudades.

## Contar

Cada método empieza en **1**. Sumamos **1** por cada:

- `if` o `else if`.
- Bucle, como `for` o `while`.
- Operador `&&` o `||`.

## Resultados actuales

En los cálculos, la cantidad de `if` incluye los `else if`.

### RecomendadorDestinos

| Método | Cálculo | Complejidad |
|---|---|---:|
| `calcularPuntajePresupuesto` | 1 base + 2 if | 3 |
| `calcularCoincidenciaPonderada` | 1 base + 2 for + 4 if + 2 AND | 9 |
| `calcularPuntajeTotal` | 1 base + 1 for + 3 if | 5 |
| `diferenciaPresupuesto` | 1 base, sin decisiones | 1 |
| `excedePresupuesto` | 1 base + 3 if | 4 |
| `validarCiudades` | 1 base + 2 for + 8 if | 11 |
| `puntuarCiudadesValidas` | 1 base + 1 for + 1 if + 1 OR | 4 |
| `todosMismoPuntaje` | 1 base + 1 for + 2 if | 4 |

AND corresponde a `&&` y OR a `||`.

### RecomendacionControlador

| Método | Cálculo | Complejidad |
|---|---|---:|
| `mostrarRecomendaciones` | 1 base + 7 if + 1 for + 1 OR | 10 |

### LlenarAtributosCiudad

| Método | Cálculo | Complejidad |
|---|---|---:|
| `enriquecer` | 1 base + 5 if + 1 for | 7 |
| `incumpleIndispensableManual` | 1 base + 1 for + 1 if + 3 AND | 6 |

Los tres constructores tienen complejidad **1** cada uno porque solo asignan dependencias. Se registran aparte y no se incluyen en los promedios.

## Resumen

| Clase | Métodos | Promedio | Máximo |
|---|---:|---:|---:|
| RecomendadorDestinos | 8 | 5,125 | 11 |
| RecomendacionControlador | 1 | 10 | 10 |
| LlenarAtributosCiudad | 2 | 6,5 | 7 |
 

Los métodos que conviene revisar primero son `validarCiudades` (11), `mostrarRecomendaciones` (10) y `calcularCoincidenciaPonderada` (9), porque concentran más decisiones. Esto no significa que tengan errores.

## ¿Qué cambió?

*En `RecomendadorDestinos` se separaron cálculos largos, se usaron nombres más claros, se eliminó un else innecesario y se explicó el ordenamiento. También se cambió `anyMatch` porque no lo entendia y El conteo pasó de **3 a 5**..
*En `RecomendacionControlador` solo se añadieron comentarios con la medición; no se refactorizó su lógica.
*En `LlenarAtributosCiudad` se aclararon el nombre del proveedor y el radio de búsqueda. Además, la versión final consulta las preferencias directamente en el mapa y usa condiciones explícitas para guardar los estados de los atributos.


`validarCiudades` dio **8** en el primer conteo. Cuando empezamos esta
serie de cambios ya daba **11**, y terminó en **11**.

El objetivo de estos cambios fue hacer el código más fácil de seguir.
En los métodos comparados, el conteo se mantuvo o subió.

## ¿Cómo lo verificamos?

Después de los cambios en `RecomendadorDestinos`, pasaron sus **20 pruebas
ya hechas**. 

ademas se pued everificar en el sonarqube 

`RecomendacionControlador.java` 
<img width="1842" height="1017" alt="image" src="https://github.com/user-attachments/assets/4eac88c3-f851-4a1d-baef-797132294a7e" />

`RecomendadorDestinos.java`
<img width="1089" height="592" alt="image" src="https://github.com/user-attachments/assets/be26e3cf-4dd4-407a-8e43-4036d162ad40" />

como este se analiza en el análisis de `main`, el código que
muestra Sonar todavía usa `anyMatch()` en `calcularPuntajeTotal`,
mientras que la versión del conteo final usa un `for`.

El conteo manual final suma 42 para el archivo, incluidos los ocho
métodos y el constructor. Como los valores corresponden a versiones
distintas, los registramos como referencia. Para compararlos
directamente, queda pendiente ejecutar Sonar sobre la misma versión
que contamos a mano.

<img width="928" height="319" alt="image" src="https://github.com/user-attachments/assets/32a49072-b590-49c8-aeea-8a545f5a4726" />

`LlenarAtributosCiudad.java`
<img width="1482" height="726" alt="image" src="https://github.com/user-attachments/assets/134e1846-888a-4422-ab3f-d5d15162f8ec" />

SonarQube reporta 15 en `main`.
El conteo manual de la versión final suma 14 (7 de `enriquecer`, 6 de
`incumpleIndispensableManual` y 1 del constructor).

En Sonar todavía aparecen los imports de `Set` y `Collectors`, que se
usaban para armar el conjunto de atributos activos. En la versión final,
`enriquecer` consulta cada atributo directamente en el mapa. Para saber
si la diferencia de un punto se debe a la versión del código o al
criterio de conteo, queda pendiente medir la misma versión con ambos
métodos.




