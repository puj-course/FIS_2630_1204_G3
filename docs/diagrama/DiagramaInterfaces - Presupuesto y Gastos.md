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
        +registrarGasto(idViaje: int, gasto: Gasto): Gasto
        +consultarGastos(idViaje: int): List<Gasto>
        +consultarResumen(idViaje: int): ResumenPresupuesto
    }

    interface IRepositorioGastos {
        +guardar(idViaje: int, gasto: Gasto): Gasto
        +listarPorViaje(idViaje: int): List<Gasto>
        +totalGastadoPorViaje(idViaje: int): BigDecimal
    }

    interface IRepositorioPresupuestoViaje {
        +buscarPresupuesto(idViaje: int): PresupuestoViaje
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
    class GastosServicio
    class RepartoServicio
}

package "Controladores existentes" {
    class PlanificacionControlador
    class PlanControlador
}

package "Persistencia existente" {
    class ViajeDAO
}

package "Modelos" {
    class Presupuesto
    class Gasto <<record>>
    class PresupuestoViaje <<record>>
    class ResumenPresupuesto <<record>>
    class RepartoPresupuesto
    class FechasViaje
}

PresupuestoServicio ..|> GestorPresupuesto
GastosServicio ..|> IGestorGastos
RepartoServicio ..|> IGestionReparto
IGestionReparto --|> IValidadorReparto
IGestionReparto --|> ICalculadoraReparto
RepartoPresupuesto ..|> IDistribuible
ViajeDAO ..|> IRepositorioViajes

PlanificacionControlador ..> GestorPresupuesto
PlanControlador ..> IGestionReparto
PlanControlador ..> PresupuestoServicio
GastosServicio ..> IRepositorioGastos
GastosServicio ..> IRepositorioPresupuestoViaje

GestorPresupuesto ..> Presupuesto
IGestorGastos ..> Gasto
IGestorGastos ..> ResumenPresupuesto
IRepositorioGastos ..> Gasto
IRepositorioPresupuestoViaje ..> PresupuestoViaje

IRepositorioViajes ..> FechasViaje
RepartoPresupuesto ..> IDistribuible

@enduml


<img width="1940" height="1799" alt="DiagramaInterfaces - control Gatos y presupuestos" src="https://github.com/user-attachments/assets/9a62b9bd-1312-4e42-b283-2d6bd7523c37" />
