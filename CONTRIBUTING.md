# Cómo contribuir

1. Trabaja en una rama a partir de `develop` (o `main` si el equipo así lo acuerda).
2. Un issue o historia de usuario por cambio funcional.
3. Ejecuta los tests antes de abrir un pull request:

```bash
./mvnw test
```

4. Describe en el PR qué cambió y cómo probarlo.
5. No subas secretos, `.env`, `target/` ni archivos de IDE.

La organización de carpetas está definida en `BOILERPLATE_template.md`. El código de la aplicación vive en `src/`.
