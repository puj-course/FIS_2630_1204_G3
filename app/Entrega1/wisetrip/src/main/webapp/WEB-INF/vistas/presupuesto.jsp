<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Presupuesto | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta">

    <div class="barra">
        <span>Hola, <strong>${usuario.nombreCompleto}</strong></span>
        <a href="<c:url value='/logout'/>">Cerrar sesión</a>
    </div>

    <div class="pasos">
        <span class="paso hecho">Origen</span>
        <span class="paso hecho">Preferencias</span>
        <span class="paso hecho">Fechas</span>
        <span class="paso activo">Presupuesto</span>
    </div>

    <h1>¿Cuánto puedes gastar?</h1>
    <p class="subtitulo">
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

    <form action="<c:url value='/presupuesto'/>" method="post">

        <label>Moneda</label>
        <select name="moneda">
            <option value="">Selecciona una moneda</option>
            <c:forEach var="m" items="${monedas}">
                <option value="${m.key}" ${presupuesto.moneda == m.key ? 'selected' : ''}>${m.value}</option>
            </c:forEach>
        </select>
        <c:if test="${not empty errores.moneda}">
            <span class="error">${errores.moneda}</span>
        </c:if>

        <label>Presupuesto total</label>
        <input type="text" name="monto" value="${presupuesto.monto}"
               placeholder="Ej: 2500000" inputmode="decimal">
        <c:if test="${not empty errores.monto}">
            <span class="error">${errores.monto}</span>
        </c:if>

        <div class="nota">
            Escribe solo el número, sin el símbolo de la moneda.
            <c:if test="${not empty fechas and fechas.duracionDias > 0}">
                Es el total para los ${fechas.duracionDias} días del viaje.
            </c:if>
        </div>

        <button type="submit">Ver mi resumen</button>
    </form>

    <a class="volver" href="<c:url value='/fechas'/>">&larr; Volver a las fechas</a>
</div>
</body>
</html>