package org.group.analysis.structure.grafo;

import org.group.analysis.model.Usuario;
import org.group.analysis.structure.ListaEnlazada;
import org.group.analysis.structure.Nodo;
import org.group.analysis.structure.contactos.IndiceContactos;

import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Set;
import java.util.HashSet;

public class Grafo {
    private ListaEnlazada<Vertice> vertices;

    public Grafo() {
        this.vertices = new ListaEnlazada<>();
    }

    public ListaEnlazada<Vertice> getVertices() {
        return vertices;
    }

    public Vertice buscarVertice(String nombreUsuario) {
        Nodo<Vertice> actual = vertices.getCabeza();
        while (actual != null) {
            if (actual.getDato().getUsuario().getNombreUsuario().equals(nombreUsuario)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public void agregarVertice(Usuario usuario) {
        if (buscarVertice(usuario.getNombreUsuario()) == null) {
            vertices.agregar(new Vertice(usuario));
        }
    }

    public void agregarArista(String usuarioA, String usuarioB) {
        if (usuarioA.equals(usuarioB)) {
            return;
        }

        Vertice vA = buscarVertice(usuarioA);
        Vertice vB = buscarVertice(usuarioB);

        if (vA != null && vB != null) {
            vA.agregarVecino(vB);
            vB.agregarVecino(vA);
        }
    }

    public void construirGrafo(ListaEnlazada<Usuario> listaUsuarios, IndiceContactos indiceContactos) {
        //agregar todos los usuarios como vértices
        Nodo<Usuario> actualU = listaUsuarios.getCabeza();
        while (actualU != null) {
            agregarVertice(actualU.getDato());
            actualU = actualU.getSiguiente();
        }

        // 2. Agregar las aristas basadas en el IndiceContactos, asegurando simetría
        actualU = listaUsuarios.getCabeza();
        while (actualU != null) {
            Usuario u = actualU.getDato();
            ListaEnlazada<String> amigos = indiceContactos.obtenerContactos(u.getNombreUsuario());
            Nodo<String> actualAmigo = amigos.getCabeza();
            while (actualAmigo != null) {
                String nombreAmigo = actualAmigo.getDato();
                agregarArista(u.getNombreUsuario(), nombreAmigo);
                actualAmigo = actualAmigo.getSiguiente();
            }
            actualU = actualU.getSiguiente();
        }
    }

    /**
     * Retorna los contactos de 1°, 2° y 3° grado de un usuario raíz usando BFS por niveles.
     */
    public ResultadoBFS obtenerGradosConexion(String nombreRaiz) {
        Vertice raiz = buscarVertice(nombreRaiz);
        if (raiz == null) {
            return new ResultadoBFS();
        }

        ResultadoBFS resultado = new ResultadoBFS();
        
        // Estructuras auxiliares para el recorrido BFS
        Queue<Vertice> queue = new ArrayDeque<>();
        Set<String> visitados = new HashSet<>();

        // Inicializar BFS
        queue.add(raiz);
        visitados.add(raiz.getUsuario().getNombreUsuario());

        int nivel = 0;

        while (!queue.isEmpty() && nivel < 3) {
            int nodosEnNivel = queue.size();
            nivel++;

            for (int i = 0; i < nodosEnNivel; i++) {
                Vertice actual = queue.poll();
                
                Nodo<Vertice> vecinoNodo = actual.getVecinos().getCabeza();
                while (vecinoNodo != null) {
                    Vertice vecino = vecinoNodo.getDato();
                    String nombreVecino = vecino.getUsuario().getNombreUsuario();

                    if (!visitados.contains(nombreVecino)) {
                        visitados.add(nombreVecino);
                        queue.add(vecino);

                        // Clasificar el contacto según el nivel/grado
                        if (nivel == 1) {
                            resultado.getGrado1().agregar(nombreVecino);
                        } else if (nivel == 2) {
                            resultado.getGrado2().agregar(nombreVecino);
                        } else if (nivel == 3) {
                            resultado.getGrado3().agregar(nombreVecino);
                        }
                    }
                    vecinoNodo = vecinoNodo.getSiguiente();
                }
            }
        }

        return resultado;
    }

    public static class ResultadoBFS {
        private ListaEnlazada<String> grado1 = new ListaEnlazada<>();
        private ListaEnlazada<String> grado2 = new ListaEnlazada<>();
        private ListaEnlazada<String> grado3 = new ListaEnlazada<>();

        public ListaEnlazada<String> getGrado1() {
            return grado1;
        }

        public ListaEnlazada<String> getGrado2() {
            return grado2;
        }

        public ListaEnlazada<String> getGrado3() {
            return grado3;
        }
    }
}