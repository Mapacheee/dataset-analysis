package org.group.analysis.structure.indice;

import org.group.analysis.model.Post;
import org.group.analysis.structure.LinkedList;

class ContenedorIndice {
    private String palabra;
    private LinkedList<Post> posts;

    public ContenedorIndice(String palabra) {
        this.palabra = palabra;
        this.posts = new LinkedList<>();
    }

    public String getPalabra() {
        return palabra;
    }

    public LinkedList<Post> getPosts() {
        return posts;
    }
}
