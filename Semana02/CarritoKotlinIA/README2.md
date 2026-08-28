# 🧭 Guía paso a paso — Configuración del entorno (Spring Boot)
**Desarrollo de Aplicaciones Web · Sesión 01**

Esta es tu guía de laboratorio (GLAB-S01) explicada con calma, clic por clic y línea por línea. Sigue el orden: cada parte se apoya en la anterior, así que no saltes pasos.

## Índice
1. [Instalar Java JDK](#1-instalar-java-jdk)
2. [Configurar la variable de entorno PATH](#2-configurar-la-variable-de-entorno-path)
3. [Instalar el IDE](#3-instalar-el-ide)
4. [Crear el proyecto con Spring Initializr](#4-crear-el-proyecto-con-spring-initializr)
5. [Tu primer controlador (@Controller)](#5-tu-primer-controlador-controller)
6. [Pasar datos a la vista con Model](#6-pasar-datos-a-la-vista-con-model)
7. [RestController — devolver JSON](#7-restcontroller--devolver-json)
8. [Agrupar rutas con @RequestMapping](#8-agrupar-rutas-con-requestmapping)
9. [Trabajar con un objeto propio (clase Empleado)](#9-trabajar-con-un-objeto-propio-clase-empleado)
10. [Extra: objeto completo en una vista HTML](#10-extra-mostrar-el-objeto-completo-en-una-vista-html)
11. [La actividad que debes entregar](#11--la-actividad-que-debes-entregar)
12. [Preguntas de reflexión](#12--preguntas-de-reflexión)
13. [Las 5 conclusiones](#13--las-5-conclusiones)

---

## 1. Instalar Java JDK

Spring Boot corre sobre Java, así que lo primero es instalar el JDK (Java Development Kit).

1. Abre este enlace: `https://www.oracle.com/pe/java/technologies/downloads/#jdk22-windows`
   > 💡 Es posible que Oracle te pida crear una cuenta gratuita para descargar. Es normal, créala o inicia sesión con una que ya tengas.
2. En la página, en la sección **Windows**, busca la fila **"x64 Installer"** y dale clic al link azul de descarga (el archivo `.exe`).
3. Ejecuta el instalador descargado y presiona **"Next"** en cada pantalla del asistente hasta que termine. No necesitas cambiar ninguna opción por defecto.
4. Cuando termine, ciérralo. Ya tienes Java instalado — pero falta un paso clave para que Windows lo reconozca desde cualquier lugar: la variable de entorno.

---

## 2. Configurar la variable de entorno PATH

Esto le dice a Windows dónde encontrar Java cuando escribes comandos como `java` en una terminal.

1. Abre el explorador de archivos y ubica esta ruta (ajusta el número de versión al que tú instalaste):
   ```
   C:\Program Files\Java\jdk-25.0.2\bin
   ```
   Selecciónala en la barra de direcciones y cópiala (`Ctrl + C`).
2. Presiona la tecla **Windows** y escribe **"variables de entorno"**.
3. Abre **"Editar las variables de entorno del sistema"**. En la ventana "Propiedades del sistema" que aparece, presiona el botón **"Variables de entorno..."**.
4. En la sección de abajo, **"Variables del sistema"**, busca la fila **Path**, selecciónala y presiona **"Editar..."**.
5. En la ventana "Editar variable de entorno": presiona **"Nuevo"**, pega la ruta que copiaste en el paso 1, y luego **"Aceptar"** en esa ventana y en las dos siguientes (Variables de entorno → Propiedades del sistema).
6. Para comprobar que funcionó, abre una ventana de comandos (busca **"cmd"** en el menú de inicio) y escribe:
   ```
   java --version
   ```
   Si te muestra la versión de Java, quedó listo ✅. Si da error, revisa que la ruta pegada sea exactamente la de tu carpeta de instalación, y prueba abriendo una ventana de `cmd` **nueva** (las que ya estaban abiertas no toman el cambio).

---

## 3. Instalar el IDE

El laboratorio menciona dos opciones. Todos los ejemplos y capturas de la guía están hechos en **Visual Studio Code**, así que te explico esa a fondo (IntelliJ queda como alternativa).

### Opción A — Visual Studio Code (la que usa toda la guía)
1. Ve a `https://code.visualstudio.com/` y presiona **"Download for Windows"**.
2. Ejecuta el instalador → acepta la licencia → Next, Next, Next → Install → Finish.
3. Abre VS Code. Ahora instala **3 extensiones**: clic en el ícono de **Extensiones** de la barra lateral izquierda (o `Ctrl+Shift+X`), busca cada una por nombre y presiona **Install**:
    - **Spring Boot Extension Pack** (de *VMware*) → trae las herramientas de Spring Boot y de Spring Initializr.
    - **Extension Pack for Java** (de *Microsoft*) → trae el soporte de Java, debugger, Maven, etc.
    - **Material Icon Theme** (de *Philipp Kief*) → opcional, solo cambia los íconos de carpetas/archivos para ubicarte mejor visualmente.

### Opción B — IntelliJ IDEA Community (alternativa)
1. Ve a `https://www.jetbrains.com/idea/download/other/`
2. Busca la versión más reciente, pestaña **Windows**, sección **Community Edition**, y descarga **Windows x64 (exe)**.
3. Instálalo con el asistente (Next, Next, Finish).

> 💡 De aquí en adelante, todos los pasos e imágenes de tu guía están hechos en **VS Code**. Te recomiendo seguir con esa para que todo te calce igual, salvo que tu profesor pida IntelliJ explícitamente.

---

## 4. Crear el proyecto con Spring Initializr

Spring Initializr genera la "base" del proyecto ya armada (carpetas, dependencias, `pom.xml`), para no tener que crear todo eso a mano.

1. Entra a `https://start.spring.io/`
2. Configura así (igual que en tu guía):

| Campo | Valor |
|---|---|
| Project | Maven |
| Language | Java |
| Spring Boot | La versión **estable** más reciente (evita las que digan `SNAPSHOT` o `M1`, son versiones de prueba) |
| Group | `com.tecsup` |
| Artifact | `demo` |
| Name | `demo_01` |
| Description | `Demo project for Spring Boot` |
| Package name | `com.tecsup` |

3. Clic en **"ADD DEPENDENCIES..."** (arriba a la derecha) y agrega estas tres:
    - **Spring Web**
    - **Spring Boot DevTools**
    - **Thymeleaf**
4. Presiona **"GENERATE"** (o `Ctrl + Enter`). Se descarga un archivo `.zip`.
5. Descomprime ese `.zip` en la carpeta donde quieras guardar tu laboratorio.
6. Abre VS Code → **File → Open Folder...** → selecciona la carpeta que acabas de descomprimir.
7. Espera unos segundos: abajo, en la barra de estado, debe decir **"Java: Ready"** cuando termine de indexar el proyecto. No avances hasta que aparezca eso.

> 💡 Fíjate en la estructura creada: `src/main/java/com/tecsup` es donde va tu código Java, y `src/main/resources` es donde van los HTML y configuraciones.

---

## 5. Tu primer controlador (`@Controller`)

Un **controlador** es la clase que "atiende" una URL y decide qué mostrar.

### 5.1 Crea la carpeta y la clase
1. En el panel Explorer, clic derecho sobre `com.tecsup` → **New Folder** → nómbrala `controller`.
2. Clic derecho sobre `controller` → **New File** → nómbrala `EjemploController.java`.
3. Pega este código:

```java
package com.tecsup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EjemploController {

    @GetMapping("/info")

    public String info(){
        return "info";
    }
}
```

> 💡 `@GetMapping("/info")` significa "cuando alguien entre a `/info`, ejecuta este método". El método devuelve el texto `"info"`, que Spring interpreta como el **nombre de un archivo HTML** a mostrar (gracias a `@Controller` + Thymeleaf).

### 5.2 Diferencia clave: `@Controller` vs `@RestController`

| Anotación | ¿Qué devuelve? | ¿Busca un HTML? |
|---|---|---|
| `@RestController` | Texto plano / JSON | ❌ No |
| `@Controller` | Una vista (HTML con Thymeleaf) | ✅ Sí |

### 5.3 Crea la vista HTML
1. Clic derecho en `src/main/resources/templates` → **New File** → `info.html`.
2. Pega:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Inicio</title>
</head>
<body>
    <h1>Bienvenidos a Thymeleaf 🚀</h1>
</body>
</html>
```

### 5.4 Cambia el puerto
Por defecto Spring Boot usa el puerto 8080, que a veces está ocupado por otro programa. Para evitar conflictos:
1. Abre `src/main/resources/application.properties`.
2. Escribe:
```properties
server.port=8081
```

### 5.5 Ejecuta la aplicación
1. En la barra lateral izquierda de VS Code busca el ícono de **Spring Boot Dashboard** (una hoja verde).
2. Bajo **APPS** vas a ver tu proyecto (`demo`). Pasa el mouse encima y presiona el ícono de **▷ (Run)**.
3. Espera a que en la terminal aparezca algo como `Started Demo01Application in X seconds`. Eso confirma que el servidor ya está corriendo.
4. Abre tu navegador y entra a:
   ```
   http://localhost:8081/info
   ```
   Deberías ver: **"Bienvenidos a Thymeleaf 🚀"**

---

## 6. Pasar datos a la vista con `Model`

Ahora vamos a enviar información desde el controlador hacia el HTML, en vez de solo mostrar texto fijo.

### 6.1 Modifica el controlador
```java
package com.tecsup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EjemploController {

    @GetMapping("/info")

    public String info(Model model){

        model.addAttribute("Nombre","Ricardo");
        model.addAttribute("Apellidos","Coello Palomino");

        return "info";
    }
}
```
> 💡 `Model` es como una "bolsa" donde metes datos con `addAttribute("clave", valor)`, y esa bolsa viaja hasta el HTML.

### 6.2 Modifica la vista `info.html`
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Inicio</title>
</head>
<body>
    <h1>Datos</h1>

    <p th:text="${Nombre}"></p>
    <p th:text="${Apellidos}"></p>

</body>
</html>
```
> 💡 `th:text="${Nombre}"` le dice a Thymeleaf: "reemplaza el contenido de esta etiqueta con lo que venga en el atributo `Nombre`".

### 6.3 Ejecuta de nuevo
Detén el servidor (ícono de stop ⏹ en el Spring Boot Dashboard) y vuelve a darle Run, o si DevTools ya está corriendo, solo guarda el archivo y espera a que recargue solo. Entra otra vez a `http://localhost:8081/info`. Ahora deberías ver:
```
Datos
Ricardo
Coello Palomino
```

> 💡 **Dato extra:** en vez de `Model` también puedes usar un `Map<String, Object>` para lograr algo parecido:
> ```java
> public String info(Map<String, Object> modelo){
>     modelo.put("Titulo", "Servidor en linea");
>     modelo.put("Servidor", "Informaticonfig");
>     modelo.put("Ip", "192.168.1.1");
>     return "detalles_info";
> }
> ```

---

## 7. `RestController` — devolver JSON

Cuando quieres construir una **API** (para que otra aplicación consuma tus datos, no una persona viendo una página), usas `@RestController` en vez de `@Controller`.

### 7.1 Crea `Ejemplo2Controller.java` (dentro de la carpeta `controller`)
```java
package com.tecsup.controller;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class Ejemplo2Controller {

    @GetMapping("/info2")

    public Map<String,Object> info2(Model model){

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("NroDNI","42174379");
        respuesta.put("Nombre","Ricardo");
        respuesta.put("Apellidos","Coello Palomino");

        return respuesta;
    }
}
```

### 7.2 Ejecuta y entra a
```
http://localhost:8081/info2
```
Vas a ver directamente el JSON:
```json
{
    "Nombre": "Ricardo",
    "Apellidos": "Coello Palomino",
    "NroDNI": "42174379"
}
```
> 💡 Esta vez no hiciste ningún HTML — `@RestController` convierte automáticamente el `Map` en JSON.

---

## 8. Agrupar rutas con `@RequestMapping`

Cuando tienes varios endpoints relacionados, puedes ponerles un "prefijo" común a nivel de clase.

### 8.1 Agrega `@RequestMapping("/api")` arriba de la clase
```java
package com.tecsup.controller;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Ejemplo2Controller {

    @GetMapping("/info2")

    public Map<String,Object> info2(Model model){

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("NroDNI","42174379");
        respuesta.put("Nombre","Ricardo");
        respuesta.put("Apellidos","Coello Palomino");

        return respuesta;
    }
}
```

### 8.2 Ahora la ruta cambia
Entra a:
```
http://localhost:8081/api/info2
```
La ruta final siempre es la suma del `@RequestMapping` de la clase + el `@GetMapping` del método: `/api` + `/info2` = `/api/info2`.

---

## 9. Trabajar con un objeto propio (clase `Empleado`)

En vez de devolver datos sueltos, ahora vamos a modelar un **objeto** completo.

### 9.1 Crea la carpeta `models`
Dentro de `com.tecsup` (mismo procedimiento que usaste con `controller`).

### 9.2 Crea la clase `Empleado.java`
```java
package com.tecsup.models;

public class Empleado {

    private String nombres, apellidos, direccion;
    private int telefono, id;

    public Empleado(String apellidos, String direccion, int id, String nombres, int telefono) {
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.id = id;
        this.nombres = nombres;
        this.telefono = telefono;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
```
> 💡 **Atajo:** no necesitas escribir todos los `get`/`set` a mano. En VS Code: clic derecho dentro de la clase → **Source Action...** → **Generate Getters and Setters** → selecciona todos los campos → Enter. El IDE los escribe por ti.

### 9.3 Crea `Ejemplo3Controller.java` (dentro de `controller`)
```java
package com.tecsup.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tecsup.models.Empleado;

@RestController
@RequestMapping("/api")
public class Ejemplo3Controller {

    @GetMapping("/info3")

    public Map<String,Object> info3(){

        Empleado empleado1 = new Empleado("Coello Palomino", "Ate", 1, "Ricardo", 933588638);

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("Empleado","Datos Empleado");
        respuesta.put("Informacion",empleado1);

        return respuesta;
    }
}
```

### 9.4 Ejecuta y entra a
```
http://localhost:8081/api/info3
```
Verás:
```json
{
    "Empleado": "Datos Empleado",
    "Informacion": {
        "apellidos": "Coello Palomino",
        "direccion": "Ate",
        "id": 1,
        "nombres": "Ricardo",
        "telefono": 933588638
    }
}
```

---

## 10. Extra: mostrar el objeto completo en una vista HTML

Este es un paso complementario de tu guía que combina todo lo anterior: un `@Controller` (no Rest) que arma un objeto y lo muestra en una página HTML con todos sus campos. Adaptándolo a tu propia clase `Empleado` de la Parte 9, quedaría así:

**Controlador:**
```java
package com.tecsup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tecsup.models.Empleado;

@Controller
public class Ejemplo4Controller {

    @GetMapping("/detalles_info")

    public String detalles(Model model){

        Empleado empleado1 = new Empleado("Coello Palomino", "Ate", 1, "Ricardo", 933588638);

        model.addAttribute("Titulo", "Página de empleado");
        model.addAttribute("Empleado", empleado1);

        return "detalles_info";
    }
}
```

**Vista `detalles_info.html`** (en `templates`):
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title th:text="${Titulo}">Pagina Spring Boot</title>
</head>
<body>
    <h1>Información de empleado</h1>
    <ul>
        <li><strong>Nombre:</strong> <span th:text="${Empleado.nombres}"></span></li>
        <li><strong>Apellidos:</strong> <span th:text="${Empleado.apellidos}"></span></li>
        <li><strong>Dirección:</strong> <span th:text="${Empleado.direccion}"></span></li>
        <li><strong>Teléfono:</strong> <span th:text="${Empleado.telefono}"></span></li>
        <li><strong>ID:</strong> <span th:text="${Empleado.id}"></span></li>
    </ul>
</body>
</html>
```
> 💡 Tu documento original muestra este mismo ejemplo con una clase que además tenía los campos `puesto` y `edad`. Si quieres replicarlo exactamente igual, solo agrega esos dos campos (con su get/set) a tu clase `Empleado` y dos líneas más en el HTML.

Entra a `http://localhost:8081/detalles_info` y verás la info dentro de una página HTML normal (no JSON), porque esta vez usaste `@Controller`.

---

## 11. 📝 La actividad que debes entregar

Tu guía pide una app llamada **"Información Personal"**, aplicando los 4 conceptos: **Controladores, Vistas HTML, Model y RestController**. Aquí está armada con tus datos, siguiendo el mismo patrón que ya usaste en las Partes 5-9:

- Nombre: Luis Miguel Maldonado Linares
- Edad: 18 años
- Dirección: El Agustino
- Teléfono: 916796360
- DNI: 60330977

### 11.1 Vista HTML con `Model` (`@Controller`)

Crea `PersonalController.java` dentro de `controller`:
```java
package com.tecsup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PersonalController {

    @GetMapping("/personal")

    public String personal(Model model){

        model.addAttribute("Nombre", "Luis Miguel Maldonado Linares");
        model.addAttribute("Edad", 18);
        model.addAttribute("Direccion", "El Agustino");
        model.addAttribute("Telefono", "916796360");
        model.addAttribute("Dni", "60330977");

        return "personal";
    }
}
```

Crea `personal.html` dentro de `templates`:
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Información Personal</title>
</head>
<body>
    <h1>Información Personal</h1>

    <p><strong>Nombre:</strong> <span th:text="${Nombre}"></span></p>
    <p><strong>Edad:</strong> <span th:text="${Edad}"></span></p>
    <p><strong>Dirección:</strong> <span th:text="${Direccion}"></span></p>
    <p><strong>Teléfono:</strong> <span th:text="${Telefono}"></span></p>
    <p><strong>DNI:</strong> <span th:text="${Dni}"></span></p>

</body>
</html>
```

Prueba en: `http://localhost:8081/personal`

### 11.2 API en JSON (`@RestController`)

Crea `PersonalApiController.java` dentro de `controller`:
```java
package com.tecsup.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PersonalApiController {

    @GetMapping("/personal")

    public Map<String,Object> personal(){

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("Nombre", "Luis Miguel Maldonado Linares");
        respuesta.put("Edad", 18);
        respuesta.put("Direccion", "El Agustino");
        respuesta.put("Telefono", "916796360");
        respuesta.put("Dni", "60330977");

        return respuesta;
    }
}
```

Prueba en: `http://localhost:8081/api/personal` y verás:
```json
{
    "Nombre": "Luis Miguel Maldonado Linares",
    "Edad": 18,
    "Direccion": "El Agustino",
    "Telefono": "916796360",
    "Dni": "60330977"
}
```

> 💡 Con esto ya cumples los 4 puntos de la actividad: Controlador, Vista HTML, `Model` y `RestController`. Ojo: ambos métodos se llaman `personal(...)` pero están en clases distintas (`PersonalController` y `PersonalApiController`), así que no chocan entre sí.

---

## 12. 💭 Preguntas de reflexión

Ideas base para que redactes tus propias respuestas:

- **¿Qué diferencia existe entre `@Controller` y `@RestController`?** `@Controller` devuelve el nombre de una vista HTML (Thymeleaf la renderiza); `@RestController` devuelve datos directos (texto/JSON) sin buscar ningún HTML.
- **¿Para qué sirve el objeto `Model`?** Es el "puente" para enviar datos desde el controlador Java hacia la vista HTML, usando `addAttribute("clave", valor)`.
- **¿Qué función cumple Spring Initializr?** Genera automáticamente la estructura base de un proyecto Spring Boot (carpetas, `pom.xml`, dependencias) según lo que configures, para no armarlo todo a mano.
- **¿Qué carpeta contiene las páginas HTML en Spring Boot?** `src/main/resources/templates`.
- **¿Qué archivo permite cambiar el puerto de la aplicación?** `src/main/resources/application.properties` (con la propiedad `server.port`).

---

## 13. ✅ Las 5 conclusiones

1. Seguir la guía paso a paso hizo que todo fuera fácil: no tuve problemas ni instalando el programa (JDK, VS Code y las extensiones) ni escribiendo el código de los ejemplos, porque cada parte estaba bien explicada y una llevaba a la otra.

2. Lo que más se me quedó grabado fue Spring Initializr: me sorprendió que con un par de clics arme todo el proyecto (carpetas, dependencias, el `pom.xml`) sin tener que armar nada a mano. Es algo que voy a seguir usando en los próximos laboratorios.

3. Entender la diferencia entre `@Controller` y `@RestController` me ayudó a entender todo el laboratorio: uno muestra una página HTML y el otro devuelve los datos directo en JSON. Verlo funcionar en el navegador me ayudó a entenderlo mejor.

4. Aun así, siento que todavía me faltan algunas bases: pude hacer todo porque seguía la guía, pero no sé si podría armar un controlador nuevo yo solo, sin mirar un ejemplo. Es algo que quiero practicar más.

5. En resumen, este laboratorio me ayudó a entender cómo funciona Spring Boot (los controladores, las vistas, el Model y la API), y ahora lo que sigue es practicar sin la guía para sentirme más seguro.

---

## 🔒 No olvides (normas del laboratorio)
- No manipules hardware ni conexiones eléctricas o de red.
- No comas ni bebas dentro del laboratorio.
- Deja tu mesa y silla limpias y ordenadas al terminar.

---

¡Éxitos con el laboratorio! Si te trabas en algún paso puntual (un error exacto en la terminal, una extensión que no aparece, un puerto ocupado, etc.), pégame el mensaje de error y lo revisamos juntos.