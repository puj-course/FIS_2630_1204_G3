## Codigo del diagrama

```
@startuml
title WiseTrip — Diagrama de clases del dominio

left to right direction
skinparam classAttributeIconSize 0
skinparam linetype ortho
skinparam packageStyle rectangle
hide empty members


package "Planificación" {

    class Usuario {
        -idUsuario: int
        -nombreCompleto: String
        -tipoDocumento: String
        -numeroDocumento: String
        -fechaNacimiento: String
        -correo: String
        -password: String
        -rol: String
        -chatId: String
        --
        +normalizar(): void
        +normalizarCorreo(): void
        +validarDatos(confirmarPassword: String): Map<String, String>
        +calcularEdad(): int
        +verificarPassword(passwordIngresada: String): boolean
        +vincularTelegram(nuevoChatId: String): void
        +tieneTelegramVinculado(): boolean
        +primerNombre(): String
        +iniciales(): String
    }

    class Viaje {
        -idViaje: int
        -presupuestoEnUsd: Double
        --
        +estaGuardado(): boolean
        +tieneOrigen(): boolean
        +tienePreferencias(): boolean
        +tieneFechas(): boolean
        +tienePresupuesto(): boolean
        +tieneDestino(): boolean
        +duracionDias(): long
        +destino(): Ciudad
        +paisDestino(): String
        +definirPresupuesto(nuevoPresupuesto: Presupuesto, equivalenteEnUsd: double): void
        +elegirDestino(destino: ResultadoRecomendacion): void
        +puedeRecomendar(): boolean
        +crearPreferenciasUsuario(importancias: Map<String, Importancia>): PreferenciasUsuario
        +puedeGuardarse(): boolean
    }

    class Ubicacion {
        -pais: String
        -ciudad: String
        -detalle: String
        --
        +estaCompleta(): boolean
        +tieneDetalle(): boolean
        +getDescripcion(): String
        +cambiarPais(nuevoPais: String): void
        +normalizar(): void
        +validar(): Map<String, String>
    }

    class FechasViaje {
        -fechaInicio: String
        -fechaFin: String
        --
        +tieneRangoValido(): boolean
        +getDuracionDias(): long
    }
}

package "Preferencias y recomendaciones" {

    class Preferencias {
        -respuestas: Map<String, String>
        --
        +getTotalRespondidas(): int
        +tienePreferencia(atributo: String): boolean
        +esIndispensable(atributo: String): boolean
        +obtenerImportancia(atributo: String): Importancia
    }

    class PreferenciasUsuario {
        -atributos: Map<String, Importancia>
        -presupuestoUsd: Double
        -duracionDias: long
        --
        +tienePreferenciasPuntuables(): boolean
        +tienePreferencia(atributo: String): boolean
        +esIndispensable(atributo: String): boolean
        +importanciaDe(atributo: String): Importancia
        +pesoDe(atributo: String): int
    }

    class CategoriaPreferencia {
        -nombre: String
        -icono: String
        -descripcion: String
    }

    class Pregunta {
        -clave: String
        -texto: String
    }

    class DefPregunta {
        +id: String {readOnly}
        +texto: String {readOnly}
        +categorias: String {readOnly}
        +umbral: int {readOnly}
        --
        +esAutomatica(): boolean
        +esManual(): boolean
        +esDePerfil(): boolean
        +tieneCategorias(): boolean
        +tieneUmbral(): boolean
    }

    class Ciudad {
        -id: int
        -nombre: String
        -pais: String
        -latitud: double
        -longitud: double
        -costoPromedio: double
        -atributos: Map<String, Boolean>
        --
        +estadoAtributo(atributo: String): EstadoAtributo
        +tiene(atributo: String): boolean
        +getTipo(): String
        +getDescripcion(): String
    }

    class ResultadoRecomendacion {
        -puntajeTotal: double
        -puntajePresupuesto: double
        -puntajePreferencias: double
        -preferenciasPuntuables: boolean
        -coincidenciaProvisional: boolean
    }

    class SeleccionDestinos {
        -mensaje: String
        --
        +isVacio(): boolean
    }

    class ValidacionCiudades <<record>> {
        -validas: List<Ciudad>
        -descartadas: List<Ciudad>
        -pendientes: List<Ciudad>
    }

    enum Importancia {
        no
        gustar
        prefiero
        si
        --
        -peso: int
        --
        +esPuntuable(): boolean
        +estaSeleccionada(): boolean
        +esIndispensable(): boolean
    }

    enum EstadoAtributo {
        cumple
        noCumple
        noSabemos
        --
        {static} +desdeConteo(conteo: Integer, umbral: int): EstadoAtributo
    }

    enum TipoAtributo {
        MANUAL
        AUTO
        PERFIL
    }
}

package "Presupuesto" {

    class Presupuesto {
        -monto: String
        -moneda: String
        --
        +getMontoNumerico(): double
        +esValido(): boolean
        +esSuficientePara(costo: double): boolean
        +tieneMoneda(): boolean
    }

    class RepartoPresupuesto {
        -hospedaje: int
        -alimentacion: int
        -transporte: int
        -actividades: int
        -imprevistos: int
        --
        +porcentajeDe(categoria: String): int
        +asignar(categoria: String, porcentaje: int): void
        +getTotal(): int
        +restaurarSugerido(): void
        +esValido(): boolean
        --
        +porcentajeDisponible(): int
        +porcentajeExcedido(): int
        +excedeElMaximo(): boolean
        +estaCompleto(): boolean
        +tienePorcentajesNegativos(): boolean
        +cubreLosBasicos(): boolean
        +montoDe(categoria: String, presupuestoTotal: double): double
        +distribuir(presupuestoTotal: double): Map<String, Double>
    }
}

package "Notificaciones" {

    class Notificacion {
        -asunto: String
        -cuerpo: String
    }
}

' Relaciones de planificación.

Viaje --> "1" Usuario : usuario
Viaje --> "0..1" Ubicacion : origen
Viaje --> "0..1" FechasViaje : fechas
Viaje --> "0..1" Presupuesto : presupuesto
Viaje --> "0..1" RepartoPresupuesto : reparto
Viaje --> "0..1" Preferencias : preferencias
Viaje --> "0..1" ResultadoRecomendacion : destinoElegido

Viaje ..> PreferenciasUsuario : crea
Viaje ..> Ciudad : obtiene destino

' Relaciones de preferencias y recomendaciones.

CategoriaPreferencia --> "0..*" Pregunta : preguntas


Preferencias ..> Importancia : interpreta respuestas
PreferenciasUsuario ..> Importancia : niveles de preferencia
DefPregunta --> TipoAtributo : tipo

ResultadoRecomendacion --> "1" Ciudad : ciudad
SeleccionDestinos --> "0..*" ResultadoRecomendacion : destinos

ValidacionCiudades ..> Ciudad : clasifica
Ciudad ..> EstadoAtributo : evalúa
```

## Diagrama Visual 






