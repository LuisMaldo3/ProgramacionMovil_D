# Lab 02 - Carrito de Compras en Kotlin

Nombre: Luis Miguel Maldonado Linares

## Descripción
Este proeycto meustyra en si una boleta de ventas, donde esta el total, el subtotal, el ig y un
descuento si esque el producto supera una cantidad, este se palica y tambien  muestra un orden detalaldo 
de todo el proceso que se realizo.

## Funciones implementadas
- calcularSubtotal():Recorre la lista de productos y por cada uno multiplica precio × cantidad
- calcularIGV():Recibe el subtotal y devuelve el 18% de ese valor
- calcularTotal():Recibe subtotal e IGV, y devuelve la suma de ambos
- mostrarDetalle():Imprime en consola la lista de productos con formato de columnas alineadas
- calcularDescuento():Usa when para decidir cuánto descuento aplicar: 10% si el total supera 5000, 5% si supera 3000, y 0 en cualquier otro caso

## Captura de consola
<img width="940" height="739" alt="image" src="https://github.com/user-attachments/assets/3a1c6abb-31ae-49d0-b633-3749970c962f" />
<img width="770" height="456" alt="image" src="https://github.com/user-attachments/assets/b1549663-5da9-4f22-823b-ad577acfae5b" />



## val vs var
val: es un valor inmutable, no s epuede reasignar depues de crealo
var: es variables, pues este si se puede reasignar.
