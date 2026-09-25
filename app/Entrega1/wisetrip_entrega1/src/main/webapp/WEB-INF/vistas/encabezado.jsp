<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="cabecera">
    <a class="cabecera-marca" href="<c:url value='/'/>">
        <img src="/img/portada/logo.png" alt="">
        <span>Wise<em>Trip</em></span>
    </a>

    <nav class="pasos-nav">
        <c:forEach var="p" items="${pasos}" varStatus="i">
            <c:choose>
                <c:when test="${i.index + 1 < pasoActual}">
                    <span class="paso-nav hecho"><span class="paso-check">✓</span> ${p}</span>
                </c:when>
                <c:when test="${i.index + 1 == pasoActual}">
                    <span class="paso-nav activo"><span class="paso-num">${i.index + 1}</span> ${p}</span>
                </c:when>
                <c:otherwise>
                    <span class="paso-nav">${i.index + 1} ${p}</span>
                </c:otherwise>
            </c:choose>
            <c:if test="${!i.last}"><span class="paso-linea"></span></c:if>
        </c:forEach>
    </nav>

    <div class="cabecera-usuario">
        <span class="avatar">${inicialesUsuario}</span>
        <div>
            <strong>${usuario.nombreCompleto}</strong>
            <a href="<c:url value='/logout'/>">Cerrar sesión</a>
        </div>
    </div>
</header>