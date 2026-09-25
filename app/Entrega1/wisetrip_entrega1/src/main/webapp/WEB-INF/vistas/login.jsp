<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Iniciar sesión | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/login.css'/>">
</head>
<body class="lg">

<div class="lg-split">

    <aside class="lg-visual">
        <a class="lg-marca" href="<c:url value='/'/>">
            <img src="/img/portada/logo.png" alt="">
            <span>Wise<em>Trip</em></span>
        </a>

        <span class="lg-sol"></span>
        <span class="lg-onda lg-onda-1"></span>
        <span class="lg-onda lg-onda-2"></span>
        <span class="lg-curva"></span>
        <span class="lg-curva-2"></span>

        <figure class="lg-polaroid lg-pol-1">
            <img src="/img/portada/Cartegena.jpg" alt="">
            <figcaption>Cartagena · COL</figcaption>
        </figure>
        <figure class="lg-polaroid lg-pol-2">
            <img src="/img/portada/Argentina.jpg" alt="">
            <figcaption>Buenos Aires · ARG</figcaption>
        </figure>
        <figure class="lg-polaroid lg-pol-3">
            <img src="/img/portada/Peru.jpg" alt="">
            <figcaption>Cusco · PER</figcaption>
        </figure>

        <span class="lg-sello-flotante">20 países</span>

        <p class="lg-leyenda">
            Tu plan sigue guardado.<br>Entra y sigue donde lo dejaste.
        </p>
    </aside>

    <main class="lg-panel">

        <span class="lg-sello">Continúa donde quedaste</span>

        <h1 class="lg-titulo">Hola de<br>nuevo</h1>

        <p class="lg-bajada">
            Inicia sesión para continuar con la configuración de tu viaje
            por Latinoamérica.
        </p>

        <c:if test="${not empty error}">
            <div class="lg-alerta">${error}</div>
        </c:if>

        <form action="<c:url value='/login'/>" method="post">

            <label for="correo">Correo electrónico</label>
            <div class="lg-campo">
                <svg viewBox="0 0 20 20" fill="none">
                    <rect x="2" y="4" width="16" height="12" rx="2" stroke="#5B3FA8" stroke-width="2"/>
                    <path d="m2.8 5.2 7.2 5.4 7.2-5.4" stroke="#5B3FA8" stroke-width="2" stroke-linejoin="round"/>
                </svg>
                <input id="correo" type="text" name="correo" value="${correo}"
                       placeholder="tucorreo@ejemplo.com">
            </div>

            <div class="lg-fila-etiqueta">
                <label for="clave">Contraseña</label>
                <a class="lg-enlace-fino" href="<c:url value='/registro'/>">¿Olvidaste tu contraseña?</a>
            </div>
            <div class="lg-campo lg-campo-clave">
                <svg viewBox="0 0 20 20" fill="none">
                    <rect x="3" y="8.6" width="14" height="9" rx="2" stroke="#5B3FA8" stroke-width="2"/>
                    <path d="M6.6 8.6V6.4a3.4 3.4 0 0 1 6.8 0v2.2" stroke="#5B3FA8" stroke-width="2"/>
                </svg>
                <input id="clave" type="password" name="password" placeholder="Tu contraseña">
                <button type="button" class="lg-mostrar" id="btnMostrar">Mostrar</button>
            </div>

            <button type="submit" class="lg-enviar">Iniciar sesión &rarr;</button>
        </form>

        <div class="lg-separador"><span>¿Primera vez aquí?</span></div>

        <a class="lg-btn-amarillo" href="<c:url value='/registro'/>">Crear una cuenta gratis</a>

        <p class="lg-pie">WiseTrip · 20 países de Latinoamérica</p>

    </main>

</div>

<script>
    const clave = document.getElementById('clave');
    const btn = document.getElementById('btnMostrar');

    btn.addEventListener('click', () => {
        const oculta = clave.type === 'password';
        clave.type = oculta ? 'text' : 'password';
        btn.textContent = oculta ? 'Ocultar' : 'Mostrar';
    });
</script>
</body>
</html> 