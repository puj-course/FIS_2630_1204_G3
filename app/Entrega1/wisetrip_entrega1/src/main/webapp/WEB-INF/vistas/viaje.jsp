<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tu viaje a ${destino.ciudad.nombre} | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/viaje.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/itinerario.css'/>">
</head>
<body class="vj">

<header class="vj-cabecera">
    <a class="vj-marca" href="<c:url value='/'/>">
        <img src="/img/portada/logo.png" alt="">
        <span>Wise<em>Trip</em></span>
    </a>

    <div class="vj-usuario">
        <span class="vj-avatar">${inicialesUsuario}</span>
        <div>
            <strong>${usuario.nombreCompleto}</strong>
            <a href="<c:url value='/logout'/>">Cerrar sesión</a>
        </div>
    </div>
</header>

<div class="vj-distribucion">

<!-- ===== Barra lateral: resumen de lo que el viajero puede ver ===== -->
<aside class="vj-lateral">
    <span class="vj-rotulo">Tu viaje a ${destino.ciudad.nombre}</span>
    <nav class="vj-menu vj-menu-lateral">
        <a class="vj-menu-activo" href="#resumen">Resumen general</a>
        <a href="#presupuesto">Presupuesto</a>
        <a href="#itinerario">Itinerario</a>
        <a href="#antes-de-viajar">Antes de viajar</a>
    </nav>
</aside>

