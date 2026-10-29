/*
 * WiseTrip · Mi viaje
 * Calcula, con el presupuesto y el reparto guardados, cuánto dinero hay
 * para cada gasto, cuánto alcanza por día y cuánto falta
 * para el viaje. Solo visual.
 */
(function () {
    var raiz = document.getElementById('vjViaje');
    if (!raiz) return;

    var CATEGORIAS = ['hospedaje', 'alimentacion', 'transporte', 'actividades', 'imprevistos'];

    var total = leerMonto(raiz.dataset.total);
    var moneda = raiz.dataset.moneda || '';
    var dias = parseInt(raiz.dataset.dias, 10) || 1;

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

    function pct(cat) {
        var v = parseFloat(raiz.dataset[cat]);
        return isNaN(v) ? 0 : v;
    }

    document.getElementById('vjTotal').textContent = formatear(total);
    document.getElementById('vjPorDia').textContent = formatear(total / dias);

    CATEGORIAS.forEach(function (cat) {
        var p = pct(cat);
        var dinero = total * p / 100;

        document.querySelectorAll('[data-seg="' + cat + '"]').forEach(function (s) { s.style.width = p + '%'; });
        document.querySelectorAll('[data-pct="' + cat + '"]').forEach(function (s) { s.textContent = Math.round(p) + '%'; });
        document.querySelectorAll('[data-monto="' + cat + '"]').forEach(function (s) { s.textContent = formatear(dinero); });
        document.querySelectorAll('[data-por-dia="' + cat + '"]').forEach(function (s) { s.textContent = formatear(dinero / dias); });
    });

    /* Cuenta regresiva hasta la fecha de salida (formato AAAA-MM-DD) */
    var inicio = raiz.dataset.inicio;
    var cuenta = document.getElementById('vjCuenta');
    if (inicio && /^\d{4}-\d{2}-\d{2}$/.test(inicio)) {
        var partes = inicio.split('-');
        var salida = new Date(+partes[0], +partes[1] - 1, +partes[2]);
        var hoy = new Date();
        hoy.setHours(0, 0, 0, 0);
        var faltan = Math.round((salida - hoy) / 86400000);

        if (faltan > 1) cuenta.textContent = 'Faltan ' + faltan + ' días';
        else if (faltan === 1) cuenta.textContent = 'Sales mañana';
        else if (faltan === 0) cuenta.textContent = 'Sales hoy';
        else cuenta.textContent = 'Buen viaje';
    }

    /* ===== Secciones por separado (menú lateral) ===== */

    var secciones = raiz.querySelectorAll('.vj-seccion[id]');
    var enlacesMenu = document.querySelectorAll('.vj-menu-lateral a[href^="#"]');

    // muestra solo la sección pedida; si no existe, el resumen general
    function mostrarSeccion(id) {
        var existe = Array.prototype.some.call(secciones, function (s) { return s.id === id; });
        if (!existe) id = 'resumen';

        secciones.forEach(function (s) { s.hidden = s.id !== id; });
        enlacesMenu.forEach(function (a) {
            var activo = a.getAttribute('href') === '#' + id;
            a.classList.toggle('vj-menu-activo', activo);
            if (activo) a.setAttribute('aria-current', 'page');
            else a.removeAttribute('aria-current');
        });
        window.scrollTo(0, 0);
    }

    // cualquier enlace a "#seccion" (menú, tarjetas del resumen, botones) cambia la URL
    window.addEventListener('hashchange', function () {
        mostrarSeccion(location.hash.slice(1));
    });
    mostrarSeccion(location.hash.slice(1));

    /* ===== Editar presupuesto: primero el aviso ===== */

    var dialogo = document.getElementById('vjDialogoPresupuesto');
    document.getElementById('vjEditarPresupuesto').addEventListener('click', function () {
        if (typeof dialogo.showModal === 'function') {
            dialogo.showModal();
        } else {
            // navegador sin <dialog>: va directo a editar
            location.href = dialogo.querySelector('a').href;
        }
    });
})();
