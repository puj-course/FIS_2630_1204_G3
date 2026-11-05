package com.wisetrip.modelo;

/**
 * Clase de DOMINIO: el viaje que el usuario va planificando.
 *
 * Reune lo que antes viajaba suelto en la sesion HTTP (origen, fechas,
 * presupuesto, reparto y destino elegido) y conoce las reglas que
 * relacionan esas partes entre si.
 */
public class Viaje {

    private int idViaje;                         // 0 = todavia no se guardo en la base de datos
    private final Usuario usuario;
    private Ubicacion origen;
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

    // ------------------------------------------------------------------
    // Getters y setters
    // ------------------------------------------------------------------

    public int getIdViaje() { return idViaje; }
    public void setIdViaje(int idViaje) { this.idViaje = idViaje; }

    public Usuario getUsuario() { return usuario; }

    public Ubicacion getOrigen() { return origen; }
    public void setOrigen(Ubicacion origen) { this.origen = origen; }

    public FechasViaje getFechas() { return fechas; }
    public void setFechas(FechasViaje fechas) { this.fechas = fechas; }

    public Presupuesto getPresupuesto() { return presupuesto; }
    public Double getPresupuestoEnUsd() { return presupuestoEnUsd; }

    public RepartoPresupuesto getReparto() { return reparto; }
    public void setReparto(RepartoPresupuesto reparto) { this.reparto = reparto; }

    public ResultadoRecomendacion getDestinoElegido() { return destinoElegido; }
}