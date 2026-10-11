package com.wisetrip.modelo;

import java.util.Map;

/**
 * Clase de DOMINIO: el viaje que el usuario va planificando.
 *
 * Reune lo que antes viajaba suelto en la sesion HTTP (origen, preferencias,
 * fechas, presupuesto, reparto y destino elegido) y conoce las reglas que
 * relacionan esas partes entre si.
 */
public class Viaje {

    private int idViaje;                         // 0 = todavia no se guardo en la base de datos
    private final Usuario usuario;
    private Ubicacion origen;
    private Preferencias preferencias;
    private FechasViaje fechas;
    private Presupuesto presupuesto;
    private Double presupuestoEnUsd;             // null = todavia no se calcula
    private RepartoPresupuesto reparto;
    private ResultadoRecomendacion destinoElegido;

    public Viaje(Usuario usuario) {
        this.usuario = usuario;
    }

    // ------------------------------------------------------------------
    // Comportamiento del dominio
    // ------------------------------------------------------------------

    public boolean estaGuardado() {
        return idViaje > 0;
    }

    public boolean tieneOrigen() {
        return origen != null;
    }

    public boolean tienePreferencias() {
        return preferencias != null;
    }

    public boolean tieneFechas() {
        return fechas != null;
    }

    public boolean tienePresupuesto() {
        return presupuesto != null && presupuestoEnUsd != null;
    }

    public boolean tieneDestino() {
        return destinoElegido != null;
    }

    /** Dias del viaje; 0 si todavia no hay fechas. */
    public long duracionDias() {
        return fechas != null ? fechas.getDuracionDias() : 0;
    }

    /** Ciudad del destino elegido; null si todavia no se elige. */
    public Ciudad destino() {
        return destinoElegido != null ? destinoElegido.getCiudad() : null;
    }

    /** Pais del destino elegido; null si todavia no se elige. */
    public String paisDestino() {
        Ciudad destino = destino();
        return destino != null ? destino.getPais() : null;
    }

    /**
     * Define el presupuesto. Si el presupuesto cambia, el destino elegido y el
     * viaje guardado dejan de ser validos y hay que recalcular las recomendaciones.
     */
    public void definirPresupuesto(Presupuesto nuevoPresupuesto, double equivalenteEnUsd) {
        this.presupuesto = nuevoPresupuesto;
        this.presupuestoEnUsd = equivalenteEnUsd;
        this.idViaje = 0;
        this.destinoElegido = null;
    }

    public void elegirDestino(ResultadoRecomendacion destino) {
        this.destinoElegido = destino;
    }

    /** Para recomendar hace falta presupuesto en USD y al menos un dia de viaje. */
    public boolean puedeRecomendar() {
        return presupuestoEnUsd != null && duracionDias() >= 1;
    }

    /** Arma las preferencias que usa el recomendador. Solo si puedeRecomendar(). */
    public PreferenciasUsuario crearPreferenciasUsuario(Map<String, Importancia> importancias) {
        if (!puedeRecomendar()) {
            throw new IllegalStateException("Faltan el presupuesto o las fechas del viaje.");
        }
        return new PreferenciasUsuario(presupuestoEnUsd, importancias, duracionDias());
    }

    /** Se guarda una sola vez y solo si hay fechas y un usuario ya registrado. */
    public boolean puedeGuardarse() {
        return !estaGuardado() && fechas != null && usuario != null && usuario.getIdUsuario() > 0;
    }

    // ------------------------------------------------------------------
    // Getters y setters
    // ------------------------------------------------------------------

    public int getIdViaje() { return idViaje; }
    public void setIdViaje(int idViaje) { this.idViaje = idViaje; }

    public Usuario getUsuario() { return usuario; }

    public Ubicacion getOrigen() { return origen; }
    public void setOrigen(Ubicacion origen) { this.origen = origen; }

    public Preferencias getPreferencias() { return preferencias; }
    public void setPreferencias(Preferencias preferencias) { this.preferencias = preferencias; }

    public FechasViaje getFechas() { return fechas; }
    public void setFechas(FechasViaje fechas) { this.fechas = fechas; }

    public Presupuesto getPresupuesto() { return presupuesto; }
    public Double getPresupuestoEnUsd() { return presupuestoEnUsd; }

    public RepartoPresupuesto getReparto() { return reparto; }
    public void setReparto(RepartoPresupuesto reparto) { this.reparto = reparto; }

    public ResultadoRecomendacion getDestinoElegido() { return destinoElegido; }
}