<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Destinos recomendados | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/recomendaciones.css'/>">
</head>
<body class="rc">

<header class="rc-cabecera">
    <a class="rc-marca" href="<c:url value='/'/>">
        <img src="/img/portada/logo.png" alt="">
        <span>Wise<em>Trip</em></span>
    </a>

    <nav class="rc-pasos">
        <span class="rc-paso rc-paso-hecho"><span class="rc-paso-check">✓</span> Origen</span>
        <span class="rc-linea"></span>
        <span class="rc-paso rc-paso-hecho"><span class="rc-paso-check">✓</span> Preferencias</span>
        <span class="rc-linea"></span>
        <span class="rc-paso rc-paso-hecho"><span class="rc-paso-check">✓</span> Fechas</span>
        <span class="rc-linea"></span>
        <span class="rc-paso rc-paso-hecho"><span class="rc-paso-check">✓</span> Presupuesto</span>
        <span class="rc-linea"></span>
        <span class="rc-paso rc-paso-activo"><span class="rc-paso-num">5</span> Destinos</span>
    </nav>

    <div class="rc-usuario">
        <span class="rc-avatar">${inicialesUsuario}</span>
        <div>
            <strong>${usuario.nombreCompleto}</strong>
            <a href="<c:url value='/logout'/>">Cerrar sesión</a>
        </div>
    </div>
</header>

<main class="rc-pantalla">

    <div class="rc-cabeza">
        <div>
            <span class="rc-sello">Resultado de tu búsqueda</span>
            <h1 class="rc-titulo">Tus destinos</h1>
            <p class="rc-bajada">${seleccion.mensaje}</p>
        </div>

        <div class="rc-ficha">
            <div>
                ${ubicacion.ciudad} &rarr; ???<br>
                <c:if test="${not empty fechas}">${fechas.fechaInicio} — ${fechas.fechaFin}</c:if>
                <c:if test="${not empty presupuesto}"> · ${presupuesto.moneda} ${presupuesto.monto}</c:if>
            </div>
            <a class="rc-ficha-editar" href="<c:url value='/resumen'/>">Editar búsqueda</a>
        </div>
    </div>

    <c:choose>

        <c:when test="${seleccion.vacio}">
            <div class="rc-vacio">
                <span class="rc-vacio-rotulo">Sin destinos disponibles</span>
                <h2>No encontramos coincidencias</h2>
                <p>
                    Ninguna ciudad se ajusta a tu presupuesto y tus preferencias.
                    Prueba ampliando el presupuesto o cambiando algunas respuestas.
                </p>
                <a class="rc-elegir" href="<c:url value='/presupuesto'/>">Ajustar presupuesto</a>
            </div>
        </c:when>

        <c:otherwise>
            <div class="rc-destinos">
                <c:forEach var="r" items="${seleccion.destinos}" varStatus="pos">

                    <article class="rc-destino">

                        <div class="rc-posicion">${pos.index + 1}</div>

                        <div class="rc-cuerpo">
                            <span class="rc-pais">${r.ciudad.pais}</span>
                            <span class="rc-ciudad">${r.ciudad.nombre}</span>

                            <div class="rc-barras">
                                <div>
                                    <div class="rc-barra-cabeza">
                                        <span>Presupuesto</span>
                                        <span class="rc-barra-valor">
                                            <fmt:formatNumber value="${r.puntajePresupuesto * 100}" maxFractionDigits="0"/>%
                                        </span>
                                    </div>
                                    <div class="rc-barra rc-barra-presupuesto">
                                        <span data-ancho="${r.puntajePresupuesto * 100}"></span>
                                    </div>
                                </div>
                                <div>
                                    <div class="rc-barra-cabeza">
                                        <span>Gustos</span>
                                        <span class="rc-barra-valor">
                                            <fmt:formatNumber value="${r.puntajePreferencias * 100}" maxFractionDigits="0"/>%
                                        </span>
                                    </div>
                                    <div class="rc-barra rc-barra-gustos">
                                        <span data-ancho="${r.puntajePreferencias * 100}"></span>
                                    </div>
                                </div>
                            </div>

                            <a class="rc-elegir" href="<c:url value='/destino/${r.ciudad.id}'/>">
                                Elegir este destino &rarr;
                            </a>
                        </div>

                        <div class="rc-talon">
                            <span class="rc-talon-rotulo">Coincidencia</span>
                            <span class="rc-talon-puntaje">
                                <fmt:formatNumber value="${r.puntajeTotal * 100}" maxFractionDigits="0"/>%
                            </span>
                            <c:if test="${pos.index == 0}">
                                <span class="rc-talon-nota">Mejor opción</span>
                            </c:if>
                        </div>

                    </article>

                </c:forEach>
            </div>
        </c:otherwise>

    </c:choose>

    <div class="rc-pie">
        <p class="rc-nota">
            La coincidencia combina qué tanto se ajusta el costo del destino a tu
            presupuesto (40%) y cuántas de tus preferencias cumple (60%).
        </p>
        <a class="rc-volver" href="<c:url value='/resumen'/>">&larr; Volver al resumen</a>
    </div>

</main>

<script>
    document.querySelectorAll('.rc-barra span[data-ancho]').forEach(b => {
        b.style.width = b.dataset.ancho + '%';
    });
</script>

</body>
</html>