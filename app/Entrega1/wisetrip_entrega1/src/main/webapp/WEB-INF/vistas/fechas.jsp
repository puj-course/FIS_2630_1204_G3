<%-- Fechas de viaje.
     Vista donde el usuario elige fecha de inicio y regreso con un selector
     de rango tipo calendario, muestra la duración calculada del viaje y
     los errores de validación si los hay.
     Es el tercer paso del flujo (Origen > Preferencias > Fechas > Presupuesto).

     Rediseño: hoja propia fechas.css con prefijo fc-.
     Las clases sin prefijo (campo-fecha, calendario-*, dia, activo...) y
     los id los usa el script del calendario (HU-51). No renombrarlos. --%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Fechas del viaje | WiseTrip</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Archivo+Black&family=IBM+Plex+Mono:wght@400;500;600&family=Instrument+Sans:wght@400;500;600;700&display=swap">
    <link rel="stylesheet" href="/css/fechas.css">
</head>
<body class="fc">
<div class="fc-pagina">

    <header class="fc-barra">
        <a class="fc-logo" href="/"><img src="/img/portada/logo.png" alt="WiseTrip"></a>
        <div class="fc-usuario">
            <span class="fc-avatar" aria-hidden="true">${inicialesUsuario}</span>
            <span class="fc-hola">Hola, <strong>${usuario.nombreCompleto}</strong></span>
            <a class="fc-salir" href="<c:url value='/logout'/>">Cerrar sesión</a>
        </div>
    </header>

    <%-- HU-37: Indicador visual de en qué paso del flujo está el viajero. --%>
    <nav class="fc-pasos" aria-label="Pasos de la planificación">
        <span class="fc-paso fc-paso-hecho">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5 12.5l4.5 4.5L19 7.5"/></svg>
            Origen
        </span>
        <span class="fc-paso fc-paso-hecho">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5 12.5l4.5 4.5L19 7.5"/></svg>
            Preferencias
        </span>
        <span class="fc-paso fc-paso-activo" aria-current="step">Fechas</span>
        <span class="fc-paso">Presupuesto</span>
    </nav>

    <main class="fc-grid">

        <section class="fc-columna">
            <p class="fc-eyebrow">Paso 3 de 4</p>
            <h1 class="fc-titulo">¿Cuándo<br>viajas?</h1>
            <p class="fc-sub">
                Con las fechas calculamos cuántos días dura el viaje y cómo repartir tu presupuesto.
            </p>

            <%-- HU-51: Selector de rango de fechas tipo calendario, con hover
                 que muestra el rango y las noches antes de confirmar. --%>
            <form action="<c:url value='/fechas'/>" method="post" id="formFechas">

                <div class="selector-fechas fc-selector" id="selectorFechas">
                    <div class="campo-fecha fc-campo activo" id="campoInicio" data-campo="inicio">
                        <span class="fc-campo-icono" aria-hidden="true">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="5" width="18" height="16" rx="2.5"/><path d="M3 10h18M8 3v4M16 3v4"/></svg>
                        </span>
                        <div>
                            <label>Entrada</label>
                            <span class="campo-fecha-valor fc-campo-valor" id="valorInicio">Selecciona</span>
                        </div>
                    </div>

                    <div class="campo-fecha-divisor fc-divisor">
                        <span aria-hidden="true">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.6" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14M13 6l6 6-6 6"/></svg>
                        </span>
                    </div>

                    <div class="campo-fecha fc-campo" id="campoFin" data-campo="fin">
                        <span class="fc-campo-icono fc-campo-icono-salida" aria-hidden="true">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="5" width="18" height="16" rx="2.5"/><path d="M3 10h18M8 3v4M16 3v4M9 15.5l2 2 4-4"/></svg>
                        </span>
                        <div>
                            <label>Salida</label>
                            <span class="campo-fecha-valor fc-campo-valor" id="valorFin">Selecciona</span>
                        </div>
                    </div>

                    <div class="calendario-flotante" id="calendarioFlotante" hidden>
                        <div class="calendario-nav">
                            <button type="button" class="calendario-flecha" id="mesAnterior" aria-label="Mes anterior">&larr;</button>
                            <button type="button" class="calendario-flecha" id="mesSiguiente" aria-label="Mes siguiente">&rarr;</button>
                        </div>
                        <div class="calendario-meses">
                            <div class="calendario-mes" id="mesA"></div>
                            <div class="calendario-mes" id="mesB"></div>
                        </div>
                    </div>
                </div>

                <input type="hidden" name="fechaInicio" id="inputFechaInicio" value="${fechas.fechaInicio}">
                <input type="hidden" name="fechaFin" id="inputFechaFin" value="${fechas.fechaFin}">

                <%-- HU-39: Si ya hay fechas guardadas en sesión, quedan precargadas
                     en los inputs ocultos y el JS las muestra en las casillas. --%>

                <c:if test="${not empty errores.fechaInicio}">
                    <p class="fc-error">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" aria-hidden="true"><circle cx="12" cy="12" r="9"/><path d="M12 7.5v5.5M12 16.5v.01"/></svg>
                        ${errores.fechaInicio}
                    </p>
                </c:if>
                <c:if test="${not empty errores.fechaFin}">
                    <p class="fc-error">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" aria-hidden="true"><circle cx="12" cy="12" r="9"/><path d="M12 7.5v5.5M12 16.5v.01"/></svg>
                        ${errores.fechaFin}
                    </p>
                </c:if>

                <div class="fc-duracion">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="9"/><path d="M12 7v5l3.5 2"/></svg>
                    <span id="fcDuracionTexto">
                        <c:choose>
                            <c:when test="${fechas.duracionDias > 0}">Tu viaje duraría <strong>${fechas.duracionDias} días</strong>.</c:when>
                            <c:otherwise>Elige la entrada y la salida para calcular la duración.</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="fc-acciones">
                    <%-- HU-37: Botón "Atrás" del flujo, sin perder los datos ya ingresados. --%>
                    <a class="fc-volver" href="<c:url value='/preferencias'/>">&larr; Volver a preferencias</a>
                    <button type="submit" class="fc-boton">
                        Continuar
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5 12h14M13 6l6 6-6 6"/></svg>
                    </button>
                </div>
            </form>
        </section>

        <%-- Panel ilustrado: boleto con las dos hojas de calendario.
             Se actualiza solo al elegir fechas (script de vista previa, abajo). --%>
        <aside class="fc-panel" aria-hidden="true">
            <span class="fc-sol"></span>
            <span class="fc-nube fc-nube-1"></span>
            <span class="fc-nube fc-nube-2"></span>

            <div class="fc-boleto-envoltura">
                <div class="fc-boleto">
                    <div class="fc-boleto-cabeza">
                        <span>WISETRIP</span>
                        <span>ITINERARIO</span>
                    </div>

                    <div class="fc-boleto-cuerpo">
                        <div class="fc-hojas">
                            <div class="fc-hoja">
                                <span class="fc-hoja-tira fc-hoja-tira-ida">Ida</span>
                                <span class="fc-hoja-mes" id="fcIdaMes">Por elegir</span>
                                <span class="fc-hoja-dia" id="fcIdaDia">--</span>
                                <span class="fc-hoja-semana" id="fcIdaSemana">&nbsp;</span>
                            </div>

                            <span class="fc-hojas-flecha">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.6" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14M13 6l6 6-6 6"/></svg>
                            </span>

                            <div class="fc-hoja">
                                <span class="fc-hoja-tira fc-hoja-tira-vuelta">Regreso</span>
                                <span class="fc-hoja-mes" id="fcVueltaMes">Por elegir</span>
                                <span class="fc-hoja-dia" id="fcVueltaDia">--</span>
                                <span class="fc-hoja-semana" id="fcVueltaSemana">&nbsp;</span>
                            </div>
                        </div>

                        <div class="fc-boleto-corte"></div>

                        <div class="fc-boleto-datos">
                            <div>
                                <span class="fc-dato-etiqueta">Días</span>
                                <span class="fc-dato-valor" id="fcDias">--</span>
                            </div>
                            <div>
                                <span class="fc-dato-etiqueta">Noches</span>
                                <span class="fc-dato-valor" id="fcNoches">--</span>
                            </div>
                            <div>
                                <span class="fc-dato-etiqueta">Estado</span>
                                <span class="fc-dato-valor fc-dato-estado" id="fcEstado">Pendiente</span>
                            </div>
                        </div>
                    </div>

                    <div class="fc-boleto-pie">
                        <span class="fc-codigo"></span>
                        <span>PASO 3 / 4</span>
                    </div>
                </div>

                <span class="fc-sello" id="fcSello">Sin fechas</span>
            </div>

            <svg class="fc-colinas" viewBox="0 0 600 140" preserveAspectRatio="none">
                <path d="M0 90 C 90 40, 170 40, 260 80 S 430 30, 600 70 L600 140 L0 140 Z"
                      fill="#2FBFAF" stroke="#16161D" stroke-width="3" vector-effect="non-scaling-stroke"/>
                <path d="M0 115 C 120 80, 220 95, 330 110 S 500 85, 600 105 L600 140 L0 140 Z"
                      fill="#7BC97F" stroke="#16161D" stroke-width="3" vector-effect="non-scaling-stroke"/>
            </svg>
        </aside>

    </main>
