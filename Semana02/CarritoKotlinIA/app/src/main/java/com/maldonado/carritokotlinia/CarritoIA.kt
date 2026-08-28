package com.maldonado.carritokotlinia

class CarritoIA {
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

    class ProductoElectronico(nombre: String, precio: Double, cantidad: Int) :
        Producto(nombre, precio, cantidad) {
        override fun calcularImpuesto(): Double = calcularSubtotal() * 0.18
    }

    class ProductoAlimento(nombre: String, precio: Double, cantidad: Int) :
        Producto(nombre, precio, cantidad) {
        override fun calcularImpuesto(): Double = calcularSubtotal() * 0.10
    }

    class ProductoRopa(nombre: String, precio: Double, cantidad: Int) :
        Producto(nombre, precio, cantidad) {
        override fun calcularImpuesto(): Double = calcularSubtotal() * 0.12
    }

    fun main() {
        println("=========================================")
        println("   CARRITO DE COMPRAS IA - TIENDA TECSUP  ")
        println("=========================================")
    }
}
class Carrito {
    private val productos = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
        println("Producto agregado: ${producto.nombre}")
    }

    fun obtenerProductos(): List<Producto> = productos.toList()

    val size: Int
        get() = productos.size
}