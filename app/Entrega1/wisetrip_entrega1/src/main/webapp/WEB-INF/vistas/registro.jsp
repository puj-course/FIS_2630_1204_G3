<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crear cuenta | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta">
    <a class="volver" href="<c:url value='/'/>">&larr; Volver al inicio</a>
    <h1>Crea tu cuenta</h1>
    <p class="subtitulo">Empieza a planear tu próximo viaje en minutos.</p>

    <form action="<c:url value='/registro'/>" method="post">

        <label>Nombre completo</label>
        <input type="text" name="nombreCompleto" value="${usuario.nombreCompleto}"
               placeholder="Ej: Laura Gómez Peña">
        <c:if test="${not empty errores.nombreCompleto}">
            <span class="error">${errores.nombreCompleto}</span>
        </c:if>

        <div class="fila">
            <div>
                <label>Tipo de documento</label>
                <select name="tipoDocumento">
                    <option value="">Selecciona una opción</option>
                    <option value="CC" ${usuario.tipoDocumento == 'CC' ? 'selected' : ''}>Cédula de ciudadanía</option>
                    <option value="CE" ${usuario.tipoDocumento == 'CE' ? 'selected' : ''}>Cédula de extranjería</option>
                    <option value="TI" ${usuario.tipoDocumento == 'TI' ? 'selected' : ''}>Tarjeta de identidad</option>
                    <option value="PA" ${usuario.tipoDocumento == 'PA' ? 'selected' : ''}>Pasaporte</option>
                </select>
                <c:if test="${not empty errores.tipoDocumento}">
                    <span class="error">${errores.tipoDocumento}</span>
                </c:if>
            </div>
            <div>
                <label>Número de documento</label>
                <input type="text" name="numeroDocumento" value="${usuario.numeroDocumento}"
                       placeholder="Ej: 1020304050">
                <c:if test="${not empty errores.numeroDocumento}">
                    <span class="error">${errores.numeroDocumento}</span>
                </c:if>
            </div>
        </div>

        <label>Fecha de nacimiento</label>
        <input type="date" name="fechaNacimiento" value="${usuario.fechaNacimiento}">
        <c:if test="${not empty errores.fechaNacimiento}">
            <span class="error">${errores.fechaNacimiento}</span>
        </c:if>

        <label>Correo electrónico</label>
        <input type="text" name="correo" value="${usuario.correo}" placeholder="tucorreo@ejemplo.com">
        <c:if test="${not empty errores.correo}">
            <span class="error">${errores.correo}</span>
        </c:if>

        <label>Contraseña</label>
        <input type="password" name="password" placeholder="Mínimo 6 caracteres">
        <c:if test="${not empty errores.password}">
            <span class="error">${errores.password}</span>
        </c:if>

        <label>Confirmar contraseña</label>
        <input type="password" name="confirmarPassword" placeholder="Repite tu contraseña">
        <c:if test="${not empty errores.confirmarPassword}">
            <span class="error">${errores.confirmarPassword}</span>
        </c:if>

        <button type="submit">Crear cuenta</button>
    </form>

    <p class="pie">¿Ya tienes cuenta? <a href="<c:url value='/login'/>">Inicia sesión</a></p>
</div>
</body>
</html>