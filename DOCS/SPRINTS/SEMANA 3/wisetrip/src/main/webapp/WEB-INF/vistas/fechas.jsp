<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Fechas del viaje | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta">
    <a class="volver" href="<c:url value='/'/>">&larr; Volver al inicio</a>
    <h1>Cuando quieres viajar?</h1>
    <p class="subtitulo">Elige la fecha de inicio y de fin de tu viaje.</p>

    <form action="<c:url value='/fechas'/>" method="post">

        <label>Fecha de inicio</label>
        <input type="date" name="fechaInicio" value="${fechas.fechaInicio}">
        <c:if test="${not empty errores.fechaInicio}">
            <div class="alerta">${errores.fechaInicio}</div>
        </c:if>

        <label>Fecha de finalizacion</label>
        <input type="date" name="fechaFin" value="${fechas.fechaFin}">
        <c:if test="${not empty errores.fechaFin}">
            <div class="alerta">${errores.fechaFin}</div>
        </c:if>

        <button type="submit">Continuar</button>
    </form>
</div>
</body>
</html>
