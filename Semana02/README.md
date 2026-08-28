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


## val vs var
val: es un valor inmutable, no s epuede reasignar depues de crealo
var: es variables, pues este si se puede reasignar.
