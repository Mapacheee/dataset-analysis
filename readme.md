# Proyecto de Análisis de Red Social (TikTok)

**Grupo 8**

---

## Como ejecutarlo

### Requisitos
- Java

### Ejecutar la aplicación

1. Compilar y empaquetar el proyecto:
   ```bash
   ./gradlew shadowJar
   ```
2. Ejecutar la aplicación:
   ```bash
   java -jar build/libs/tarea-1.0-SNAPSHOT.jar
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
    ├── contactos/
    │   ├── ContenedorContactos.java  # Asocia usuario con sus amigos
    │   └── IndiceContactos.java      # Índice de contactos (amigos)
    ├── grafo/
    │   ├── Vertice.java           # Vértice del grafo social
    │   └── Grafo.java             # Representación y algoritmo BFS por niveles
    └── hash/
        ├── EntradaHash.java       # Par (término, contador)
        └── TablaHash.java         # Tabla hash propia con djb2 y encadenamiento
```

---

## Estructuras de Datos Implementadas

### `Nodo<T>` (`structure/Nodo.java`)
Nodo para lista enlazada simple. Cada nodo contiene un dato de tipo genérico `T` y una referencia al siguiente nodo.

### `ListaEnlazada<T>` (`structure/ListaEnlazada.java`)
Lista enlazada simple que funciona como contenedor principal en todo el proyecto. No permite duplicados al agregar elementos.

### `ContenedorIndice` (`structure/indice/ContenedorIndice.java`)
Contenedor que asocia una palabra clave con una lista de publicaciones que la contienen.

### `IndiceInvertido` (`structure/indice/IndiceInvertido.java`)
Estructura de índice invertido que guarda palabras clave a publicaciones.
- **Método importante**: `getTamanoVocabulario()` Retorna la cantidad $N$ de términos únicos en el vocabulario.

### `ContenedorContactos` (`structure/contactos/ContenedorContactos.java`)
Contenedor que asocia un nombre de usuario con su lista de amigos.

### `IndiceContactos` (`structure/contactos/IndiceContactos.java`)
Estructura que mantiene un índice de amistades entre usuarios, permitiendo obtener los amigos de un usuario.

### `Vertice` (`structure/grafo/Vertice.java`)
Representa un nodo en el grafo social. Contiene una referencia a su `Usuario` y una sublista de adyacencia de tipo `ListaEnlazada<Vertice>`.

### `Grafo` (`structure/grafo/Grafo.java`)
Grafo no dirigido para representar las relaciones de amistad.
- **Métodos**:
  - `construirGrafo(ListaEnlazada<Usuario>, IndiceContactos)`: Construye las aristas asegurando simetría, previniendo autolazos y duplicados.
  - `obtenerGradosConexion(String)`: Realiza un recorrido BFS por niveles para retornar contactos de 1°, 2° y 3° grado sin duplicidad.

### `EntradaHash` (`structure/hash/EntradaHash.java`)
Representa el par `(término, contador)` para almacenar las frecuencias de palabras.

### `TablaHash` (`structure/hash/TablaHash.java`)
Estructura de tabla hash propia. Resuelve colisiones por encadenamiento separado utilizando `ListaEnlazada` y aplica la función hash `djb2`.
- **Métodos**:
  - `insertarOIncrementar(String)`: Registra un término o aumenta su frecuencia en 1.
  - `obtenerTopN(int)`: Obtiene los N términos más frecuentes ordenados usando Insertion Sort in-place.
  - `siguientePrimo(int)`: Determina dinámicamente el menor número primo mayor o igual al parámetro dado.

---

## Funcionamiento recorrido BFS

El recorrido para encontrar los amigos de 1°, 2° y 3° grado funciona de manera muy directa y controlada:

- **Tiempo de ejecución**: Para encontrar los contactos directos e indirectos, el programa solo visita a cada usuario de la red una sola vez y revisa sus enlaces de amistad. Al marcar a las personas como "visitadas" apenas las encontramos, evitamos buscar dos veces a la misma persona o quedar atrapados en ciclos repetidos. Esto hace que el programa termine rápidamente sin importar qué tan grande sea la red.
- **Uso de memoria**: La memoria que requiere el algoritmo es muy baja. Solo guardamos de manera temporal la lista de personas que ya visitamos para no repetir y una cola de espera con los usuarios que están listos para ser revisados. La cantidad de elementos en estas listas auxiliares nunca supera la cantidad total de usuarios cargados en la aplicación.

---

## Parámetros de la Tabla Hash 

- **Cantidad de términos únicos (N)**: 24
- **Tamaño de la tabla (M)**: 37 
- **Factor de carga** : 0.6486
---

## Funciones clave

| Función | Descripción |
|---|---|
| `main(String[])` | Carga el dataset, construye el grafo, la tabla hash y muestra el menú interactivo. |
| `obtenerGradosConexion(String)` | Realiza un recorrido por niveles para retornar contactos directos e indirectos hasta 3° grado. |
| `insertarOIncrementar(String)` | Inserta un término en la tabla hash o incrementa su contador si ya existe usando la función djb2. |
| `obtenerTopN(int)` | Retorna un arreglo con los N términos más frecuentes de la tabla hash de mayor a menor frecuencia. |
| `buscarUsuario(ListaEnlazada<Usuario>, String)` | Recorre la lista enlazada para encontrar un usuario por su nombre. |
| `obtenerPalabrasClave(String)` | Convierte el texto a minúsculas, limpia signos de puntuación, filtra stopwords y retorna una lista de términos válidos. |

---
