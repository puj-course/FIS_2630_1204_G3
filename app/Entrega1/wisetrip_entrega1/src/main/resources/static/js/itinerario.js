/*
 * WiseTrip · Itinerario durante el viaje
 * Permite arrastrar las actividades del panel a las celdas del calendario
 * (día + hora) y moverlas de una celda a otra. Solo visual.
 */
(function () {
    var raiz = document.getElementById('itItinerario');
    if (!raiz) return;

    var arrastrada = null;

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
        raiz.querySelectorAll('.it-cal-celda.it-encima')
            .forEach(function (c) { c.classList.remove('it-encima'); });
    });

    /* ===== Celdas del calendario ===== */

    raiz.querySelectorAll('.it-cal-celda').forEach(function (celda) {

        celda.addEventListener('dragover', function (e) {
            if (!arrastrada) return;
            e.preventDefault();
            e.dataTransfer.dropEffect = 'move';
            celda.classList.add('it-encima');
        });

        celda.addEventListener('dragleave', function () {
            celda.classList.remove('it-encima');
        });

        celda.addEventListener('drop', function (e) {
            e.preventDefault();
            celda.classList.remove('it-encima');
            if (!arrastrada) return;

            arrastrada.dataset.fecha = celda.dataset.fecha;
            arrastrada.dataset.hora = celda.dataset.hora;
            celda.appendChild(arrastrada);
        });
    });
})();
