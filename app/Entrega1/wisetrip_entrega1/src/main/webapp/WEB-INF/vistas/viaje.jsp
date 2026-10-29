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
        <a href="<c:url value='/itinerario'/>">Itinerario</a>
        <a href="<c:url value='/itinerario'/>">Antes de viajar</a>
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
                <a class="vj-btn" href="<c:url value='/itinerario'/>">Organizar mi itinerario</a>
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
        <a class="vj-resumen-tarjeta vj-actividades" href="<c:url value='/itinerario'/>">
            <span class="vj-rotulo">Itinerario</span>
            <strong>${dias} días</strong>
            <span>Del ${fechas.fechaInicio} al ${fechas.fechaFin}</span>
        </a>
        <a class="vj-resumen-tarjeta vj-transporte" href="<c:url value='/itinerario'/>">
            <span class="vj-rotulo">Antes de viajar</span>
            <strong>Checklist</strong>
            <span>Lo que no puede faltar y consejos</span>
        </a>
    </div>
    </section>

    <!-- ===== Presupuesto ===== -->
    <section class="vj-presupuesto" id="presupuesto">
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

        <div class="vj-colchon vj-imprevistos">
            <span class="vj-colchon-icono">
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

</div>

<script src="/js/viaje.js"></script>
</body>
</html>