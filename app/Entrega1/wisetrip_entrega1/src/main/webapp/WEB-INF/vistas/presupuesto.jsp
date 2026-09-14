<%-- Presupuesto y moneda.
     Vista donde el usuario elige la moneda (local o USD si ya hay destino)
     e ingresa el monto total del viaje. Muestra la duración del viaje como
     referencia y los errores de validación si los hay. Al lado del selector
     de moneda se muestra la bandera del país correspondiente (HU-55).
     Es el último paso del flujo (Origen > Preferencias > Fechas > Presupuesto). --%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Presupuesto | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
    <style>
        /* HU-55: ubica el select y la bandera uno al lado del otro */
        .campo-moneda {
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .campo-moneda select {
            flex: 1;
        }
        .bandera-moneda {
            width: 28px;
            height: 21px;
            border-radius: 2px;
            object-fit: cover;
            border: 1px solid #ddd;
            display: none; /* se muestra con JS solo cuando hay una moneda elegida */
        }
    </style>
</head>
<body>
<div class="tarjeta">

    <div class="barra">
        <span>Hola, <strong>${usuario.nombreCompleto}</strong></span>
        <a href="<c:url value='/logout'/>">Cerrar sesión</a>
    </div>

    <div class="pasos">
<%-- HU-37: Indicador visual de en qué paso del flujo está el viajero. --%>
        <span class="paso hecho">Origen</span>
        <span class="paso hecho">Preferencias</span>
        <span class="paso hecho">Fechas</span>
        <span class="paso activo">Presupuesto</span>
    </div>

    <h1>¿Cuánto puedes gastar?</h1>
    <p class="subtitulo">
        <c:choose>
            <c:when test="${not empty paisDestino}">
                Como tu destino es ${paisDestino}, puedes usar la moneda local o dólares.
            </c:when>
            <c:otherwise>
                Indica el total disponible para todo el viaje. Lo repartiremos entre
                hospedaje, comida, transporte y actividades.
            </c:otherwise>
        </c:choose>
    </p>

    <form action="<c:url value='/presupuesto'/>" method="post">

        <label for="moneda">Moneda</label>
        <div class="campo-moneda">
            <select name="moneda" id="moneda" onchange="actualizarBanderaMoneda()">
                <option value="">Selecciona una moneda</option>
                <c:forEach var="m" items="${monedas}">
                    <option value="${m.key}" ${presupuesto.moneda == m.key ? 'selected' : ''}>${m.value}</option>
                </c:forEach>
            </select>
            <%-- HU-55: aquí se muestra la bandera de la moneda escogida --%>
            <img id="bandera-moneda" class="bandera-moneda" src="" alt="">
        </div>
        <c:if test="${not empty errores.moneda}">
            <span class="error">${errores.moneda}</span>
        </c:if>

        <label>Presupuesto total</label>
        <input type="text" name="monto" value="${presupuesto.monto}"
               placeholder="Ej: 2500000" inputmode="decimal">
<%-- HU-39: Si ya hay un monto guardado en sesión, se precarga aquí. --%>
        <c:if test="${not empty errores.monto}">
            <span class="error">${errores.monto}</span>
        </c:if>

        <div class="nota">
            Escribe solo el número, sin el símbolo de la moneda.
            <c:if test="${not empty fechas and fechas.duracionDias > 0}">
                Es el total para los ${fechas.duracionDias} días del viaje.
            </c:if>
        </div>

        <button type="submit">Ver mi resumen</button>
    </form>

    <a class="volver" href="<c:url value='/fechas'/>">&larr; Volver a las fechas</a>
<%-- HU-37: Botón "Atrás" del flujo. --%>
</div>

<script>
    // HU-55: cada vez que el usuario cambia la moneda en el select,
    // esta función busca el archivo de bandera correspondiente y lo muestra.
    function actualizarBanderaMoneda() {
        // 1. Tomamos el select y la imagen de la bandera por su "id"
        var select = document.getElementById("moneda");
        var bandera = document.getElementById("bandera-moneda");

        // 2. El "value" del option elegido es el código de moneda (ej: "COP")
        var codigoMoneda = select.value;

        if (codigoMoneda === "") {
            // No hay moneda seleccionada todavía: ocultamos la bandera
            bandera.style.display = "none";
        } else {
            // 3. Como guardamos las banderas con el mismo nombre que el código
            //    de moneda (ej: COP.svg, MXN.svg...), armamos la ruta así:
            bandera.src = "<c:url value='/img/banderas/'/>" + codigoMoneda + ".svg";
            bandera.style.display = "inline-block";
        }
    }

    // 4. Si al cargar la página ya viene una moneda precargada (ej. HU-39),
    //    mostramos su bandera de una vez, sin esperar a que el usuario la cambie.
    document.addEventListener("DOMContentLoaded", actualizarBanderaMoneda);
</script>
</body>
</html>
