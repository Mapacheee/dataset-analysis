package org.group.analysis;

import org.group.analysis.model.Post;
import org.group.analysis.model.User;
import org.group.analysis.structure.LinkedList;
import org.group.analysis.structure.Node;
import org.group.analysis.structure.RevertedIndex;
import org.group.analysis.structure.UserContactsIndex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AnalysisProject {

    private static long idPostManual = 2000000L;

    public static void main(String[] args) {
        List<User> usuarios = new ArrayList<>();
        HashMap<String, User> mapaUsuarios = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/dataset.csv"));
            String linea = br.readLine();
            boolean esCabecera = true;

            while (linea != null) {
                if (esCabecera) {
                    esCabecera = false;
                    linea = br.readLine();
                    continue;
                }

				String[] data = linea.split(",");
				if (data.length < 15) {
					linea = br.readLine();
					continue;
				}

				String username = data[1].replace("\"", "").trim();
				String biografia = data[3].replace("\"", "").trim();
				int seguidores = Integer.parseInt(data[9].replace("\"", "").trim());
				int seguidos = Integer.parseInt(data[10].replace("\"", "").trim());
				Long id = Long.parseLong(data[14].replace("\"", "").trim());

				if (username.isEmpty() || username.equals("null")) {
					linea = br.readLine();
					continue;
				}

				if (!mapaUsuarios.containsKey(username)) {
					User user = new User(id, username, biografia, seguidores, seguidos);
					usuarios.add(user);
					mapaUsuarios.put(username, user);
				}
				linea = br.readLine();
            }
            br.close();


            RevertedIndex indicePosts = new RevertedIndex();
            UserContactsIndex indiceAmigos = new UserContactsIndex();
            generarDatos(usuarios, indicePosts, indiceAmigos);

            Scanner scanner = new Scanner(System.in);
            boolean continuar = true;

            while (continuar) {
                System.out.println("1. Buscar posts");
                System.out.println("2. Ver amigos de un usuario");
                System.out.println("3. Ver perfil");
                System.out.println("4. Crear post");
                System.out.println("5. Dar like a un post");
                System.out.println("6. Salir");
                System.out.print("Elija opcion (1-6): ");

                String opcion = scanner.nextLine().trim();
                System.out.println("---------------------------------------------");

                if (opcion.equals("1")) {
                    System.out.print("Ingrese palabras: ");
                    String consulta = scanner.nextLine().trim();
                    LinkedList<String> palabrasClave = obtenerPalabrasClave(consulta);

                    LinkedList<Post> posts = indicePosts.buscarInterseccion(palabrasClave);
                    if (posts.isEmpty()) {
                        System.out.println("No se encontraron posts.");
                    } else {
                        Node<Post> curr = posts.getHead();
                        while (curr != null) {
                            imprimirPost(curr.getData());
                            curr = curr.getNext();
                        }
                    }
                } 
                else if (opcion.equals("2")) {
                    System.out.print("Ingrese usuario: ");
                    String username = scanner.nextLine().trim();
                    User u = mapaUsuarios.get(username);
                    if (u == null) {
                        System.out.println("No existe el usuario.");
                    } else {
                        LinkedList<String> amigos = indiceAmigos.getContacts(username);
                        System.out.print("Amigos de " + username + ": ");
                        imprimirLista(amigos);
                    }
                } 
                else if (opcion.equals("3")) {
                    System.out.print("Ingrese usuario: ");
                    String username = scanner.nextLine().trim();
                    User u = mapaUsuarios.get(username);
                    if (u == null) {
                        System.out.println("No existe el usuario.");
                    } else {
                        System.out.println("Username: @" + u.getUsername());
                        System.out.println("Bio: " + u.getBiography());
                        System.out.println("Seguidores: " + u.getFollowers() + " | Seguidos: " + u.getFollowing());
                        System.out.print("Amigos: ");
                        imprimirLista(u.getFriends());
                        System.out.println("Posts:");
                        Node<Post> curr = u.getPosts().getHead();
                        while (curr != null) {
                            System.out.println("  ID Post: " + curr.getData().getId());
                            System.out.println("  Texto: " + curr.getData().getText());
                            System.out.print("  Likes: ");
                            imprimirLista(curr.getData().getLikes());
                            curr = curr.getNext();
                        }
                    }
                } 
                else if (opcion.equals("4")) {
                    System.out.print("Ingrese su usuario: ");
                    String username = scanner.nextLine().trim();
                    User u = mapaUsuarios.get(username);
                    if (u == null) {
                        System.out.println("Usuario no existe.");
                    } else {
                        System.out.print("Ingrese texto: ");
                        String texto = scanner.nextLine().trim();
                        if (texto.isEmpty()) {
                            System.out.println("Vacio.");
                        } else {
                            boolean yaExiste = false;
                            Node<Post> curr = u.getPosts().getHead();
                            while (curr != null) {
                                if (curr.getData().getText().equalsIgnoreCase(texto)) {
                                    yaExiste = true;
                                    break;
                                }
                                curr = curr.getNext();
                            }

                            if (yaExiste) {
                                System.out.println("Error: Duplicado.");
                            } else {
                                long id = idPostManual++;
                                Post nuevo = new Post(id, u.getUsername(), texto, new LinkedList<String>());
                                u.getPosts().add(nuevo);

                                LinkedList<String> palabras = obtenerPalabrasClave(texto);
                                Node<String> n = palabras.getHead();
                                while (n != null) {
                                    indicePosts.addIndex(n.getData(), nuevo);
                                    n = n.getNext();
                                }
                                System.out.println("Post guardado con éxito! ID: " + id);
                            }
                        }
                    }
                } 
                else if (opcion.equals("5")) {
                    System.out.print("Ingrese su usuario: ");
                    String username = scanner.nextLine().trim();
                    User u = mapaUsuarios.get(username);
                    if (u == null) {
                        System.out.println("No existe.");
                        continue;
                    }
                    System.out.print("Ingrese ID del post: ");
                    long idBusq = Long.parseLong(scanner.nextLine().trim());

                    Post postDestino = null;
                    for (User user : usuarios) {
                        Node<Post> curr = user.getPosts().getHead();
                        while (curr != null) {
                            if (curr.getData().getId() == idBusq) {
                                postDestino = curr.getData();
                                break;
                            }
                            curr = curr.getNext();
                        }
                        if (postDestino != null) break;
                    }

                    if (postDestino == null) {
                        System.out.println("Post no encontrado.");
                    } else {
                        if (postDestino.getLikes().contains(username)) {
                            System.out.println("Ya tiene tu like.");
                        } else {
                            postDestino.getLikes().add(username);
                            System.out.println("Like guardado.");
                        }
                    }
                } 
                else if (opcion.equals("6")) {
                    System.out.println("Saliendo.");
                    continuar = false;
                } 
                else {
                    System.out.println("Opcion incorrecta.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
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

    private static LinkedList<String> obtenerPalabrasClave(String texto) {
        LinkedList<String> palabras = new LinkedList<>();
        if (texto == null) return palabras;

        String limpio = texto.toLowerCase()
            .replace(",", " ")
            .replace(".", " ")
            .replace("!", " ")
            .replace("?", " ")
            .replace("(", " ")
            .replace(")", " ")
            .replace("\"", " ")
            .replace(";", " ")
            .replace(":", " ");

        String[] tokens = limpio.split("\\s+");
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (!token.isEmpty() && !esStopWord(token)) {
                palabras.add(token);
            }
        }
        return palabras;
    }

    private static void generarDatos(List<User> usuarios, RevertedIndex indicePosts, UserContactsIndex indiceAmigos) {
        Random rand = new Random(42);
        long idPostInicial = 1000000L;

        for (User u : usuarios) {
            int numAmigos = rand.nextInt(3) + 2; // 2 a 4 amigos
            while (u.getFriends().size() < numAmigos) {
                User amigo = usuarios.get(rand.nextInt(usuarios.size()));
                if (!amigo.getUsername().equals(u.getUsername())) {
                    u.getFriends().add(amigo.getUsername());
                    indiceAmigos.addContact(u.getUsername(), amigo.getUsername());

                    amigo.getFriends().add(u.getUsername());
                    indiceAmigos.addContact(amigo.getUsername(), u.getUsername());
                }
            }
        }

        for (User u : usuarios) {
            int numPosts = rand.nextInt(2) + 1; // 1 o 2 posts
            for (int i = 0; i < numPosts; i++) {
                String texto = "Hola a todos! Estoy probando mi tarea en #java #coding";
                if (i == 1) {
                    texto = "Mi primera estructura de datos propia. #datastructures #happy";
                }

                LinkedList<String> likes = new LinkedList<>();
                if (!u.getFriends().isEmpty()) {
                    int cantLikes = rand.nextInt(u.getFriends().size()) + 1;
                    String[] amigos = new String[u.getFriends().size()];
                    Node<String> c = u.getFriends().getHead();
                    int idx = 0;
                    while (c != null) {
                        amigos[idx++] = c.getData();
                        c = c.getNext();
                    }
                    for (int j = 0; j < cantLikes; j++) {
                        likes.add(amigos[rand.nextInt(amigos.length)]);
                    }
                }

                Post post = new Post(idPostInicial++, u.getUsername(), texto, likes);
                u.getPosts().add(post);

                LinkedList<String> terminos = obtenerPalabrasClave(texto);
                Node<String> n = terminos.getHead();
                while (n != null) {
                    indicePosts.addIndex(n.getData(), post);
                    n = n.getNext();
                }
            }
        }
    }

    private static void imprimirLista(LinkedList<String> lista) {
        Node<String> curr = lista.getHead();
        if (curr == null) {
            System.out.println("Vacia");
            return;
        }
        while (curr != null) {
            System.out.print(curr.getData());
            if (curr.getNext() != null) {
                System.out.print(", ");
            }
            curr = curr.getNext();
        }
        System.out.println();
    }

    private static void imprimirPost(Post post) {
        System.out.println("--------------------------------------------------");
        System.out.println("ID Post: " + post.getId());
        System.out.println("Autor: @" + post.getAutor());
        System.out.println("Contenido: \"" + post.getText() + "\"");
        System.out.print("Likes (" + post.getLikes().size() + "): ");
        imprimirLista(post.getLikes());
    }
}
