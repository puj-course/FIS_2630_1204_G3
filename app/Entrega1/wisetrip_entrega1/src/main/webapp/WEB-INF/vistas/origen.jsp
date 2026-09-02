<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Ubicacion de origen | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta">
    <div class="barra">
        <span>Hola, <strong>${usuario.nombreCompleto}</strong></span>
        <a href="<c:url value='/logout'/>">Cerrar sesion</a>
    </div>

    <h1>De donde sales?</h1>
    <p class="subtitulo">
        Indica tu ubicacion de origen para que WiseTrip pueda calcular rutas,
        transporte y recomendaciones para tu viaje.
    </p>

    <form action="<c:url value='/origen'/>" method="post">

        <label>Pais de origen</label>
        <select name="pais" onchange="this.form.submit()">
            <option value="">Selecciona un pais</option>
            <c:forEach var="p" items="${paises}">
                <option value="${p}" ${ubicacion.pais == p ? 'selected' : ''}>${p}</option>
            </c:forEach>
        </select>
        <c:if test="${not empty errores.pais}">
            <span class="error">${errores.pais}</span>
        </c:if>

        <label>Ciudad de origen</label>
        <select name="ciudad">
            <option value="">
                <c:choose>
                    <c:when test="${empty ciudades}">Primero selecciona un pais</c:when>
                    <c:otherwise>Selecciona una ciudad</c:otherwise>
                </c:choose>
            </option>
            <c:forEach var="ciu" items="${ciudades}">
                <option value="${ciu}" ${ubicacion.ciudad == ciu ? 'selected' : ''}>${ciu}</option>
            </c:forEach>
        </select>
        <c:if test="${not empty errores.ciudad}">
            <span class="error">${errores.ciudad}</span>
        </c:if>

        <label>Punto de partida <span class="opcional">(opcional)</span></label>
        <input type="text" name="detalle" value="${ubicacion.detalle}"
               placeholder="Ej: Aeropuerto El Dorado, barrio Chapinero">
        <c:if test="${not empty errores.detalle}">
            <span class="error">${errores.detalle}</span>
        </c:if>

        <button type="submit">Continuar</button>
    </form>
</div>
</body>
</html>