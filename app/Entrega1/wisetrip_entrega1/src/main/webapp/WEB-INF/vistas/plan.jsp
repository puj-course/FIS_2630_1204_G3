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
        <h1 class="pl-titulo">Reparte tu<br>presupuesto</h1>
        <p class="pl-bajada">
            Estos son los porcentajes que sugerimos para un viaje a
            ${destino.ciudad.nombre}. Puedes dejarlos así o ajustarlos
            a tu manera.
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

        <form action="<c:url value='/plan'/>" method="post" id="formPlan">

            <div class="pl-total">
                <div class="pl-total-cabeza">
                    <span class="pl-total-rotulo">Total repartido</span>
                    <span class="pl-total-cifra" id="totalCifra">100%</span>
                </div>
                <div class="pl-total-barra">
                    <span class="pl-seg-hospedaje"   id="segHospedaje"></span>
                    <span class="pl-seg-alimentacion" id="segAlimentacion"></span>
                    <span class="pl-seg-transporte"  id="segTransporte"></span>
                    <span class="pl-seg-actividades" id="segActividades"></span>
                    <span class="pl-seg-imprevistos" id="segImprevistos"></span>
                </div>
            </div>

            <div class="pl-categoria">
                <div class="pl-cat-cabeza">
                    <span class="pl-cat-punto" style="background:#FFC627"></span>
                    <label class="pl-cat-nombre" for="hospedaje">Hospedaje</label>
                    <span class="pl-cat-monto" data-monto="hospedaje"></span>
                    <span class="pl-cat-pct" data-pct="hospedaje">${reparto.hospedaje}%</span>
                </div>
                <input class="pl-slider" type="range" id="hospedaje" name="hospedaje"
                       min="0" max="100" step="1" value="${reparto.hospedaje}" disabled>
            </div>

            <div class="pl-categoria">
                <div class="pl-cat-cabeza">
                    <span class="pl-cat-punto" style="background:#2FBFAF"></span>
                    <label class="pl-cat-nombre" for="alimentacion">Alimentación</label>
                    <span class="pl-cat-monto" data-monto="alimentacion"></span>
                    <span class="pl-cat-pct" data-pct="alimentacion">${reparto.alimentacion}%</span>
                </div>
                <input class="pl-slider" type="range" id="alimentacion" name="alimentacion"
                       min="0" max="100" step="1" value="${reparto.alimentacion}" disabled>
            </div>

            <div class="pl-categoria">
                <div class="pl-cat-cabeza">
                    <span class="pl-cat-punto" style="background:#F3A6C0"></span>
                    <label class="pl-cat-nombre" for="transporte">Transporte</label>
                    <span class="pl-cat-monto" data-monto="transporte"></span>
                    <span class="pl-cat-pct" data-pct="transporte">${reparto.transporte}%</span>
                </div>
                <input class="pl-slider" type="range" id="transporte" name="transporte"
                       min="0" max="100" step="1" value="${reparto.transporte}" disabled>
            </div>

            <div class="pl-categoria">
                <div class="pl-cat-cabeza">
                    <span class="pl-cat-punto" style="background:#7BC97F"></span>
                    <label class="pl-cat-nombre" for="actividades">Actividades</label>
                    <span class="pl-cat-monto" data-monto="actividades"></span>
                    <span class="pl-cat-pct" data-pct="actividades">${reparto.actividades}%</span>
                </div>
                <input class="pl-slider" type="range" id="actividades" name="actividades"
                       min="0" max="100" step="1" value="${reparto.actividades}" disabled>
            </div>

            <div class="pl-categoria">
                <div class="pl-cat-cabeza">
                    <span class="pl-cat-punto" style="background:#6BB8E8"></span>
                    <label class="pl-cat-nombre" for="imprevistos">Imprevistos</label>
                    <span class="pl-cat-monto" data-monto="imprevistos"></span>
                    <span class="pl-cat-pct" data-pct="imprevistos">${reparto.imprevistos}%</span>
                </div>
                <input class="pl-slider" type="range" id="imprevistos" name="imprevistos"
                       min="0" max="100" step="1" value="${reparto.imprevistos}" disabled>
            </div>

            <div class="pl-acciones">
                <button type="button" class="pl-btn-claro" id="btnEditar">Editar montos</button>
                <button type="submit" class="pl-btn" id="btnGuardar">Guardar reparto</button>
                <a class="pl-volver" href="<c:url value='/recomendaciones'/>">&larr; Cambiar destino</a>
            </div>
        </form>
    </section>

    <aside>
        <div class="pl-sobre">
            <div class="pl-sobre-top">
                <span>WiseTrip · Plan de gastos</span>
                <span>${dias}D</span>
            </div>

            <div class="pl-sobre-cuerpo">
                <div class="pl-sobre-destino">
                    <span class="pl-sobre-rotulo">Tu destino</span>
                    <span class="pl-sobre-ciudad">${destino.ciudad.nombre}</span>
                    <span class="pl-sobre-pais">${destino.ciudad.pais}</span>
                </div>

                <div class="pl-sobre-perforado"></div>

                <c:forEach var="m" items="${montos}">
                    <div class="pl-linea">
                        <span class="pl-linea-nombre">${m.key}</span>
                        <span class="pl-linea-monto" data-sobre="${m.key}">
                            <fmt:formatNumber value="${m.value}" maxFractionDigits="0"/>
                        </span>
                    </div>
                </c:forEach>

                <div class="pl-sobre-total">
                    <span class="pl-sobre-total-rotulo">Total</span>
                    <span class="pl-sobre-total-cifra">${montoTotalFormateado} ${presupuesto.moneda}</span>
                </div>
            </div>

            <div class="pl-sobre-abajo">
                <span>Por día</span>
                <span>${porDiaTotal} ${presupuesto.moneda}</span>
            </div>
        </div>
    </aside>

</main>

</body>
</html>