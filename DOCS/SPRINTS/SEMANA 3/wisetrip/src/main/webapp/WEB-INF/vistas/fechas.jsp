<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Fechas del viaje | WiseTrip</title>
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
        <span class="paso activo">Fechas</span>
        <span class="paso">Presupuesto</span>
    </div>

    <h1>¿Cuándo viajas?</h1>
    <p class="subtitulo">
        Con las fechas calculamos cuántos días dura el viaje y cómo repartir tu presupuesto.
    </p>

    <form action="<c:url value='/fechas'/>" method="post">

        <div class="fila">
            <div>
                <label>Fecha de inicio</label>
                <input type="date" name="fechaInicio" min="${hoy}" value="${fechas.fechaInicio}">
                <c:if test="${not empty errores.fechaInicio}">
                    <span class="error">${errores.fechaInicio}</span>
                </c:if>
            </div>
            <div>
                <label>Fecha de regreso</label>
                <input type="date" name="fechaFin" min="${hoy}" value="${fechas.fechaFin}">
                <c:if test="${not empty errores.fechaFin}">
                    <span class="error">${errores.fechaFin}</span>
                </c:if>
            </div>
        </div>

        <c:if test="${fechas.duracionDias > 0}">
            <div class="nota">Tu viaje duraría <strong>${fechas.duracionDias} días</strong>.</div>
        </c:if>

        <button type="submit">Continuar</button>
    </form>

    <a class="volver" href="<c:url value='/preferencias'/>">&larr; Volver a preferencias</a>
</div>
</body>
</html>