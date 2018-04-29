# proyecto_call_center

Existe un call center donde hay 3 tipos de empleados: operador, supervisor y director. El proceso de la atención de una llamada telefónica en primera instancia debe ser atendida por un operador, si no hay ninguno libre debe ser atendida por un supervisor, y de no haber tampoco supervisores libres debe ser atendida por un director.

**Requerimientos**
- Debe existir una clase Dispatcher encargada de manejar las llamadas, y debe contener el método dispatchCall para que las asigne a los empleados disponibles.
- El método dispatchCall puede invocarse por varios hilos al mismo tiempo.
- La clase Dispatcher debe tener la capacidad de poder procesar 10 llamadas al mismo tiempo (de modo concurrente).
- Cada llamada puede durar un tiempo aleatorio entre 5 y 10 segundos.
- Debe tener un test unitario donde lleguen 10 llamadas.

**Observaciones**
1. Se creó un pool de hilos con el fin de administrar las 10 llamadas concurrentes y tener en espera hasta máximo tres llamadas.
2. Existe un tiempo de espera de 15 segundos para las llamadas que entren cuando ya hay 10 en ejecución.
3. Cuando entra una llamada y no hay asesor disponible, entra en "Standby" de 15 segundos, una vez cumplido ese tiempo y aún no hay asesor disponibles, la llamada se cancela.
