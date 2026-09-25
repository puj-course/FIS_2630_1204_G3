<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pasoActual" value="1"/>
<c:set var="pasos" value="${['Origen','Preferencias','Fechas','Presupuesto','Destinos']}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>¿De dónde sales? | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>

<jsp:include page="fragmentos/encabezado.jsp"/>

<main class="pantalla pantalla-doble">

    <section>
        <span class="rotulo-paso">Paso 1 de 4</span>
        <h1 class="titulo-grande">¿De dónde<br>sales?</h1>
        <p class="bajada">
            Con tu punto de origen calculamos rutas, transporte y cuánto te
            cuesta llegar a cada destino.
        </p>

        <form action="<c:url value='/origen'/>" method="post">

            <div class="fila">
                <div>
                    <label>País de origen</label>
                    <select name="pais" onchange="this.form.submit()">
                        <option value="">Selecciona un país</option>
                        <c:forEach var="p" items="${paises}">
                            <option value="${p}" ${ubicacion.pais == p ? 'selected' : ''}>${p}</option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty errores.pais}">
                        <span class="error">${errores.pais}</span>
                    </c:if>
                </div>
                <div>
                    <label>Ciudad de origen</label>
                    <select name="ciudad">
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
                            <span class="error">${errores.ciudad}</span>
                        </c:when>
                        <c:when test="${not empty ubicacion.ciudad}">
                            <span class="confirmado">✓ Ciudad seleccionada</span>
                        </c:when>
                    </c:choose>
                </div>
            </div>

            <label>Punto de partida <span class="opcional">(opcional)</span></label>
            <div class="campo-con-icono">
                <svg viewBox="0 0 20 20" fill="none">
                    <path d="M10 18s6-5.2 6-9.4A6 6 0 0 0 4 8.6C4 12.8 10 18 10 18z"
                          stroke="#5B3FA8" stroke-width="2"/>
                    <circle cx="10" cy="8.5" r="2.2" fill="#5B3FA8"/>
                </svg>
                <input type="text" name="detalle" id="campoDetalle" maxlength="60"
                       value="${ubicacion.detalle}" placeholder="Aeropuerto, terminal o barrio">
                <span class="contador-campo"><b id="cuenta">0</b>/60</span>
            </div>
            <c:if test="${not empty errores.detalle}">
                <span class="error">${errores.detalle}</span>
            </c:if>

            <div class="sugerencias">
                <button type="button" class="chip" data-valor="Aeropuerto principal">Aeropuerto principal</button>
                <button type="button" class="chip" data-valor="Terminal de transportes">Terminal de transportes</button>
                <button type="button" class="chip" data-valor="Centro de la ciudad">Centro de la ciudad</button>
            </div>

            <div class="acciones-pie">
                <a class="enlace-atras" href="<c:url value='/'/>">&larr; Atrás</a>
                <button type="submit">Continuar &rarr;</button>
            </div>
        </form>
    </section>

    <aside class="panel-visual">
        <span class="panel-sol"></span>
        <span class="panel-onda"></span>

        <div class="pase">
            <div class="pase-top">
                <span>WiseTrip</span>
                <span>Pase de abordar</span>
            </div>

            <div class="pase-cuerpo">
                <div class="pase-ruta">
                    <div>
                        <span class="pase-rotulo">Desde</span>
                        <span class="pase-codigo">
                            <c:choose>
                                <c:when test="${empty ubicacion.ciudad}">— — —</c:when>
                                <c:otherwise>${ubicacion.ciudad}</c:otherwise>
                            </c:choose>
                        </span>
                        <span class="pase-detalle">
                            <c:choose>
                                <c:when test="${empty ubicacion.pais}">Por definir</c:when>
                                <c:otherwise>${ubicacion.pais}</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <span class="pase-flecha">- - - &rarr;</span>
                    <div class="pase-destino">
                        <span class="pase-rotulo">Hacia</span>
                        <span class="pase-interrogante">???</span>
                        <span class="pase-detalle">Tu destino ideal</span>
                    </div>
                </div>

                <div class="pase-perforado"></div>

                <div class="pase-datos">
                    <div>
                        <span class="pase-rotulo">Pasajera</span>
                        <strong>${usuario.nombreCompleto}</strong>
                    </div>
                    <div>
                        <span class="pase-rotulo">Salida</span>
                        <strong>
                            <c:choose>
                                <c:when test="${empty ubicacion.detalle}">Por definir</c:when>
                                <c:otherwise>${ubicacion.detalle}</c:otherwise>
                            </c:choose>
                        </strong>
                    </div>
                    <div>
                        <span class="pase-rotulo">Fechas</span>
                        <strong>Por definir</strong>
                    </div>
                    <div>
                        <span class="pase-rotulo">Presupuesto</span>
                        <strong>Por definir</strong>
                    </div>
                </div>
            </div>

            <div class="pase-abajo">
                <span class="pase-barras"></span>
                <span>Paso 1 / 4</span>
            </div>
        </div>
    </aside>

</main>

<script>
    const campo = document.getElementById('campoDetalle');
    const cuenta = document.getElementById('cuenta');

    function actualizar() { cuenta.textContent = campo.value.length; }
    campo.addEventListener('input', actualizar);
    actualizar();

    document.querySelectorAll('.chip').forEach(c => {
        c.addEventListener('click', () => {
            campo.value = c.dataset.valor;
            actualizar();
            campo.focus();
        });
    });
</script>
</body>
</html>