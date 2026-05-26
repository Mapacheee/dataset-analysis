# Proyecto de Análisis de Red Social (TikTok)

**Grupo 8**

---

## Como ejecutarlo

### Requisitos
- Java

### Ejecutar la aplicación
```bash
./gradlew run
```
---

## Estructura del Proyecto

```
src/main/java/org/group/analysis/
├── ProyectoAnalisis.java          # Clase principal con menú interactivo
├── model/
│   ├── Usuario.java               # Modelo de usuario
│   └── Publicacion.java           # Modelo de publicación
└── structure/
    ├── Nodo.java                  # Nodo genérico para lista enlazada
    ├── ListaEnlazada.java         # Lista enlazada simple genérica
    ├── indice/
    │   ├── ContenedorIndice.java  # Asocia palabra clave con publicaciones
    │   └── IndiceInvertido.java   # Índice invertido para búsqueda textual
    └── contactos/
        ├── ContenedorContactos.java  # Asocia usuario con sus amigos
        └── IndiceContactos.java      # Índice de contactos (amigos)
```

---

## Estructuras de Datos Implementadas

### `Nodo<T>` (`structure/Nodo.java`)
Nodo para lista enlazada simple. Cada nodo contiene un dato de tipo genérico `T` y una referencia al siguiente nodo.

- **Atributos:** `dato` (T), `siguiente` (Nodo<T>)
- **Métodos:** `getDato()`, `getSiguiente()`, `setSiguiente()`

### `ListaEnlazada<T>` (`structure/ListaEnlazada.java`)
Lista enlazada simple que funciona como contenedor principal en todo el proyecto. No permite duplicados al agregar elementos.

- **Métodos:**
  - `agregar(T)` Agrega un elemento al final si no existe ya
  - `tamano()` Retorna la cantidad de elementos
  - `contiene(T)` Verifica si un elemento existe en la lista
  - `estaVacia()` Retorna `true` si la lista está vacía
  - `getCabeza()` Retorna el primer nodo (cabeza)

### `ContenedorIndice` (`structure/indice/ContenedorIndice.java`)
Contenedor que asocia una palabra clave con una lista de publicaciones que la contienen.

### `IndiceInvertido` (`structure/indice/IndiceInvertido.java`)
Estructura de índice invertido que guarda palabras clave a publicaciones.

- **Métodos:**
  - `agregarIndice(String, Publicacion)` Agraga al indice una publicación bajo una palabra clave
  - `obtenerPublicaciones(String)` Obtiene todas las publicaciones asociadas a una palabra
  - `buscarInterseccion(ListaEnlazada<String>)` Busca publicaciones que contengan todos los términos dados

### `ContenedorContactos` (`structure/contactos/ContenedorContactos.java`)
Contenedor que asocia un nombre de usuario con su lista de amigos.

### `IndiceContactos` (`structure/contactos/IndiceContactos.java`)
Estructura que mantiene un índice de amistades entre usuarios, permitiendo obtener los amigos de un usuario.

- **Métodos:**
  - `agregarContacto(String, String)` Registra una amistad
  - `obtenerContactos(String)` Retorna la lista de amigos de un usuario

---

## Descripción de Funciones

| Función | Descripción                                                                                                                     |
|---|---------------------------------------------------------------------------------------------------------------------------------|
| `main(String[])` | Carga el dataset CSV, genera datos (amigos y publicaciones) y muestra menu en consola                                           |
| `buscarUsuario(ListaEnlazada<Usuario>, String)` | Recorre la lista enlazada para encontrar un usuario por su nombre                                                               |
| `obtenerUsuarioPorIndice(ListaEnlazada<Usuario>, int)` | Obtiene un usuario por su posición en la lista                                                                                  |
| `esStopWord(String)` | Verifica si una palabra es considerada "stop word"                                                                              |
| `obtenerPalabrasClave(String)` | Convierte un texto a minúsculas, elimina puntuación (cosas como ".,;"), filtra stop words y retorna una lista de palabras clave |
| `generarDatos(ListaEnlazada<Usuario>, IndiceInvertido, IndiceContactos)` | Genera datos sintéticos: asigna 2-4 amigos aleatorios por usuario, crea 1-2 publicaciones con palabras y likes aleatorios       |
| `imprimirLista(ListaEnlazada<String>)` | Imprime los elementos de una lista separados por coma                                                                           |
| `imprimirPublicacion(Publicacion)` | Muestra los detalles de una publicación                                                                                         |
| `textoAleatorio(Random)` | Escoge una palabra aleatoria de: `perro`, `gato`, `blanco`, `casa`, `arbol`, `auto`                                             |

---

## Menu de consola

Al ejecutar se tienen todas estas opciones:

1. **Buscar publicaciones** Ingresa palabras clave y busca publicaciones que contengan todas ellas
2. **Ver amigos de un usuario** Muestra la lista de amigos de un usuario dado
3. **Ver perfil** Muestra información completa
4. **Crear publicación** Crea una nueva publicación para un usuario
5. **Dar like a una publicación** Agrega un "me gusta" de un usuario a una publicación por su ID
6. **Salir** Termina el programa

---
