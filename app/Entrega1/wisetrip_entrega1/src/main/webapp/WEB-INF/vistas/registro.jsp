<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crear cuenta | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/registro.css'/>">
</head>
<body class="rg">

<div class="rg-split">

    <aside class="rg-visual">
        <a class="rg-marca" href="<c:url value='/'/>">
            <img src="/img/portada/logo.png" alt="">
            <span>Wise<em>Trip</em></span>
        </a>

        <span class="rg-sol"></span>
        <span class="rg-onda rg-onda-1"></span>
        <span class="rg-onda rg-onda-2"></span>
        <span class="rg-curva"></span>
        <span class="rg-curva-2"></span>

        <figure class="rg-polaroid rg-pol-1">
            <img src="/img/portada/Cartegena.jpg" alt="">
            <figcaption>Cartagena · COL</figcaption>
        </figure>
        <figure class="rg-polaroid rg-pol-2">
            <img src="/img/portada/Rio.jpg" alt="">
            <figcaption>Río de Janeiro · BRA</figcaption>
        </figure>
        <figure class="rg-polaroid rg-pol-3">
            <img src="/img/portada/Amazonas.jpg" alt="">
            <figcaption>Amazonas · COL</figcaption>
        </figure>

        <span class="rg-sello-flotante">Gratis</span>

        <p class="rg-leyenda">
            20 países de Latinoamérica.<br>Un solo plan hecho a tu medida.
        </p>
    </aside>

    <main class="rg-panel">

        <span class="rg-sello">Toma menos de un minuto</span>

        <h1 class="rg-titulo">Crea<br>tu cuenta</h1>

        <p class="rg-bajada">
            Empieza a planear tu próximo viaje. Solo necesitamos unos datos
            para guardar tu plan.
        </p>

        <form action="<c:url value='/registro'/>" method="post">

            <label for="nombre">Nombre completo</label>
            <input id="nombre" type="text" name="nombreCompleto" value="${usuario.nombreCompleto}"
                   placeholder="Ej: Laura Gómez Peña">
            <c:if test="${not empty errores.nombreCompleto}">
                <span class="rg-error">${errores.nombreCompleto}</span>
            </c:if>

            <div class="rg-fila">
                <div>
                    <label for="tipoDoc">Tipo de documento</label>
                    <select id="tipoDoc" name="tipoDocumento">
                        <option value="">Selecciona una opción</option>
                        <option value="CC" ${usuario.tipoDocumento == 'CC' ? 'selected' : ''}>Cédula de ciudadanía</option>
                        <option value="CE" ${usuario.tipoDocumento == 'CE' ? 'selected' : ''}>Cédula de extranjería</option>
                        <option value="TI" ${usuario.tipoDocumento == 'TI' ? 'selected' : ''}>Tarjeta de identidad</option>
                        <option value="PA" ${usuario.tipoDocumento == 'PA' ? 'selected' : ''}>Pasaporte</option>
                    </select>
                    <c:if test="${not empty errores.tipoDocumento}">
                        <span class="rg-error">${errores.tipoDocumento}</span>
                    </c:if>
                </div>
                <div>
                    <label for="numDoc">Número de documento</label>
                    <input id="numDoc" type="text" name="numeroDocumento" value="${usuario.numeroDocumento}"
                           placeholder="Ej: 1020304050">
                    <c:if test="${not empty errores.numeroDocumento}">
                        <span class="rg-error">${errores.numeroDocumento}</span>
                    </c:if>
                </div>
            </div>

            <label for="nacimiento">Fecha de nacimiento</label>
            <input id="nacimiento" type="date" name="fechaNacimiento" value="${usuario.fechaNacimiento}">
            <c:if test="${not empty errores.fechaNacimiento}">
                <span class="rg-error">${errores.fechaNacimiento}</span>
            </c:if>

            <label for="correo">Correo electrónico</label>
            <input id="correo" type="text" name="correo" value="${usuario.correo}"
                   placeholder="tucorreo@ejemplo.com">
            <c:if test="${not empty errores.correo}">
                <span class="rg-error">${errores.correo}</span>
            </c:if>

            <div class="rg-fila">
                <div>
                    <label for="clave">Contraseña</label>
                    <input id="clave" type="password" name="password" placeholder="Mínimo 6 caracteres">
                    <c:if test="${not empty errores.password}">
                        <span class="rg-error">${errores.password}</span>
                    </c:if>
                </div>
                <div>
                    <label for="clave2">Confirmar contraseña</label>
                    <input id="clave2" type="password" name="confirmarPassword" placeholder="Repite tu contraseña">
                    <c:if test="${not empty errores.confirmarPassword}">
                        <span class="rg-error">${errores.confirmarPassword}</span>
                    </c:if>
                </div>
            </div>

            <button type="submit" class="rg-enviar">Crear mi cuenta &rarr;</button>
        </form>

        <p class="rg-pie">¿Ya tienes cuenta? <a href="<c:url value='/login'/>">Inicia sesión</a></p>

    </main>

</div>

</body>
</html>