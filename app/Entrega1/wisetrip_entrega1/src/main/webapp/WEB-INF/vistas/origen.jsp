<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Ubicacion de origen | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
    
    <!-- Estilo para el boton flotante de Telegram -->
    <style>
        .boton-flotante-telegram {
        position: fixed;
        left: 24px;
        bottom: 24px;
        width: 56px;
        height: 56px;
        background-color: #229ED9;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 2px 8px rgba(0,0,0,0.25);
        z-index: 1000;
        text-decoration: none;
        }
        .boton-flotante-telegram:hover {
        background-color: #1b87bd;
        }
    </style>
</head>
<body>
<div class="tarjeta">
    <div class="barra">
        <span>Hola, <strong>${usuario.nombreCompleto}</strong></span>
        <a href="<c:url value='/logout'/>">Cerrar sesión</a>
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

        <label for="detalle">Punto de partida <span class="opcional">(opcional)</span></label>
        <input type="text" id="detalle" name="detalle" value="${ubicacion.detalle}"
               maxlength="60" aria-describedby="contadorDetalle"
               placeholder="Ej: Aeropuerto El Dorado, barrio Chapinero">
        <small id="contadorDetalle">0/60 caracteres</small>
        <c:if test="${not empty errores.detalle}">
            <span class="error">${errores.detalle}</span>
        </c:if>

        <button type="submit">Continuar</button>
    </form>
</div>
<script>
    const detalle = document.getElementById('detalle');
    const contadorDetalle = document.getElementById('contadorDetalle');

    function actualizarContadorDetalle() {
        contadorDetalle.textContent = detalle.value.length + '/60 caracteres';
    }

    detalle.addEventListener('input', actualizarContadorDetalle);
    actualizarContadorDetalle();
</script>

<!--Boton flotante de Telegram -->
<a href="<c:url value='/perfil/telegram'/>" class="boton-flotante-telegram" title="Conecta tu Telegram">
    <svg width="26" height="26" viewBox="0 0 24 24" fill="white">
        <path d="M21.5 4.5L2.7 11.9c-1.2.5-1.2 1.2-.2 1.5l4.8 1.5 1.8 5.6c.2.6.4.8.8.8.5 0 .7-.2 1-.5l2.4-2.3 4.9 3.6c.9.5 1.5.2 1.8-.8l3.2-15.1c.4-1.3-.3-1.9-1.7-1.3zM8.5 13.9l9.5-6c.5-.3.9-.1.5.2l-7.8 7.1-.3 3.3-1.4-4.6z"/>
    </svg>
</a>
</body>
</html>