</div>

<%-- ===== Script del calendario (HU-51). Sin cambios. ===== --%>
<script>
(function () {
    const HOY = new Date(new Date().toDateString());
    const MESES = ["Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"];
    const DIAS = ["Dom","Lun","Mar","Mié","Jue","Vie","Sáb"];

    const inputInicio = document.getElementById('inputFechaInicio');
    const inputFin = document.getElementById('inputFechaFin');
    const valorInicio = document.getElementById('valorInicio');
    const valorFin = document.getElementById('valorFin');
    const campoInicio = document.getElementById('campoInicio');
    const campoFin = document.getElementById('campoFin');
    const flotante = document.getElementById('calendarioFlotante');

    // Convierte "yyyy-mm-dd" a Date en horario LOCAL (evita el corrimiento
    // de un día que causa `new Date("yyyy-mm-dd")` al interpretarlo como UTC).
    function parseISO(str) {
        const [y, m, d] = str.split('-').map(Number);
        return new Date(y, m - 1, d);
    }

    function iso(d) {
        return d.getFullYear() + '-' + String(d.getMonth()+1).padStart(2,'0') + '-' + String(d.getDate()).padStart(2,'0');
    }
    function formatoCorto(d) {
        return d.getDate() + ' ' + MESES[d.getMonth()].slice(0,3) + '. ' + d.getFullYear();
    }
    function mismoDia(a, b) {
        return a && b && a.getFullYear()===b.getFullYear() && a.getMonth()===b.getMonth() && a.getDate()===b.getDate();
    }

    let mesBase = new Date(HOY.getFullYear(), HOY.getMonth(), 1);
    let seleccionando = 'inicio';
    let fechaInicio = inputInicio.value ? parseISO(inputInicio.value) : null;
    let fechaFin = inputFin.value ? parseISO(inputFin.value) : null;
    let hoverFecha = null;

    function actualizarCampos() {
        valorInicio.textContent = fechaInicio ? formatoCorto(fechaInicio) : 'Selecciona';
        valorFin.textContent = fechaFin ? formatoCorto(fechaFin) : 'Selecciona';
        inputInicio.value = fechaInicio ? iso(fechaInicio) : '';
        inputFin.value = fechaFin ? iso(fechaFin) : '';
        campoInicio.classList.toggle('activo', seleccionando === 'inicio');
        campoFin.classList.toggle('activo', seleccionando === 'fin');
    }

    function limiteMinimo() {
        return seleccionando === 'fin' && fechaInicio ? fechaInicio : HOY;
    }

    function renderMes(contenedor, año, mes) {
        contenedor.innerHTML = '';
        const titulo = document.createElement('div');
        titulo.className = 'calendario-titulo';
        titulo.textContent = MESES[mes] + ' ' + año;
        contenedor.appendChild(titulo);

        const filaDias = document.createElement('div');
        filaDias.className = 'calendario-semana';
        DIAS.forEach(d => {
            const s = document.createElement('span');
            s.textContent = d;
            filaDias.appendChild(s);
        });
        contenedor.appendChild(filaDias);

        const grilla = document.createElement('div');
        grilla.className = 'calendario-grilla';
        const primerDia = new Date(año, mes, 1);
        const offset = primerDia.getDay();
        const diasEnMes = new Date(año, mes+1, 0).getDate();

        for (let i=0; i<offset; i++) grilla.appendChild(document.createElement('span'));

        for (let dia=1; dia<=diasEnMes; dia++) {
            const fecha = new Date(año, mes, dia);
            const boton = document.createElement('button');
            boton.type = 'button';
            boton.className = 'dia';
            boton.textContent = dia;
            boton.dataset.fecha = iso(fecha);

            boton.addEventListener('mouseenter', () => {
                if (boton.disabled) return;
                hoverFecha = fecha;
                actualizarEstadosDias();
            });
            boton.addEventListener('click', () => {
                if (boton.disabled) return;
                elegir(fecha);
            });

            grilla.appendChild(boton);
        }
        contenedor.appendChild(grilla);
    }

    function actualizarEstadosDias() {
        const minimo = limiteMinimo();
        document.querySelectorAll('.dia[data-fecha]').forEach(boton => {
            const fecha = parseISO(boton.dataset.fecha);
            const deshabilitado = fecha < minimo;
            boton.disabled = deshabilitado;

            boton.classList.remove('dia-inicio', 'dia-fin', 'dia-en-rango');
            const globoViejo = boton.querySelector('.noches-globo');
            if (globoViejo) globoViejo.remove();

            if (mismoDia(fecha, fechaInicio)) boton.classList.add('dia-inicio');
            if (mismoDia(fecha, fechaFin)) boton.classList.add('dia-fin');

            if (fechaInicio && !fechaFin && hoverFecha) {
                if (fecha > fechaInicio && fecha <= hoverFecha) boton.classList.add('dia-en-rango');
                if (mismoDia(fecha, hoverFecha) && fecha > fechaInicio) {
                    const noches = Math.round((fecha - fechaInicio) / 86400000);
                    const globo = document.createElement('span');
                    globo.className = 'noches-globo';
                    globo.textContent = noches + (noches === 1 ? ' noche' : ' noches');
                    boton.appendChild(globo);
                }
            } else if (fechaInicio && fechaFin && fecha > fechaInicio && fecha < fechaFin) {
                boton.classList.add('dia-en-rango');
            }
        });
    }

    function pintar() {
        const a = new Date(mesBase);
        const b = new Date(mesBase.getFullYear(), mesBase.getMonth()+1, 1);
        renderMes(document.getElementById('mesA'), a.getFullYear(), a.getMonth());
        renderMes(document.getElementById('mesB'), b.getFullYear(), b.getMonth());
        actualizarEstadosDias();
    }

    // HU-51 (ajuste): elegir un día SIEMPRE cierra el calendario.
    // El usuario debe hacer clic explícito en "Salida" para abrir
    // el segundo calendario — no se abre automáticamente.
    function elegir(fecha) {
        if (seleccionando === 'inicio') {
            fechaInicio = fecha;
            fechaFin = null;
        } else {
            if (fecha < fechaInicio) {
                fechaInicio = fecha;
                fechaFin = null;
            } else {
                fechaFin = fecha;
            }
        }
        hoverFecha = null;
        actualizarCampos();
        pintar();
        cerrar();
    }

    function abrir(campo) {
        seleccionando = campo;
        flotante.hidden = false;
        actualizarCampos();
        pintar();
    }
    function cerrar() { flotante.hidden = true; }

    campoInicio.addEventListener('click', () => abrir('inicio'));
    campoFin.addEventListener('click', () => abrir('fin'));
    document.getElementById('mesAnterior').addEventListener('click', () => { mesBase.setMonth(mesBase.getMonth()-1); pintar(); });
    document.getElementById('mesSiguiente').addEventListener('click', () => { mesBase.setMonth(mesBase.getMonth()+1); pintar(); });
    document.addEventListener('click', (e) => {
        const dentro = e.composedPath().includes(document.getElementById('selectorFechas'));
        if (!dentro) cerrar();
    });

    actualizarCampos();
})();
</script>

