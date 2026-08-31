

## Commit 1 — Ingreso de datos

**Prompt:**

Estoy haciendo una app en Kotlin con Android Studio, plantilla Empty Activity, para un trabajo
de mi curso de Programación Móvil. Es un control de estacionamiento y quiero avanzar por partes:
en esta primera parte solo necesito el ingreso de datos. Por cada vehículo que entra se deben
registrar cuatro datos: la placa, el tipo de vehículo, las horas que va a permanecer y el nombre
del cliente. El tipo de vehículo solo puede ser moto, auto o camioneta, no hay más opciones.
Hay una regla que se tiene que respetar sí o sí: ningún vehículo puede registrarse con menos de
una hora, así que si alguien pone 0 horas el sistema debe rechazarlo y decir por qué. No quiero
pantallas con formularios ni base de datos, los datos de prueba van directo en el código. Todo lo
que se registre (y lo que se rechace) debe verse en la consola, porque así lo vamos a revisar en
clase. Hazlo ordenado y con comentarios en español para que yo pueda explicarlo.

---

## Commit 2 — Cálculos

**Prompt:**

Sigo con la misma app de estacionamiento en Kotlin. Ya tengo el ingreso de datos funcionando;
ahora necesito la parte de los cálculos, que es la más importante. La tarifa básica depende del
vehículo: la moto paga 2 soles por hora, el auto 4 soles y la camioneta 10 soles. Sobre esa tarifa
se aplican estas reglas que nos dio el profesor: si el vehículo permanece hasta 2 horas paga la
tarifa normal; si permanece más de 2 horas y hasta 5, cada una de esas horas lleva un recargo del
20 %; y si permanece más de 5 horas, las horas que vienen después de la quinta llevan un recargo
del 50 %. Ojo que el recargo es por hora, no sobre toda la cuenta: por ejemplo un auto con 3 horas
paga 4.00 la primera hora, 4.00 la segunda y 4.80 la tercera, total 12.80. Aparte, si el cliente es
frecuente se le hace un 10 % de descuento sobre el total. Para decidir quién es frecuente quiero
usar la placa: si una misma placa ya se registró tres veces, desde la cuarta visita ya cuenta como
cliente frecuente y se le aplica el descuento. Necesito que el cálculo esté separado de la pantalla
para poder probarlo, y que en la consola salga por cada vehículo su tarifa básica, el subtotal, si
es frecuente o no, el descuento y el total.

---

## Commit 3 — Mostrar resultados

**Prompt:**

Última parte de la app de estacionamiento en Kotlin. Ya calculo todo bien; ahora necesito mostrar
el resultado exactamente con el formato que el profesor puso en la pizarra. Por cada vehículo se
debe ver primero la línea "Tarifa básica: S/ X", después una tabla con cuatro columnas: Hora,
Tarifa, Recargo e Importe, con una fila por cada hora que estuvo el vehículo, donde se vea la
tarifa de esa hora, el porcentaje de recargo que le tocó (0 %, 20 % o 50 %) y el importe que se
cobra por esa hora. Debajo de la tabla va el total a pagar, y si el cliente fue frecuente debe
indicarse el descuento del 10 % que se le aplicó. Con el ejemplo de la pizarra (auto, 3 horas) la
tabla debe mostrar 4.00, 4.00 y 4.80, y el total S/ 12.80. Todo esto tiene que imprimirse en la
consola con las columnas alineadas para que se lea como una tabla de verdad. Al final agrégame un
README que explique el trabajo y lo que se hizo en cada commit.