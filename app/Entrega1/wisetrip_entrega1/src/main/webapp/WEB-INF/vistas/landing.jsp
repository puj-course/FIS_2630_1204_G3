<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>WiseTrip — Planea tu viaje por Latinoamérica sin gastar de más</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="<c:url value='/css/landing.css'/>">
</head>
<body class="lp">

<section class="lp-hero">

    <div class="lp-hero-fondos">
        <div class="lp-hero-foto" style="background-image:url('/img/portada/Cartegena.jpg')"></div>
        <div class="lp-hero-foto" style="background-image:url('/img/portada/Rio.jpg')"></div>
        <div class="lp-hero-foto" style="background-image:url('/img/portada/Argentina.jpg')"></div>
    </div>

    <nav class="lp-nav">
        <a class="lp-marca" href="<c:url value='/'/>">
            <img src="/img/portada/logo.png" alt="">
            <span>Wise<em>Trip</em></span>
        </a>
        <div class="lp-nav-links">
            <a href="#destinos">Destinos</a>
            <a href="#como-funciona">Cómo funciona</a>
            <a href="<c:url value='/login'/>">Iniciar sesión</a>
            <a class="lp-btn" href="<c:url value='/registro'/>">Crear cuenta</a>
        </div>
    </nav>

    <div class="lp-hero-contenido">
        <span class="lp-sello">20 países de Latinoamérica</span>

        <h1 class="lp-titulo">
            Viaja lejos<br>
            sin <span class="resalte">gastar</span> de más
        </h1>

        <p class="lp-bajada">
            Cuéntanos de dónde sales, cuánto tienes y qué te gusta hacer.
            WiseTrip reparte tu presupuesto y te recomienda los destinos
            de la región que sí puedes pagar.
        </p>

        <div class="lp-acciones">
            <a class="lp-btn-morado" href="<c:url value='/registro'/>">Empezar a planear</a>
            <a class="lp-btn-blanco" href="<c:url value='/login'/>">Ya tengo cuenta</a>
        </div>
    </div>

    <div class="lp-hero-pie">
        <span>Cartagena · Colombia</span>
        <span>Río de Janeiro · Brasil</span>
        <span>Buenos Aires · Argentina</span>
    </div>

</section>

<section class="lp-cifras">
    <div class="lp-cifra">
        <span class="lp-cifra-num">20</span>
        <span class="lp-cifra-lbl">Países de<br>Latinoamérica</span>
    </div>
    <div class="lp-cifra">
        <span class="lp-cifra-num">19</span>
        <span class="lp-cifra-lbl">Monedas<br>soportadas</span>
    </div>
    <div class="lp-cifra">
        <span class="lp-cifra-num">41</span>
        <span class="lp-cifra-lbl">Preguntas para<br>tu perfil</span>
    </div>
    <div class="lp-cifra">
        <span class="lp-cifra-num">3</span>
        <span class="lp-cifra-lbl">Destinos<br>a tu medida</span>
    </div>
</section>

