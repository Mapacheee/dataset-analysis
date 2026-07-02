package org.group.analysis;

import org.group.analysis.model.Publicacion;
import org.group.analysis.model.Usuario;
import org.group.analysis.structure.ListaEnlazada;
import org.group.analysis.structure.Nodo;
import org.group.analysis.structure.indice.IndiceInvertido;
import org.group.analysis.structure.contactos.IndiceContactos;
import com.opencsv.CSVReader;
import org.group.analysis.structure.grafo.Grafo;
import org.group.analysis.structure.grafo.Grafo.ResultadoBFS;
import org.group.analysis.structure.hash.TablaHash;
import org.group.analysis.structure.hash.EntradaHash;

import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

public class ProyectoAnalisis {

    private static long idPublicacion = 2000000L;

    public static void main(String[] args) {
        ListaEnlazada<Usuario> usuarios = new ListaEnlazada<>();

        try {
            CSVReader csvReader = new CSVReader(new FileReader("src/main/resources/dataset.csv"));
            String[] data = csvReader.readNext();
            while (data != null) {
                try {
                    String nombreUsuario = data[1].trim();
                    String biografia = data[3].trim();
                    
                    int seguidores = 0;
                    try {
                        seguidores = Integer.parseInt(data[9].trim());
                    } catch (Exception e) {
                        // puede ser null
                    }

                    int seguidos = 0;
                    try {
                        seguidos = Integer.parseInt(data[10].trim());
                    } catch (Exception e) {
                        // puede ser null
                    }

                    Long id = 0L;
                    try {
                        id = Long.parseLong(data[14].trim());
                    } catch (Exception e) {
                        // puede ser null
                    }

                    if (buscarUsuario(usuarios, nombreUsuario) == null) {
                        Usuario usuario = new Usuario(id, nombreUsuario, biografia, seguidores, seguidos);
                        usuarios.agregar(usuario);
                    }

                } catch (Exception e) {}

                data = csvReader.readNext();
            }
            csvReader.close();

            IndiceInvertido indicePublicaciones = new IndiceInvertido();
            IndiceContactos indiceAmigos = new IndiceContactos();
            generarDatos(usuarios, indicePublicaciones, indiceAmigos);

            Grafo grafoSocial = new Grafo();
            grafoSocial.construirGrafo(usuarios, indiceAmigos);

            // Construir la Tabla Hash para frecuencia de términos (Entrega III)
            int N = indicePublicaciones.getTamanoVocabulario();
            int minM = (int) Math.ceil(1.5 * N);
            int M = TablaHash.siguientePrimo(minM);
            TablaHash tablaFrecuencia = new TablaHash(M);

            // Poblar la tabla hash recorriendo todas las publicaciones de todos los usuarios
            Nodo<Usuario> actualU = usuarios.getCabeza();
            while (actualU != null) {
                Usuario u = actualU.getDato();
                Nodo<Publicacion> actualPub = u.getPublicaciones().getCabeza();
                while (actualPub != null) {
                    ListaEnlazada<String> palabras = obtenerPalabrasClave(actualPub.getDato().getTexto());
                    Nodo<String> actualPal = palabras.getCabeza();
                    while (actualPal != null) {
                        tablaFrecuencia.insertarOIncrementar(actualPal.getDato());
                        actualPal = actualPal.getSiguiente();
                    }
                    actualPub = actualPub.getSiguiente();
                }
                actualU = actualU.getSiguiente();
            }

            double factorCarga = (double) N / M;
            System.out.println("Información tabla hash:");
            System.out.println("Tamaño del vocabulario (N): " + N);
            System.out.println("Tamaño elegido para la tabla (M): " + M + " (Número primo)");
            System.out.println("Factor de carga obtenido: " + String.format("%.4f", factorCarga));
            System.out.println("Total de colisiones: " + tablaFrecuencia.getTotalColisiones());
            System.out.println("Largo máximo de cadena de colisiones: " + tablaFrecuencia.getLargoMaximoCadena());
            System.out.println("Largo promedio de cadena de colisiones: " + String.format("%.4f", tablaFrecuencia.getLargoPromedioCadena()));
            System.out.println();
            Scanner scanner = new Scanner(System.in);
            boolean continuar = true;

            while (continuar) {
                System.out.println("1. Buscar publicaciones");
                System.out.println("2. Ver amigos de un usuario");
                System.out.println("3. Ver perfil");
                System.out.println("4. Crear publicación");
                System.out.println("5. Dar like a una publicación");
                System.out.println("6. Ver grados de conexión (BFS)");
                System.out.println("7. Ver Top-N términos más frecuentes");
                System.out.println("8. Salir");
                System.out.print("Elija opción (1-8): ");

                String opcion = scanner.nextLine().trim();
                System.out.println("---------------------------------------------");

                if (opcion.equals("1")) {
                    System.out.print("Ingrese palabras: ");
                    if (!scanner.hasNextLine()) {
                        continuar = false;
                        break;
                    }
                    String consulta = scanner.nextLine().trim();
                    ListaEnlazada<String> palabrasClave = obtenerPalabrasClave(consulta);

                    ListaEnlazada<Publicacion> publicaciones = indicePublicaciones.buscarInterseccion(palabrasClave);
                    if (publicaciones.estaVacia()) {
                        System.out.println("No se encontraron publicaciones.");
                    } else {
                        Nodo<Publicacion> actual = publicaciones.getCabeza();
                        while (actual != null) {
                            imprimirPublicacion(actual.getDato());
                            actual = actual.getSiguiente();
                        }
                    }
                } 
                else if (opcion.equals("2")) {
                    System.out.print("Ingrese usuario: ");
                    String nombreUsuario = scanner.nextLine().trim();
                    Usuario usuario = buscarUsuario(usuarios, nombreUsuario);

                    if (usuario == null) {
                        System.out.println("No existe el usuario.");
                    } else {
                        ListaEnlazada<String> amigos = indiceAmigos.obtenerContactos(nombreUsuario);
                        System.out.print("Amigos de " + nombreUsuario + ": ");
                        imprimirLista(amigos);
                    }
                } 
                else if (opcion.equals("3")) {
                    System.out.print("Ingrese usuario: ");
                    if (!scanner.hasNextLine()) {
                        continuar = false;
                        break;
                    }
                    String nombreUsuario = scanner.nextLine().trim();
                    Usuario usuario = buscarUsuario(usuarios, nombreUsuario);
                    if (usuario == null) {
                        System.out.println("No existe el usuario.");
                    } else {
                        System.out.println("Usuario: @" + usuario.getNombreUsuario());
                        System.out.println("Biografía: " + usuario.getBiografia());
                        System.out.println("Seguidores: " + usuario.getSeguidores() + " | Seguidos: " + usuario.getSeguidos());
                        System.out.print("Amigos: ");
                        imprimirLista(usuario.getAmigos());
                        System.out.println("Publicaciones:");
                        Nodo<Publicacion> actual = usuario.getPublicaciones().getCabeza();
                        while (actual != null) {
                            System.out.println("  ID Publicación: " + actual.getDato().getId());
                            System.out.println("  Texto: " + actual.getDato().getTexto());
                            System.out.print("  Likes: ");
                            imprimirLista(actual.getDato().getLikes());
                            actual = actual.getSiguiente();
                        }
                    }
                } 
                else if (opcion.equals("4")) {
                    System.out.print("Ingrese su usuario: ");
                    String nombreUsuario = scanner.nextLine().trim();
                    Usuario usuario = buscarUsuario(usuarios, nombreUsuario);
                    if (usuario == null) {
                        System.out.println("Usuario no existe.");
                    } else {
                        System.out.print("Ingrese texto: ");
                        String texto = scanner.nextLine().trim();
                        if (texto.isEmpty()) {
                            System.out.println("Vacío.");
                        } else {
                            boolean yaExiste = false;
                            Nodo<Publicacion> actual = usuario.getPublicaciones().getCabeza();
                            while (actual != null) {
                                if (actual.getDato().getTexto().equalsIgnoreCase(texto)) {
                                    yaExiste = true;
                                    break;
                                }
                                actual = actual.getSiguiente();
                            }

                            if (yaExiste) {
                                System.out.println("Error: duplicado.");
                            } else {
                                long id = idPublicacion++;
                                Publicacion nuevo = new Publicacion(id, usuario.getNombreUsuario(), texto, new ListaEnlazada<String>());
                                usuario.getPublicaciones().agregar(nuevo);

                                ListaEnlazada<String> palabras = obtenerPalabrasClave(texto);
                                Nodo<String> nodo = palabras.getCabeza();
                                while (nodo != null) {
                                    indicePublicaciones.agregarIndice(nodo.getDato(), nuevo);
                                    nodo = nodo.getSiguiente();
                                }
                                System.out.println("Publicación guardada. ID: " + id);
                            }
                        }
                    }
                } 
                else if (opcion.equals("5")) {
                    System.out.print("Ingrese su usuario: ");
                    if (!scanner.hasNextLine()) {
                        continuar = false;
                        break;
                    }
                    String nombreUsuario = scanner.nextLine().trim();
                    Usuario u = buscarUsuario(usuarios, nombreUsuario);
                    if (u == null) {
                        System.out.println("No existe.");
                        continue;
                    }
                    System.out.print("Ingrese ID de la publicación: ");
                    long idBusq = 0;
                    try {
                        idBusq = Long.parseLong(scanner.nextLine().trim());
                    } catch (Exception e) {
                        System.out.println("ID inválido.");
                        continue;
                    }

                    Publicacion publicacionDestino = null;
                    Nodo<Usuario> actualUsuario = usuarios.getCabeza();
                    while (actualUsuario != null) {
                        Usuario usuario = actualUsuario.getDato();
                        Nodo<Publicacion> actualPub = usuario.getPublicaciones().getCabeza();
                        while (actualPub != null) {
                            if (actualPub.getDato().getId() == idBusq) {
                                publicacionDestino = actualPub.getDato();
                                break;
                            }
                            actualPub = actualPub.getSiguiente();
                        }
                        if (publicacionDestino != null) break;
                        actualUsuario = actualUsuario.getSiguiente();
                    }

                    if (publicacionDestino == null) {
                        System.out.println("Publicación no encontrada.");
                    } else {
                        if (publicacionDestino.getLikes().contiene(nombreUsuario)) {
                            System.out.println("Ya tiene tu like.");
                        } else {
                            publicacionDestino.getLikes().agregar(nombreUsuario);
                            System.out.println("Like guardado.");
                        }
                    }
                } 
                else if (opcion.equals("6")) {
                    System.out.print("Ingrese usuario: ");
                    String nombreUsuario = scanner.nextLine().trim();
                    Usuario usuario = buscarUsuario(usuarios, nombreUsuario);

                    if (usuario == null) {
                        System.out.println("No existe el usuario.");
                    } else {
                        ResultadoBFS resultado = grafoSocial.obtenerGradosConexion(nombreUsuario);
                        System.out.println("Grados de conexión para @" + nombreUsuario + ":");
                        System.out.print("1° grado (amigos directos): ");
                        imprimirLista(resultado.getGrado1());
                        System.out.print("2° grado (amigos de amigos): ");
                        imprimirLista(resultado.getGrado2());
                        System.out.print("3° grado (amigos de 2° grado): ");
                        imprimirLista(resultado.getGrado3());
                    }
                } 
                else if (opcion.equals("7")) {
                    System.out.print("Ingrese el valor de N para ver los términos más frecuentes (ej. 5, 10, 20): ");
                    int topN = 5;
                    try {
                        topN = Integer.parseInt(scanner.nextLine().trim());
                    } catch (Exception e) {
                        System.out.println("Valor inválido. Se usará N = 5 por defecto.");
                    }
                    if (topN <= 0) {
                        System.out.println("El valor debe ser mayor que 0. Se usará N = 5 por defecto.");
                        topN = 5;
                    }
                    EntradaHash[] topTerminos = tablaFrecuencia.obtenerTopN(topN);
                    System.out.println("Top-" + topTerminos.length + " términos más frecuentes:");
                    for (int i = 0; i < topTerminos.length; i++) {
                        System.out.println((i + 1) + ". \"" + topTerminos[i].getTermino() + "\" - Frecuencia: " + topTerminos[i].getContador());
                    }
                }
                else if (opcion.equals("8")) {
                    System.out.println("saliendo.");
                    continuar = false;
                } 
                else {
                    System.out.println("opción incorrecta.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Usuario buscarUsuario(ListaEnlazada<Usuario> usuarios, String nombreUsuario) {
        Nodo<Usuario> actual = usuarios.getCabeza();
        while (actual != null) {
            if (actual.getDato().getNombreUsuario().equals(nombreUsuario)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    private static Usuario obtenerUsuarioPorIndice(ListaEnlazada<Usuario> usuarios, int idx) {
        Nodo<Usuario> actual = usuarios.getCabeza();
        int contador = 0;
        while (actual != null) {
            if (contador == idx) {
                return actual.getDato();
            }
            contador++;
            actual = actual.getSiguiente();
        }
        return null;
    }

    private static boolean esStopWord(String palabra) {
        String[] stopWords = {
            "el", "la", "los", "las", "un", "una", "unos", "unas", "y", "o", "pero", "en", "de", "del", "al", "que", "con", "para", "por", "si", "no", "su", "sus", "a",
            "the", "a", "an", "and", "or", "but", "in", "on", "at", "of", "to", "for", "with", "by", "is", "am", "are", "was", "were", "be", "have", "has", "had", "i", "you", "he", "she", "it"
        };
        for (int i = 0; i < stopWords.length; i++) {
            if (stopWords[i].equalsIgnoreCase(palabra)) {
                return true;
            }
        }
        return false;
    }

    private static ListaEnlazada<String> obtenerPalabrasClave(String texto) {
        ListaEnlazada<String> palabras = new ListaEnlazada<>();
        if (texto == null) return palabras;

        String limpio = texto.toLowerCase().replace(",", " ").replace(".", " ").replace("!", " ").replace("?", " ").replace("(", " ").replace(")", " ").replace("\"", " ").replace(";", " ").replace(":", " ");

        String[] palabrasLimpias = limpio.split("\\s+");
        for (int i = 0; i < palabrasLimpias.length; i++) {
            String palabraLimpia = palabrasLimpias[i].trim();
            if (!palabraLimpia.isEmpty() && !esStopWord(palabraLimpia)) {
                palabras.agregar(palabraLimpia);
            }
        }
        return palabras;
    }

    private static void generarDatos(ListaEnlazada<Usuario> usuarios, IndiceInvertido indicePublicaciones, IndiceContactos indiceAmigos) {
        Random rand = new Random();
        long idPostInicial = 1000000L;

        // generar amigos
        Nodo<Usuario> actualU1 = usuarios.getCabeza();
        while (actualU1 != null) {
            Usuario usuario = actualU1.getDato();
            int numAmigos = rand.nextInt(3) + 2; // 2 a 4 amigos
            while (usuario.getAmigos().tamano() < numAmigos) {
                Usuario amigo = obtenerUsuarioPorIndice(usuarios, rand.nextInt(usuarios.tamano()));
                if (amigo != null && !amigo.getNombreUsuario().equals(usuario.getNombreUsuario())) {
                    usuario.getAmigos().agregar(amigo.getNombreUsuario());
                    indiceAmigos.agregarContacto(usuario.getNombreUsuario(), amigo.getNombreUsuario());

                    amigo.getAmigos().agregar(usuario.getNombreUsuario());
                    indiceAmigos.agregarContacto(amigo.getNombreUsuario(), usuario.getNombreUsuario());
                }
            }
            actualU1 = actualU1.getSiguiente();
        }

        // generar publicaciones
        Nodo<Usuario> actualU2 = usuarios.getCabeza();
        while (actualU2 != null) {
            Usuario usuario = actualU2.getDato();
            int numPosts = rand.nextInt(2) + 1; // 1 o 2 posts
            for (int i = 0; i < numPosts; i++) {
                String texto = textoAleatorio(rand);

                ListaEnlazada<String> likes = new ListaEnlazada<>();
                if (!usuario.getAmigos().estaVacia()) {
                    int cantLikes = rand.nextInt(usuario.getAmigos().tamano()) + 1;
                    String[] amigos = new String[usuario.getAmigos().tamano()];
                    Nodo<String> cabeza = usuario.getAmigos().getCabeza();
                    int idx = 0;
                    while (cabeza != null) {
                        amigos[idx++] = cabeza.getDato();
                        cabeza = cabeza.getSiguiente();
                    }
                    for (int j = 0; j < cantLikes; j++) {
                        likes.agregar(amigos[rand.nextInt(amigos.length)]);
                    }
                }

                Publicacion publicacion = new Publicacion(idPostInicial++, usuario.getNombreUsuario(), texto, likes);
                usuario.getPublicaciones().agregar(publicacion);

                ListaEnlazada<String> terminos = obtenerPalabrasClave(texto);
                Nodo<String> cabezaNodo = terminos.getCabeza();
                while (cabezaNodo != null) {
                    indicePublicaciones.agregarIndice(cabezaNodo.getDato(), publicacion);
                    cabezaNodo = cabezaNodo.getSiguiente();
                }
            }
            actualU2 = actualU2.getSiguiente();
        }
    }

    private static void imprimirLista(ListaEnlazada<String> lista) {
        Nodo<String> actual = lista.getCabeza();
        if (actual == null) {
            System.out.println("vacia");
            return;
        }
        while (actual != null) {
            System.out.print(actual.getDato());
            if (actual.getSiguiente() != null) {
                System.out.print(", ");
            }
            actual = actual.getSiguiente();
        }
        System.out.println();
    }

    private static void imprimirPublicacion(Publicacion publicacion) {
        System.out.println("--------------------------------------------------");
        System.out.println("ID Publicación: " + publicacion.getId());
        System.out.println("Autor: @" + publicacion.getAutor());
        System.out.println("Contenido: \"" + publicacion.getTexto() + "\"");
        System.out.print("Likes (" + publicacion.getLikes().tamano() + "): ");
        imprimirLista(publicacion.getLikes());
    }

    private static String textoAleatorio(Random rand) {
        String[] palabras = {
                "Mi perro esta bailando, le voy a dar unas croquetas",
                "El gato rompió el sillón",
                "Mi pieza es blanco",
                "La casa es linda, pero es chica",
                "EL arbol se cayo",
                "El auto fue chocado por sus primos"
        };
        return palabras[rand.nextInt(palabras.length)];
    }
}
