package org.group.analysis.structure;

import org.group.analysis.model.Post;

import java.util.HashMap;

public class RevertedIndex {

    private HashMap<String, LinkedList<Post>> index;

    public RevertedIndex() {
        this.index = new HashMap<>();
    }

    public void addIndex(String word, Post post) {
        index.putIfAbsent(word, new LinkedList<Post>());
        index.get(word).add(post);
    }

    public LinkedList<Post> getPosts(String word) {
        LinkedList<Post> list = index.get(word);
        if (list == null) {
            return new LinkedList<Post>();
        }
        return list;
    }

    public LinkedList<Post> buscarInterseccion(LinkedList<String> terminos) {
        LinkedList<Post> resultado = new LinkedList<>();
        if (terminos == null || terminos.getHead() == null) {
            return resultado;
        }

        String primerTermino = terminos.getHead().getData();
        LinkedList<Post> candidatos = index.get(primerTermino);
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
                LinkedList<Post> postsDelTermino = index.get(termino);
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