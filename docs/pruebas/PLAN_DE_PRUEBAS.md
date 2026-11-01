# Plan de pruebas unitarias - WiseTrip

Servicios probados: GastosServicio, PresupuestoServicio, FechasServicio. Columna *Resultado real*: se completa con el informe del pipeline.

| Caso | Servicio | Método | Descripción | Entrada | Tipo | Resultado esperado | Resultado real | Justificación |
|---|---|---|---|---|---|---|---|---|
| CP01 | GastosServicio | `validarGasto` | Gasto válido dentro del cupo | viaje 1; hospedaje $100.000 COP; presupuesto $1.000.000; cupo hospedaje 35% | Normal | Mapa de errores vacío |  | Camino feliz: confirma que un gasto correcto no se bloquea |
| CP02 | GastosServicio | `validarGasto` | idViaje no positivo | idViaje = 0 | Negativa | Error 'idViaje' y no consulta repositorios |  | Evita consultas con ids inválidos |
| CP03 | GastosServicio | `validarGasto` | Gasto nulo | gasto = null | Negativa | Error 'gasto' |  | Protege contra NullPointerException desde la vista |
| CP04 | GastosServicio | `validarGasto` | Moneda distinta a la del viaje | gasto en USD, viaje en COP | Negativa | Error 'moneda'; no se evalúa el cupo |  | Evita mezclar monedas en el presupuesto |
| CP05 | GastosServicio | `validarGasto` | Monto igual al cupo exacto | hospedaje $350.000 (cupo = 35% de $1.000.000) | Borde | Sin errores |  | Límite superior permitido: el cupo se puede agotar |
| CP06 | GastosServicio | `validarGasto` | Un centavo sobre el cupo | hospedaje $350.000,01 | Borde | Error 'cupoCategoria' |  | Primer valor que debe bloquearse (control estricto) |
| CP07 | GastosServicio | `validarGasto` | Monto cero | transporte $0 | Borde | Error 'monto' |  | El monto debe ser mayor que cero |
| CP08 | GastosServicio | `advertenciasGasto` | Imprevistos supera el cupo | cupo $50.000; gastado $40.000; nuevo gasto $20.000 | Negocio | validarGasto sin errores y advertencia 'cupoCategoria' |  | Regla de negocio: imprevistos es un colchón (control flexible, solo advierte) |
| CP09 | GastosServicio | `advertenciasGasto` | Gasto inválido no genera advertencias | descripción en blanco | Negativa | Mapa vacío |  | No se advierte sobre algo que ya es un error |
| CP10 | GastosServicio | `registrarGasto` | Registro de un gasto válido | alimentación $50.000 | Normal | Se guarda y devuelve el gasto con id 55 |  | Camino feliz de persistencia (repositorio simulado) |
| CP11 | GastosServicio | `registrarGasto` | Registro de gasto inválido | categoría inexistente | Excepción | IllegalArgumentException y nunca se llama a guardar |  | Garantiza que no se persisten datos inválidos |
| CP12 | GastosServicio | `consultarGastos` | Lista de solo lectura | 1 gasto en el repositorio | Normal | Lista de 1 elemento; add() lanza UnsupportedOperationException |  | Protege el estado interno del repositorio |
| CP13 | GastosServicio | `consultarResumen` | Cálculo de saldo | total $1.000.000; gastado $250.000 | Normal | saldo = $750.000 en COP |  | Cálculo central del resumen financiero |
| CP14 | GastosServicio | `consultarResumen` | Se gasta todo el presupuesto | gastado = total | Borde | saldo = 0 |  | Límite entre saldo positivo y negativo |
| CP15 | GastosServicio | `consultarResumen` | Total gastado negativo | gastado = -1 | Excepción | IllegalStateException |  | Detecta datos corruptos del repositorio |
| CP16 | GastosServicio | `consultarResumen` | Presupuesto de otro viaje | repositorio devuelve idViaje 99 | Excepción | IllegalStateException |  | Evita mostrar datos de otro viaje |
| CP17 | PresupuestoServicio | `validarPresupuesto` | Monto y moneda válidos | '1.500.000', COP | Normal | Sin errores |  | Camino feliz con formato colombiano |
| CP18 | PresupuestoServicio | `validarPresupuesto` | Monto vacío | '' | Negativa | Error 'monto' |  | Campo obligatorio |
| CP19 | PresupuestoServicio | `validarPresupuesto` | Monto con letras | 'abc' | Negativa | Error 'monto' |  | Entrada no numérica del formulario |
| CP20 | PresupuestoServicio | `validarPresupuesto` | Monto negativo | '-5' | Negativa | Error 'monto' |  | No existen presupuestos negativos |
| CP21 | PresupuestoServicio | `validarPresupuesto` | Monto cero | '0' | Borde | Error 'mayor que cero' |  | Límite inferior |
| CP22 | PresupuestoServicio | `validarPresupuesto` | Monto máximo permitido | '1.000.000.000' | Borde | Sin errores |  | Último valor aceptado |
| CP23 | PresupuestoServicio | `validarPresupuesto` | Monto sobre el máximo | '1.000.000.001' | Borde | Error 'realista' |  | Primer valor rechazado |
| CP24 | PresupuestoServicio | `validarPresupuesto` | Moneda inexistente | '100', 'XXX' | Negativa | Error 'moneda' |  | Solo se aceptan las 19 monedas de la lista |
| CP25 | PresupuestoServicio | `validarPresupuesto` | Moneda nula | '100', null | Negativa | Error 'moneda' |  | Campo obligatorio |
| CP26 | PresupuestoServicio | `convertirAUsd` | Conversión COP a USD | '4.000.000' COP (tasa 4000) | Normal | 1000.0 USD |  | Base de la comparación del recomendador |
| CP27 | PresupuestoServicio | `convertirAUsd` | Moneda desconocida | '100' XXX | Negativa | 0.0 |  | Evita división por tasa inexistente |
| CP28 | PresupuestoServicio | `convertirAUsd` | Monto inválido | 'abc' COP | Negativa | 0.0 |  | No propaga valores inválidos |
| CP29 | PresupuestoServicio | `convertirAUsd` | Redondeo a 2 decimales | '100' COP | Borde | 0.03 USD |  | Verifica el redondeo (100/4000 = 0.025) |
| CP30 | PresupuestoServicio | `monedasDisponibles` | Lista de monedas | país 'Perú' y país null | Normal | 19 monedas, incluye COP y USD, igual para cualquier país |  | Contrato del selector de moneda |
| CP31 | PresupuestoServicio | `nombreMoneda` | Nombre de moneda | 'COP' y 'ZZZ' | Borde | 'Peso colombiano (COP)' y 'ZZZ' |  | Un código desconocido no debe romper la vista |
| CP32 | PresupuestoServicio | `formatear` | Formato de números | 1234567.891 y 0 | Borde | '1,234,567.89' y '0.00' |  | Presentación consistente del dinero |
| CP33 | PresupuestoServicio | `getMontoNumerico (usado por el servicio)` | Formato numérico colombiano | '1.500,50' y '1.5' | Negocio | 1500.5 y 15.0 |  | Regla particular: el punto es separador de miles; documenta que '1.5' vale 15 |
| CP34 | FechasServicio | `validarFechas` | Rango futuro válido | inicio hoy+1, fin hoy+5 | Normal | Sin errores |  | Camino feliz |
| CP35 | FechasServicio | `validarFechas` | Sin fechas | inicio y fin vacíos | Negativa | Errores 'fechaInicio' y 'fechaFin' |  | Campos obligatorios en la cadena de reglas |
| CP36 | FechasServicio | `validarFechas` | Inicio en el pasado | inicio ayer | Negativa | Error 'fechaInicio' |  | No se planean viajes ya iniciados |
| CP37 | FechasServicio | `validarFechas` | Formato inválido | inicio '2026-13-45' | Negativa | Error 'fechaInicio' |  | Entrada manipulada o mal escrita |
| CP38 | FechasServicio | `validarFechas` | Inicio hoy | inicio hoy, fin hoy+1 | Borde | Sin errores |  | Límite de la regla 'no anterior a hoy' |
| CP39 | FechasServicio | `validarFechas` | Fin igual al inicio | inicio = fin = hoy+1 | Borde | Error 'fechaFin' (debe ser posterior) |  | Límite de la regla de orden |
| CP40 | FechasServicio | `validarFechas` | Viaje de 60 días | inicio hoy, fin hoy+59 | Borde | Sin errores |  | Máximo permitido |
| CP41 | FechasServicio | `validarFechas` | Viaje de 61 días | inicio hoy, fin hoy+60 | Borde | Error 'fechaFin' (máximo 60 días) |  | Primer valor rechazado |
| CP42 | FechasServicio | `validarFechas (con usuario)` | Cruce con otro viaje | repositorio indica cruce | Negocio | Error 'fechaFin' (se cruzan) |  | Regla de negocio: un usuario no puede tener viajes solapados |
| CP43 | FechasServicio | `validarFechas (con usuario)` | Sin cruce | repositorio indica que no hay cruce | Normal | Sin errores y se consulta el repositorio |  | Verifica la interacción con el repositorio |
| CP44 | FechasServicio | `calcularDuracion` | Duración de un viaje | 1 al 5 de diciembre de 2026 | Normal | 5 días |  | Cuenta inicio y fin |
| CP45 | FechasServicio | `calcularDuracion` | Sin fechas | FechasViaje vacío | Negativa | 0 |  | No lanza excepción con datos vacíos |
| CP46 | FechasServicio | `calcularDuracion` | Inicio igual al fin | 1 de diciembre = 1 de diciembre | Borde | 0 (hallazgo: debería ser 1) |  | Expone una inconsistencia con validarFechas |
| CP47 | FechasServicio | `hoy` | Fecha actual | ninguna | Normal | Fecha actual en formato yyyy-MM-dd |  | Se usa en el atributo min del formulario |