<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tus preferencias | WiseTrip</title>
    <link rel="stylesheet" href="<c:url value='/css/estilos.css'/>">
</head>
<body>
<div class="tarjeta ancha">

    <div class="barra">
        <span>Hola, <strong>${usuario.nombreCompleto}</strong></span>
        <a href="<c:url value='/logout'/>">Cerrar sesión</a>
    </div>

    <div class="pasos">
        <span class="paso hecho">Origen</span>
        <span class="paso activo">Preferencias</span>
        <span class="paso">Fechas</span>
        <span class="paso">Presupuesto</span>
    </div>

    <h1>¿Qué tipo de viajero eres?</h1>
    <p class="subtitulo">
        Responde sí o no a cada pregunta. Con esto WiseTrip elige destinos y
        actividades que de verdad te gusten. Son ${totalPreguntas} preguntas rápidas.
    </p>

    <c:if test="${not empty faltantes}">
        <div class="alerta">
            Te faltan <strong>${faltantes}</strong> preguntas por responder.
            Están marcadas más abajo.
        </div>
    </c:if>

    <form action="<c:url value='/preferencias'/>" method="post" id="formPreferencias">

        <c:forEach var="categoria" items="${categorias}" varStatus="estado">
            <section class="categoria">
                <div class="categoria-cabeza">
                    <span class="categoria-num">${estado.index + 1}</span>
                    <div>
                        <h2>${categoria.nombre}</h2>
                        <p>${categoria.descripcion}</p>
                    </div>
                </div>

                <c:forEach var="p" items="${categoria.preguntas}">
                    <div class="pregunta ${not empty errores[p.clave] ? 'pregunta-error' : ''}">
                        <span class="pregunta-texto">${p.texto}</span>

                        <div class="opciones">
                            <input type="radio" id="${p.clave}_si"
                                   name="respuestas[${p.clave}]" value="si"
                                   ${preferencias.respuestas[p.clave] == 'si' ? 'checked' : ''}>
                            <label for="${p.clave}_si" class="op-si">Sí</label>

                            <input type="radio" id="${p.clave}_no"
                                   name="respuestas[${p.clave}]" value="no"
                                   ${preferencias.respuestas[p.clave] == 'no' ? 'checked' : ''}>
                            <label for="${p.clave}_no" class="op-no">No</label>
                        </div>
                    </div>
                </c:forEach>
            </section>
        </c:forEach>

        <div class="pie-fijo">
            <div class="progreso">
                <div class="progreso-barra"><span id="progresoRelleno"></span></div>
                <span class="progreso-texto"><b id="contador">0</b> de ${totalPreguntas} respondidas</span>
            </div>
            <button type="submit">Continuar</button>
        </div>
    </form>

    <a class="volver" href="<c:url value='/origen'/>">&larr; Volver a la ubicación de origen</a>
</div>

<script>
    const pie = document.querySelector('.pie-fijo');
    const total = parseInt(pie.getAttribute('data-total'), 10) || 26;
    const contador = document.getElementById('contador');
    const relleno = document.getElementById('progresoRelleno');

    function actualizarProgreso() {
        const hechas = document.querySelectorAll('#formPreferencias input[type=radio]:checked').length;
        contador.textContent = hechas;
        relleno.style.width = (hechas / total * 100) + '%';
    }

    document.querySelectorAll('#formPreferencias input[type=radio]').forEach(r => {
        r.addEventListener('change', e => {
            e.target.closest('.pregunta').classList.remove('pregunta-error');
            actualizarProgreso();
        });
    });

    actualizarProgreso();
</script>
</body>
</html>