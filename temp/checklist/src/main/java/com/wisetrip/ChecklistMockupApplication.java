package com.wisetrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Propuesta de HU: checklist de equipaje.
 * Un solo archivo: no usa JSP ni vistas separadas, la pantalla se
 * devuelve directo como HTML desde el controlador. Es solo para
 * mostrar cómo se vería la pantalla, no hay persistencia real
 * (el marcar/desmarcar es visual, con JS del propio navegador).
 */

@SpringBootApplication
public class ChecklistMockupApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChecklistMockupApplication.class, args);
    }

    @Controller
    static class ChecklistControlador {

        @GetMapping("/")
        @ResponseBody
        String mostrarChecklist() {
            return PAGINA;
        }

        private static final String PAGINA = """
<!--
    Checklist de equipaje — solo pantalla (mockup en Spring Boot).
    El marcar/desmarcar es solo visual (JS del propio navegador),
    no hay persistencia real. Usa la misma paleta y tipografía
    que el resto de WiseTrip.
-->
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Checklist de equipaje (propuesta) | WiseTrip</title>
<style>
@import url('https://fonts.googleapis.com/css2?family=Archivo+Black&family=IBM+Plex+Mono:wght@400;500;600&family=Instrument+Sans:wght@400;500;600;700&display=swap');

:root {
    --tinta: #16161D;
    --morado: #5B3FA8;
    --turquesa: #2FBFAF;
    --turquesa-suave: #D6F2EE;
    --turquesa-hondo: #1BA3A0;
    --crema: #EFE9DC;
    --linea: #16161D;
    --gris: #5B5B66;
    --gris-claro: #9A9AA6;

    --display: 'Archivo Black', 'Arial Black', sans-serif;
    --texto: 'Instrument Sans', system-ui, sans-serif;
    --dato: 'IBM Plex Mono', monospace;
}

* { box-sizing: border-box; }

body {
    font-family: var(--texto);
    background: var(--crema);
    color: var(--tinta);
    margin: 0;
    padding: 44px 24px;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    -webkit-font-smoothing: antialiased;
}

.tarjeta {
    background: #FFFFFF;
    border: 3px solid var(--tinta);
    border-radius: 18px;
    box-shadow: 8px 8px 0 var(--morado);
    padding: 34px 32px;
    width: 100%;
    max-width: 780px;
}

h1 {
    font-family: var(--display);
    font-size: 30px;
    font-weight: 400;
    line-height: 1.02;
    letter-spacing: -1.2px;
    text-transform: uppercase;
    margin: 0 0 8px;
}

h2 {
    font-family: var(--display);
    font-size: 18px;
    font-weight: 400;
    letter-spacing: -.6px;
    text-transform: uppercase;
    margin: 0;
}

.subtitulo {
    color: var(--gris);
    font-size: 14.5px;
    line-height: 1.6;
    margin: 0 0 26px;
}

.barra {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 14px;
    color: var(--gris);
    padding-bottom: 15px;
    border-bottom: 1px solid var(--linea);
    margin-bottom: 22px;
}

.progreso { display: flex; align-items: center; gap: 14px; margin: 0 0 28px; }
.progreso-barra { flex: 1; height: 7px; background: #E4DFD1; border-radius: 999px; overflow: hidden; }
.progreso-barra span { display: block; height: 100%; background: var(--turquesa); transition: width .25s ease; }
.progreso-texto { font-family: var(--dato); font-size: 12px; color: var(--gris); white-space: nowrap; }

.categoria { margin-bottom: 30px; }
.categoria-cabeza { display: flex; gap: 13px; align-items: center; margin-bottom: 13px; }
.categoria-num {
    font-size: 17px;
    background: var(--turquesa-suave);
    border-radius: 10px;
    width: 38px; height: 38px;
    display: flex; align-items: center; justify-content: center;
    flex-shrink: 0;
}
.categoria-cabeza h2 { font-size: 17px; }

.item-equipaje {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 13px 15px;
    border: 1.5px solid var(--linea);
    border-radius: 10px;
    margin-bottom: 8px;
    cursor: pointer;
    transition: border-color .15s ease, background .15s ease;
}
.item-equipaje:hover { border-color: var(--turquesa); }

.item-equipaje input[type=checkbox] { position: absolute; opacity: 0; pointer-events: none; }

.item-equipaje-marca {
    width: 22px; height: 22px;
    border-radius: 6px;
    border: 1.5px solid var(--linea);
    display: flex; align-items: center; justify-content: center;
    font-size: 13px;
    color: transparent;
    background: #FFFFFF;
    flex-shrink: 0;
    transition: background .15s ease, border-color .15s ease, color .15s ease;
}

.item-equipaje-texto { font-size: 14.5px; line-height: 1.45; color: var(--tinta); transition: color .15s ease; }

.item-equipaje.marcado { border-color: var(--turquesa); background: var(--turquesa-suave); }
.item-equipaje.marcado .item-equipaje-marca { background: var(--turquesa); border-color: var(--turquesa); color: #FFFFFF; }
.item-equipaje.marcado .item-equipaje-texto { color: var(--gris); text-decoration: line-through; }

@media (max-width: 620px) {
    body { padding: 20px 14px; }
    .tarjeta { padding: 24px 20px; }
    h1 { font-size: 25px; }
    .item-equipaje { padding: 11px 13px; }
}

</style>
</head>
<body>
<div class="tarjeta">

    <div class="barra">
        <span>Propuesta de HU &mdash; Checklist de equipaje</span>
    </div>

    <h1>Checklist de equipaje</h1>
    <p class="subtitulo">
        Repasa los artículos básicos recomendados y marca lo que ya empacaste,
        para que no se te olvide nada antes de salir de viaje.
    </p>

    <div class="progreso">
        <div class="progreso-barra"><span id="relleno" style="width:0%"></span></div>
        <span class="progreso-texto"><b id="contador">0</b> de <b id="total">0</b> empacados</span>
    </div>

    <div id="lista"></div>

</div>

<script>
// Solo para que el mockup se vea vivo al hacer clic; nada se guarda.
const categorias = [
    { nombre: "Documentos", icono: "📄", items: [
        "Pasaporte o cédula", "Tiquetes o reserva de vuelo",
        "Reserva de alojamiento", "Seguro de viaje", "Efectivo y tarjetas"
    ]},
    { nombre: "Ropa", icono: "👕", items: [
        "Ropa para el clima del destino", "Zapatos cómodos",
        "Ropa interior y medias", "Pijama", "Chaqueta o abrigo"
    ]},
    { nombre: "Electrónica", icono: "🔌", items: [
        "Cargador de celular", "Cargador portátil (power bank)",
        "Adaptador de corriente", "Audífonos"
    ]},
    { nombre: "Salud e higiene", icono: "🧴", items: [
        "Cepillo y crema dental", "Medicamentos personales",
        "Protector solar", "Kit de aseo básico"
    ]},
    { nombre: "Varios", icono: "🎒", items: [
        "Botella de agua reutilizable", "Snacks para el viaje",
        "Documentos impresos o digitales de reservas", "Candado para maleta"
    ]}
];

const lista = document.getElementById("lista");
let total = 0;

categorias.forEach(cat => {
    const seccion = document.createElement("section");
    seccion.className = "categoria";
    seccion.innerHTML = `
        <div class="categoria-cabeza">
            <span class="categoria-num">${cat.icono}</span>
            <h2>${cat.nombre}</h2>
        </div>`;

    cat.items.forEach(texto => {
        total++;
        const label = document.createElement("label");
        label.className = "item-equipaje";
        label.innerHTML = `
            <input type="checkbox">
            <span class="item-equipaje-marca">&#10003;</span>
            <span class="item-equipaje-texto">${texto}</span>`;
        label.querySelector("input").addEventListener("change", e => {
            label.classList.toggle("marcado", e.target.checked);
            actualizarProgreso();
        });
        seccion.appendChild(label);
    });

    lista.appendChild(seccion);
});

document.getElementById("total").textContent = total;

function actualizarProgreso() {
    const marcados = document.querySelectorAll(".item-equipaje input:checked").length;
    document.getElementById("contador").textContent = marcados;
    document.getElementById("relleno").style.width = (marcados / total * 100) + "%";
}
</script>
</body>
</html>
""";
    }
}
