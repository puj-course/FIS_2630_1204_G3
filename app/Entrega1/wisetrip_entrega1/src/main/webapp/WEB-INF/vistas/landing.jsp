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

<div class="lp-contenedor">

    <nav class="lp-nav">
        <div class="lp-marca">
            <svg width="30" height="30" viewBox="0 0 32 32" fill="none">
                <circle cx="16" cy="16" r="14" fill="#FFC627" stroke="#16161D" stroke-width="2.5"/>
                <path d="M6 17l18-8-4 8 4 8-18-8z" fill="#5B3FA8" stroke="#16161D" stroke-width="2"
                      stroke-linejoin="round"/>
            </svg>
            WiseTrip
        </div>
        <div class="lp-nav-links">
            <a href="<c:url value='/login'/>">Iniciar sesión</a>
            <a class="lp-btn" href="<c:url value='/registro'/>">Crear cuenta</a>
        </div>
    </nav>

    <section class="lp-hero">

        <div>
            <span class="lp-sello">20 países de Latinoamérica</span>

            <h1 class="lp-titulo">
                Viaja lejos<br>
                sin <span class="subrayado">gastar</span><br>
                de más
            </h1>

            <p class="lp-bajada">
                Cuéntanos de dónde sales, cuánto tienes y qué te gusta hacer.
                WiseTrip reparte tu presupuesto y te recomienda los destinos
                de la región que sí puedes pagar.
            </p>

            <div class="lp-acciones">
                <a class="lp-btn" href="<c:url value='/registro'/>">Empezar a planear</a>
                <a class="lp-btn-claro" href="<c:url value='/login'/>">Ya tengo cuenta</a>
            </div>
        </div>

        <div class="lp-mapa">
            <svg viewBox="0 0 400 530" xmlns="http://www.w3.org/2000/svg" role="img"
                 aria-label="Mapa ilustrado de Latinoamérica">

                <!-- Camino invisible que sigue el rotulo del continente -->
                <defs>
                    <path id="curvaLatam"
                          d="M64 300 q6 100 64 160 q58 54 130 40"
                          fill="none"/>
                </defs>

                <!-- Mar -->
                <rect x="0" y="0" width="400" height="530" rx="10" fill="#6BB8E8"/>

                <!-- Olas decorativas -->
                <path d="M20 440q10-7 20 0t20 0" stroke="#fff" stroke-width="2.5" fill="none" opacity=".7"/>
                <path d="M20 458q10-7 20 0t20 0" stroke="#fff" stroke-width="2.5" fill="none" opacity=".7"/>
                <path d="M330 90q10-7 20 0t20 0" stroke="#fff" stroke-width="2.5" fill="none" opacity=".7"/>

                <!-- Sol -->
                <circle cx="345" cy="52" r="20" fill="#FFC627" stroke="#16161D" stroke-width="2.5"/>
                <g stroke="#16161D" stroke-width="2.5" stroke-linecap="round">
                    <path d="M345 20v-8M345 92v8M313 52h-8M385 52h8M322 29l-6-6M368 75l6 6M368 29l6-6M322 75l-6 6"/>
                </g>

                <!-- Continente -->
                <path d="M28 62 L92 36 L128 52 L152 46 L168 72 L152 92 L176 104 L156 118
                         L172 140 L198 160 L218 174 L250 176 L292 168 L330 190 L345 228
                         L352 268 L332 318 L302 352 L288 398 L272 438 L252 502
                         L236 468 L226 418 L218 372 L204 326 L192 282 L180 246
                         L190 210 L206 186 L196 176 L170 150 L150 128 L138 112
                         L118 96 L92 86 L60 78 Z"
                      fill="#56A860" stroke="#16161D" stroke-width="3" stroke-linejoin="round"/>

                <!-- Selva amazónica -->
                <path d="M240 220 q40-10 70 14 q14 40-10 62 q-46 16-70-10 q-14-40 10-66z"
                      fill="#3E8A4B" opacity=".85"/>

                <!-- Cordillera de los Andes -->
                <g fill="#8A6FD4" stroke="#16161D" stroke-width="2" stroke-linejoin="round">
                    <path d="M200 250 l13-22 13 22z"/>
                    <path d="M208 296 l13-22 13 22z"/>
                    <path d="M214 344 l13-22 13 22z"/>
                    <path d="M222 392 l13-22 13 22z"/>
                </g>

                <!-- Desierto / cactus -->
                <g stroke="#16161D" stroke-width="2.5" stroke-linecap="round">
                    <path d="M78 62v18" stroke="#2FBFAF"/>
                    <path d="M78 70h-7v-7M78 74h7v-8" stroke="#2FBFAF"/>
                </g>

                <!-- Playa: palmera -->
                <g>
                    <path d="M300 196v22" stroke="#16161D" stroke-width="3" stroke-linecap="round"/>
                    <path d="M300 196q-14-8-20 2M300 196q14-8 20 2M300 196q-4-14 4-16"
                          stroke="#16161D" stroke-width="2.5" fill="none" stroke-linecap="round"/>
                </g>

                <!-- Avión -->
                <g transform="translate(60 150) rotate(-18)">
                    <path d="M0 10l34-14-7 14 7 14z" fill="#E8452C" stroke="#16161D"
                          stroke-width="2.5" stroke-linejoin="round"/>
                </g>
                <path d="M40 176q30 16 62 8" stroke="#fff" stroke-width="2.5"
                      stroke-dasharray="6 7" fill="none" stroke-linecap="round"/>

                <!-- Pines de ciudades -->
                <g>
                    <!-- Ciudad de México -->
                    <path d="M104 78c0-8 7-15 15-15s15 7 15 15c0 11-15 24-15 24s-15-13-15-24z"
                          fill="#5B3FA8" stroke="#16161D" stroke-width="2.5"/>
                    <circle cx="119" cy="78" r="5" fill="#EFE9DC"/>

                    <!-- Cartagena -->
                    <path d="M228 186c0-8 7-15 15-15s15 7 15 15c0 11-15 24-15 24s-15-13-15-24z"
                          fill="#E8452C" stroke="#16161D" stroke-width="2.5"/>
                    <circle cx="243" cy="186" r="5" fill="#EFE9DC"/>

                    <!-- Río de Janeiro -->
                    <path d="M300 282c0-8 7-15 15-15s15 7 15 15c0 11-15 24-15 24s-15-13-15-24z"
                          fill="#FFC627" stroke="#16161D" stroke-width="2.5"/>
                    <circle cx="315" cy="282" r="5" fill="#16161D"/>

                    <!-- Cusco -->
                    <path d="M188 300c0-8 7-15 15-15s15 7 15 15c0 11-15 24-15 24s-15-13-15-24z"
                          fill="#2FBFAF" stroke="#16161D" stroke-width="2.5"/>
                    <circle cx="203" cy="300" r="5" fill="#16161D"/>

                    <!-- Bariloche -->
                    <path d="M212 412c0-8 7-15 15-15s15 7 15 15c0 11-15 24-15 24s-15-13-15-24z"
                          fill="#F3A6C0" stroke="#16161D" stroke-width="2.5"/>
                    <circle cx="227" cy="412" r="5" fill="#16161D"/>
                </g>

                <!-- Etiqueta de precio -->
                <g transform="translate(276 356) rotate(-6)">
                    <rect x="0" y="0" width="104" height="38" rx="8" fill="#fff"
                          stroke="#16161D" stroke-width="2.5"/>
                    <text x="52" y="17" text-anchor="middle" font-family="IBM Plex Mono, monospace"
                          font-size="9" fill="#5B5B66">TU PRESUPUESTO</text>
                    <text x="52" y="31" text-anchor="middle" font-family="Archivo Black, sans-serif"
                          font-size="13" fill="#16161D">SÍ ALCANZA</text>
                </g>

                <!-- Rótulo del continente, siguiendo la costa -->
                <text font-family="Archivo Black, sans-serif" font-size="19"
                      fill="#16161D" opacity=".8" letter-spacing="2.5">
                    <textPath href="#curvaLatam" startOffset="8%">LATINOAMÉRICA</textPath>
                </text>
            </svg>
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
            <span class="lp-cifra-num">26</span>
            <span class="lp-cifra-lbl">Preguntas para<br>conocer tu perfil</span>
        </div>
        <div class="lp-cifra">
            <span class="lp-cifra-num">3</span>
            <span class="lp-cifra-lbl">Destinos<br>recomendados</span>
        </div>
    </section>

    <h2 class="lp-seccion-titulo">Cómo funciona</h2>
    <p class="lp-seccion-bajada">Cuatro pasos y tienes tu plan. No toma más de cinco minutos.</p>

    <section class="lp-pasos">
        <article class="lp-paso">
            <span class="lp-paso-num">1</span>
            <h3>Dinos de dónde sales</h3>
            <p>Tu ciudad de origen define las rutas, el transporte y lo que te cuesta llegar.</p>
        </article>
        <article class="lp-paso">
            <span class="lp-paso-num">2</span>
            <h3>Cuéntanos qué te gusta</h3>
            <p>Playa o montaña, museos o vida nocturna, lujo o mochilero. Responde sí o no.</p>
        </article>
        <article class="lp-paso">
            <span class="lp-paso-num">3</span>
            <h3>Pon fechas y presupuesto</h3>
            <p>En tu moneda local. Nosotros hacemos la conversión para comparar destinos.</p>
        </article>
        <article class="lp-paso">
            <span class="lp-paso-num">4</span>
            <h3>Recibe tus destinos</h3>
            <p>Tres ciudades ordenadas por qué tanto coinciden contigo y con tu bolsillo.</p>
        </article>
    </section>

    <section class="lp-cierre">
        <h2>Tu próximo viaje empieza aquí</h2>
        <p>Crear la cuenta es gratis y toma menos de un minuto.</p>
        <a class="lp-btn-claro" href="<c:url value='/registro'/>">Crear mi cuenta</a>
    </section>

    <footer class="lp-pie">
        <span>WiseTrip · Proyecto académico</span>
        <span>Fundamentos de Ingeniería de Software · Grupo 3</span>
    </footer>

</div>
</body>
</html>