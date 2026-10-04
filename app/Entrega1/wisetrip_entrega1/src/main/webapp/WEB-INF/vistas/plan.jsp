<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reparte tu presupuesto | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/plan.css'/>">
</head>
<body class="pl">

<header class="pl-cabecera">
    <a class="pl-marca" href="<c:url value='/'/>">
        <img src="/img/portada/logo.png" alt="">
        <span>Wise<em>Trip</em></span>
    </a>

    <div class="pl-destino-chip">
        <span>${ubicacionCiudad} &rarr; ${destino.ciudad.nombre}</span>
    </div>

    <div class="pl-usuario">
        <span class="pl-avatar">${inicialesUsuario}</span>
        <div>
            <strong>${usuario.nombreCompleto}</strong>
            <a href="<c:url value='/logout'/>">Cerrar sesión</a>
        </div>
    </div>
</header>

<main class="pl-pantalla">

    <section>
        <span class="pl-rotulo">Último paso</span>
        <h1 class="pl-titulo">¿Cómo repartimos<br>tu presupuesto?</h1>
        <p class="pl-bajada">
            Antes de empezar tu viaje a ${destino.ciudad.nombre}, cuéntanos
            cómo quieres distribuir tu dinero entre los gastos del viaje.
        </p>

        <c:if test="${not empty errores.general}">
            <div class="pl-aviso">${errores.general}</div>
        </c:if>
        <c:if test="${not empty errores.basicos}">
            <div class="pl-aviso">${errores.basicos}</div>
        </c:if>
        <c:if test="${guardado}">
            <div class="pl-aviso pl-aviso-ok">Tu reparto quedó guardado.</div>
        </c:if>

        <form action="<c:url value='/plan'/>" method="post" id="formPlan"
              data-total="${presupuesto.monto}"
              data-moneda="${presupuesto.moneda}"
              data-dias="${dias}"
              data-abrir="${not empty errores.general or not empty errores.basicos ? 'personalizado' : ''}">

            <div class="pl-opciones" role="radiogroup" aria-label="Forma de repartir el presupuesto">
                <label class="pl-opcion">
                    <input type="radio" name="modoReparto" value="auto">
                    <span class="pl-opcion-marca">
                        <svg viewBox="0 0 14 14" fill="none" aria-hidden="true">
                            <path d="M3 7.3l2.5 2.5L11 4.3" stroke="#fff" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </span>
                    <span class="pl-opcion-icono">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                            <circle cx="12" cy="12" r="9" stroke="#16161D" stroke-width="2.2"/>
                            <path d="M12 3v9l6.4 6.4" stroke="#16161D" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </span>
                    <strong>Háganlo ustedes</strong>
                    <span class="pl-opcion-texto">
                        Usamos el reparto que sugerimos para ${destino.ciudad.nombre}.
                    </span>
                </label>

                <label class="pl-opcion">
                    <input type="radio" name="modoReparto" value="personalizado">
                    <span class="pl-opcion-marca">
                        <svg viewBox="0 0 14 14" fill="none" aria-hidden="true">
                            <path d="M3 7.3l2.5 2.5L11 4.3" stroke="#fff" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </span>
                    <span class="pl-opcion-icono">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                            <path d="M4 7h16M4 12h16M4 17h16" stroke="#16161D" stroke-width="2.2" stroke-linecap="round"/>
                            <circle cx="9" cy="7" r="2.4" fill="#fff" stroke="#16161D" stroke-width="2.2"/>
                            <circle cx="16" cy="12" r="2.4" fill="#fff" stroke="#16161D" stroke-width="2.2"/>
                            <circle cx="7" cy="17" r="2.4" fill="#fff" stroke="#16161D" stroke-width="2.2"/>
                        </svg>
                    </span>
                    <strong>Lo personalizo yo</strong>
                    <span class="pl-opcion-texto">
                        Tú decides qué porcentaje va a cada tipo de gasto.
                    </span>
                </label>
            </div>

            <div class="pl-reparto" id="plReparto" hidden>

                <div class="pl-medidor" id="plMedidor">
                    <div class="pl-medidor-cifras">
                        <div>
                            <span class="pl-medidor-rotulo">Usado</span>
                            <span class="pl-medidor-cifra" id="plUsado">100%</span>
                        </div>
                        <div>
                            <span class="pl-medidor-rotulo">Disponible</span>
                            <span class="pl-medidor-cifra" id="plDisponible">0%</span>
                        </div>
                    </div>
                    <div class="pl-medidor-barra">
                        <span class="pl-hospedaje"    data-seg="hospedaje"    style="background:var(--c)"></span>
                        <span class="pl-alimentacion" data-seg="alimentacion" style="background:var(--c)"></span>
                        <span class="pl-transporte"   data-seg="transporte"   style="background:var(--c)"></span>
                        <span class="pl-actividades"  data-seg="actividades"  style="background:var(--c)"></span>
                        <span class="pl-imprevistos"  data-seg="imprevistos"  style="background:var(--c)"></span>
                    </div>
                    <p class="pl-exceso" id="plExceso" hidden></p>
                </div>

                <div class="pl-cat pl-hospedaje">
                    <div class="pl-cat-cabeza">
                        <label class="pl-cat-nombre" for="pct-hospedaje">Hospedaje</label>
                        <span class="pl-cat-monto" data-monto="hospedaje"></span>
                    </div>
                    <div class="pl-pildora">
                        <div class="pl-pct">
                            <input type="number" id="pct-hospedaje" name="hospedaje"
                                   min="0" max="100" step="1" inputmode="numeric"
                                   value="${reparto.hospedaje}" data-sugerido="${reparto.hospedaje}" readonly>
                            <span>%</span>
                        </div>
                        <div class="pl-pista" data-pista="hospedaje">
                            <span class="pl-pista-relleno" data-relleno="hospedaje"></span>
                        </div>
                    </div>
                </div>

                <div class="pl-cat pl-alimentacion">
                    <div class="pl-cat-cabeza">
                        <label class="pl-cat-nombre" for="pct-alimentacion">Alimentación</label>
                        <span class="pl-cat-monto" data-monto="alimentacion"></span>
                    </div>
                    <div class="pl-pildora">
                        <div class="pl-pct">
                            <input type="number" id="pct-alimentacion" name="alimentacion"
                                   min="0" max="100" step="1" inputmode="numeric"
                                   value="${reparto.alimentacion}" data-sugerido="${reparto.alimentacion}" readonly>
                            <span>%</span>
                        </div>
                        <div class="pl-pista" data-pista="alimentacion">
                            <span class="pl-pista-relleno" data-relleno="alimentacion"></span>
                        </div>
                    </div>
                </div>

                <div class="pl-cat pl-transporte">
                    <div class="pl-cat-cabeza">
                        <label class="pl-cat-nombre" for="pct-transporte">Transporte</label>
                        <span class="pl-cat-monto" data-monto="transporte"></span>
                    </div>
                    <div class="pl-pildora">
                        <div class="pl-pct">
                            <input type="number" id="pct-transporte" name="transporte"
                                   min="0" max="100" step="1" inputmode="numeric"
                                   value="${reparto.transporte}" data-sugerido="${reparto.transporte}" readonly>
                            <span>%</span>
                        </div>
                        <div class="pl-pista" data-pista="transporte">
                            <span class="pl-pista-relleno" data-relleno="transporte"></span>
                        </div>
                    </div>
                </div>

                <div class="pl-cat pl-actividades">
                    <div class="pl-cat-cabeza">
                        <label class="pl-cat-nombre" for="pct-actividades">Actividades</label>
                        <span class="pl-cat-monto" data-monto="actividades"></span>
                    </div>
                    <div class="pl-pildora">
                        <div class="pl-pct">
                            <input type="number" id="pct-actividades" name="actividades"
                                   min="0" max="100" step="1" inputmode="numeric"
                                   value="${reparto.actividades}" data-sugerido="${reparto.actividades}" readonly>
                            <span>%</span>
                        </div>
                        <div class="pl-pista" data-pista="actividades">
                            <span class="pl-pista-relleno" data-relleno="actividades"></span>
                        </div>
                    </div>
                </div>

                <div class="pl-cat pl-imprevistos">
                    <div class="pl-cat-cabeza">
                        <label class="pl-cat-nombre" for="pct-imprevistos">Imprevistos</label>
                        <span class="pl-cat-monto" data-monto="imprevistos"></span>
                    </div>
                    <div class="pl-pildora">
                        <div class="pl-pct">
                            <input type="number" id="pct-imprevistos" name="imprevistos"
                                   min="0" max="100" step="1" inputmode="numeric"
                                   value="${reparto.imprevistos}" data-sugerido="${reparto.imprevistos}" readonly>
                            <span>%</span>
                        </div>
                        <div class="pl-pista" data-pista="imprevistos">
                            <span class="pl-pista-relleno" data-relleno="imprevistos"></span>
                        </div>
                    </div>
                </div>

                <div class="pl-acciones">
                    <button type="submit" class="pl-btn" id="btnGuardar">Usar este reparto</button>
                    <button type="button" class="pl-btn-claro" id="btnRestaurar" hidden>Volver al sugerido</button>
                    <a class="pl-volver" href="<c:url value='/recomendaciones'/>">&larr; Cambiar destino</a>
                </div>
            </div>
        </form>
    </section>

    <aside>
        <div class="pl-sobre">
            <div class="pl-sobre-top">
                <span>WiseTrip · Plan de gastos</span>
                <span>${dias} días</span>
            </div>

            <div class="pl-sobre-cuerpo">
                <div>
                    <span class="pl-sobre-rotulo">Tu destino</span>
                    <span class="pl-sobre-ciudad">${destino.ciudad.nombre}</span>
                    <span class="pl-sobre-pais">${destino.ciudad.pais}</span>
                </div>

                <div class="pl-sobre-perforado"></div>

                <div class="pl-linea pl-hospedaje">
                    <span class="pl-linea-nombre"><span class="pl-linea-punto"></span>Hospedaje</span>
                    <span class="pl-linea-monto" data-sobre="hospedaje"></span>
                </div>
                <div class="pl-linea pl-alimentacion">
                    <span class="pl-linea-nombre"><span class="pl-linea-punto"></span>Alimentación</span>
                    <span class="pl-linea-monto" data-sobre="alimentacion"></span>
                </div>
                <div class="pl-linea pl-transporte">
                    <span class="pl-linea-nombre"><span class="pl-linea-punto"></span>Transporte</span>
                    <span class="pl-linea-monto" data-sobre="transporte"></span>
                </div>
                <div class="pl-linea pl-actividades">
                    <span class="pl-linea-nombre"><span class="pl-linea-punto"></span>Actividades</span>
                    <span class="pl-linea-monto" data-sobre="actividades"></span>
                </div>
                <div class="pl-linea pl-imprevistos">
                    <span class="pl-linea-nombre"><span class="pl-linea-punto"></span>Imprevistos</span>
                    <span class="pl-linea-monto" data-sobre="imprevistos"></span>
                </div>

                <div class="pl-sobre-total">
                    <span class="pl-sobre-total-rotulo">Total</span>
                    <span class="pl-sobre-total-cifra" id="plSobreTotal">${montoTotalFormateado} ${presupuesto.moneda}</span>
                </div>
            </div>

            <div class="pl-sobre-abajo">
                <span>Por día</span>
                <span id="plSobreDia">${porDiaTotal} ${presupuesto.moneda}</span>
            </div>
        </div>
    </aside>

</main>

<script src="/js/plan-reparto.js"></script>
</body>
</html>