<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Resumen | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta">
    <div class="barra">
        <span>Hola, <strong>${usuario.nombreCompleto}</strong></span>
        <a href="<c:url value='/logout'/>">Cerrar sesion</a>
    </div>

    <div class="check">&#10003;</div>
    <h1 class="centrado">Ubicacion registrada</h1>

    <div class="dato">
        <span class="etiqueta">Sales desde</span>
        <span class="valor">${ubicacion.descripcion}</span>
    </div>
    <div class="dato">
        <span class="etiqueta">Pais</span>
        <span class="valor">${ubicacion.pais}</span>
    </div>
    <div class="dato">
        <span class="etiqueta">Ciudad</span>
        <span class="valor">${ubicacion.ciudad}</span>
    </div>
    <c:if test="${not empty ubicacion.detalle}">
        <div class="dato">
            <span class="etiqueta">Punto de partida</span>
            <span class="valor">${ubicacion.detalle}</span>
        </div>
    </c:if>

    <p class="subtitulo" style="margin-top:20px">
        Con esta informacion WiseTrip podra recomendarte rutas y transporte.
        El siguiente paso sera elegir tus preferencias de viaje.
    </p>

    <a class="boton" href="<c:url value='/origen'/>">Cambiar ubicacion</a>
</div>
</body>
</html>