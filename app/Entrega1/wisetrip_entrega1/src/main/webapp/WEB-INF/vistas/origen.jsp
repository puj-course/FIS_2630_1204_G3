<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>¿De dónde sales? | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/origen.css'/>">
</head>
<body class="og">

<header class="og-cabecera">
    <a class="og-marca" href="<c:url value='/'/>">
        <img src="/img/portada/logo.png" alt="">
        <span>Wise<em>Trip</em></span>
    </a>

    <nav class="og-pasos">
        <span class="og-paso og-paso-activo"><span class="og-paso-num">1</span> Origen</span>
        <span class="og-linea"></span>
        <span class="og-paso">2 Preferencias</span>
        <span class="og-linea"></span>
        <span class="og-paso">3 Fechas</span>
        <span class="og-linea"></span>
        <span class="og-paso">4 Presupuesto</span>
        <span class="og-linea"></span>
        <span class="og-paso">5 Destinos</span>
    </nav>

    <div class="og-usuario">
        <span class="og-avatar">${inicialesUsuario}</span>
        <div>
            <strong>${usuario.nombreCompleto}</strong>
            <a href="<c:url value='/logout'/>">Cerrar sesión</a>
        </div>
    </div>
</header>

<main class="og-pantalla">

    <section>
        <span class="og-rotulo">Paso 1 de 4</span>
        <h1 class="og-titulo">¿De dónde<br>sales?</h1>
        <p class="og-bajada">
            Con tu punto de origen calculamos rutas, transporte y cuánto te
            cuesta llegar a cada destino.
        </p>

        <form action="<c:url value='/origen'/>" method="post">

            <div class="og-fila">
                <div>
                    <label for="pais">País de origen</label>
                    <select id="pais" name="pais" onchange="this.form.submit()">
                        <option value="">Selecciona un país</option>
                        <c:forEach var="p" items="${paises}">
                            <option value="${p}" ${ubicacion.pais == p ? 'selected' : ''}>${p}</option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty errores.pais}">
                        <span class="og-error">${errores.pais}</span>
                    </c:if>
                </div>
                <div>
                    <label for="ciudad">Ciudad de origen</label>
                    <select id="ciudad" name="ciudad">
                        <option value="">
                            <c:choose>
                                <c:when test="${empty ciudades}">Primero elige un país</c:when>
                                <c:otherwise>Selecciona una ciudad</c:otherwise>
                            </c:choose>
                        </option>
                        <c:forEach var="ciu" items="${ciudades}">
                            <option value="${ciu}" ${ubicacion.ciudad == ciu ? 'selected' : ''}>${ciu}</option>
                        </c:forEach>
                    </select>
                    <c:choose>
                        <c:when test="${not empty errores.ciudad}">
                            <span class="og-error">${errores.ciudad}</span>
                        </c:when>
                        <c:when test="${not empty ubicacion.ciudad}">
                            <span class="og-confirmado">✓ Ciudad seleccionada</span>
                        </c:when>
                    </c:choose>
                </div>
            </div>

            <label for="detalle">Punto de partida <span class="og-opcional">(opcional)</span></label>
            <div class="og-campo">
                <svg viewBox="0 0 20 20" fill="none">
                    <path d="M10 18s6-5.2 6-9.4A6 6 0 0 0 4 8.6C4 12.8 10 18 10 18z"
                          stroke="#5B3FA8" stroke-width="2"/>
                    <circle cx="10" cy="8.5" r="2.2" fill="#5B3FA8"/>
                </svg>
                <input id="detalle" type="text" name="detalle" maxlength="60"
                       value="${ubicacion.detalle}" placeholder="Aeropuerto, terminal o barrio">
                <span class="og-contador"><b id="cuenta">0</b>/60</span>
            </div>
            <c:if test="${not empty errores.detalle}">
                <span class="og-error">${errores.detalle}</span>
            </c:if>

            <div class="og-chips">
                <button type="button" class="og-chip" data-valor="Aeropuerto principal">Aeropuerto principal</button>
                <button type="button" class="og-chip" data-valor="Terminal de transportes">Terminal de transportes</button>
                <button type="button" class="og-chip" data-valor="Centro de la ciudad">Centro de la ciudad</button>
            </div>

            <div class="og-acciones">
                <a class="og-atras" href="<c:url value='/'/>">&larr; Atrás</a>
                <button type="submit" class="og-continuar">Continuar &rarr;</button>
            </div>
        </form>
    </section>

    <aside class="og-panel">
        <span class="og-sol"></span>
        <span class="og-onda"></span>

        <div class="og-pase">
            <div class="og-pase-top">
                <span>WiseTrip</span>
                <span>Pase de abordar</span>
            </div>

            <div class="og-pase-cuerpo">
                <div class="og-ruta">
                    <div>
                        <span class="og-pase-rotulo">Desde</span>
                        <span class="og-pase-codigo">
                            <c:choose>
                                <c:when test="${empty ubicacion.ciudad}">— — —</c:when>
                                <c:otherwise>${ubicacion.ciudad}</c:otherwise>
                            </c:choose>
                        </span>
                        <span class="og-pase-detalle">
                            <c:choose>
                                <c:when test="${empty ubicacion.pais}">Por definir</c:when>
                                <c:otherwise>${ubicacion.pais}</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <span class="og-flecha">- - - &rarr;</span>
                    <div class="og-ruta-destino">
                        <span class="og-pase-rotulo">Hacia</span>
                        <span class="og-pase-interrogante">???</span>
                        <span class="og-pase-detalle">Tu destino ideal</span>
                    </div>
                </div>

                <div class="og-perforado"></div>

                <div class="og-pase-datos">
                    <div>
                        <span class="og-pase-rotulo">Pasajera</span>
                        <strong>${usuario.nombreCompleto}</strong>
                    </div>
                    <div>
                        <span class="og-pase-rotulo">Salida</span>
                        <strong>
                            <c:choose>
                                <c:when test="${empty ubicacion.detalle}">Por definir</c:when>
                                <c:otherwise>${ubicacion.detalle}</c:otherwise>
                            </c:choose>
                        </strong>
                    </div>
                    <div>
                        <span class="og-pase-rotulo">Fechas</span>
                        <strong>Por definir</strong>
                    </div>
                    <div>
                        <span class="og-pase-rotulo">Presupuesto</span>
                        <strong>Por definir</strong>
                    </div>
                </div>
            </div>

            <div class="og-pase-abajo">
                <span class="og-barras"></span>
                <span>Paso 1 / 4</span>
            </div>
        </div>
    </aside>

</main>

<script>
    const campo = document.getElementById('detalle');
    const cuenta = document.getElementById('cuenta');

    function actualizar() { cuenta.textContent = campo.value.length; }
    campo.addEventListener('input', actualizar);
    actualizar();

    document.querySelectorAll('.og-chip').forEach(c => {
        c.addEventListener('click', () => {
            campo.value = c.dataset.valor;
            actualizar();
            campo.focus();
        });
    });
</script>
</body>
</html>