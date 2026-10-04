<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Destinos recomendados | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/recomendaciones.css'/>">
</head>
<body class="rc">

<header class="rc-cabecera">
    <a class="rc-marca" href="<c:url value='/'/>">
        <img src="/img/portada/logo.png" alt="">
        <span>Wise<em>Trip</em></span>
    </a>

    <nav class="rc-pasos">
        <span class="rc-paso rc-paso-hecho">
            <span class="rc-paso-check">
                <svg viewBox="0 0 12 12" fill="none" aria-hidden="true">
                    <path d="M2.5 6.3l2.3 2.3L9.5 3.6" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </span>
            Origen
        </span>
        <span class="rc-linea"></span>
        <span class="rc-paso rc-paso-hecho">
            <span class="rc-paso-check">
                <svg viewBox="0 0 12 12" fill="none" aria-hidden="true">
                    <path d="M2.5 6.3l2.3 2.3L9.5 3.6" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </span>
            Preferencias
        </span>
        <span class="rc-linea"></span>
        <span class="rc-paso rc-paso-hecho">
            <span class="rc-paso-check">
                <svg viewBox="0 0 12 12" fill="none" aria-hidden="true">
                    <path d="M2.5 6.3l2.3 2.3L9.5 3.6" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </span>
            Fechas
        </span>
        <span class="rc-linea"></span>
        <span class="rc-paso rc-paso-hecho">
            <span class="rc-paso-check">
                <svg viewBox="0 0 12 12" fill="none" aria-hidden="true">
                    <path d="M2.5 6.3l2.3 2.3L9.5 3.6" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </span>
            Presupuesto
        </span>
        <span class="rc-linea"></span>
        <span class="rc-paso rc-paso-activo"><span class="rc-paso-num">5</span> Destinos</span>
    </nav>

    <div class="rc-usuario">
        <span class="rc-avatar">${inicialesUsuario}</span>
        <div>
            <strong>${usuario.nombreCompleto}</strong>
            <a href="<c:url value='/logout'/>">Cerrar sesión</a>
        </div>
    </div>
</header>

<main class="rc-pantalla">

    <section class="rc-portada">

        <div class="rc-collage" aria-hidden="true">
            <figure class="rc-foto" data-tipo="playa">
                <img src="/img/recomendaciones/playa.jpg" alt="">
                <span class="rc-foto-sello">Playa</span>
            </figure>
            <figure class="rc-foto" data-tipo="montana">
                <img src="/img/recomendaciones/montana.jpg" alt="">
                <span class="rc-foto-sello">Montaña</span>
            </figure>
            <figure class="rc-foto" data-tipo="aventura">
                <img src="/img/recomendaciones/aventura.jpg" alt="">
                <span class="rc-foto-sello">Aventura</span>
            </figure>
            <figure class="rc-foto" data-tipo="sabores">
                <img src="/img/recomendaciones/sabores.jpg" alt="">
                <span class="rc-foto-sello">Sabores</span>
            </figure>
            <figure class="rc-foto" data-tipo="descanso">
                <img src="/img/recomendaciones/descanso.jpg" alt="">
                <span class="rc-foto-sello">Descanso</span>
            </figure>
            <figure class="rc-foto" data-tipo="fiesta">
                <img src="/img/recomendaciones/fiesta.jpg" alt="">
                <span class="rc-foto-sello">Fiesta</span>
            </figure>
        </div>

        <div class="rc-portada-velo"></div>

        <div class="rc-cabeza">
            <span class="rc-sello">Resultado de tu búsqueda</span>
            <h1 class="rc-titulo">Tus destinos</h1>
            <p class="rc-bajada">${seleccion.mensaje}</p>

            <div class="rc-ficha">
                <div>
                    ${ubicacion.ciudad} &rarr; Tus destinos<br>
                    <c:if test="${not empty fechas}">${fechas.fechaInicio} — ${fechas.fechaFin}</c:if>
                    <c:if test="${not empty presupuesto}"> · ${presupuesto.moneda} ${presupuesto.monto}</c:if>
                </div>
                <a class="rc-ficha-editar" href="<c:url value='/resumen'/>">Editar búsqueda</a>
            </div>

            <p class="rc-portada-nota" id="rcNotaCollage">
                A color, lo que tienen en común tus destinos.
            </p>
        </div>

    </section>

    <c:choose>

        <c:when test="${seleccion.vacio}">
            <div class="rc-vacio">
                <span class="rc-vacio-rotulo">Sin destinos disponibles</span>
                <h2>No encontramos coincidencias</h2>
                <p>
                    Ninguna ciudad se ajusta a tu presupuesto y tus preferencias.
                    Prueba ampliando el presupuesto o cambiando algunas respuestas.
                </p>
                <a class="rc-elegir" href="<c:url value='/presupuesto'/>">Ajustar presupuesto</a>
            </div>
        </c:when>

        <c:otherwise>
            <div class="rc-destinos">
                <c:forEach var="r" items="${seleccion.destinos}" varStatus="pos">

                    <article class="rc-destino" data-ciudad="${r.ciudad.nombre}">

                        <div class="rc-posicion">${pos.index + 1}</div>

                        <div class="rc-cuerpo">
                            <span class="rc-pais">${r.ciudad.pais}</span>
                            <span class="rc-ciudad">${r.ciudad.nombre}</span>

                            <div class="rc-barras">
                                <div>
                                    <div class="rc-barra-cabeza">
                                        <span>Presupuesto</span>
                                        <span class="rc-barra-valor">
                                            <fmt:formatNumber value="${r.puntajePresupuesto * 100}" maxFractionDigits="0"/>%
                                        </span>
                                    </div>
                                    <div class="rc-barra rc-barra-presupuesto">
                                        <span data-ancho="${r.puntajePresupuesto * 100}"></span>
                                    </div>
                                </div>
                                <div>
                                    <div class="rc-barra-cabeza">
                                        <span>Gustos</span>
                                        <span class="rc-barra-valor">
                                            <fmt:formatNumber value="${r.puntajePreferencias * 100}" maxFractionDigits="0"/>%
                                        </span>
                                    </div>
                                    <div class="rc-barra rc-barra-gustos">
                                        <span data-ancho="${r.puntajePreferencias * 100}"></span>
                                    </div>
                                </div>
                            </div>

                            <a class="rc-elegir" href="<c:url value='/destino/${r.ciudad.id}'/>">
                                Elegir este destino &rarr;
                            </a>
                        </div>

                        <div class="rc-talon">
                            <span class="rc-talon-rotulo">Coincidencia</span>
                            <span class="rc-talon-puntaje">
                                <fmt:formatNumber value="${r.puntajeTotal * 100}" maxFractionDigits="0"/>%
                            </span>
                            <c:if test="${pos.index == 0}">
                                <span class="rc-talon-nota">Mejor opción</span>
                            </c:if>
                        </div>

                    </article>

                </c:forEach>
            </div>
        </c:otherwise>

    </c:choose>

    <div class="rc-pie">
        <p class="rc-nota">
            La coincidencia combina qué tanto se ajusta el costo del destino a tu
            presupuesto (40%) y cuántas de tus preferencias cumple (60%).
        </p>
        <a class="rc-volver" href="<c:url value='/resumen'/>">&larr; Volver al resumen</a>
    </div>

