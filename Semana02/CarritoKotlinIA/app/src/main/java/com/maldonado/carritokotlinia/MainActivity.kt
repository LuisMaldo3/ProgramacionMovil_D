fun main() {
    println("=========================================")
    println("   CARRITO DE COMPRAS IA - TIENDA TECSUP  ")
    println("=========================================")

    val nombreCliente = "Juan Leon"
    val carrito = Carrito()
    println("Cliente: $nombreCliente")
    println()

    carrito.agregarProducto(ProductoElectronico("Laptop HP", 2500.0, 1))
    carrito.agregarProducto(ProductoElectronico("Mouse Logitech", 45.5, 2))
    carrito.agregarProducto(ProductoAlimento("Cafe Premium", 35.0, 3))
    carrito.agregarProducto(ProductoRopa("Polo Deportivo", 60.0, 2))

    println()
    carrito.mostrarDetalle()
    val subtotal = carrito.calcularSubtotalGeneral()
    val impuesto = carrito.calcularImpuestoGeneral()
    val total = carrito.calcularTotal()

    println("Subtotal: S/ ${"%.2f".format(subtotal)}")
    println("Impuesto: S/ ${"%.2f".format(impuesto)}")
    println("Total:    S/ ${"%.2f".format(total)}")

    val masCaro = carrito.productoMasCaro()
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " + String.format("(S/ %.2f)", masCaro.precio))
    }

    val descuento = carrito.calcularDescuento()
    val totalConDescuento = total - descuento
    println("Descuento aplicado: S/ ${"%.2f".format(descuento)}")
    println("TOTAL CON DESCUENTO: S/ ${"%.2f".format(totalConDescuento)}")
}