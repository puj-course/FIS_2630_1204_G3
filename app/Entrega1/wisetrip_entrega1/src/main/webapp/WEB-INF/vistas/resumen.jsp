<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Resumen de tu viaje | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/resumen.css'/>">
</head>
<body class="rs">

<header class="rs-cabecera">
    <a class="rs-marca" href="<c:url value='/'/>">
        <img src="/img/portada/logo.png" alt="">
        <span>Wise<em>Trip</em></span>
    </a>

    <nav class="rs-pasos">
        <span class="rs-paso rs-paso-hecho"><span class="rs-paso-check">✓</span> Origen</span>
        <span class="rs-linea"></span>
        <span class="rs-paso rs-paso-hecho"><span class="rs-paso-check">✓</span> Preferencias</span>
        <span class="rs-linea"></span>
        <span class="rs-paso rs-paso-hecho"><span class="rs-paso-check">✓</span> Fechas</span>
        <span class="rs-linea"></span>
        <span class="rs-paso rs-paso-hecho"><span class="rs-paso-check">✓</span> Presupuesto</span>
        <span class="rs-linea"></span>
        <span class="rs-paso">5 Destinos</span>
    </nav>

    <div class="rs-usuario">
        <span class="rs-avatar">${inicialesUsuario}</span>
        <div>
            <strong>${usuario.nombreCompleto}</strong>
            <a href="<c:url value='/logout'/>">Cerrar sesión</a>
        </div>
    </div>
</header>

<main class="rs-pantalla">

    <section>
        <span class="rs-check">✓</span>

        <h1 class="rs-titulo">Tu viaje está<br>listo para<br>calcularse</h1>

        <p class="rs-bajada">
            Revisa tu pase. Con estos datos WiseTrip busca los tres destinos
            que mejor encajan contigo y con tu presupuesto.
        </p>

        <c:if test="${not empty resumenPreferencias}">
            <div class="rs-bloque-cabeza">
                <span class="rs-rotulo">Tus preferencias</span>
                <a class="rs-editar" href="<c:url value='/preferencias'/>">Editar</a>
            </div>

            <div class="rs-pastillas">
                <c:set var="mostradas" value="0"/>
                <c:forEach var="entrada" items="${resumenPreferencias}">
                    <c:forEach var="etiqueta" items="${entrada.value}">
                        <c:if test="${mostradas < 6}">
                            <span class="rs-pastilla">${etiqueta}</span>
                            <c:set var="mostradas" value="${mostradas + 1}"/>
                        </c:if>
                    </c:forEach>
                </c:forEach>

                <c:if test="${afirmativas > 6}">
                    <span class="rs-pastilla-mas">+${afirmativas - 6} más</span>
                </c:if>
            </div>
        </c:if>

        <a class="rs-calcular" href="<c:url value='/recomendaciones'/>">
            Calcular mis destinos &rarr;
        </a>
    </section>

    <aside class="rs-zona">
        <div class="rs-pase">

            <div class="rs-pase-top">
                <span>WiseTrip · Pase de abordar</span>
                <span>${usuario.nombreCompleto}</span>
            </div>

            <div class="rs-pase-columnas">

                <div class="rs-pase-cuerpo">

                    <div class="rs-ruta">
                        <div>
                            <span class="rs-pase-rotulo">Desde</span>
                            <span class="rs-pase-codigo">${ubicacion.ciudad}</span>
                            <span class="rs-pase-detalle">
                                <c:if test="${not empty ubicacion.detalle}">${ubicacion.detalle} · </c:if>
                                ${ubicacion.pais}
                            </span>
                        </div>
                        <span class="rs-flecha">- - - &rarr;</span>
                        <div class="rs-ruta-destino">
                            <span class="rs-pase-rotulo">Hacia</span>
                            <span class="rs-pase-interrogante">???</span>
                            <span class="rs-pase-detalle">Lo calculamos ahora</span>
                        </div>
                    </div>

                    <div class="rs-perforado"></div>

                    <div class="rs-fechas">
                        <div>
                            <span class="rs-pase-rotulo">Inicio</span>
                            <strong class="rs-fecha">${fechas.fechaInicio}</strong>
                        </div>
                        <div>
                            <span class="rs-pase-rotulo">Regreso</span>
                            <strong class="rs-fecha">${fechas.fechaFin}</strong>
                        </div>
                        <div>
                            <span class="rs-pase-rotulo">Duración</span>
                            <strong class="rs-fecha">${fechas.duracionDias} días</strong>
                            <a class="rs-enlace-fino" href="<c:url value='/fechas'/>">Editar fechas</a>
                        </div>
                    </div>

                    <div class="rs-dinero">
                        <div class="rs-monto">
                            <span class="rs-pase-rotulo">Presupuesto</span>
                            <strong>${montoFormateado} ${presupuesto.moneda}</strong>
                            <span class="rs-pase-detalle">
                                ${nombreMoneda}
                                <c:if test="${not empty usdFormateado}"> · ≈ USD ${usdFormateado}</c:if>
                            </span>
                        </div>
                        <div>
                            <span class="rs-pase-rotulo">Por día</span>
                            <strong class="rs-fecha">
                                <c:choose>
                                    <c:when test="${fechas.duracionDias > 0}">
                                        <fmt:formatNumber value="${presupuesto.montoNumerico / fechas.duracionDias}"
                                                          maxFractionDigits="0"/>
                                        ${presupuesto.moneda}
                                    </c:when>
                                    <c:otherwise>—</c:otherwise>
                                </c:choose>
                            </strong>
                            <a class="rs-enlace-fino" href="<c:url value='/presupuesto'/>">Editar presupuesto</a>
                        </div>
                    </div>

                </div>

                <div class="rs-talon">
                    <span class="rs-talon-marca">WiseTrip</span>
                    <span class="rs-barras"></span>
                    <span class="rs-talon-pie">${fechas.duracionDias}D · 1 PAX</span>
                </div>

            </div>
        </div>
    </aside>

</main>

</body>
</html>