</main>

<script>
    document.querySelectorAll('.rc-barra span[data-ancho]').forEach(b => {
        b.style.width = b.dataset.ancho + '%';
    });
</script>

<script>
    /* Solo visual: enciende en el collage los tipos de viaje que tienen
       los destinos recomendados. Los tipos salen de los atributos de
       cada ciudad en el catálogo. No cambia nada de la recomendación. */
    (function () {
        const TIPOS = {
            'Ciudad de México': ['sabores', 'fiesta'],
            'Cancún': ['playa', 'descanso'],
            'Guadalajara': ['sabores', 'descanso', 'fiesta'],
            'Ciudad de Guatemala': ['fiesta'],
            'Flores': ['aventura', 'descanso'],
            'Tegucigalpa': ['fiesta'],
            'Roatán': ['playa', 'descanso'],
            'San Salvador': ['sabores', 'fiesta'],
            'Santa Ana': ['descanso'],
            'Managua': ['fiesta'],
            'Granada': ['descanso'],
            'San José': ['descanso', 'fiesta'],
            'La Fortuna': ['aventura', 'descanso'],
            'Ciudad de Panamá': ['fiesta'],
            'Bocas del Toro': ['playa', 'aventura', 'descanso'],
            'Belize City': ['aventura'],
            'Bogotá': ['sabores', 'fiesta'],
            'Medellín': ['sabores', 'fiesta'],
            'Cartagena de Indias': ['playa', 'sabores'],
            'Caracas': ['fiesta'],
            'Porlamar / Isla de Margarita': ['playa', 'descanso'],
            'La Habana': ['fiesta'],
            'Santiago de Cuba': ['descanso', 'fiesta'],
            'Santo Domingo': ['fiesta'],
            'Punta Cana': ['playa', 'descanso'],
            'Quito': ['montana'],
            'Guayaquil': ['sabores', 'fiesta'],
            'Lima': ['sabores', 'fiesta'],
            'Cusco': ['montana', 'aventura'],
            'La Paz': ['montana', 'aventura'],
            'Santa Cruz de la Sierra': ['sabores', 'descanso', 'fiesta'],
            'Río de Janeiro': ['fiesta'],
            'São Paulo': ['sabores', 'fiesta'],
            'Brasilia': ['descanso', 'fiesta'],
            'Santiago de Chile': ['montana', 'sabores', 'fiesta'],
            'San Pedro de Atacama': ['aventura', 'descanso'],
            'Chillán / Nevados de Chillán': ['montana', 'descanso'],
            'Buenos Aires': ['sabores', 'fiesta'],
            'Mendoza': ['montana', 'sabores', 'descanso'],
            'San Carlos de Bariloche': ['montana', 'descanso'],
            'Montevideo': ['sabores', 'descanso', 'fiesta'],
            'Punta del Este': ['descanso'],
            'Asunción': ['descanso', 'fiesta'],
            'Ciudad del Este': ['fiesta']
        };

        const activos = new Set();
        document.querySelectorAll('.rc-destino[data-ciudad]').forEach(d => {
            (TIPOS[d.dataset.ciudad.trim()] || []).forEach(t => activos.add(t));
        });

        if (activos.size === 0) {
            document.getElementById('rcNotaCollage').hidden = true;
            return;
        }

        document.querySelectorAll('.rc-foto').forEach(f => {
            f.classList.toggle('rc-foto-apagada', !activos.has(f.dataset.tipo));
        });
    })();
</script>

</body>
</html>