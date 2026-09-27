<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Vincular Telegram | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta centrado">
    <h1>Vincula tu cuenta con Telegram</h1>
    <p class="subtitulo">
        Recibe una notificación cada vez que inicies sesión en WiseTrip.
    </p>

    <ol style="text-align: left;">
        <li>Busca en Telegram a <strong>@wisetrip_notif_bot</strong> y pulsa "Start".</li>
        <li>Busca a <strong>@userinfobot</strong>, pulsa "Start" y copia el número "Id" que te muestra.</li>
        <li>Pega ese número abajo y guarda.</li>
    </ol>

    <c:if test="${not empty mensaje}">
        <p style="color: green;">${mensaje}</p>
    </c:if>

    <form method="post" action="<c:url value='/perfil/telegram'/>">
        <label for="chatId">Tu chat_id de Telegram:</label>
        <input type="text" id="chatId" name="chatId" value="${chatIdActual}" required />
        <button class="boton" type="submit">Guardar</button>
    </form>
</div>
</body>
</html>