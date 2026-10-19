# Diagrama de interfaces — Control de presupuesto y gastos (M4)

Este diagrama muestra las interfaces de negocio del módulo de Control de Presupuesto y Gastos (M4), qué clase implementa cada una y quién depende de ellas. Una interfaz define qué se puede hacer (sus métodos), pero no cómo se hace; eso lo decide la clase que la implementa. Los controladores y servicios dependen de estos contratos y no de clases concretas, siguiendo el principio de inversión de dependencias: si el equipo cambia cómo se valida un presupuesto o cómo se controla un gasto, quien usa la interfaz no se entera ni se rompe, mientras el contrato se mantenga.

`GestorPresupuesto` la implementan dos clases: `PresupuestoServicio` y `PresupuestoConMargenDecorador`. Así funciona el patrón Decorator: `PlanificacionControlador` pide un `GestorPresupuesto` y no sabe si recibe el servicio original o el decorado, que suma un 5% de margen de imprevistos al convertir a USD. Para el reparto se aplicó el principio de segregación de interfaces. `IValidadorReparto` (validar) e `ICalculadoraReparto` (calcular montos) son contratos pequeños y separados, que `IGestionReparto` hereda y `RepartoServicio` implementa. `IDistribuible` la implementa el modelo `RepartoPresupuesto` y trae métodos `default`, así que la clase solo debe completar los tres métodos básicos.

Para los gastos, `IGestorGastos` la implementa `GastosServicio`, que depende de tres repositorios (`IRepositorioGastos`, `IRepositorioPresupuestoViaje` e `IRepositorioRepartoPresupuesto`). Estos repositorios todavía no tienen un DAO que los implemente; por eso `GastosServicio` aparece como «pendiente de conexión». Para controlar el cupo de cada categoría se usa el patrón Factory Method: la clase abstracta `ControlCategoriaCreador` (Creator), heredada por `CreadorControlEstricto` y `CreadorControlFlexible`, crea objetos de tipo `IControlCategoriaGasto` (Product), implementada por `ControlCategoriaEstricto` y `ControlCategoriaFlexible`.

Por último, `IRepositorioViajes` la implementa `ViajeDAO` y la usa `RecomendacionControlador` para guardar el viaje con su presupuesto en USD. El paquete Modelos agrupa las clases y records de datos que reciben o devuelven los contratos. `IRepartoServicio` se conserva marcada como «legado/no conectado», porque nadie la implementa ni la usa: es una versión vieja de `IGestionReparto`.

## Código PlantUML

