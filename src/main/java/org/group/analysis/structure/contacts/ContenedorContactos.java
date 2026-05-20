package org.group.analysis.structure.contacts;

import org.group.analysis.structure.LinkedList;

class ContenedorContactos {
    private String username;
    private LinkedList<String> amigos;

    public ContenedorContactos(String username) {
        this.username = username;
        this.amigos = new LinkedList<>();
    }

    public String getUsername() {
        return username;
    }
    public LinkedList<String> getAmigos() {
        return amigos;
    }
}
