<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Destinos recomendados | WiseTrip</title>

    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>

<body>

<div class="tarjeta ancha">

    <div class="barra">
        <span>
            Hola, <strong>${usuario.nombreCompleto}</strong>
        </span>

        <a href="<c:url value='/logout'/>">
            Cerrar sesión
        </a>
    </div>

    <h1>Destinos recomendados</h1>

    <p class="subtitulo">
        ${seleccion.mensaje}
    </p>

    <c:choose>

        <c:when test="${seleccion.vacio}">

            <div class="vacio">

                <span class="vacio-rotulo">
                    Sin destinos disponibles
                </span>

                <p>
                    No encontramos ciudades que se ajusten a tu presupuesto y preferencias.
                    Prueba ampliando el presupuesto o cambiando algunas respuestas.
                </p>

                <a class="boton" href="<c:url value='/presupuesto'/>">
                    Ajustar presupuesto
                </a>

            </div>

        </c:when>

        <c:otherwise>

            <c:forEach var="r"
                       items="${seleccion.destinos}"
                       varStatus="pos">

                <div class="ticket">

                    <div class="ticket-info">

                        <span class="ticket-sello">
                            ${pos.index + 1}
                        </span>

                        <div>

                            <span class="ticket-ciudad">
                                ${r.ciudad.nombre}
                            </span>

                            <span class="ticket-pais">
                                ${r.ciudad.pais}
                            </span>

                        </div>

                    </div>

                    <div class="ticket-talon">

                        <span class="ticket-rotulo">
                            Coincidencia
                        </span>

                        <span class="ticket-puntaje">
                            <fmt:formatNumber value="${r.puntajeTotal * 100}" maxFractionDigits="1"/>%
                        </span>

                        <span class="ticket-desglose">
                            Presupuesto <fmt:formatNumber value="${r.puntajePresupuesto * 100}" maxFractionDigits="1"/>% ·
                            Gustos <fmt:formatNumber value="${r.puntajePreferencias * 100}" maxFractionDigits="1"/>%
                        </span>

                    </div>

                </div>

            </c:forEach>

        </c:otherwise>

    </c:choose>

    <p class="nota">
        Los puntajes combinan qué tanto se ajusta el costo del destino a tu presupuesto
        (40%) y cuántas de tus preferencias cumple (60%).
    </p>

    <a class="volver" href="<c:url value='/resumen'/>">
        &larr; Volver al resumen
    </a>

</div>

</body>
</html>
