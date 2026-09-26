<%-- Presupuesto y moneda.
     Vista donde el usuario elige la moneda (local o USD si ya hay destino)
     e ingresa el monto total del viaje. Muestra la duración del viaje como
     referencia y los errores de validación si los hay. Al lado del selector
     de moneda se muestra la bandera del país correspondiente (HU-55).
     Es el último paso del flujo (Origen > Preferencias > Fechas > Presupuesto).

     Rediseño: hoja propia presupuesto.css con prefijo pp-.
     HU-55 intacta: select id="moneda", img id="bandera-moneda" y la función
     actualizarBanderaMoneda() no cambian. --%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Presupuesto | WiseTrip</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Archivo+Black&family=IBM+Plex+Mono:wght@400;500;600&family=Instrument+Sans:wght@400;500;600;700&display=swap">
    <link rel="stylesheet" href="/css/presupuesto.css">
</head>
<body class="pp">
<div class="pp-pagina">

    <header class="pp-barra">
        <a class="pp-logo" href="/"><img src="/img/portada/logo.png" alt="WiseTrip"></a>
        <div class="pp-usuario">
            <span class="pp-avatar" aria-hidden="true">${inicialesUsuario}</span>
            <span class="pp-hola">Hola, <strong>${usuario.nombreCompleto}</strong></span>
            <a class="pp-salir" href="<c:url value='/logout'/>">Cerrar sesión</a>
        </div>
    </header>

    <%-- HU-37: Indicador visual de en qué paso del flujo está el viajero. --%>
    <nav class="pp-pasos" aria-label="Pasos de la planificación">
        <span class="pp-paso pp-paso-hecho">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5 12.5l4.5 4.5L19 7.5"/></svg>
            Origen
        </span>
        <span class="pp-paso pp-paso-hecho">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5 12.5l4.5 4.5L19 7.5"/></svg>
            Preferencias
        </span>
        <span class="pp-paso pp-paso-hecho">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5 12.5l4.5 4.5L19 7.5"/></svg>
            Fechas
        </span>
        <span class="pp-paso pp-paso-activo" aria-current="step">Presupuesto</span>
    </nav>

    <main class="pp-grid">

        <section class="pp-columna">
            <p class="pp-eyebrow">Paso 4 de 4</p>
            <h1 class="pp-titulo">¿Cuánto<br>puedes gastar?</h1>
            <p class="pp-sub">
                <c:choose>
                    <c:when test="${not empty paisDestino}">
                        Como tu destino es ${paisDestino}, puedes usar la moneda local o dólares.
                    </c:when>
                    <c:otherwise>
                        Indica el total disponible para todo el viaje. Lo repartiremos entre
                        hospedaje, comida, transporte y actividades.
                    </c:otherwise>
                </c:choose>
            </p>

            <form action="<c:url value='/presupuesto'/>" method="post" id="formPresupuesto">

                <div class="pp-campo">
                    <label for="moneda" class="pp-etiqueta">Moneda</label>
                    <div class="campo-moneda pp-moneda">
                        <div class="pp-select">
                            <select name="moneda" id="moneda" onchange="actualizarBanderaMoneda()">
                                <option value="">Selecciona una moneda</option>
                                <c:forEach var="m" items="${monedas}">
                                    <option value="${m.key}" ${presupuesto.moneda == m.key ? 'selected' : ''}>${m.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <%-- HU-55: aquí se muestra la bandera de la moneda escogida --%>
                        <span class="pp-bandera-marco" id="ppBanderaMarco">
                            <svg class="pp-bandera-vacia" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5 21V4M5 4h11l-2 4 2 4H5"/></svg>
                            <img id="bandera-moneda" class="bandera-moneda" src="" alt="">
                        </span>
                    </div>
                    <c:if test="${not empty errores.moneda}">
                        <p class="pp-error">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" aria-hidden="true"><circle cx="12" cy="12" r="9"/><path d="M12 7.5v5.5M12 16.5v.01"/></svg>
                            ${errores.moneda}
                        </p>
                    </c:if>
                </div>

                <div class="pp-campo">
                    <label for="monto" class="pp-etiqueta">Presupuesto total</label>
                    <div class="pp-monto ${not empty errores.monto ? 'pp-monto-error' : ''}">
                        <span class="pp-prefijo" id="ppPrefijo">---</span>
                        <input type="text" name="monto" id="monto" value="${presupuesto.monto}"
                               placeholder="Ej: 2500000" inputmode="decimal" autocomplete="off">
                    </div>
                    <%-- HU-39: Si ya hay un monto guardado en sesión, se precarga aquí. --%>
                    <c:if test="${not empty errores.monto}">
                        <p class="pp-error">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" aria-hidden="true"><circle cx="12" cy="12" r="9"/><path d="M12 7.5v5.5M12 16.5v.01"/></svg>
                            ${errores.monto}
                        </p>
                    </c:if>

                    <p class="pp-nota">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" aria-hidden="true"><circle cx="12" cy="12" r="9"/><path d="M12 11v5.5M12 7.5v.01"/></svg>
                        <span>
                            Escribe solo el número, sin el símbolo de la moneda.
                            <c:if test="${not empty fechas and fechas.duracionDias > 0}">
                                Es el total para los ${fechas.duracionDias} días del viaje.
                            </c:if>
                        </span>
                    </p>
                </div>

                <div class="pp-acciones">
                    <%-- HU-37: Botón "Atrás" del flujo. --%>
                    <a class="pp-volver" href="<c:url value='/fechas'/>">&larr; Volver a las fechas</a>
                    <button type="submit" class="pp-boton">
                        Ver mi resumen
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.6" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5 12h14M13 6l6 6-6 6"/></svg>
                    </button>
                </div>
            </form>
        </section>

        <%-- Panel ilustrado: recibo que se llena mientras el usuario escribe. --%>
        <aside class="pp-panel" aria-hidden="true"
               data-dias="${not empty fechas ? fechas.duracionDias : 0}">
            <span class="pp-sol"></span>
            <span class="pp-moneda-dibujo pp-moneda-1">$</span>
            <span class="pp-moneda-dibujo pp-moneda-2">$</span>

            <div class="pp-recibo-envoltura">
                <div class="pp-recibo-papel">
                    <div class="pp-recibo">
                        <div class="pp-recibo-cabeza">
                            <span>WISETRIP</span>
                            <span>PRESUPUESTO</span>
                        </div>

                        <div class="pp-recibo-cuerpo">
                            <span class="pp-recibo-etiqueta">Total del viaje</span>
                            <div class="pp-recibo-total">
                                <span class="pp-recibo-cifra" id="ppTotal">0</span>
                                <span class="pp-recibo-codigo" id="ppCodigoTotal">---</span>
                            </div>

                            <div class="pp-recibo-lineas">
                                <div class="pp-linea">
                                    <span>Duración</span>
                                    <span class="pp-puntos"></span>
                                    <strong>
                                        <c:choose>
                                            <c:when test="${not empty fechas and fechas.duracionDias > 0}">${fechas.duracionDias} días</c:when>
                                            <c:otherwise>--</c:otherwise>
                                        </c:choose>
                                    </strong>
                                </div>
                                <div class="pp-linea">
                                    <span>Por día</span>
                                    <span class="pp-puntos"></span>
                                    <strong id="ppPorDia">--</strong>
                                </div>
                                <div class="pp-linea">
                                    <span>En dólares (aprox.)</span>
                                    <span class="pp-puntos"></span>
                                    <strong id="ppUsd">--</strong>
                                </div>
                            </div>
                        </div>
                    </div>

                    <svg class="pp-recibo-borde" viewBox="0 0 400 18" preserveAspectRatio="none">
                        <polygon fill="#FFFFFF" points="1.5,0 1.5,6 11.5,16 21.5,6 31.5,16 41.5,6 51.5,16 61.5,6 71.5,16 81.5,6 91.5,16 101.5,6 111.5,16 121.5,6 131.5,16 141.5,6 151.5,16 161.5,6 171.5,16 181.5,6 191.5,16 201.5,6 211.5,16 221.5,6 231.5,16 241.5,6 251.5,16 261.5,6 271.5,16 281.5,6 291.5,16 301.5,6 311.5,16 321.5,6 331.5,16 341.5,6 351.5,16 361.5,6 371.5,16 381.5,6 391.5,16 398.5,6 398.5,0"/>
                        <polyline fill="none" stroke="#16161D" stroke-width="3" stroke-linejoin="round" vector-effect="non-scaling-stroke"
                                  points="1.5,0 1.5,6 11.5,16 21.5,6 31.5,16 41.5,6 51.5,16 61.5,6 71.5,16 81.5,6 91.5,16 101.5,6 111.5,16 121.5,6 131.5,16 141.5,6 151.5,16 161.5,6 171.5,16 181.5,6 191.5,16 201.5,6 211.5,16 221.5,6 231.5,16 241.5,6 251.5,16 261.5,6 271.5,16 281.5,6 291.5,16 301.5,6 311.5,16 321.5,6 331.5,16 341.5,6 351.5,16 361.5,6 371.5,16 381.5,6 391.5,16 398.5,6 398.5,0"/>
                    </svg>
                </div>

                <span class="pp-sello" id="ppSello">Falta el monto</span>
            </div>

            <svg class="pp-colinas" viewBox="0 0 600 140" preserveAspectRatio="none">
                <path d="M0 80 C 110 40, 200 50, 300 85 S 470 40, 600 75 L600 140 L0 140 Z"
                      fill="#F3A6C0" stroke="#16161D" stroke-width="3" vector-effect="non-scaling-stroke"/>
                <path d="M0 112 C 130 85, 240 100, 350 112 S 510 88, 600 108 L600 140 L0 140 Z"
                      fill="#7BC97F" stroke="#16161D" stroke-width="3" vector-effect="non-scaling-stroke"/>
            </svg>
        </aside>

    </main>
</div>

<%-- ===== HU-55: bandera de la moneda. Sin cambios. ===== --%>
<script>
    // HU-55: cada vez que el usuario cambia la moneda en el select,
    // esta función busca el archivo de bandera correspondiente y lo muestra.
    function actualizarBanderaMoneda() {
        // 1. Tomamos el select y la imagen de la bandera por su "id"
        var select = document.getElementById("moneda");
        var bandera = document.getElementById("bandera-moneda");

        // 2. El "value" del option elegido es el código de moneda (ej: "COP")
        var codigoMoneda = select.value;

        if (codigoMoneda === "") {
            // No hay moneda seleccionada todavía: ocultamos la bandera
            bandera.style.display = "none";
        } else {
            // 3. Como guardamos las banderas con el mismo nombre que el código
            //    de moneda (ej: COP.svg, MXN.svg...), armamos la ruta así:
            bandera.src = "<c:url value='/img/banderas/'/>" + codigoMoneda + ".svg";
            bandera.style.display = "inline-block";
        }
    }

    // 4. Si al cargar la página ya viene una moneda precargada (ej. HU-39),
    //    mostramos su bandera de una vez, sin esperar a que el usuario la cambie.
    document.addEventListener("DOMContentLoaded", actualizarBanderaMoneda);
</script>

<%-- ===== Rediseño: vista previa del recibo =====
     Script aparte. No modifica la función de la bandera: solo escucha
     los mismos campos y pinta el panel de la derecha. --%>
<script>
    // Unidades de cada moneda por 1 USD (vienen de PresupuestoServicio)
    const TASAS_USD = {
        <c:forEach var="t" items="${tasasUsd}" varStatus="st">"${t.key}": ${t.value}<c:if test="${not st.last}">,</c:if></c:forEach>
    };

    (function () {
        const select = document.getElementById('moneda');
        const monto = document.getElementById('monto');
        const bandera = document.getElementById('bandera-moneda');
        const marco = document.getElementById('ppBanderaMarco');
        const panel = document.querySelector('.pp-panel');
        const dias = Number(panel.dataset.dias) || 0;
        const formato = new Intl.NumberFormat('es-CO', { maximumFractionDigits: 0 });

        // Si el archivo de la bandera no existe, se oculta el ícono roto
        // y queda visible el dibujo de bandera del marco.
        bandera.addEventListener('error', function () {
            if (!bandera.getAttribute('src')) return;
            bandera.style.display = 'none';
        });

        // Mismo criterio que Presupuesto.getMontoNumerico():
        // el punto separa miles y la coma separa decimales.
        function leerMonto(texto) {
            if (!texto) return -1;
            const limpio = texto.trim().replace(/\./g, '').replace(/,/g, '.').replace(/ /g, '');
            if (limpio === '') return -1;
            const numero = Number(limpio);
            return isNaN(numero) ? -1 : numero;
        }

        function actualizar() {
            const codigo = select.value;
            const valor = leerMonto(monto.value);
            const sello = document.getElementById('ppSello');

            document.getElementById('ppPrefijo').textContent = codigo || '---';
            document.getElementById('ppCodigoTotal').textContent = codigo || '---';

            if (valor > 0) {
                document.getElementById('ppTotal').textContent = formato.format(valor);
                document.getElementById('ppPorDia').textContent =
                    dias > 0 ? formato.format(valor / dias) + (codigo ? ' ' + codigo : '') : '--';
                document.getElementById('ppUsd').textContent =
                    codigo && TASAS_USD[codigo] ? 'USD ' + formato.format(valor / TASAS_USD[codigo]) : '--';
            } else {
                document.getElementById('ppTotal').textContent = '0';
                document.getElementById('ppPorDia').textContent = '--';
                document.getElementById('ppUsd').textContent = '--';
            }

            if (!codigo) {
                sello.textContent = 'Falta la moneda';
                sello.classList.remove('pp-sello-listo');
            } else if (valor <= 0) {
                sello.textContent = 'Falta el monto';
                sello.classList.remove('pp-sello-listo');
            } else {
                sello.textContent = 'Listo';
                sello.classList.add('pp-sello-listo');
            }
        }

        select.addEventListener('change', actualizar);
        monto.addEventListener('input', actualizar);
        actualizar();
    })();
</script>
</body>
</html>