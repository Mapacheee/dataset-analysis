package org.group.analysis.structure.contactos;

import org.group.analysis.structure.ListaEnlazada;

class ContenedorContactos {
    private String nombreUsuario;
    private ListaEnlazada<String> amigos;

    public ContenedorContactos(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.amigos = new ListaEnlazada<>();
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public ListaEnlazada<String> getAmigos() {
        return amigos;
    }
}
