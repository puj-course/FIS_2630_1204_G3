<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>WiseTrip — Planea tu viaje sin perder el control</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Fraunces:ital,opsz,wght@0,9..144,500;0,9..144,600;1,9..144,600&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/landing.css'/>">
</head>
<body class="lp">

<div class="lp-contenedor">

    <nav class="lp-nav">
        <div class="lp-marca">
            <svg width="26" height="26" viewBox="0 0 24 24" fill="none">
                <path d="M2 12l19-8-4 8 4 8-19-8z" fill="#1BA3A0"/>
                <circle cx="17" cy="12" r="1.6" fill="#E8623D"/>
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
            <span class="lp-etiqueta">✦ Planeación inteligente de viajes</span>

            <h1 class="lp-titulo">Viaja lejos<br>sin <em>gastar de más</em>.</h1>

            <p class="lp-bajada">
                Cuéntanos a dónde quieres ir, cuánto tienes y qué te gusta hacer.
                WiseTrip reparte tu presupuesto y te arma un plan que sí puedes pagar.
            </p>

            <div class="lp-acciones">
                <a class="lp-btn" href="<c:url value='/registro'/>">Empezar a planear</a>
                <a class="lp-btn-fantasma" href="<c:url value='/login'/>">Ya tengo cuenta</a>
            </div>

            <div class="lp-cifras">
                <div>
                    <span class="lp-cifra-num">4</span>
                    <span class="lp-cifra-lbl">estilos de viaje</span>
                </div>
                <div>
                    <span class="lp-cifra-num">8</span>
                    <span class="lp-cifra-lbl">países de origen</span>
                </div>
                <div>
                    <span class="lp-cifra-num">5 min</span>
                    <span class="lp-cifra-lbl">para tener tu plan</span>
                </div>
            </div>
        </div>

        <div class="lp-pase">
            <div class="lp-pase-top">
                <span>Pase de abordar</span>
                <strong>WISETRIP</strong>
            </div>

            <div class="lp-ruta">
                <div>
                    <div class="lp-ruta-cod">BOG</div>
                    <div class="lp-ruta-ciudad">Bogotá</div>
                </div>
                <div class="lp-ruta-linea"></div>
                <div style="text-align:right">
                    <div class="lp-ruta-cod">CTG</div>
                    <div class="lp-ruta-ciudad">Cartagena</div>
                </div>
            </div>

            <div class="lp-perforado"></div>

            <div class="lp-pase-abajo">
                <div class="lp-pase-titulo">Tu presupuesto, repartido</div>

                <div class="lp-gasto">
                    <div class="lp-gasto-fila"><span>Hospedaje</span><span class="lp-gasto-pct">35%</span></div>
                    <div class="lp-barra"><span style="width:35%;background:#0E3A53"></span></div>
                </div>
                <div class="lp-gasto">
                    <div class="lp-gasto-fila"><span>Alimentación</span><span class="lp-gasto-pct">25%</span></div>
                    <div class="lp-barra"><span style="width:25%;background:#1BA3A0"></span></div>
                </div>
                <div class="lp-gasto">
                    <div class="lp-gasto-fila"><span>Transporte</span><span class="lp-gasto-pct">20%</span></div>
                    <div class="lp-barra"><span style="width:20%;background:#E8623D"></span></div>
                </div>
                <div class="lp-gasto">
                    <div class="lp-gasto-fila"><span>Actividades</span><span class="lp-gasto-pct">15%</span></div>
                    <div class="lp-barra"><span style="width:15%;background:#93A3AC"></span></div>
                </div>
            </div>
        </div>

    </section>

    <section class="lp-tarjetas">
        <div class="lp-tarjeta">
            <div class="lp-icono" style="background:#E4F5F1">🧭</div>
            <h3>Dinos de dónde sales</h3>
            <p>Tu ciudad de origen define las rutas, el transporte y lo que te va a costar llegar.</p>
        </div>
        <div class="lp-tarjeta">
            <div class="lp-icono" style="background:#FDEDE8">💰</div>
            <h3>Pon tu presupuesto</h3>
            <p>Repartimos tu dinero entre hospedaje, comida, transporte y actividades.</p>
        </div>
        <div class="lp-tarjeta">
            <div class="lp-icono" style="background:#F0EDE2">🗺️</div>
            <h3>Recibe tu plan</h3>
            <p>Actividades según tus gustos, con las fechas y los costos ya calculados.</p>
        </div>
    </section>

    <footer class="lp-pie">
        <span>WiseTrip · Proyecto académico</span>
        <span>Fundamentos de Ingeniería de Software · Grupo 3</span>
    </footer>

</div>
</body>
</html>