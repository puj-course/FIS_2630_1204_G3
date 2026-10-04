/*
 * WiseTrip · Plan
 * Reparto del presupuesto: elegir modo, editar porcentajes, ver montos
 * en vivo y bloquear la confirmación si se pasa de 100%.
 * Los campos se siguen llamando igual y se envían a /plan como antes.
 */
(function () {
    var form = document.getElementById('formPlan');
    if (!form) return;

    var CATEGORIAS = ['hospedaje', 'alimentacion', 'transporte', 'actividades', 'imprevistos'];

    var total = leerMonto(form.dataset.total);
    var moneda = form.dataset.moneda || '';
    var dias = parseInt(form.dataset.dias, 10) || 0;

    var panel = document.getElementById('plReparto');
    var opciones = document.querySelectorAll('input[name="modoReparto"]');
    var cifraUsado = document.getElementById('plUsado');
    var cifraDisponible = document.getElementById('plDisponible');
    var medidor = document.getElementById('plMedidor');
    var exceso = document.getElementById('plExceso');
    var boton = document.getElementById('btnGuardar');
    var restaurar = document.getElementById('btnRestaurar');
    var sobreTotal = document.getElementById('plSobreTotal');
    var sobreDia = document.getElementById('plSobreDia');

    /* Mismo criterio que Presupuesto: punto = miles, coma = decimales */
    function leerMonto(texto) {
        var t = (texto || '').trim().replace(/\s/g, '');
        if (!t) return 0;
        t = t.replace(/\./g, '').replace(',', '.');
        var n = parseFloat(t);
        return isNaN(n) ? 0 : n;
    }

    function formatear(n) {
        return Math.round(n).toLocaleString('es-CO') + (moneda ? ' ' + moneda : '');
    }

    function campo(cat) { return document.getElementById('pct-' + cat); }

    function valor(cat) {
        var v = parseInt(campo(cat).value, 10);
        if (isNaN(v)) return 0;
        return Math.max(0, Math.min(100, v));
    }

    function actualizar() {
        var suma = 0;

        CATEGORIAS.forEach(function (cat) {
            var v = valor(cat);
            suma += v;
            var dinero = total * v / 100;

            document.querySelector('[data-relleno="' + cat + '"]').style.width = v + '%';
            document.querySelector('[data-monto="' + cat + '"]').textContent = formatear(dinero);
            document.querySelector('[data-sobre="' + cat + '"]').textContent = formatear(dinero);
            document.querySelector('[data-seg="' + cat + '"]').style.width = v + '%';
        });

        var disponible = 100 - suma;
        cifraUsado.textContent = suma + '%';
        cifraDisponible.textContent = (disponible < 0 ? 0 : disponible) + '%';

        medidor.classList.toggle('pl-excedido', suma > 100);
        medidor.classList.toggle('pl-completo', suma === 100);

        if (suma > 100) {
            exceso.hidden = false;
            exceso.textContent = 'Te pasaste por ' + (suma - 100) + '%. Baja alguna categoría para poder confirmar.';
            boton.disabled = true;
        } else {
            exceso.hidden = true;
            boton.disabled = false;
        }

        sobreTotal.textContent = formatear(total);
        sobreDia.textContent = dias > 0 ? formatear(total / dias) : formatear(total);
    }

    function ponerSugeridos() {
        CATEGORIAS.forEach(function (cat) {
            campo(cat).value = campo(cat).dataset.sugerido;
        });
    }

    function elegirModo(modo) {
        panel.hidden = false;
        var personalizado = modo === 'personalizado';
        panel.classList.toggle('pl-modo-auto', !personalizado);

        if (!personalizado) ponerSugeridos();

        CATEGORIAS.forEach(function (cat) {
            campo(cat).readOnly = !personalizado;
        });

        restaurar.hidden = !personalizado;
        boton.textContent = personalizado ? 'Confirmar mi reparto' : 'Usar este reparto';

        actualizar();
        if (personalizado) campo(CATEGORIAS[0]).focus();
    }

    opciones.forEach(function (op) {
        op.addEventListener('change', function () { elegirModo(op.value); });
    });

    CATEGORIAS.forEach(function (cat) {
        var c = campo(cat);
        c.addEventListener('input', actualizar);
        c.addEventListener('blur', function () {
            c.value = valor(cat);
            actualizar();
        });

        /* Clic o arrastre sobre la pista para fijar el porcentaje */
        var pista = document.querySelector('[data-pista="' + cat + '"]');
        var arrastrando = false;

        function desdePuntero(e) {
            if (c.readOnly) return;
            var caja = pista.getBoundingClientRect();
            var f = (e.clientX - caja.left) / caja.width;
            c.value = Math.round(Math.max(0, Math.min(1, f)) * 100);
            actualizar();
        }

        pista.addEventListener('pointerdown', function (e) {
            if (c.readOnly) return;
            arrastrando = true;
            pista.setPointerCapture(e.pointerId);
            desdePuntero(e);
        });
        pista.addEventListener('pointermove', function (e) { if (arrastrando) desdePuntero(e); });
        pista.addEventListener('pointerup', function () { arrastrando = false; });
        pista.addEventListener('pointercancel', function () { arrastrando = false; });
    });

    restaurar.addEventListener('click', function () {
        ponerSugeridos();
        actualizar();
    });

    form.addEventListener('submit', function (e) {
        var suma = CATEGORIAS.reduce(function (s, cat) { return s + valor(cat); }, 0);
        if (suma > 100) {
            e.preventDefault();
            actualizar();
        }
    });

    /* Si el servidor devolvió un error, abrir directamente el modo personalizado */
    var abrir = form.dataset.abrir;
    if (abrir) {
        var op = document.querySelector('input[name="modoReparto"][value="' + abrir + '"]');
        if (op) { op.checked = true; elegirModo(abrir); }
    }

    actualizar();
})();