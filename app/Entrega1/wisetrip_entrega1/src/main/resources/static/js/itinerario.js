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
    // y recibe su botón ×, que el CSS solo muestra dentro del calendario
    function prepararTarjeta(tarjeta) {
        tarjeta.dataset.origen = tarjeta.parentElement.id;

        var quitar = document.createElement('button');
        quitar.type = 'button';
        quitar.className = 'it-quitar';
        quitar.setAttribute('aria-label', 'Quitar del calendario');
        quitar.textContent = '×';
        tarjeta.appendChild(quitar);
    }

    raiz.querySelectorAll('.it-actividad').forEach(prepararTarjeta);

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

    // suma el costo (USD) de las actividades que ya están en el calendario;
    // con categoría, solo las de esa categoría
    function gastadoEnCalendario(categoria) {
        var suma = 0;
        raiz.querySelectorAll('.it-cal-celda .it-actividad').forEach(function (t) {
            if (!categoria || t.dataset.categoria === categoria) {
                suma += parseFloat(t.dataset.costo) || 0;
            }
        });
        return suma;
    }

    function enUsd(valor) {
        return 'US$ ' + valor.toLocaleString('en-US');
    }

    function actualizarPresupuesto() {
        var gastado = gastadoEnCalendario();
        var cerca = totalUsd > 0 && gastado >= totalUsd * LIMITE_ALERTA;

        document.getElementById('itGastado').textContent = enUsd(gastado);
        document.getElementById('itAlertaRoja').hidden = !cerca;
        document.getElementById('itAlertaVerde').hidden = cerca;
    }

    function actualizarGastosPorCategoria() {
        raiz.querySelectorAll('[data-gasto]').forEach(function (celda) {
            celda.textContent = enUsd(gastadoEnCalendario(celda.dataset.gasto));
        });
    }

    /* ===== Próxima actividad ===== */

    // fecha "2026-11-21" + hora "14" -> Date local
    function momentoDe(tarjeta) {
        var p = tarjeta.dataset.fecha.split('-');
        return new Date(+p[0], +p[1] - 1, +p[2], +tarjeta.dataset.hora);
    }

    // 14 -> "2:00 p. m."
    function horaLegible(hora) {
        var h = +hora;
        return (h % 12 || 12) + ':00 ' + (h < 12 ? 'a. m.' : 'p. m.');
    }

    function actualizarProxima() {
        var ahora = new Date();
        var proxima = null;

        raiz.querySelectorAll('.it-cal-celda .it-actividad').forEach(function (t) {
            var momento = momentoDe(t);
            if (momento >= ahora && (!proxima || momento < momentoDe(proxima))) {
                proxima = t;
            }
        });

        document.getElementById('itProxima').hidden = !proxima;
        document.getElementById('itProximaVacia').hidden = !!proxima;
        if (!proxima) return;

        var dia = raiz.querySelector('.it-cal-dia[data-fecha="' + proxima.dataset.fecha + '"]');
        document.getElementById('itProximaNombre').textContent =
            proxima.querySelector('strong').textContent;
        document.getElementById('itProximaCuando').textContent =
            dia.querySelector('span').textContent + ' ' + dia.querySelector('strong').textContent +
            ' · ' + horaLegible(proxima.dataset.hora);

        // reutiliza la foto de la actividad si la tiene; si no, queda la plantilla
        var foto = document.getElementById('itProximaFoto');
        var img = proxima.querySelector('.it-foto img');
        foto.innerHTML = '';
        if (img) foto.appendChild(img.cloneNode());
    }

    // todo lo que depende de dónde están las actividades
    function refrescar() {
        actualizarDiasVacios();
        actualizarPresupuesto();
        actualizarGastosPorCategoria();
        actualizarProxima();
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

    raiz.addEventListener('click', function (e) {
        var quitar = e.target.closest('.it-quitar');
        if (quitar) devolverAlPanel(quitar.closest('.it-actividad'));
    });

    /* ===== Actividad propia ===== */

    var formNueva = document.getElementById('itFormNueva');
    var contadorPropias = 0;

    // crea una tarjeta como las de plantilla y la pone en el panel de actividades
    function crearActividad(nombre) {
        var tarjeta = document.createElement('li');
        tarjeta.className = 'it-actividad vj-actividades';
        tarjeta.draggable = true;
        tarjeta.dataset.categoria = 'actividades';
        tarjeta.dataset.id = 'propia-' + Date.now() + '-' + (++contadorPropias);
        tarjeta.dataset.costo = '0';

        var titulo = document.createElement('strong');
        titulo.textContent = nombre;
        var detalle = document.createElement('span');
        detalle.textContent = 'Propia · 2 h';
        tarjeta.append(titulo, detalle);

        document.getElementById('itListaActividades').appendChild(tarjeta);
        prepararTarjeta(tarjeta);
        return tarjeta;
    }

    formNueva.addEventListener('submit', function (e) {
        e.preventDefault();
        var campo = formNueva.elements.nombre;
        var nombre = campo.value.trim();
        if (!nombre) {
            campo.focus();
            return;
        }
        crearActividad(nombre);
        campo.value = '';
        document.getElementById('itNuevaAviso').textContent =
            '"' + nombre + '" quedó en tu panel de actividades. Arrástrala al calendario.';
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
