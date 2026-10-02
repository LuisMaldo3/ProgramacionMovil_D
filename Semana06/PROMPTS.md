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