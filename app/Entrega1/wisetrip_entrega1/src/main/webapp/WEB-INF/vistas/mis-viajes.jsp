<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tus viajes | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/recomendaciones.css'/>">
</head>
<body class="rc">
<header class="rc-cabecera">
    <a class="rc-marca" href="<c:url value='/'/>">Wise<em>Trip</em></a>
    <span>Hola, <c:out value="${usuario.nombreCompleto}"/></span>
    <a href="<c:url value='/logout'/>">Cerrar sesión</a>
</header>
<main class="rc-pantalla">
    <h1 class="rc-titulo">Tus viajes guardados</h1>
    <c:if test="${not empty errorContinuar}">
        <p role="alert"><c:out value="${errorContinuar}"/></p>
    </c:if>
    <c:choose>
        <c:when test="${not empty errorConsulta}">
            <p role="alert"><c:out value="${errorConsulta}"/></p>
            <a class="rc-elegir" href="<c:url value='/viajes'/>">Reintentar</a>
        </c:when>
        <c:otherwise>
            <p>Tienes viajes vinculados a esta cuenta. ¿Quieres continuar uno o crear un viaje nuevo?</p>
            <c:forEach var="viaje" items="${viajes}">
                <article>
                    <h2><c:out value="${viaje.destinoElegido.ciudad.nombre}"/></h2>
                    <p>Del <c:out value="${viaje.fechas.fechaInicio}"/>
                        al <c:out value="${viaje.fechas.fechaFin}"/></p>
                    <p>Origen:
                        <c:choose>
                            <c:when test="${not empty viaje.origen}"><c:out value="${viaje.origen.descripcion}"/></c:when>
                            <c:otherwise>Pendiente de completar</c:otherwise>
                        </c:choose>
                    </p>
                    <p>Presupuesto: <fmt:formatNumber value="${viaje.presupuestoEnUsd}" minFractionDigits="2" maxFractionDigits="2"/> USD</p>
                    <form method="post" action="<c:url value='/viajes/${viaje.idViaje}/continuar'/>">
                        <button class="rc-elegir" type="submit">Continuar este viaje</button>
                    </form>
                </article>
            </c:forEach>
            <form method="post" action="<c:url value='/viajes/nuevo'/>">
                <button class="rc-elegir" type="submit">Crear un viaje nuevo</button>
            </form>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
