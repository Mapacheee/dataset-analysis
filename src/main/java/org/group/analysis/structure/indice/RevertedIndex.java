package org.group.analysis.structure.indice;

import org.group.analysis.model.Post;
import org.group.analysis.structure.LinkedList;
import org.group.analysis.structure.Node;

public class RevertedIndex {

    private LinkedList<ContenedorIndice> index;

    public RevertedIndex() {
        this.index = new LinkedList<>();
    }

    public void addIndex(String word, Post post) {
        Node<ContenedorIndice> curr = index.getHead();
        ContenedorIndice entrada = null;
        while (curr != null) {
            if (curr.getData().getPalabra().equalsIgnoreCase(word)) {
                entrada = curr.getData();
                break;
            }
            curr = curr.getNext();
        }

        if (entrada == null) {
            entrada = new ContenedorIndice(word);
            index.add(entrada);
        }

        if (!entrada.getPosts().contains(post)) {
            entrada.getPosts().add(post);
        }
    }

    public LinkedList<Post> getPosts(String word) {
        Node<ContenedorIndice> curr = index.getHead();
        while (curr != null) {
            if (curr.getData().getPalabra().equalsIgnoreCase(word)) {
                return curr.getData().getPosts();
            }
            curr = curr.getNext();
        }
        return new LinkedList<Post>();
    }

    public LinkedList<Post> buscarInterseccion(LinkedList<String> terminos) {
        LinkedList<Post> resultado = new LinkedList<>();
        if (terminos == null || terminos.getHead() == null) {
            return resultado;
        }

        String primerTermino = terminos.getHead().getData();
        LinkedList<Post> candidatos = getPosts(primerTermino);
        if (candidatos == null || candidatos.getHead() == null) {
            return resultado;
        }

        Node<Post> currPost = candidatos.getHead();
        while (currPost != null) {
            Post post = currPost.getData();
            boolean coincideConTodos = true;

            Node<String> currTerm = terminos.getHead().getNext();
            while (currTerm != null) {
                String termino = currTerm.getData();
                LinkedList<Post> postsDelTermino = getPosts(termino);
                if (postsDelTermino == null || !postsDelTermino.contains(post)) {
                    coincideConTodos = false;
                    break;
                }
                currTerm = currTerm.getNext();
            }

            if (coincideConTodos) {
                resultado.add(post);
            }
            currPost = currPost.getNext();
        }

        return resultado;
    }
}