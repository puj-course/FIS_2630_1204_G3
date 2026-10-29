/*
 * WiseTrip · Itinerario durante el viaje
 * Permite arrastrar las actividades del panel a las celdas del calendario
 * (día + hora) y moverlas de una celda a otra. Solo visual.
 */
(function () {
    var raiz = document.getElementById('itItinerario');
    if (!raiz) return;

    var listas = raiz.querySelectorAll('.it-lista-actividades');
    var arrastrada = null;

    // cada tarjeta recuerda la lista de la que salió (propias o recomendadas)
    raiz.querySelectorAll('.it-actividad').forEach(function (tarjeta) {
        tarjeta.dataset.origen = tarjeta.parentElement.id;
    });

    // saca la actividad del calendario y la vuelve a poner en su lista
    function devolverAlPanel(tarjeta) {
        delete tarjeta.dataset.fecha;
        delete tarjeta.dataset.hora;
        document.getElementById(tarjeta.dataset.origen).appendChild(tarjeta);
        refrescar();
    }

    // la celda tiene una actividad distinta a la que se está moviendo
    function estaOcupada(celda) {
        var actual = celda.querySelector('.it-actividad');
        return actual !== null && actual !== arrastrada;
    }

    // muestra un aviso dentro de la celda y lo quita a los pocos segundos
    function avisar(celda, texto) {
        var previo = celda.querySelector('.it-aviso');
        if (previo) previo.remove();

        var aviso = document.createElement('p');
        aviso.className = 'it-aviso';
        aviso.textContent = texto;
        celda.appendChild(aviso);
        setTimeout(function () { aviso.remove(); }, 2600);
    }

    // muestra "Aún no tienes actividades" en la primera celda de cada día vacío
    function actualizarDiasVacios() {
        raiz.querySelectorAll('.it-cal-dia').forEach(function (dia) {
            var celdas = raiz.querySelectorAll('.it-cal-celda[data-fecha="' + dia.dataset.fecha + '"]');
            if (celdas.length === 0) return;

            var tieneActividades = Array.prototype.some.call(celdas, function (c) {
                return c.querySelector('.it-actividad') !== null;
            });
            var mensaje = celdas[0].querySelector('.it-vacio');

            if (tieneActividades && mensaje) {
                mensaje.remove();
            } else if (!tieneActividades && !mensaje) {
                mensaje = document.createElement('p');
                mensaje.className = 'it-vacio';
                mensaje.textContent = 'Aún no tienes actividades para este día.';
                celdas[0].appendChild(mensaje);
            }
        });
    }

    /* ===== Presupuesto ===== */

    // a partir de este porcentaje gastado se muestra la alerta roja
    var LIMITE_ALERTA = 0.9;
    var cajaPresupuesto = document.getElementById('itPresupuesto');
    var totalUsd = parseFloat(cajaPresupuesto.dataset.totalUsd) || 0;

    // suma el costo (USD) de las actividades que ya están en el calendario
    function gastadoEnCalendario() {
        var suma = 0;
        raiz.querySelectorAll('.it-cal-celda .it-actividad').forEach(function (t) {
            suma += parseFloat(t.dataset.costo) || 0;
        });
        return suma;
    }

    function actualizarPresupuesto() {
        var gastado = gastadoEnCalendario();
        var cerca = totalUsd > 0 && gastado >= totalUsd * LIMITE_ALERTA;

        document.getElementById('itGastado').textContent =
            'US$ ' + gastado.toLocaleString('en-US');
        document.getElementById('itAlertaRoja').hidden = !cerca;
        document.getElementById('itAlertaVerde').hidden = cerca;
    }

    // todo lo que depende de dónde están las actividades
    function refrescar() {
        actualizarDiasVacios();
        actualizarPresupuesto();
    }

    /* ===== Tarjetas ===== */

    raiz.addEventListener('dragstart', function (e) {
        var tarjeta = e.target.closest('.it-actividad');
        if (!tarjeta) return;
        arrastrada = tarjeta;
        e.dataTransfer.effectAllowed = 'move';
        e.dataTransfer.setData('text/plain', tarjeta.dataset.id);
        tarjeta.classList.add('it-arrastrando');
    });

    raiz.addEventListener('dragend', function () {
        if (arrastrada) arrastrada.classList.remove('it-arrastrando');
        arrastrada = null;
        raiz.querySelectorAll('.it-encima, .it-ocupada')
            .forEach(function (c) { c.classList.remove('it-encima', 'it-ocupada'); });
    });

    // botón × de cada tarjeta; el CSS solo lo muestra dentro del calendario
    raiz.querySelectorAll('.it-actividad').forEach(function (tarjeta) {
        var quitar = document.createElement('button');
        quitar.type = 'button';
        quitar.className = 'it-quitar';
        quitar.setAttribute('aria-label', 'Quitar del calendario');
        quitar.textContent = '×';
        tarjeta.appendChild(quitar);
    });

    raiz.addEventListener('click', function (e) {
        var quitar = e.target.closest('.it-quitar');
        if (quitar) devolverAlPanel(quitar.closest('.it-actividad'));
    });

    /* ===== Panel: también recibe actividades de vuelta ===== */

    listas.forEach(function (lista) {

        lista.addEventListener('dragover', function (e) {
            if (!arrastrada || arrastrada.parentElement === lista) return;
            e.preventDefault();
            e.dataTransfer.dropEffect = 'move';
            lista.classList.add('it-encima');
        });

        lista.addEventListener('dragleave', function (e) {
            if (!lista.contains(e.relatedTarget)) lista.classList.remove('it-encima');
        });

        lista.addEventListener('drop', function (e) {
            e.preventDefault();
            lista.classList.remove('it-encima');
            if (arrastrada) devolverAlPanel(arrastrada);
        });
    });

    /* ===== Celdas del calendario ===== */

    raiz.querySelectorAll('.it-cal-celda').forEach(function (celda) {

        celda.addEventListener('dragover', function (e) {
            if (!arrastrada) return;
            e.preventDefault();
            e.dataTransfer.dropEffect = 'move';
            celda.classList.add(estaOcupada(celda) ? 'it-ocupada' : 'it-encima');
        });

        celda.addEventListener('dragleave', function () {
            celda.classList.remove('it-encima', 'it-ocupada');
        });

        celda.addEventListener('drop', function (e) {
            e.preventDefault();
            celda.classList.remove('it-encima', 'it-ocupada');
            if (!arrastrada) return;

            if (estaOcupada(celda)) {
                avisar(celda, 'Ya tienes una actividad programada a esa hora.');
                return;
            }

            arrastrada.dataset.fecha = celda.dataset.fecha;
            arrastrada.dataset.hora = celda.dataset.hora;
            celda.appendChild(arrastrada);
            refrescar();
        });
    });

    refrescar();
})();
