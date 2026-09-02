<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Resumen de tu viaje | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta ancha">

    <div class="barra">
        <span>Hola, <strong>${usuario.nombreCompleto}</strong></span>
        <a href="<c:url value='/logout'/>">Cerrar sesión</a>
    </div>

    <div class="check">&#10003;</div>
    <h1 class="centrado">Tu viaje está listo para calcularse</h1>
    <p class="subtitulo centrado">
        Con esta información WiseTrip puede recomendarte destinos y actividades.
    </p>

    <h2 class="seccion">Punto de partida</h2>
    <div class="dato">
        <span class="etiqueta">Sales desde</span>
        <span class="valor">${ubicacion.descripcion}</span>
    </div>

    <c:if test="${not empty fechas}">
        <h2 class="seccion">Fechas <a class="editar" href="<c:url value='/fechas'/>">editar</a></h2>
        <div class="dato">
            <span class="etiqueta">Inicio</span><span class="valor">${fechas.fechaInicio}</span>
        </div>
        <div class="dato">
            <span class="etiqueta">Regreso</span><span class="valor">${fechas.fechaFin}</span>
        </div>
        <div class="dato">
            <span class="etiqueta">Duración</span><span class="valor">${fechas.duracionDias} días</span>
        </div>
    </c:if>

    <c:if test="${not empty presupuesto}">
        <h2 class="seccion">Presupuesto <a class="editar" href="<c:url value='/presupuesto'/>">editar</a></h2>
        <div class="dato">
            <span class="etiqueta">Monto</span>
            <span class="valor valor-cifra">${montoFormateado} ${presupuesto.moneda}</span>
        </div>
        <div class="dato">
            <span class="etiqueta">Moneda</span><span class="valor">${nombreMoneda}</span>
        </div>
        <c:if test="${not empty usdFormateado}">
            <div class="dato">
                <span class="etiqueta">Equivalente aproximado</span>
                <span class="valor valor-cifra">USD ${usdFormateado}</span>
            </div>
        </c:if>
    </c:if>

    <c:if test="${not empty preferencias}">
        <h2 class="seccion">Tus preferencias <a class="editar" href="<c:url value='/preferencias'/>">editar</a></h2>
        <c:forEach var="categoria" items="${categorias}">
            <div class="dato">
                <span class="etiqueta">${categoria.nombre}</span>
                <span class="valor">
                    <c:forEach var="p" items="${categoria.preguntas}">
                        <c:if test="${preferencias.respuestas[p.clave] == 'si'}">
                            <span class="pastilla">${p.texto}</span>
                        </c:if>
                    </c:forEach>
                </span>
            </div>
        </c:forEach>
    </c:if>

    <a class="boton" href="<c:url value='/recomendaciones'/>" style="margin-top:24px">
        Ver mis destinos recomendados
    </a>
</div>
</body>
</html>