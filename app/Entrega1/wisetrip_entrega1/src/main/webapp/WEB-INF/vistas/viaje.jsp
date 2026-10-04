<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tu viaje a ${destino.ciudad.nombre} | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/viaje.css'/>">
</head>
<body class="vj">

<header class="vj-cabecera">
    <a class="vj-marca" href="<c:url value='/'/>">
        <img src="/img/portada/logo.png" alt="">
        <span>Wise<em>Trip</em></span>
    </a>

    <nav class="vj-menu">
        <a class="vj-menu-activo" href="<c:url value='/viaje'/>">Mi viaje</a>
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
    <section class="vj-portada">
        <div class="vj-portada-texto">
            <span class="vj-sello">Tu viaje está en marcha</span>
            <h1 class="vj-titulo">${destino.ciudad.nombre}</h1>
            <p class="vj-bajada">
                ${destino.ciudad.pais} te espera. Aquí vas a armar todo tu viaje:
                dónde dormir, cómo moverte, dónde comer y qué hacer, siempre
                dentro de lo que separaste para cada gasto.
            </p>
            <div class="vj-portada-acciones">
                <a class="vj-btn" href="#arma-tu-viaje">Empezar a armar</a>
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
    </section>

    <!-- ===== Presupuesto ===== -->
    <section class="vj-presupuesto">
        <div class="vj-presupuesto-cifras">
            <div>
                <span class="vj-rotulo">Presupuesto total</span>
                <strong class="vj-cifra" id="vjTotal"></strong>
            </div>
            <div>
                <span class="vj-rotulo">Por día</span>
                <strong class="vj-cifra vj-cifra-chica" id="vjPorDia"></strong>
            </div>
        </div>

        <div class="vj-reparto-barra">
            <span class="vj-hospedaje"    data-seg="hospedaje"></span>
            <span class="vj-alimentacion" data-seg="alimentacion"></span>
            <span class="vj-transporte"   data-seg="transporte"></span>
            <span class="vj-actividades"  data-seg="actividades"></span>
            <span class="vj-imprevistos"  data-seg="imprevistos"></span>
        </div>

        <ul class="vj-leyenda">
            <li class="vj-hospedaje"><span class="vj-punto"></span>Hospedaje <b data-pct="hospedaje"></b></li>
            <li class="vj-alimentacion"><span class="vj-punto"></span>Alimentación <b data-pct="alimentacion"></b></li>
            <li class="vj-transporte"><span class="vj-punto"></span>Transporte <b data-pct="transporte"></b></li>
            <li class="vj-actividades"><span class="vj-punto"></span>Actividades <b data-pct="actividades"></b></li>
            <li class="vj-imprevistos"><span class="vj-punto"></span>Imprevistos <b data-pct="imprevistos"></b></li>
        </ul>
    </section>

    <!-- ===== Módulos para las APIs ===== -->
    <section class="vj-modulos-seccion" id="arma-tu-viaje">
        <div class="vj-seccion-cabeza">
            <h2 class="vj-seccion-titulo">Arma tu viaje</h2>
            <p class="vj-seccion-nota">
                Cada módulo busca opciones en ${destino.ciudad.nombre} que quepan
                en lo que separaste para ese gasto.
            </p>
        </div>

        <div class="vj-modulos">

            <article class="vj-modulo vj-hospedaje">
                <div class="vj-modulo-cabeza">
                    <span class="vj-modulo-icono">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                            <path d="M3 19V6M3 15h18v4M21 15v-3a3 3 0 0 0-3-3h-7v6" stroke="#16161D" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
                            <circle cx="7" cy="11" r="2" stroke="#16161D" stroke-width="2.2"/>
                        </svg>
                    </span>
                    <h3>Hospedaje</h3>
                    <span class="vj-pronto">Próximamente</span>
                </div>
                <p>Hoteles, hostales y apartamentos comparados por precio por noche y ubicación.</p>
                <div class="vj-modulo-cifras">
                    <div>
                        <span class="vj-rotulo">Separaste</span>
                        <strong data-monto="hospedaje"></strong>
                    </div>
                    <div>
                        <span class="vj-rotulo">Te alcanza por noche</span>
                        <strong data-por="hospedaje" data-divisor="noches"></strong>
                    </div>
                </div>
                <button type="button" class="vj-modulo-btn" disabled>Buscar hospedaje</button>
            </article>

            <article class="vj-modulo vj-transporte">
                <div class="vj-modulo-cabeza">
                    <span class="vj-modulo-icono">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                            <path d="M21 15.5 13.5 11V5.5a1.5 1.5 0 0 0-3 0V11L3 15.5v2l7.5-2.2V19L8.5 20.5V22l3.5-1 3.5 1v-1.5L13.5 19v-3.7l7.5 2.2z" stroke="#16161D" stroke-width="2" stroke-linejoin="round"/>
                        </svg>
                    </span>
                    <h3>Transporte</h3>
                    <span class="vj-pronto">Próximamente</span>
                </div>
                <p>Vuelos, buses y traslados para llegar y moverte por la ciudad.</p>
                <div class="vj-modulo-cifras">
                    <div>
                        <span class="vj-rotulo">Separaste</span>
                        <strong data-monto="transporte"></strong>
                    </div>
                    <div>
                        <span class="vj-rotulo">Por día</span>
                        <strong data-por="transporte" data-divisor="dias"></strong>
                    </div>
                </div>
                <button type="button" class="vj-modulo-btn" disabled>Buscar transporte</button>
            </article>

            <article class="vj-modulo vj-alimentacion">
                <div class="vj-modulo-cabeza">
                    <span class="vj-modulo-icono">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                            <path d="M7 3v8M4.5 3v5a2.5 2.5 0 0 0 5 0V3M7 11v10M17 21V3c-2.2 0-3.5 2.5-3.5 6v4H17" stroke="#16161D" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </span>
                    <h3>Restaurantes</h3>
                    <span class="vj-pronto">Próximamente</span>
                </div>
                <p>Dónde comer según tu presupuesto diario, desde comida local hasta cenas especiales.</p>
                <div class="vj-modulo-cifras">
                    <div>
                        <span class="vj-rotulo">Separaste</span>
                        <strong data-monto="alimentacion"></strong>
                    </div>
                    <div>
                        <span class="vj-rotulo">Por día</span>
                        <strong data-por="alimentacion" data-divisor="dias"></strong>
                    </div>
                </div>
                <button type="button" class="vj-modulo-btn" disabled>Buscar restaurantes</button>
            </article>

            <article class="vj-modulo vj-actividades">
                <div class="vj-modulo-cabeza">
                    <span class="vj-modulo-icono">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                            <path d="M3 20 9.5 8l4 7 2.5-4 5 9z" stroke="#16161D" stroke-width="2.2" stroke-linejoin="round"/>
                            <circle cx="17.5" cy="5.5" r="2.2" stroke="#16161D" stroke-width="2.2"/>
                        </svg>
                    </span>
                    <h3>Actividades</h3>
                    <span class="vj-pronto">Próximamente</span>
                </div>
                <p>Tours, planes al aire libre y experiencias que coinciden con tus gustos.</p>
                <div class="vj-modulo-cifras">
                    <div>
                        <span class="vj-rotulo">Separaste</span>
                        <strong data-monto="actividades"></strong>
                    </div>
                    <div>
                        <span class="vj-rotulo">Por día</span>
                        <strong data-por="actividades" data-divisor="dias"></strong>
                    </div>
                </div>
                <button type="button" class="vj-modulo-btn" disabled>Buscar actividades</button>
            </article>

        </div>

        <div class="vj-colchon vj-imprevistos">
            <span class="vj-modulo-icono">
                <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M12 3 4 6v6c0 4.5 3.4 8 8 9 4.6-1 8-4.5 8-9V6z" stroke="#16161D" stroke-width="2.2" stroke-linejoin="round"/>
                    <path d="M8.5 12l2.5 2.5L15.5 10" stroke="#16161D" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </span>
            <div>
                <strong>Colchón para imprevistos</strong>
                <p>Este dinero no se toca a menos que surja algo fuera del plan.</p>
            </div>
            <span class="vj-colchon-cifra" data-monto="imprevistos"></span>
        </div>
    </section>

    <footer class="vj-pie">
        <span>WiseTrip · ${ubicacionCiudad} &rarr; ${destino.ciudad.nombre}</span>
        <a href="<c:url value='/recomendaciones'/>">&larr; Elegir otro destino</a>
    </footer>

</main>

<script src="/js/viaje.js"></script>
</body>
</html>