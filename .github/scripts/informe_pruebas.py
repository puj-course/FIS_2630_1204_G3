#!/usr/bin/env python3
"""Genera el informe de pruebas alineado con el plan (docs/pruebas/plan_pruebas.json).

Lee los XML de Surefire (target/surefire-reports/TEST-*.xml), cruza cada caso CPxx del plan
con su resultado real y escribe:
  - informe-pruebas.html  (para abrir en el navegador)
  - informe-pruebas.json  (para procesar)
  - un resumen en Markdown en $GITHUB_STEP_SUMMARY (si existe)
Siempre termina con código 0: quien falla el pipeline es `mvn verify`, no este script.
"""
import glob, html, json, os, re, sys
import xml.etree.ElementTree as ET

APP = "app/Entrega1/wisetrip_entrega1"
PLAN = "docs/pruebas/plan_pruebas.json"
REPORTS = os.path.join(APP, "target", "surefire-reports")
OUT = os.path.join(APP, "target", "informe-pruebas")

plan = json.load(open(PLAN, encoding="utf-8"))
resultados = {}
for xml in glob.glob(os.path.join(REPORTS, "TEST-*.xml")):
    for tc in ET.parse(xml).getroot().iter("testcase"):
        m = re.search(r"cp(\d+)", tc.get("name", ""), re.I)
        if not m:
            continue
        cid = "CP" + m.group(1).zfill(2)
        estado, mensaje = "PASSED", ""
        for tag, nombre in (("failure", "FAILED"), ("error", "ERROR"), ("skipped", "SKIPPED")):
            nodo = tc.find(tag)
            if nodo is not None:
                estado = nombre
                mensaje = (nodo.get("message") or (nodo.text or "").strip().splitlines()[0:1] and (nodo.text or "").strip().splitlines()[0] or "")
                break
        resultados[cid] = (estado, mensaje)

filas = []
for caso in plan:
    estado, mensaje = resultados.get(caso["id"], ("NO EJECUTADO", "No se encontró la prueba en los reportes de Surefire"))
    filas.append({**caso, "estado": estado, "resultado_real": "Cumple lo esperado" if estado == "PASSED" else mensaje, "mensaje": mensaje})

total = len(filas)
conteo = {e: sum(1 for f in filas if f["estado"] == e) for e in ("PASSED", "FAILED", "ERROR", "SKIPPED", "NO EJECUTADO")}
os.makedirs(OUT, exist_ok=True)
json.dump({"resumen": {"total": total, **conteo}, "casos": filas}, open(os.path.join(OUT, "informe-pruebas.json"), "w", encoding="utf-8"), ensure_ascii=False, indent=2)

color = {"PASSED": "#1a7f37", "FAILED": "#cf222e", "ERROR": "#cf222e", "SKIPPED": "#9a6700", "NO EJECUTADO": "#6e7781"}
h = ["<!doctype html><html lang='es'><meta charset='utf-8'><title>Informe de pruebas - WiseTrip</title>",
     "<style>body{font-family:system-ui,sans-serif;margin:24px}table{border-collapse:collapse;width:100%;font-size:13px}"
     "th,td{border:1px solid #d0d7de;padding:6px 8px;vertical-align:top}th{background:#f6f8fa;text-align:left}</style>",
     "<h1>Informe de pruebas unitarias - WiseTrip</h1>",
     f"<p><b>Total:</b> {total} &nbsp; <b>Exitosos:</b> {conteo['PASSED']} &nbsp; <b>Fallidos:</b> {conteo['FAILED'] + conteo['ERROR']} "
     f"&nbsp; <b>Omitidos:</b> {conteo['SKIPPED']} &nbsp; <b>No ejecutados:</b> {conteo['NO EJECUTADO']}</p>",
     "<table><tr><th>Caso</th><th>Servicio</th><th>Método</th><th>Descripción</th><th>Entrada</th><th>Tipo</th>"
     "<th>Resultado esperado</th><th>Resultado real</th><th>Estado</th></tr>"]
for f in filas:
    e = html.escape
    h.append(f"<tr><td>{e(f['id'])}</td><td>{e(f['servicio'])}</td><td>{e(f['metodo'])}</td><td>{e(f['descripcion'])}</td>"
             f"<td>{e(f['entrada'])}</td><td>{e(f['tipo'])}</td><td>{e(f['esperado'])}</td><td>{e(f['resultado_real'])}</td>"
             f"<td style='color:{color[f['estado']]};font-weight:600'>{f['estado']}</td></tr>")
h.append("</table></html>")
open(os.path.join(OUT, "informe-pruebas.html"), "w", encoding="utf-8").write("\n".join(h))

resumen = os.environ.get("GITHUB_STEP_SUMMARY")
if resumen:
    with open(resumen, "a", encoding="utf-8") as s:
        s.write(f"## Informe de pruebas unitarias\n\n**Total:** {total} | **Exitosos:** {conteo['PASSED']} | "
                f"**Fallidos:** {conteo['FAILED'] + conteo['ERROR']} | **No ejecutados:** {conteo['NO EJECUTADO']}\n\n"
                "| Caso | Método | Tipo | Estado | Mensaje |\n|---|---|---|---|---|\n")
        for f in filas:
            s.write(f"| {f['id']} | {f['servicio']}.{f['metodo']} | {f['tipo']} | {f['estado']} | {f['mensaje'].replace('|', '/')[:150]} |\n")
print(f"Informe generado en {OUT}: {conteo}")
sys.exit(0)