<main class="vj-pantalla" id="vjViaje"
      data-total="${presupuesto.monto}"
      data-moneda="${presupuesto.moneda}"
      data-dias="${dias}"
      data-inicio="${fechas.fechaInicio}"
      data-hospedaje="${reparto.hospedaje}"
      data-alimentacion="${reparto.alimentacion}"
      data-transporte="${reparto.transporte}"
      data-actividades="${reparto.actividades}"
      data-imprevistos="${reparto.imprevistos}">

    <!-- ===== Portada del viaje ===== -->
    <section class="vj-seccion" id="resumen">
    <div class="vj-portada">
        <div class="vj-portada-texto">
            <span class="vj-sello">Tu viaje está en marcha</span>
            <h1 class="vj-titulo">${destino.ciudad.nombre}</h1>
            <p class="vj-bajada">
                ${destino.ciudad.pais} te espera. Aquí tienes todo tu viaje en un
                solo lugar: tu presupuesto organizado, tu itinerario día por día
                y lo que necesitas antes de salir.
            </p>
            <div class="vj-portada-acciones">
                <a class="vj-btn" href="#itinerario">Organizar mi itinerario</a>
                <a class="vj-enlace" href="<c:url value='/plan'/>">Cambiar reparto</a>
            </div>
        </div>

        <div class="vj-pase">
            <div class="vj-pase-top">
                <span>WiseTrip</span>
                <span>Pase de abordar</span>
            </div>

            <div class="vj-pase-cuerpo">
                <div class="vj-ruta">
                    <div>
                        <span class="vj-pase-rotulo">Desde</span>
                        <span class="vj-pase-ciudad">${ubicacionCiudad}</span>
                    </div>
                    <svg class="vj-ruta-flecha" viewBox="0 0 80 24" fill="none" aria-hidden="true">
                        <path d="M4 12h66" stroke="#16161D" stroke-width="2.5" stroke-dasharray="5 5" stroke-linecap="round"/>
                        <path d="M64 5l9 7-9 7" stroke="#16161D" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                    <div class="vj-ruta-destino">
                        <span class="vj-pase-rotulo">Hacia</span>
                        <span class="vj-pase-ciudad">${destino.ciudad.nombre}</span>
                    </div>
                </div>

                <div class="vj-perforado"></div>

                <div class="vj-pase-datos">
                    <div>
                        <span class="vj-pase-rotulo">Pasajera</span>
                        <strong>${usuario.nombreCompleto}</strong>
                    </div>
                    <div>
                        <span class="vj-pase-rotulo">Duración</span>
                        <strong>${dias} días</strong>
                    </div>
                    <c:if test="${not empty fechas}">
                        <div>
                            <span class="vj-pase-rotulo">Salida</span>
                            <strong>${fechas.fechaInicio}</strong>
                        </div>
                        <div>
                            <span class="vj-pase-rotulo">Regreso</span>
                            <strong>${fechas.fechaFin}</strong>
                        </div>
                    </c:if>
                </div>
            </div>

            <div class="vj-pase-abajo">
                <span class="vj-barras"></span>
                <span class="vj-cuenta" id="vjCuenta">${dias} días de viaje</span>
            </div>
        </div>
    </div>

    <!-- Una tarjeta por cada cosa que el viajero puede ver por separado -->
    <div class="vj-resumen-tarjetas">
        <a class="vj-resumen-tarjeta vj-hospedaje" href="#presupuesto">
            <span class="vj-rotulo">Presupuesto</span>
            <strong>${montoFormateado} ${presupuesto.moneda}</strong>
            <span>Repartido en 5 categorías</span>
        </a>
        <a class="vj-resumen-tarjeta vj-actividades" href="#itinerario">
            <span class="vj-rotulo">Itinerario</span>
            <strong>${dias} días</strong>
            <span>Del ${fechas.fechaInicio} al ${fechas.fechaFin}</span>
        </a>
        <a class="vj-resumen-tarjeta vj-transporte" href="#antes-de-viajar">
            <span class="vj-rotulo">Antes de viajar</span>
            <strong>Checklist</strong>
            <span>Lo que no puede faltar y consejos</span>
        </a>
    </div>
    </section>

    <!-- ===== Presupuesto ===== -->
    <section class="vj-seccion" id="presupuesto">
    <div class="vj-seccion-cabeza">
        <h2 class="vj-seccion-titulo">Tu presupuesto</h2>
        <p class="vj-seccion-nota">
            Lo que separaste para cada gasto. La idea es que todo tu viaje
            quepa aquí, sin gastar de más.
        </p>
    </div>

    <div class="vj-presupuesto">
        <div class="vj-presupuesto-cifras">
            <div>
                <span class="vj-rotulo">Presupuesto total</span>
                <strong class="vj-cifra" id="vjTotal"></strong>
            </div>
            <div>
                <span class="vj-rotulo">Por día</span>
                <strong class="vj-cifra vj-cifra-chica" id="vjPorDia"></strong>
            </div>
            <div>
                <span class="vj-rotulo">Equivale a</span>
                <strong class="vj-cifra vj-cifra-chica">US$ ${usdFormateado}</strong>
            </div>
        </div>

        <div class="vj-reparto-barra">
            <span class="vj-hospedaje"    data-seg="hospedaje"></span>
            <span class="vj-alimentacion" data-seg="alimentacion"></span>
            <span class="vj-transporte"   data-seg="transporte"></span>
            <span class="vj-actividades"  data-seg="actividades"></span>
            <span class="vj-imprevistos"  data-seg="imprevistos"></span>
        </div>

        <table class="vj-reparto-tabla">
            <thead>
                <tr>
                    <th scope="col">Categoría</th>
                    <th scope="col">Porcentaje</th>
                    <th scope="col">Monto</th>
                    <th scope="col">Por día</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="cat" items="hospedaje,alimentacion,transporte,actividades">
                    <tr class="vj-${cat}">
                        <th scope="row">
                            <span class="vj-punto"></span>
                            <c:choose>
                                <c:when test="${cat == 'hospedaje'}">Hospedaje</c:when>
                                <c:when test="${cat == 'alimentacion'}">Alimentación</c:when>
                                <c:when test="${cat == 'transporte'}">Transporte</c:when>
                                <c:otherwise>Actividades</c:otherwise>
                            </c:choose>
                        </th>
                        <td data-pct="${cat}"></td>
                        <td data-monto="${cat}"></td>
                        <td data-por-dia="${cat}"></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="vj-colchon vj-imprevistos">
            <span class="vj-colchon-icono">
                <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M12 3 4 6v6c0 4.5 3.4 8 8 9 4.6-1 8-4.5 8-9V6z" stroke="#16161D" stroke-width="2.2" stroke-linejoin="round"/>
                    <path d="M8.5 12l2.5 2.5L15.5 10" stroke="#16161D" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </span>
            <div>
                <strong>Colchón para imprevistos · <span data-pct="imprevistos"></span></strong>
                <p>Este dinero no se toca a menos que surja algo fuera del plan.</p>
            </div>
            <span class="vj-colchon-cifra" data-monto="imprevistos"></span>
        </div>
    </div>
    </section>

    <!-- ===== Itinerario ===== -->
    <section class="vj-seccion" id="itinerario">
    <div class="vj-seccion-cabeza">
        <h2 class="vj-seccion-titulo">Tu itinerario</h2>
        <p class="vj-seccion-nota">
            Arrastra tus actividades al calendario y organiza cada día
            del ${fechas.fechaInicio} al ${fechas.fechaFin}.
        </p>
    </div>

    <div class="it-grilla it-grilla-viaje" id="itItinerario"
         data-clave="${usuario.idUsuario}|${destino.ciudad.nombre}|${fechas.fechaInicio}|${fechas.fechaFin}">

        <section class="it-caja it-calendario">
            <h2 class="it-caja-titulo">Calendario</h2>

            <div class="it-cal-scroll">
                <div class="it-cal" style="--dias: ${diasCalendario.size()}">
                    <div class="it-cal-esquina">Horas</div>
                    <c:forEach var="dia" items="${diasCalendario}">
                        <div class="it-cal-dia" data-fecha="${dia.fecha()}">
                            <span>${dia.nombreDia()}</span>
                            <strong>${dia.numero()} ${dia.mes()}</strong>
                        </div>
                    </c:forEach>

                    <%-- Clima: plantilla. Cuando haya API, cambiar la clase
                         it-clima-pendiente por it-clima-sol o it-clima-nube --%>
                    <div class="it-cal-hora">Clima</div>
                    <c:forEach var="dia" items="${diasCalendario}">
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
                        <c:forEach var="dia" items="${diasCalendario}">
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

        <section class="it-caja it-control" id="itPresupuesto"
                 data-total-usd="${totalUsd}">
            <h2 class="it-caja-titulo">Control de gastos</h2>
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
            <h2 class="it-caja-titulo">
                <svg class="it-titulo-icono" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M6 21V4M6 4h11l-2.5 4L17 12H6" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                Al final de actividades
            </h2>

            <p class="it-final-pregunta">¿Te quedaste sin actividades por escoger? ¿Se te ocurre alguna?</p>

            <form class="it-form-nueva" id="itFormNueva" autocomplete="off">
                <label class="it-oculto" for="itNuevaNombre">Nombre de la actividad</label>
                <input type="text" id="itNuevaNombre" name="nombre" maxlength="60"
                       placeholder="Escribe una actividad...">
                <button type="submit" aria-label="Agregar actividad">+</button>
            </form>
            <p class="it-nueva-aviso" id="itNuevaAviso" role="status"></p>
        </section>

    </div>
    </section>

    <!-- ===== Antes de viajar ===== -->
    <section class="vj-seccion" id="antes-de-viajar">
    <div class="vj-seccion-cabeza">
        <h2 class="vj-seccion-titulo">Antes de viajar</h2>
        <p class="vj-seccion-nota">
            Lo que no puede faltar en tu maleta y algunos consejos
            para ${destino.ciudad.nombre}.
        </p>
    </div>

    <div class="it-grilla it-grilla-antes">
        <section class="it-caja it-checklist-caja">
            <h2 class="it-caja-titulo">Checklist</h2>
            <ul class="it-checklist" id="itChecklist">
                <li><label><input type="checkbox" value="reservas"><span>Reservas</span></label></li>
                <li><label><input type="checkbox" value="ropa"><span>Ropa</span></label></li>
                <li><label><input type="checkbox" value="pasaporte"><span>Pasaporte</span></label></li>
                <li><label><input type="checkbox" value="billetera"><span>Billetera</span></label></li>
                <li><label><input type="checkbox" value="cargador"><span>Cargador</span></label></li>
            </ul>
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
    </div>
    </section>

    <footer class="vj-pie">
        <span>WiseTrip · ${ubicacionCiudad} &rarr; ${destino.ciudad.nombre}</span>
        <a href="<c:url value='/recomendaciones'/>">&larr; Elegir otro destino</a>
    </footer>

</main>

</div>

<script src="/js/viaje.js"></script>
<script src="/js/itinerario.js"></script>
</body>
</html>