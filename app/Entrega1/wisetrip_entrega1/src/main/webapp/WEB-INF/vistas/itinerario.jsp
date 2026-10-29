<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Itinerario en ${destino.ciudad.nombre} | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/viaje.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/itinerario.css'/>">
</head>
<body class="vj">

<header class="vj-cabecera">
    <a class="vj-marca" href="<c:url value='/'/>">
        <img src="/img/portada/logo.png" alt="">
        <span>Wise<em>Trip</em></span>
    </a>

    <nav class="vj-menu">
        <a href="<c:url value='/viaje'/>">Mi viaje</a>
        <a class="vj-menu-activo" href="<c:url value='/itinerario'/>">Itinerario</a>
        <a href="<c:url value='/plan'/>">Presupuesto</a>
        <a href="<c:url value='/recomendaciones'/>">Destinos</a>
    </nav>

    <div class="vj-usuario">
        <span class="vj-avatar">${inicialesUsuario}</span>
        <div>
            <strong>${usuario.nombreCompleto}</strong>
            <a href="<c:url value='/logout'/>">Cerrar sesión</a>
        </div>
    </div>
</header>

<main class="vj-pantalla it-pantalla" id="itItinerario">

    <section class="it-cabeza">
        <span class="vj-sello">${destino.ciudad.nombre} · ${destino.ciudad.pais}</span>
        <h1 class="it-titulo">Mi itinerario <span>durante el viaje</span></h1>
        <p class="it-bajada">
            Arrastra tus actividades al calendario y organiza cada día
            del ${fechas.fechaInicio} al ${fechas.fechaFin}.
        </p>
    </section>

    <div class="it-grilla">

        <section class="it-caja it-calendario">
            <h2 class="it-caja-titulo">Calendario</h2>

            <div class="it-cal-scroll">
                <div class="it-cal" style="--dias: ${dias.size()}">
                    <div class="it-cal-esquina">Horas</div>
                    <c:forEach var="dia" items="${dias}">
                        <div class="it-cal-dia" data-fecha="${dia.fecha()}">
                            <span>${dia.nombreDia()}</span>
                            <strong>${dia.numero()} ${dia.mes()}</strong>
                        </div>
                    </c:forEach>

                    <c:forEach var="hora" begin="8" end="20" step="2">
                        <div class="it-cal-hora">${hora}:00</div>
                        <c:forEach var="dia" items="${dias}">
                            <div class="it-cal-celda"
                                 data-fecha="${dia.fecha()}"
                                 data-hora="${hora}"></div>
                        </c:forEach>
                    </c:forEach>
                </div>
            </div>
        </section>

        <section class="it-caja it-actividades">
            <h2 class="it-caja-titulo">Actividades</h2>
        </section>

        <aside class="it-caja it-lateral">
            <h2 class="it-caja-titulo">Checklist antes de viajar</h2>
        </aside>

        <section class="it-caja it-presupuesto">
            <h2 class="it-caja-titulo">Presupuesto total</h2>
        </section>

        <section class="it-caja it-consejos">
            <h2 class="it-caja-titulo">Consejos para tu viaje</h2>
        </section>

        <section class="it-caja it-gastos">
            <h2 class="it-caja-titulo">Gastos estimados por categoría</h2>
        </section>

        <section class="it-caja it-proxima">
            <h2 class="it-caja-titulo">Próxima actividad</h2>
        </section>

        <section class="it-caja it-final">
            <h2 class="it-caja-titulo">Al final de actividades</h2>
        </section>

    </div>

</main>

</body>
</html>
