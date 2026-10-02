# Mejora con IA - Laboratorio 06

## Prompt 1

Agrega a la aplicación TECSUP Store una opción Favoritos dentro del DropdownMenu de cada producto. Al presionarla, el producto debe agregarse o quitarse de una lista de favoritos sin usar base de datos.

### Respuesta resumida

La IA propuso mantener una lista observable con los identificadores de los productos favoritos y pasar una función desde AppNavigation hacia ProductosScreen para modificar la lista.

### Corrección realizada

Se adaptó el código a la estructura existente del proyecto y se evitó crear una base de datos porque el laboratorio trabaja únicamente con estado en memoria.

---

## Prompt 2

Agrega un contador visual en el ítem Favoritos del NavigationDrawer que muestre cuántos productos fueron marcados como favoritos desde el DropdownMenu.

### Respuesta resumida

La IA propuso compartir el estado de favoritos desde AppNavigation y utilizar un Badge dentro del NavigationDrawerItem de Favoritos.

### Corrección realizada

Se mantuvo la lista de favoritos en AppNavigation para que ProductosScreen y el drawer utilicen el mismo estado. De esta forma el contador se actualiza automáticamente.

---

## Prompt 3

Agrega una pantalla sencilla de Favoritos que muestre únicamente los productos seleccionados como favoritos y que el ítem Favoritos del drawer navegue hacia ella.

### Respuesta resumida

La IA propuso filtrar la lista original de productos utilizando los identificadores almacenados en favoritos.

### Corrección realizada

Se agregó FavoritosScreen reutilizando el modelo Producto existente y se evitó duplicar información de los productos.

---

## Prompt 4

Trabaja sobre el proyecto abierto TECSUP Store (Kotlin, Jetpack Compose y Material 3, paquete com.maldonado.tecsupstore, sin base de datos). Ya existen el DropdownMenu con la opción Favoritos, el contador (Badge) en el ítem Favoritos del drawer y la pantalla Favoritos. Completa lo que pide el laboratorio, sin romper lo anterior y sin agregar dependencias:

1. Cada tarjeta de producto, en TODAS las secciones (Inicio, Productos y Favoritos), debe tener el ícono de 3 puntos a la derecha con un DropdownMenu de 3 opciones: Favoritos (o "Quitar de favoritos" si ya lo es), Compartir y Reportar, cada una con su leadingIcon. Crea una sola TarjetaProducto reutilizable en components/TarjetaProducto.kt y úsala en las tres pantallas.
2. El NavigationDrawer debe tener mínimo estos destinos: Inicio, Mis pedidos, Favoritos y Perfil (se mantiene Productos y se agrega Cerrar sesión). Crea las pantallas simples Mis pedidos y Perfil, y agrega sus rutas en Screen.kt.
3. El encabezado del drawer muestra un avatar con las iniciales y el nombre y correo del usuario (modelo Usuario con una propiedad iniciales). El destino activo se resalta con NavigationDrawerItem(selected = ...).
4. El Badge de Favoritos usa el parámetro badge de NavigationDrawerItem y lee la MISMA lista de favoritos que el DropdownMenu, que vive en la función de navegación, para que el contador se actualice solo.
5. Estructura de archivos: TarjetaProducto.kt (ícono de 3 puntos + DropdownMenu), AppDrawer.kt (contenido del drawer con ModalDrawerSheet) y AppNavegacion.kt (envuelve todo con ModalNavigationDrawer). Renombra AppNavigation por AppNavegacion y corrige el nombre del archivo FavoritosScren.kt por FavoritosScreen.kt.
6. El producto de Inicio ya no debe duplicar la lista de productos: se recibe desde la navegación.

Pruebas: (a) en Inicio, Productos y Favoritos el ícono de 3 puntos abre el menú; (b) marcar Favoritos sube el contador del drawer y volver a marcar lo baja; (c) el destino activo del drawer se ve resaltado; (d) Mis pedidos y Perfil abren; (e) el encabezado muestra las iniciales. Al terminar, dime los archivos creados y modificados.

### Respuesta resumida

La IA creó TarjetaProducto en components y la reutilizó en Inicio, Productos y Favoritos; separó el drawer en AppDrawer (encabezado con iniciales, destinos, badge y Cerrar sesión) y dejó AppNavegacion con el ModalNavigationDrawer, el estado de favoritos y las rutas. También agregó las pantallas Mis pedidos y Perfil y el modelo Usuario.

### Corrección realizada

- La tarjeta de Inicio no tenía el ícono de 3 puntos y la lista de productos estaba repetida en HomeScreen; ahora Inicio recibe los productos y los favoritos desde AppNavegacion.
- El drawer solo tenía 3 destinos y un encabezado de texto; se agregaron Mis pedidos, Perfil y el avatar con iniciales.
- Compartir y Reportar no hacían nada visible; se les agregó un Toast para ver que responden.
- Se cambió el contador a la propiedad badge de NavigationDrawerItem en lugar de armar un Row dentro del label.
- Se renombró FavoritosScren.kt (error de escritura) y AppNavigation por AppNavegacion, y se actualizó MainActivity.
- Se eliminó gradle-daemon-jvm.properties, que pedía el JDK 25, para que Android Studio use su JDK incluido.
