/*
 * WiseTrip · Itinerario durante el viaje
 * Permite arrastrar las actividades del panel a las celdas del calendario
 * (día + hora) y moverlas de una celda a otra. Solo visual.
 */
(function () {
    var raiz = document.getElementById('itItinerario');
    if (!raiz) return;

    var arrastrada = null;

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
            actualizarDiasVacios();
        });
    });

    actualizarDiasVacios();
})();