```plantuml
@startuml
title Contratos de presupuesto y gastos
left to right direction
skinparam classAttributeIconSize 0

package "Negocio" {
    interface GestorPresupuesto {
        +validarPresupuesto(presupuesto: Presupuesto, paisDestino: String): Map<String, String>
        +convertirAUsd(presupuesto: Presupuesto): double
        +monedasDisponibles(paisDestino: String): Map<String, String>
        +nombreMoneda(codigo: String): String
        +formatear(valor: double): String
    }

    interface IGestorGastos {
        +validarGasto(idViaje: int, gasto: Gasto): Map<String, String>
        +advertenciasGasto(idViaje: int, gasto: Gasto): Map<String, String>
        +registrarGasto(idViaje: int, gasto: Gasto): Gasto
        +consultarGastos(idViaje: int): List<Gasto>
        +consultarResumen(idViaje: int): ResumenPresupuesto
    }

    interface IControlCategoriaGasto {
        +evaluar(gasto: Gasto, cupoCategoria: BigDecimal, totalGastadoCategoria: BigDecimal): ResultadoControlCategoria
    }

    abstract class ControlCategoriaCreador {
        #crearControl(): IControlCategoriaGasto
        +evaluarGasto(gasto: Gasto, cupoCategoria: BigDecimal, totalGastadoCategoria: BigDecimal): ResultadoControlCategoria
    }

    interface IRepositorioGastos {
        +guardar(idViaje: int, gasto: Gasto): Gasto
        +listarPorViaje(idViaje: int): List<Gasto>
        +totalGastadoPorViaje(idViaje: int): BigDecimal
        +totalGastadoPorCategoria(idViaje: int, categoria: String): BigDecimal
    }

    interface IRepositorioPresupuestoViaje {
        +buscarPresupuesto(idViaje: int): PresupuestoViaje
    }

    interface IRepositorioRepartoPresupuesto {
        +buscarReparto(idViaje: int): IDistribuible
    }

    interface IRepositorioViajes {
        +insertar(idUsuario: int, idCiudad: int, fechas: FechasViaje, presupuestoUsd: double): int
    }

    interface IDistribuible {
        +porcentajeDe(categoria: String): int
        +asignar(categoria: String, porcentaje: int): void
        +getTotal(): int
        +porcentajeDisponible(): int
        +porcentajeExcedido(): int
        +excedeElMaximo(): boolean
        +estaCompleto(): boolean
        +tienePorcentajesNegativos(): boolean
        +cubreLosBasicos(): boolean
        +montoDe(categoria: String, presupuestoTotal: double): double
        +distribuir(presupuestoTotal: double): Map<String, Double>
    }

    interface IValidadorReparto {
        +validarReparto(reparto: IDistribuible): Map<String, String>
    }

    interface ICalculadoraReparto {
        +calcularMontos(reparto: IDistribuible, presupuestoTotal: double): Map<String, Double>
        +porDia(monto: double, dias: long): double
    }

    interface IGestionReparto {
        +listarCategorias(): Map<String, String>
    }

    interface IRepartoServicio <<legado/no conectado>> {
        +listarCategorias(): Map<String, String>
        +validarReparto(reparto: RepartoPresupuesto): Map<String, String>
        +calcularMontos(reparto: RepartoPresupuesto, presupuestoTotal: double): Map<String, Double>
        +porDia(monto: double, dias: long): double
    }
}

package "Servicios" {
    class PresupuestoServicio
    class PresupuestoConMargenDecorador <<Decorator>>
    class RepartoServicio
    class GastosServicio <<pendiente de conexión>>
    class ControlCategoriaEstricto
    class ControlCategoriaFlexible
    class CreadorControlEstricto
    class CreadorControlFlexible
}

package "Controladores existentes" {
    class PlanificacionControlador
    class PlanControlador
    class RecomendacionControlador
}

package "Persistencia existente" {
    class ViajeDAO
}

package "Modelos" {
    class Presupuesto
    class Gasto <<record>>
    class PresupuestoViaje <<record>>
    class ResumenPresupuesto <<record>>
    class ResultadoControlCategoria <<record>>
    class RepartoPresupuesto
    class FechasViaje
}

' Cada servicio implementa su interfaz de negocio.
PresupuestoServicio ..|> GestorPresupuesto
PresupuestoConMargenDecorador ..|> GestorPresupuesto
RepartoServicio ..|> IGestionReparto
GastosServicio ..|> IGestorGastos
ControlCategoriaEstricto ..|> IControlCategoriaGasto
ControlCategoriaFlexible ..|> IControlCategoriaGasto
CreadorControlEstricto --|> ControlCategoriaCreador
CreadorControlFlexible --|> ControlCategoriaCreador
RepartoPresupuesto ..|> IDistribuible
ViajeDAO ..|> IRepositorioViajes

' IGestionReparto une dos contratos pequeños (segregación de interfaces).
IGestionReparto --|> IValidadorReparto
IGestionReparto --|> ICalculadoraReparto

' Los controladores dependen de los contratos.
PlanificacionControlador ..> GestorPresupuesto
PlanControlador ..> IGestionReparto
PlanControlador ..> PresupuestoServicio
RecomendacionControlador ..> IRepositorioViajes

' Decorator: envuelve otro GestorPresupuesto.
PresupuestoConMargenDecorador ..> GestorPresupuesto

' GastosServicio depende de sus repositorios y del Factory Method.
GastosServicio ..> IRepositorioGastos
GastosServicio ..> IRepositorioPresupuestoViaje
GastosServicio ..> IRepositorioRepartoPresupuesto
GastosServicio ..> ControlCategoriaCreador
ControlCategoriaCreador ..> IControlCategoriaGasto

' Modelos que usa cada contrato.
GestorPresupuesto ..> Presupuesto
IGestorGastos ..> Gasto
IGestorGastos ..> ResumenPresupuesto
IControlCategoriaGasto ..> ResultadoControlCategoria
IRepositorioGastos ..> Gasto
IRepositorioPresupuestoViaje ..> PresupuestoViaje
IRepositorioRepartoPresupuesto ..> IDistribuible
IRepositorioViajes ..> FechasViaje
IValidadorReparto ..> IDistribuible
ICalculadoraReparto ..> IDistribuible

@enduml
```


## Diagrama
<img width="2954" height="1943" alt="Untitled (5)" src="https://github.com/user-attachments/assets/9cc30395-bea6-48c0-818f-9fef7b88dc29" />