<%-- ===== Vista previa del boleto (rediseño) =====
     No toca el calendario: solo observa cuándo cambian los textos de
     Entrada y Salida, lee los inputs ocultos y pinta el panel. --%>
<script>
(function () {
    const MESES = ["Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"];
    const SEMANA = ["Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"];

    const inputInicio = document.getElementById('inputFechaInicio');
    const inputFin = document.getElementById('inputFechaFin');

    function leer(valor) {
        if (!valor) return null;
        const [y, m, d] = valor.split('-').map(Number);
        return new Date(y, m - 1, d);
    }

    function pintarHoja(prefijo, fecha) {
        document.getElementById(prefijo + 'Mes').textContent =
            fecha ? MESES[fecha.getMonth()] + ' ' + fecha.getFullYear() : 'Por elegir';
        document.getElementById(prefijo + 'Dia').textContent =
            fecha ? fecha.getDate() : '--';
        document.getElementById(prefijo + 'Semana').innerHTML =
            fecha ? SEMANA[fecha.getDay()] : '&nbsp;';
    }

    function actualizar() {
        const inicio = leer(inputInicio.value);
        const fin = leer(inputFin.value);

        pintarHoja('fcIda', inicio);
        pintarHoja('fcVuelta', fin);

        const texto = document.getElementById('fcDuracionTexto');
        const sello = document.getElementById('fcSello');
        const estado = document.getElementById('fcEstado');

        if (inicio && fin) {
            const noches = Math.round((fin - inicio) / 86400000);
            const dias = noches + 1;   // mismo cálculo que FechasViaje.getDuracionDias()
            document.getElementById('fcDias').textContent = dias;
            document.getElementById('fcNoches').textContent = noches;
            estado.textContent = 'Listo';
            sello.textContent = dias + (dias === 1 ? ' día' : ' días');
            sello.classList.add('fc-sello-listo');
            texto.innerHTML = 'Tu viaje duraría <strong>' + dias + (dias === 1 ? ' día' : ' días') + '</strong>.';
        } else {
            document.getElementById('fcDias').textContent = '--';
            document.getElementById('fcNoches').textContent = '--';
            estado.textContent = 'Pendiente';
            sello.textContent = inicio ? 'Falta el regreso' : 'Sin fechas';
            sello.classList.remove('fc-sello-listo');
            texto.textContent = inicio
                ? 'Ahora elige el día de salida.'
                : 'Elige la entrada y la salida para calcular la duración.';
        }
    }

    const observador = new MutationObserver(actualizar);
    ['valorInicio', 'valorFin'].forEach(id => {
        observador.observe(document.getElementById(id), { childList: true, characterData: true, subtree: true });
    });

    actualizar();
})();
</script>
</body>
</html>