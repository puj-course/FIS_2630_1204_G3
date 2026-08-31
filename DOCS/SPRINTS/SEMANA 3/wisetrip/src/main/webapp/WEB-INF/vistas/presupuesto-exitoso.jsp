<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Presupuesto guardado | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta">
    <h1>Presupuesto guardado</h1>
    <p class="subtitulo">Ya sabemos con cuanto cuentas. </p>

    <c:if test="${not empty sessionScope.presupuestoViaje}">
        <p class="pie">
            Presupuesto: ${sessionScope.presupuestoViaje.monto} ${sessionScope.presupuestoViaje.moneda}
            (equivalente aproximado: <fmt:formatNumber value="${sessionScope.presupuestoEnUsd}" maxFractionDigits="2"/> USD)
        </p>
    </c:if>

    <a class="volver" href="<c:url value='/'/>">&larr; Volver al inicio</a>
</div>
</body>
</html>
