<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Presupuesto | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta">
    <a class="volver" href="<c:url value='/'/>">&larr; Volver al inicio</a>
    <h1>Cual es tu presupuesto?</h1>

    <c:choose>
        <c:when test="${not empty paisDestino}">
            <p class="subtitulo">Como elegiste viajar a <strong>${paisDestino}</strong>, te mostramos las monedas de ese destino.</p>
        </c:when>
        <c:otherwise>
            <p class="subtitulo">Aun no has elegido tu pais destino, asi que puedes elegir cualquiera de las monedas disponibles.</p>
        </c:otherwise>
    </c:choose>

    <form action="<c:url value='/presupuesto'/>" method="post">

        <label>Presupuesto</label>
        <input type="text" name="monto" value="${presupuesto.monto}" placeholder="Ej: 1500000">
        <c:if test="${not empty errores.monto}">
            <div class="alerta">${errores.monto}</div>
        </c:if>

        <label>Moneda</label>
        <select name="moneda">
            <option value="">Selecciona una moneda</option>
            <c:forEach var="m" items="${monedas}">
                <option value="${m}" <c:if test="${presupuesto.moneda == m}">selected</c:if>>${m}</option>
            </c:forEach>
        </select>
        <c:if test="${not empty errores.moneda}">
            <div class="alerta">${errores.moneda}</div>
        </c:if>

        <button type="submit">Continuar</button>
    </form>
</div>
</body>
</html>
