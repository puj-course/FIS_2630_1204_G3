## Decorator — Añadir comportamientos dinámicos sin modificar la base

**Problema de diseño:** 
El diagrama de clases se realizó para representar la implementación del patrón de diseño Decorator en el módulo de presupuesto de WiseTrip. Este patrón permite agregar una funcionalidad adicional a una clase sin tener que modificar directamente su implementación original

Problema identificado

Se necesitaba agregar el cálculo de un margen del 5 % del presupuesto destinado a cubrir posibles imprevistos durante el viaje. El problema era incorporar esta nueva funcionalidad sin alterar la lógica que ya existía para validar el presupuesto, convertir monedas y formatear los valores.

Solución implementada

Para resolver este problema, se implementó la clase PresupuestoConMargenDecorador, que utiliza la interfaz GestorPresupuesto y delega las operaciones existentes al servicio base PresupuestoServicio. Además, incorpora el método calcularMargenImprevistos(), encargado de calcular el 5 % del presupuesto convertido a dólares.

La clase PlanificacionControlador utiliza el decorador para obtener el margen calculado y enviarlo a la vista resumen, donde se muestra al usuario como un valor adicional dentro de la información del presupuesto.

Justificación del patrón

Se eligió el patrón Decorator porque permite extender las funcionalidades del módulo sin modificar el servicio original. De esta manera, se mantiene la separación de responsabilidades, se facilita el mantenimiento del código y se permite agregar nuevas funcionalidades en el futuro sin afectar las operaciones existentes.

Resultado obtenido

Con esta implementación, WiseTrip puede mostrar al usuario un margen adicional para imprevistos equivalente al 5 % de su presupuesto convertido a dólares. Así, se mejora la información que recibe el viajero al planificar sus gastos, conservando el funcionamiento original del módulo de presupuesto.

### 1: El contrato común (La Interfaz)

`negocio/GestorPreferencias.java`

```java
public interface GestorPresupuesto {
    double convertirAUsd(Presupuesto presupuesto);

    Map<String, String> validarPresupuesto(
        Presupuesto presupuesto, String paisDestino
    );

    String formatear(double valor);
}
```
Nota: "Cualquier cosa que quiera gestionar preferencias debe cumplir con este contrato". El decorador y el servicio base implementarán esta misma interfaz.

```java
public double calcularMargenImprevistos(Presupuesto presupuesto) {
    double presupuestoUsd = gestorBase.convertirAUsd(presupuesto);

    if (presupuestoUsd <= 0) {
        return 0;
    }

    return Math.round((presupuestoUsd * 0.05) * 100.0) / 100.0;
}
```

```java
double margen = decoradorMargen.calcularMargenImprevistos(presupuesto);
model.addAttribute("margenImprevistos", margen);
```


---




