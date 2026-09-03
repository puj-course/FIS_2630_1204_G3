<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Cuenta creada | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta centrado">
    <div class="check">&#10003;</div>
    <h1>Cuenta creada con exito</h1>
    <p class="subtitulo">
        Hola <strong>${nombre}</strong>, tu cuenta <strong>${correo}</strong> ya esta lista.
        Ahora inicia sesion para continuar con la planificacion de tu viaje.
    </p>
    <a class="boton" href="<c:url value='/login'/>">Ir a iniciar sesion</a>
</div>
</body>
</html>