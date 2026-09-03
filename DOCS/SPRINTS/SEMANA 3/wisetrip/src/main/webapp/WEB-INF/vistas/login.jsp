<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Iniciar sesion | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta">
    <a class="volver" href="<c:url value='/'/>">&larr; Volver al inicio</a>
    <h1>Bienvenido de vuelta</h1>
    <p class="subtitulo">Inicia sesion para continuar con la configuracion de tu viaje.</p>

    <c:if test="${not empty error}">
        <div class="alerta">${error}</div>
    </c:if>

    <form action="<c:url value='/login'/>" method="post">

        <label>Correo electronico</label>
        <input type="text" name="correo" value="${correo}" placeholder="tucorreo@ejemplo.com">

        <label>Contrasena</label>
        <input type="password" name="password" placeholder="Tu contrasena">

        <button type="submit">Iniciar sesion</button>
    </form>

    <p class="pie">No tienes cuenta? <a href="<c:url value='/registro'/>">Crea una aqui</a></p>
</div>
</body>
</html>