<div class="lp-contenedor">

    <section class="lp-seccion" id="destinos">
        <div class="lp-seccion-cabeza">
            <div>
                <span class="lp-rotulo">Inspírate</span>
                <h2 class="lp-seccion-titulo">Destinos que<br>te están esperando</h2>
            </div>
            <p class="lp-seccion-nota">
                Playas, ciudades con historia y montaña. Te mostramos solo
                los que caben en tu bolsillo.
            </p>
        </div>

        <div class="lp-destinos">
            <a class="lp-destino" href="<c:url value='/registro'/>"
               style="background-image:url('/img/portada/Cartegena.jpg')">
                <span class="lp-destino-pais">Colombia</span>
                <div class="lp-destino-info">
                    <span class="lp-destino-nombre">Cartagena</span>
                    <span class="lp-destino-tags">Playa · Ciudad amurallada · Gastronomía</span>
                </div>
            </a>

            <a class="lp-destino" href="<c:url value='/registro'/>"
               style="background-image:url('/img/portada/Argentina.jpg')">
                <span class="lp-destino-pais">Argentina</span>
                <div class="lp-destino-info">
                    <span class="lp-destino-nombre">Buenos Aires</span>
                    <span class="lp-destino-tags">Museos · Vida nocturna · Teatro</span>
                </div>
            </a>

            <a class="lp-destino" href="<c:url value='/registro'/>"
               style="background-image:url('/img/portada/Peru.jpg')">
                <span class="lp-destino-pais">Perú</span>
                <div class="lp-destino-info">
                    <span class="lp-destino-nombre">Cusco</span>
                    <span class="lp-destino-tags">Ruinas · Montaña · Cultura</span>
                </div>
            </a>
        </div>

        <div class="lp-experiencias">
            <div class="lp-experiencia">
                <div class="lp-experiencia-foto"
                     style="background-image:url('/img/portada/Amazonas.jpg')"></div>
                <div>
                    <span class="lp-experiencia-temporada">Jun — Nov</span>
                    <strong>Navega la Amazonía</strong>
                    <p>Selva, río y comunidades</p>
                </div>
            </div>

            <div class="lp-experiencia">
                <div class="lp-experiencia-foto"
                     style="background-image:url('/img/portada/desierto.jpg')"></div>
                <div>
                    <span class="lp-experiencia-temporada">Mar — Nov</span>
                    <strong>Cielos del desierto</strong>
                    <p>Paisajes áridos y estrellas</p>
                </div>
            </div>

            <div class="lp-experiencia">
                <div class="lp-experiencia-foto"
                     style="background-image:url('/img/portada/Rio.jpg')"></div>
                <div>
                    <span class="lp-experiencia-temporada">Feb</span>
                    <strong>Carnaval en Río</strong>
                    <p>La fiesta más grande del mundo</p>
                </div>
            </div>
        </div>
    </section>

    <section class="lp-seccion" id="como-funciona">
        <span class="lp-rotulo">En menos de cinco minutos</span>
        <h2 class="lp-seccion-titulo" style="margin-bottom:34px">Cómo funciona</h2>

        <div class="lp-pasos-linea">
            <span class="lp-paso-circulo">1</span>
            <span class="lp-paso-circulo">2</span>
            <span class="lp-paso-circulo">3</span>
            <span class="lp-paso-circulo">4</span>
        </div>

        <div class="lp-pasos">
            <article class="lp-paso">
                <svg viewBox="0 0 24 24" fill="none">
                    <path d="M12 22s7-6.2 7-11.2A7 7 0 0 0 5 10.8C5 15.8 12 22 12 22z"
                          stroke="#16161D" stroke-width="2.2"/>
                    <circle cx="12" cy="10.4" r="2.6" fill="#16161D"/>
                </svg>
                <h3>Dinos de dónde sales</h3>
                <p>Tu ciudad de origen define las rutas, el transporte y lo que te cuesta llegar.</p>
            </article>

            <article class="lp-paso">
                <svg viewBox="0 0 24 24" fill="none">
                    <path d="M12 20.5S3.5 14.8 3.5 9.2a4.7 4.7 0 0 1 8.5-2.8 4.7 4.7 0 0 1 8.5 2.8c0 5.6-8.5 11.3-8.5 11.3z"
                          stroke="#16161D" stroke-width="2.2" stroke-linejoin="round"/>
                </svg>
                <h3>Cuéntanos qué te gusta</h3>
                <p>Playa o montaña, museos o vida nocturna, lujo o mochilero. Responde sí o no.</p>
            </article>

            <article class="lp-paso">
                <svg viewBox="0 0 24 24" fill="none">
                    <rect x="3" y="5" width="18" height="16" rx="2.5" stroke="#16161D" stroke-width="2.2"/>
                    <path d="M3 10h18M8 2.5v5M16 2.5v5" stroke="#16161D" stroke-width="2.2" stroke-linecap="round"/>
                </svg>
                <h3>Pon fechas y presupuesto</h3>
                <p>En tu moneda local. Nosotros hacemos la conversión para comparar destinos.</p>
            </article>

            <article class="lp-paso">
                <svg viewBox="0 0 24 24" fill="none">
                    <path d="M21.5 2.5 2.5 10.2l8 3.3 3.3 8z" stroke="#16161D"
                          stroke-width="2.2" stroke-linejoin="round"/>
                </svg>
                <h3>Recibe tus destinos</h3>
                <p>Tres ciudades ordenadas por qué tanto coinciden contigo y con tu bolsillo.</p>
            </article>
        </div>
    </section>

    <section class="lp-cierre">
        <div class="lp-cierre-texto">
            <h2>Tu próximo<br>viaje empieza aquí</h2>
            <p>Crear la cuenta es gratis y toma menos de un minuto.</p>
            <a class="lp-btn" href="<c:url value='/registro'/>">Crear mi cuenta</a>
        </div>

        <div class="lp-polaroids">
            <div class="lp-polaroid">
                <img src="/img/portada/Amazonas.jpg" alt="">
                <span>Amazonas · COL</span>
            </div>
            <div class="lp-polaroid">
                <img src="/img/portada/Peru.jpg" alt="">
                <span>Cusco · PER</span>
            </div>
        </div>
    </section>

    <footer class="lp-pie">
        <span>WiseTrip · Proyecto académico</span>
        <span>Fundamentos de Ingeniería de Software · Grupo 3</span>
    </footer>

</div>

</body>
</html>