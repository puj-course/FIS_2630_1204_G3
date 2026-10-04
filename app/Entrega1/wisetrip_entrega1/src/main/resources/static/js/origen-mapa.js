/*
 * WiseTrip · Origen
 * Mapa de vuelo del panel derecho. Solo visual: lee los select de país y
 * ciudad, no cambia nada del formulario ni de lo que se envía al servidor.
 * Proyección: x = (lon + 118) * 6, y = (33 - lat) * 6 (la misma con la que
 * se dibujó el mapa del JSP, viewBox 516 x 534).
 */
(function () {
    var mapa = document.getElementById('ogMapa');
    if (!mapa) return;

    var CIUDADES = [
        ['Ciudad de México', 'MEX', 19.43, -99.13], ['Cancún', 'MEX', 21.16, -86.85],
        ['Guadalajara', 'MEX', 20.66, -103.34], ['Ciudad de Guatemala', 'GUA', 14.63, -90.51],
        ['Flores', 'GUA', 16.91, -89.89], ['Tegucigalpa', 'HON', 14.07, -87.22],
        ['Roatán', 'HON', 16.34, -86.52], ['San Salvador', 'SLV', 13.69, -89.18],
        ['Santa Ana', 'SLV', 13.99, -89.56], ['Managua', 'NIC', 12.13, -86.24],
        ['Granada', 'NIC', 11.93, -85.96], ['San José', 'CRI', 9.93, -84.08],
        ['La Fortuna', 'CRI', 10.46, -84.64], ['Ciudad de Panamá', 'PAN', 8.98, -79.53],
        ['Bocas del Toro', 'PAN', 9.34, -82.24], ['Belize City', 'BLZ', 17.50, -88.20],
        ['Bogotá', 'COL', 4.71, -74.07], ['Medellín', 'COL', 6.25, -75.57],
        ['Cartagena de Indias', 'COL', 10.42, -75.54], ['Caracas', 'VEN', 10.49, -66.88],
        ['Porlamar / Isla de Margarita', 'VEN', 10.96, -63.91], ['La Habana', 'CUB', 23.13, -82.38],
        ['Santiago de Cuba', 'CUB', 20.02, -76.83], ['Santo Domingo', 'RDO', 18.47, -69.93],
        ['Punta Cana', 'RDO', 18.46, -68.93], ['Quito', 'ECU', -0.18, -78.47],
        ['Guayaquil', 'ECU', -2.17, -79.88], ['Lima', 'PER', -12.05, -77.04],
        ['Cusco', 'PER', -13.53, -71.97], ['La Paz', 'BOL', -16.50, -68.13],
        ['Santa Cruz de la Sierra', 'BOL', -17.78, -63.18], ['Río de Janeiro', 'BRA', -22.91, -43.17],
        ['São Paulo', 'BRA', -23.55, -46.63], ['Brasilia', 'BRA', -15.80, -47.88],
        ['Santiago de Chile', 'CHI', -33.45, -70.65], ['San Pedro de Atacama', 'CHI', -22.91, -68.20],
        ['Chillán / Nevados de Chillán', 'CHI', -36.61, -72.10], ['Buenos Aires', 'ARG', -34.60, -58.38],
        ['Mendoza', 'ARG', -32.89, -68.85], ['San Carlos de Bariloche', 'ARG', -41.13, -71.31],
        ['Montevideo', 'URU', -34.90, -56.18], ['Punta del Este', 'URU', -34.95, -54.95],
        ['Asunción', 'PAR', -25.26, -57.58], ['Ciudad del Este', 'PAR', -25.51, -54.61]
    ];

    var PAISES = {
        'México': 'MEX', 'Guatemala': 'GUA', 'Honduras': 'HON', 'El Salvador': 'SLV',
        'Nicaragua': 'NIC', 'Costa Rica': 'CRI', 'Panamá': 'PAN', 'Belice': 'BLZ',
        'Colombia': 'COL', 'Venezuela': 'VEN', 'Cuba': 'CUB', 'República Dominicana': 'RDO',
        'Ecuador': 'ECU', 'Perú': 'PER', 'Bolivia': 'BOL', 'Brasil': 'BRA', 'Chile': 'CHI',
        'Argentina': 'ARG', 'Uruguay': 'URU', 'Paraguay': 'PAR'
    };

    var NS = 'http://www.w3.org/2000/svg';
    var ANCHO = 516, ALTO = 534;
    var quieto = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    var selPais = document.getElementById('pais');
    var selCiudad = document.getElementById('ciudad');
    var capaPuntos = document.getElementById('ogPuntos');
    var arco = document.getElementById('ogArco');
    var avion = document.getElementById('ogAvion');
    var pinOrigen = document.getElementById('ogPinOrigen');
    var pinDestino = document.getElementById('ogPinDestino');
    var etqOrigen = document.getElementById('ogEtqOrigen');
    var etqDestino = document.getElementById('ogEtqDestino');
    var estado = document.getElementById('ogEstado');
    var datoOrigenRotulo = document.getElementById('ogDatoOrigenRotulo');
    var datoOrigen = document.getElementById('ogDatoOrigen');
    var datoCoord = document.getElementById('ogDatoCoord');
    var datoDestino = document.getElementById('ogDatoDestino');
    var datoDestinoPais = document.getElementById('ogDatoDestinoPais');
    var datoDistancia = document.getElementById('ogDatoDistancia');

    function proyectar(c) { return [(c[3] + 118) * 6, (33 - c[2]) * 6]; }
    function nombreCorto(c) { return c[0].split(' / ')[0]; }

    function distanciaKm(a, b) {
        var r = Math.PI / 180, R = 6371;
        var dLat = (b[2] - a[2]) * r, dLon = (b[3] - a[3]) * r;
        var h = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(a[2] * r) * Math.cos(b[2] * r) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return Math.round(2 * R * Math.asin(Math.sqrt(h)));
    }

    function coordenadas(c) {
        var lat = Math.abs(c[2]).toFixed(2) + '° ' + (c[2] >= 0 ? 'N' : 'S');
        var lon = Math.abs(c[3]).toFixed(2) + '° ' + (c[3] >= 0 ? 'E' : 'O');
        return lat + '  ' + lon;
    }

    function azar(lista) { return lista[Math.floor(Math.random() * lista.length)]; }

    function ponerEtiqueta(el, punto, texto) {
        el.textContent = texto;
        el.style.left = (punto[0] / ANCHO * 100) + '%';
        el.style.top = (punto[1] / ALTO * 100) + '%';
    }

    /* Puntos de las 44 ciudades */
    CIUDADES.forEach(function (c) {
        var p = proyectar(c);
        var punto = document.createElementNS(NS, 'circle');
        punto.setAttribute('cx', p[0].toFixed(1));
        punto.setAttribute('cy', p[1].toFixed(1));
        punto.setAttribute('r', '2.6');
        punto.setAttribute('class', 'og-punto');
        punto.dataset.codigo = c[1];
        capaPuntos.appendChild(punto);
    });

    var origen = null;
    var origenReal = false;
    var temporizador = null;
    var animacion = null;

    function leerOrigen() {
        var nombreCiudad = selCiudad ? selCiudad.value : '';
        var nombrePais = selPais ? selPais.value : '';
        var codigoPais = PAISES[nombrePais] || '';

        document.querySelectorAll('.og-tierra-ruta').forEach(function (t) {
            t.classList.toggle('og-tierra-elegida', t.dataset.pais === nombrePais);
        });
        document.querySelectorAll('.og-punto').forEach(function (p) {
            p.classList.toggle('og-punto-pais', p.dataset.codigo === codigoPais);
        });

        var real = CIUDADES.filter(function (c) { return c[0] === nombreCiudad; })[0];
        if (real) {
            origen = real;
            origenReal = true;
            return;
        }
        origenReal = false;
        var delPais = CIUDADES.filter(function (c) { return c[1] === codigoPais; });
        if (delPais.length) {
            if (!origen || origen[1] !== codigoPais) origen = azar(delPais);
        } else if (!origen) {
            origen = azar(CIUDADES);
        }
    }

    function pintarOrigen() {
        var p = proyectar(origen);
        pinOrigen.setAttribute('transform', 'translate(' + p[0].toFixed(1) + ' ' + p[1].toFixed(1) + ')');
        ponerEtiqueta(etqOrigen, p, nombreCorto(origen));

        datoOrigenRotulo.textContent = origenReal ? 'Tu origen' : 'Origen de muestra';
        datoOrigen.textContent = nombreCorto(origen);
        datoCoord.textContent = coordenadas(origen);
        estado.textContent = origenReal ? 'Listo para despegar' : 'Elige tu ciudad';
        estado.classList.toggle('og-estado-listo', origenReal);
    }

    function volarA(destino) {
        var a = proyectar(origen), b = proyectar(destino);
        var mx = (a[0] + b[0]) / 2, my = (a[1] + b[1]) / 2;
        var dx = b[0] - a[0], dy = b[1] - a[1];
        var largo = Math.sqrt(dx * dx + dy * dy);
        var nx = -dy / largo, ny = dx / largo;
        if (ny > 0) { nx = -nx; ny = -ny; }
        var curva = Math.min(largo * 0.28, 90);
        var cx = mx + nx * curva, cy = my + ny * curva;

        arco.setAttribute('d', 'M' + a[0].toFixed(1) + ',' + a[1].toFixed(1) +
            ' Q' + cx.toFixed(1) + ',' + cy.toFixed(1) + ' ' + b[0].toFixed(1) + ',' + b[1].toFixed(1));
        arco.classList.add('og-visible');

        pinDestino.setAttribute('transform', 'translate(' + b[0].toFixed(1) + ' ' + b[1].toFixed(1) + ')');
        pinDestino.classList.remove('og-visible');
        etqDestino.classList.remove('og-visible');
        ponerEtiqueta(etqDestino, b, nombreCorto(destino) + ' · ' + destino[1]);

        /* Las etiquetas van en lados opuestos para no taparse */
        var destinoArriba = b[1] < a[1];
        etqOrigen.classList.toggle('og-etiqueta-abajo', destinoArriba);
        etqDestino.classList.toggle('og-etiqueta-abajo', !destinoArriba);

        datoDestino.textContent = nombreCorto(destino);
        datoDestinoPais.textContent = destino[1];
        datoDistancia.textContent = distanciaKm(origen, destino).toLocaleString('es-CO') + ' km';

        var total = arco.getTotalLength();
        var duracion = quieto ? 0 : 2200;
        var inicio = null;
        avion.classList.add('og-visible');

        function paso(t) {
            if (inicio === null) inicio = t;
            var f = duracion ? Math.min((t - inicio) / duracion, 1) : 1;
            var suave = f < .5 ? 2 * f * f : 1 - Math.pow(-2 * f + 2, 2) / 2;
            var p1 = arco.getPointAtLength(total * suave);
            var p2 = arco.getPointAtLength(Math.min(total * suave + 1, total));
            var p0 = arco.getPointAtLength(Math.max(total * suave - 1, 0));
            var angulo = Math.atan2(p2.y - p0.y, p2.x - p0.x) * 180 / Math.PI;
            avion.setAttribute('transform', 'translate(' + p1.x.toFixed(1) + ' ' + p1.y.toFixed(1) + ') rotate(' + angulo.toFixed(1) + ')');
            if (f < 1) {
                animacion = requestAnimationFrame(paso);
            } else {
                pinDestino.classList.add('og-visible');
                etqDestino.classList.add('og-visible');
            }
        }
        cancelAnimationFrame(animacion);
        animacion = requestAnimationFrame(paso);
    }

    function siguienteDestino() {
        var opciones = CIUDADES.filter(function (c) {
            return c !== origen && distanciaKm(origen, c) > 900;
        });
        volarA(azar(opciones));
    }

    function reiniciar() {
        clearInterval(temporizador);
        leerOrigen();
        pintarOrigen();
        siguienteDestino();
        temporizador = setInterval(siguienteDestino, quieto ? 7000 : 5200);
    }

    if (selCiudad) selCiudad.addEventListener('change', reiniciar);
    reiniciar();
})();