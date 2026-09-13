<%-- Fechas de viaje.
     Vista donde el usuario elige fecha de inicio y regreso con un selector
     de rango tipo calendario, muestra la duración calculada del viaje y
     los errores de validación si los hay.
     Es el tercer paso del flujo (Origen > Preferencias > Fechas > Presupuesto). --%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Fechas del viaje | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta">

    <div class="barra">
        <span>Hola, <strong>${usuario.nombreCompleto}</strong></span>
        <a href="<c:url value='/logout'/>">Cerrar sesión</a>
    </div>

    <div class="pasos">
<%-- HU-37: Indicador visual de en qué paso del flujo está el viajero. --%>
        <span class="paso hecho">Origen</span>
        <span class="paso hecho">Preferencias</span>
        <span class="paso activo">Fechas</span>
        <span class="paso">Presupuesto</span>
    </div>

    <h1>¿Cuándo viajas?</h1>
    <p class="subtitulo">
        Con las fechas calculamos cuántos días dura el viaje y cómo repartir tu presupuesto.
    </p>

    <%-- HU-51: Selector de rango de fechas tipo calendario, con hover
         que muestra el rango y las noches antes de confirmar. --%>
    <form action="<c:url value='/fechas'/>" method="post" id="formFechas">

        <div class="selector-fechas" id="selectorFechas">
            <div class="campo-fecha activo" id="campoInicio" data-campo="inicio">
                <span class="campo-fecha-icono">&#128197;</span>
                <div>
                    <label>Entrada</label>
                    <span class="campo-fecha-valor" id="valorInicio">Selecciona</span>
                </div>
            </div>
            <div class="campo-fecha-divisor"></div>
            <div class="campo-fecha" id="campoFin" data-campo="fin">
                <div>
                    <label>Salida</label>
                    <span class="campo-fecha-valor" id="valorFin">Selecciona</span>
                </div>
            </div>

            <div class="calendario-flotante" id="calendarioFlotante" hidden>
                <div class="calendario-nav">
                    <button type="button" class="calendario-flecha" id="mesAnterior">&larr;</button>
                    <button type="button" class="calendario-flecha" id="mesSiguiente">&rarr;</button>
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
            <span class="error">${errores.fechaInicio}</span>
        </c:if>
        <c:if test="${not empty errores.fechaFin}">
            <span class="error">${errores.fechaFin}</span>
        </c:if>

        <c:if test="${fechas.duracionDias > 0}">
            <div class="nota">Tu viaje duraría <strong>${fechas.duracionDias} días</strong>.</div>
        </c:if>

        <button type="submit">Continuar</button>
    </form>

    <a class="volver" href="<c:url value='/preferencias'/>">&larr; Volver a preferencias</a>
<%-- HU-37: Botón "Atrás" del flujo, sin perder los datos ya ingresados. --%>
</div>

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

    let mesBase = new Date(HOY.getFullYear(), HOY.getMonth(), 1);
    let seleccionando = 'inicio';
    let fechaInicio = inputInicio.value ? new Date(inputInicio.value) : null;
    let fechaFin = inputFin.value ? new Date(inputFin.value) : null;
    let hoverFecha = null;

    function iso(d) {
        return d.getFullYear() + '-' + String(d.getMonth()+1).padStart(2,'0') + '-' + String(d.getDate()).padStart(2,'0');
    }
    function formatoCorto(d) {
        return d.getDate() + ' ' + MESES[d.getMonth()].slice(0,3) + '. ' + d.getFullYear();
    }
    function mismoDia(a, b) {
        return a && b && a.getFullYear()===b.getFullYear() && a.getMonth()===b.getMonth() && a.getDate()===b.getDate();
    }

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

            const minimo = limiteMinimo();
            const deshabilitado = fecha < minimo;
            if (deshabilitado) boton.disabled = true;

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

            if (!deshabilitado) {
                boton.addEventListener('mouseenter', () => { hoverFecha = fecha; pintar(); });
                boton.addEventListener('click', () => elegir(fecha));
            }
            grilla.appendChild(boton);
        }
        contenedor.appendChild(grilla);
    }

    function pintar() {
        const a = new Date(mesBase);
        const b = new Date(mesBase.getFullYear(), mesBase.getMonth()+1, 1);
        renderMes(document.getElementById('mesA'), a.getFullYear(), a.getMonth());
        renderMes(document.getElementById('mesB'), b.getFullYear(), b.getMonth());
    }

    function elegir(fecha) {
        if (seleccionando === 'inicio') {
            fechaInicio = fecha;
            fechaFin = null;
            seleccionando = 'fin';
        } else {
            if (fecha < fechaInicio) { fechaInicio = fecha; fechaFin = null; }
            else { fechaFin = fecha; seleccionando = 'inicio'; cerrar(); }
        }
        actualizarCampos();
        pintar();
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
        if (!document.getElementById('selectorFechas').contains(e.target)) cerrar();
    });

    actualizarCampos();
})();
</script>
</body>
</html>