<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Preferencias de viaje | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta tarjeta-ancha">
    <a class="volver" href="<c:url value='/'/>">&larr; Volver al inicio</a>
    <h1>Cuentanos que te gusta</h1>
    <p class="subtitulo">Responde si o no en cada pregunta. Esto nos ayuda a armar tu plan ideal.</p>

    <form action="<c:url value='/preferencias'/>" method="post">

        <c:forEach var="categoria" items="${categorias}">
            <h2 class="categoria">${categoria.nombre}</h2>

            <c:forEach var="pregunta" items="${categoria.preguntas}">
                <div class="pregunta">
                    <label>${pregunta.texto}</label>

                    <label class="opcion">
                        <input type="radio" name="respuestas[${pregunta.clave}]" value="si"
                               <c:if test="${preferencias.respuestas[pregunta.clave] == 'si'}">checked</c:if>>
                        Si
                    </label>
                    <label class="opcion">
                        <input type="radio" name="respuestas[${pregunta.clave}]" value="no"
                               <c:if test="${preferencias.respuestas[pregunta.clave] == 'no'}">checked</c:if>>
                        No
                    </label>

                    <c:if test="${not empty errores[pregunta.clave]}">
                        <div class="alerta">${errores[pregunta.clave]}</div>
                    </c:if>
                </div>
            </c:forEach>
        </c:forEach>

        <button type="submit">Continuar</button>
    </form>
</div>
</body>
</html>
