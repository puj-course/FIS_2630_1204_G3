package com.wisetrip.controlador;

/** Nombres de vistas, redirecciones y atributos de sesion/modelo que se repiten en los controladores. */
final class Nombres {

    // Redirecciones
    static final String REDIRECT_LOGIN = "redirect:/login";
    static final String REDIRECT_PRESUPUESTO = "redirect:/presupuesto";
    static final String REDIRECT_RECOMENDACIONES = "redirect:/recomendaciones";
    static final String REDIRECT_VIAJES = "redirect:/viajes";

    // Vistas y atributos del modelo
    static final String LOGIN = "login";
    static final String PLAN = "plan";
    static final String REPARTO = "reparto";
    static final String ERRORES = "errores";
    static final String ERROR_CONTINUAR = "errorContinuar";
    static final String CORREO = "correo";
    static final String FECHAS = "fechas";
    static final String PREFERENCIAS = "preferencias";
    static final String PRESUPUESTO = "presupuesto";

    // Atributos de sesion
    static final String USUARIO_ACTIVO = "usuarioActivo";
    static final String DESTINO_ELEGIDO = "destinoElegido";
    static final String PAIS_DESTINO = "paisDestino";
    static final String REPARTO_PRESUPUESTO = "repartoPresupuesto";
    static final String PRESUPUESTO_VIAJE = "presupuestoViaje";
    static final String FECHAS_VIAJE = "fechasViaje";
    static final String PREFERENCIAS_VIAJE = "preferenciasViaje";

    private Nombres() {
    }
}
