<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cuenta creada | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body class="bienvenida">

<div class="bienvenida-split">

    <aside class="bienvenida-visual">
        <a class="bienvenida-marca" href="<c:url value='/'/>">
            <img src="/img/portada/logo.png" alt="">
            <span>Wise<em>Trip</em></span>
        </a>

        <span class="bienvenida-sol"></span>
        <span class="bienvenida-onda onda-1"></span>
        <span class="bienvenida-onda onda-2"></span>
        <span class="bienvenida-curva"></span>

        <div class="polaroids">
            <figure class="polaroid pol-1">
                <img src="/img/portada/guadalajara.jpg" alt="Catedral de Guadalajara, México">
                <figcaption>Guadalajara · MEX</figcaption>
            </figure>
            <figure class="polaroid pol-2">
                <img src="/img/portada/panama.jpg" alt="Rascacielos de Ciudad de Panamá">
                <figcaption>Ciudad de Panamá · PAN</figcaption>
            </figure>
            <figure class="polaroid pol-3">
                <img src="/img/portada/chillan.jpg" alt="Nevados de Chillán, Chile">
                <figcaption>Chillán · CHI</figcaption>
            </figure>
        </div>
    </aside>

    <main class="bienvenida-panel">

        <span class="check-grande">
            <svg viewBox="0 0 24 24" width="1em" height="1em" fill="none" aria-hidden="true">
                <path d="M5 12.5l4.5 4.5L19 7.5" stroke="currentColor" stroke-width="3"
                      stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
        </span>

        <span class="rotulo-paso">Cuenta creada con éxito</span>

        <h1 class="titulo-bienvenida">
            ¡Bienvenida<br>a bordo,<br>${primerNombre}!
        </h1>

        <p class="bajada">
            Tu cuenta está lista. Ahora cuéntanos de dónde sales y
            empezamos a buscar los destinos que mejor van contigo.
        </p>

        <div class="tarjeta-cuenta">
            <span class="avatar">${inicialesUsuario}</span>
            <div class="tarjeta-cuenta-datos">
                <strong>${nombre}</strong>
                <span>${correo}</span>
            </div>
            <span class="tarjeta-cuenta-check">
                <svg viewBox="0 0 24 24" width="1em" height="1em" fill="none" aria-hidden="true">
                    <path d="M5 12.5l4.5 4.5L19 7.5" stroke="currentColor" stroke-width="3"
                          stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </span>
        </div>

        <a class="boton" href="<c:url value='/login'/>">Empezar a planear mi viaje &rarr;</a>

        <a class="enlace-suave" href="<c:url value='/'/>">Prefiero iniciar sesión más tarde</a>

    </main>

</div>

</body>
</html>