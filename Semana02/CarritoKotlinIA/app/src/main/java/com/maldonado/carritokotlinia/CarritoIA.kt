package com.maldonado.carritokotlinia

abstract class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
) {
    abstract fun calcularImpuesto(): Double

    fun calcularSubtotal(): Double = precio * cantidad

    override fun toString(): String =
        "$nombre (cantidad: $cantidad, precio: S/ ${"%.2f".format(precio)})"
}

class ProductoElectronico(
    nombre: String,
    precio: Double,
    cantidad: Int
) : Producto(nombre, precio, cantidad) {

    override fun calcularImpuesto(): Double =
        calcularSubtotal() * 0.18
}

class ProductoAlimento(
    nombre: String,
    precio: Double,
    cantidad: Int
) : Producto(nombre, precio, cantidad) {

    override fun calcularImpuesto(): Double =
        calcularSubtotal() * 0.10
}

class ProductoRopa(
    nombre: String,
    precio: Double,
    cantidad: Int
) : Producto(nombre, precio, cantidad) {

    override fun calcularImpuesto(): Double =
        calcularSubtotal() * 0.12
}

class Carrito {

    private val productos = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
        println("Producto agregado: ${producto.nombre}")
    }

    fun obtenerProductos(): List<Producto> =
        productos.toList()

    val size: Int
        get() = productos.size

    fun calcularSubtotalGeneral(): Double =
        productos.sumOf { it.calcularSubtotal() }

    fun calcularImpuestoGeneral(): Double =
        productos.sumOf { it.calcularImpuesto() }

    fun calcularTotal(): Double =
        calcularSubtotalGeneral() + calcularImpuestoGeneral()

    fun mostrarDetalle() {
        println("--------- DETALLE DEL CARRITO ---------")
        var i = 1
        for (p in productos) {
            val importe = p.calcularSubtotal()
            println(String.format("%d. %-20s x%d S/ %8.2f", i, p.nombre, p.cantidad, importe))
            i++
        }
        println("---------------------------------------")
        println("Cantidad de productos: $size")
    }
}

fun main() {

    println("=========================================")
    println("   CARRITO DE COMPRAS IA - TIENDA TECSUP")
    println("=========================================")

    val nombreCliente = "Juan Leon"
    val carrito = Carrito()

    println("Cliente: $nombreCliente")
    println()

    carrito.agregarProducto(
        ProductoElectronico("Laptop HP", 2500.0, 1)
    )

    carrito.agregarProducto(
        ProductoElectronico("Mouse Logitech", 45.5, 2)
    )

    carrito.agregarProducto(
        ProductoAlimento("Cafe Premium", 35.0, 3)
    )

    carrito.agregarProducto(
        ProductoRopa("Polo Deportivo", 60.0, 2)
    )

    println()

    val subtotal = carrito.calcularSubtotalGeneral()
    val impuesto = carrito.calcularImpuestoGeneral()
    val total = carrito.calcularTotal()

    println("Subtotal: S/ ${"%.2f".format(subtotal)}")
    println("Impuesto: S/ ${"%.2f".format(impuesto)}")
    println("Total:    S/ ${"%.2f".format(total)}")
}