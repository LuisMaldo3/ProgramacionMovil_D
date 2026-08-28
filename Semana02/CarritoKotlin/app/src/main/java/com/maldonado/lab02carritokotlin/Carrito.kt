package com.maldonado.lab02carritokotlin



    data class Producto(
        val nombre: String,
        val precio: Double,
        var cantidad: Int,
    )

    fun calcularSubtotal(productos: List<Producto>): Double {
        var subtotal = 0.0
        for (p in productos) {
            subtotal += p.precio * p.cantidad
        }
        return subtotal
    }

    fun calcularIGV(subtotal: Double): Double {
        return subtotal * 0.18
    }

    fun calcularTotal(subtotal: Double, igv: Double): Double {
        return subtotal + igv
    }

    fun mostrarDetalle(productos: List<Producto>) {
        println("-----------DETALLE DEL CARRITO------------")
        var i = 1

        for (p in productos) {
            val importe = p.precio * p.cantidad

            println(
                String.format(
                    "%d. %-20s x%d S/ %8.2f",
                    i, p.nombre, p.cantidad, importe
                )
            )
            i++
        }
        println("------------------------------------------------")
    }

    fun calcularDescuento(total: Double): Double {
        return when {
            total > 5000 -> total * 0.10
            total > 3000 -> total * 0.05
            else -> 0.0
        }
    }
fun buscarProducto(productos: List<Producto>, nombre: String): Producto? {
    return productos.find { it.nombre == nombre }
}

    fun main(){
        println("=========================================")
        println("  CARRITO DE COMPRAS - TIENDA TECSUP")
        println("=========================================")

        val nombreCliente="Luis Maldonado"
        val carrito = mutableListOf<Producto>()


        println("cliente:$nombreCliente")
        println()

        carrito.add(Producto("Laptop HP", 2500.0, 1))
        carrito.add(Producto("Mouse Logitech", 45.5, 2))
        carrito.add(Producto("Teclado Tre", 150.9, 3))
        carrito.add(Producto("Audifonos R4", 122.6, 4))

        for(producto in carrito){
            println("Producto agregado: ${producto.nombre}")

        }
        mostrarDetalle(carrito)
        println("Cantidad de productos: ${carrito.size}")
        val subtotal=calcularSubtotal(carrito)
        val igv = calcularIGV(subtotal)
        val total = calcularTotal(subtotal, igv)
        val masCaro = carrito.maxByOrNull { it.precio }
        if (masCaro != null){
            println(
                "Producto mas caro : ${masCaro.nombre}"+
                String.format("(S/ %.2f)", masCaro.precio)
            )
        }

        val descuento = calcularDescuento(total)

        println(String.format(
            "Descuento aplicado: S/ %.2f", descuento
        ))

        val totalDescuento = total - descuento
        println(
            String.format(
                "TOTAL CON DESCUENTO: S/%.2f", totalDescuento
            )

        )
        val buscado = buscarProducto(carrito, "Mouse Logitech")
        if (buscado != null) {
            println("Encontrado: ${buscado.nombre}")
        } else {
            println("Producto no encontrado")
        }
        carrito.removeIf { it.nombre == "Mouse Logitech" }

        println()
        println("Carrito actualizado despues de eliminar:")
        mostrarDetalle(carrito)
        val subtotalNuevo = calcularSubtotal(carrito)
        val igvNuevo = calcularIGV(subtotalNuevo)
        val totalNuevo = calcularTotal(subtotalNuevo, igvNuevo)
        println(String.format("Nuevo subtotal: S/ %8.2f", subtotalNuevo))
        println(String.format("Nuevo IGV: S/ %8.2f", igvNuevo))
        println(String.format("Nuevo total: S/ %8.2f", totalNuevo))



        println()
        println(String.format("Subtotal: S/$ %8.2f", subtotal))
        println(String.format("IGV: S/ %8.2f", igv))
        println(String.format("Total: S/ %8.2f", total))


    }
