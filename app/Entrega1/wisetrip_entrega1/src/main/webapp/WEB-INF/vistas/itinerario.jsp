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

                    <%-- Clima: plantilla. Cuando haya API, cambiar la clase
                         it-clima-pendiente por it-clima-sol o it-clima-nube --%>
                    <div class="it-cal-hora">Clima</div>
                    <c:forEach var="dia" items="${dias}">
                        <div class="it-clima it-clima-pendiente"
                             data-fecha="${dia.fecha()}"
                             title="Pronóstico del clima: próximamente">
                            <svg class="it-clima-sol" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                                <circle cx="12" cy="12" r="4.5" stroke="currentColor" stroke-width="2"/>
                                <path d="M12 2v2.5M12 19.5V22M2 12h2.5M19.5 12H22M4.9 4.9l1.8 1.8M17.3 17.3l1.8 1.8M4.9 19.1l1.8-1.8M17.3 6.7l1.8-1.8" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                            </svg>
                            <svg class="it-clima-nube" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                                <path d="M7 18h10a4 4 0 0 0 .6-7.95A5.5 5.5 0 0 0 7.1 9.1 4.5 4.5 0 0 0 7 18z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
                            </svg>
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
            <p class="it-caja-nota">Arrastra tus actividades al calendario</p>

            <%-- Plantillas: reemplazar por las actividades reales del destino --%>
            <ul class="it-lista-actividades" id="itListaActividades">
                <li class="it-actividad vj-actividades" draggable="true" data-categoria="actividades" data-id="act-1" data-costo="15">
                    <strong>Recorrido por el centro</strong>
                    <span>Actividad · 2 h · US$ 15</span>
                </li>
                <li class="it-actividad vj-actividades" draggable="true" data-categoria="actividades" data-id="act-2" data-costo="12">
                    <strong>Visita a un museo</strong>
                    <span>Actividad · 2 h · US$ 12</span>
                </li>
                <li class="it-actividad vj-alimentacion" draggable="true" data-categoria="alimentacion" data-id="act-3" data-costo="10">
                    <strong>Almuerzo típico</strong>
                    <span>Comida · 2 h · US$ 10</span>
                </li>
                <li class="it-actividad vj-transporte" draggable="true" data-categoria="transporte" data-id="act-4" data-costo="8">
                    <strong>Traslado al hotel</strong>
                    <span>Transporte · 2 h · US$ 8</span>
                </li>
            </ul>

            <h3 class="it-subtitulo">
                <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M12 21s-6.5-6-6.5-11a6.5 6.5 0 0 1 13 0c0 5-6.5 11-6.5 11z" stroke="currentColor" stroke-width="2.2" stroke-linejoin="round"/>
                    <circle cx="12" cy="10" r="2.3" stroke="currentColor" stroke-width="2.2"/>
                </svg>
                Recomendadas en ${destino.ciudad.nombre}
            </h3>

            <%-- Plantillas: poner las fotos en static/img/itinerario/ con estos
                 nombres. Si la foto no existe, se ve el recuadro de plantilla. --%>
            <ul class="it-lista-actividades" id="itListaRecomendadas">
                <li class="it-actividad it-recomendada" draggable="true" data-categoria="actividades" data-id="rec-1" data-costo="5">
                    <span class="it-foto">
                        <img src="/img/itinerario/recomendada-1.jpg" alt="" onerror="this.remove()">
                    </span>
                    <strong>Mirador de la ciudad</strong>
                    <span>Recomendada · 2 h · US$ 5</span>
                </li>
                <li class="it-actividad it-recomendada" draggable="true" data-categoria="actividades" data-id="rec-2" data-costo="6">
                    <span class="it-foto">
                        <img src="/img/itinerario/recomendada-2.jpg" alt="" onerror="this.remove()">
                    </span>
                    <strong>Mercado local</strong>
                    <span>Recomendada · 2 h · US$ 6</span>
                </li>
            </ul>
        </section>

        <aside class="it-caja it-lateral">
            <nav class="it-accesos">
                <a class="it-acceso it-acceso-activo" href="<c:url value='/itinerario'/>" aria-current="page">
                    <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                        <rect x="3.5" y="5" width="17" height="15" rx="2.5" stroke="currentColor" stroke-width="2.2"/>
                        <path d="M3.5 10h17M8 3v4M16 3v4" stroke="currentColor" stroke-width="2.2" stroke-linecap="round"/>
                    </svg>
                    Itinerario
                </a>
                <a class="it-acceso" href="<c:url value='/recomendaciones'/>">
                    <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                        <path d="M12 3.5l2.6 5.3 5.9.9-4.3 4.1 1 5.8L12 16.9l-5.2 2.7 1-5.8-4.3-4.1 5.9-.9z" stroke="currentColor" stroke-width="2.2" stroke-linejoin="round"/>
                    </svg>
                    Recomendaciones
                </a>
                <a class="it-acceso" href="<c:url value='/plan'/>">
                    <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                        <path d="M4 7.5A2.5 2.5 0 0 1 6.5 5H18v3" stroke="currentColor" stroke-width="2.2" stroke-linejoin="round"/>
                        <rect x="4" y="8" width="16.5" height="11" rx="2.5" stroke="currentColor" stroke-width="2.2"/>
                        <circle cx="16" cy="13.5" r="1.4" fill="currentColor"/>
                    </svg>
                    Presupuesto
                </a>
            </nav>

            <h2 class="it-caja-titulo">Checklist antes de viajar</h2>

            <ul class="it-checklist" id="itChecklist">
                <li><label><input type="checkbox" value="reservas"><span>Reservas</span></label></li>
                <li><label><input type="checkbox" value="ropa"><span>Ropa</span></label></li>
                <li><label><input type="checkbox" value="pasaporte"><span>Pasaporte</span></label></li>
                <li><label><input type="checkbox" value="billetera"><span>Billetera</span></label></li>
                <li><label><input type="checkbox" value="cargador"><span>Cargador</span></label></li>
            </ul>
        </aside>

        <section class="it-caja it-presupuesto" id="itPresupuesto"
                 data-total="${presupuesto.montoNumerico}"
                 data-moneda="${presupuesto.moneda}"
                 data-total-usd="${totalUsd}">
            <h2 class="it-caja-titulo">
                <svg class="it-titulo-icono" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M5 20V12M10 20V6M15 20v-9M20 20V4" stroke="currentColor" stroke-width="2.6" stroke-linecap="round"/>
                </svg>
                Presupuesto total
            </h2>

            <div class="it-presupuesto-cifras">
                <strong class="it-presupuesto-monto">
                    ${montoFormateado} <small>${presupuesto.moneda}</small>
                </strong>
                <div class="it-presupuesto-usd">
                    <span class="vj-rotulo">Equivale a</span>
                    <strong>US$ ${usdFormateado}</strong>
                </div>
            </div>

            <p class="it-gastado">
                Actividades en el calendario: <strong id="itGastado">US$ 0</strong>
            </p>

            <div class="it-alerta it-alerta-roja" id="itAlertaRoja" role="status" hidden>
                <span class="it-alerta-icono">!</span>
                <p>
                    ¿Estás a punto de gastar el máximo de tu presupuesto?
                    <a href="<c:url value='/plan'/>">¿Te gustaría acomodarlo?</a>
                </p>
            </div>

            <div class="it-alerta it-alerta-verde" id="itAlertaVerde" role="status">
                <span class="it-alerta-icono">✓</span>
                <p>Vas súper bien, aún tienes presupuesto hábil.</p>
            </div>
        </section>

        <section class="it-caja it-consejos">
            <h2 class="it-caja-titulo">
                <svg class="it-titulo-icono it-icono-amarillo" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M9 18h6M10 21h4M12 3a6 6 0 0 0-3.5 10.9c.6.5 1 1.2 1 2V16h5v-.1c0-.8.4-1.5 1-2A6 6 0 0 0 12 3z" stroke="currentColor" stroke-width="2.2" stroke-linejoin="round"/>
                </svg>
                Consejos para tu viaje
            </h2>

            <%-- Plantillas: cambiar los textos por consejos reales del destino --%>
            <div class="it-consejos-lista">
                <details class="it-consejo vj-alimentacion">
                    <summary>
                        <span class="it-gasto-icono">
                            <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                                <path d="M8 4 4 6.5l2 3.5 2-1V20h8V9l2 1 2-3.5L16 4a4 4 0 0 1-8 0z" stroke="currentColor" stroke-width="2.2" stroke-linejoin="round"/>
                            </svg>
                        </span>
                        <strong>Clima</strong>
                        <span class="it-consejo-texto">Lleva ropa ligera y un suéter para las noches.</span>
                    </summary>
                    <p>Revisa el pronóstico de ${destino.ciudad.nombre} unos días antes de salir y empaca por capas: así te adaptas al calor del día y al fresco de la noche.</p>
                </details>

                <details class="it-consejo vj-imprevistos">
                    <summary>
                        <span class="it-gasto-icono">
                            <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                                <rect x="3" y="5.5" width="18" height="13" rx="2.5" stroke="currentColor" stroke-width="2.2"/>
                                <path d="M3 10h18M7 15h4" stroke="currentColor" stroke-width="2.2" stroke-linecap="round"/>
                            </svg>
                        </span>
                        <strong>Pagos</strong>
                        <span class="it-consejo-texto">Muchos lugares aceptan tarjeta, pero lleva algo de efectivo.</span>
                    </summary>
                    <p>Avisa a tu banco que vas a viajar a ${destino.ciudad.pais} y ten a mano efectivo en moneda local para transporte, propinas y mercados.</p>
                </details>

                <details class="it-consejo vj-hospedaje">
                    <summary>
                        <span class="it-gasto-icono">
                            <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                                <path d="M12 3 4 6v6c0 4.5 3.4 8 8 9 4.6-1 8-4.5 8-9V6z" stroke="currentColor" stroke-width="2.2" stroke-linejoin="round"/>
                                <path d="M8.5 12l2.5 2.5L15.5 10" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
                            </svg>
                        </span>
                        <strong>Seguridad</strong>
                        <span class="it-consejo-texto">Zona turística segura, mantén tus pertenencias a la vista.</span>
                    </summary>
                    <p>Guarda una copia digital de tu pasaporte y comparte tu itinerario con alguien de confianza antes de salir.</p>
                </details>
            </div>
        </section>

        <section class="it-caja it-gastos">
            <h2 class="it-caja-titulo">Gastos estimados por categoría</h2>

            <ul class="it-gastos-lista">
                <li class="vj-alimentacion">
                    <span class="it-gasto-icono">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                            <path d="M7 3v8M4.5 3v5a2.5 2.5 0 0 0 5 0V3M7 11v10M17 21V3c-2.2 0-3.5 2.5-3.5 6v4H17" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </span>
                    Comida
                    <strong data-gasto="alimentacion">US$ 0</strong>
                </li>
                <li class="vj-actividades">
                    <span class="it-gasto-icono">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                            <path d="M3 20 9.5 8l4 7 2.5-4 5 9z" stroke="currentColor" stroke-width="2.2" stroke-linejoin="round"/>
                            <circle cx="17.5" cy="5.5" r="2.2" stroke="currentColor" stroke-width="2.2"/>
                        </svg>
                    </span>
                    Actividades
                    <strong data-gasto="actividades">US$ 0</strong>
                </li>
                <li class="vj-transporte">
                    <span class="it-gasto-icono">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                            <rect x="4" y="5" width="16" height="11" rx="2.5" stroke="currentColor" stroke-width="2.2"/>
                            <path d="M4 11h16M7.5 19.5v-3.5M16.5 19.5v-3.5" stroke="currentColor" stroke-width="2.2" stroke-linecap="round"/>
                        </svg>
                    </span>
                    Transporte
                    <strong data-gasto="transporte">US$ 0</strong>
                </li>
            </ul>
        </section>

        <section class="it-caja it-proxima">
            <h2 class="it-caja-titulo">
                <svg class="it-titulo-icono" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <circle cx="12" cy="12" r="8.5" stroke="currentColor" stroke-width="2.4"/>
                    <path d="M12 7.5V12l3 2" stroke="currentColor" stroke-width="2.4" stroke-linecap="round"/>
                </svg>
                Próxima actividad
            </h2>

            <div class="it-proxima-tarjeta" id="itProxima" hidden>
                <span class="it-foto it-proxima-foto" id="itProximaFoto"></span>
                <div>
                    <strong id="itProximaNombre"></strong>
                    <span id="itProximaCuando"></span>
                </div>
            </div>

            <p class="it-proxima-vacia" id="itProximaVacia">
                Aún no tienes actividades programadas. Arrastra una al calendario.
            </p>
        </section>

        <section class="it-caja it-final">
            <h2 class="it-caja-titulo">Al final de actividades</h2>
        </section>

    </div>

</main>

<script src="/js/itinerario.js"></script>
</body>
</html>
