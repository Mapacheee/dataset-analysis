package org.group.analysis.structure.indice;

import org.group.analysis.model.Publicacion;
import org.group.analysis.structure.ListaEnlazada;

class ContenedorIndice {
    private String palabra;
    private ListaEnlazada<Publicacion> publicaciones;

    public ContenedorIndice(String palabra) {
        this.palabra = palabra;
        this.publicaciones = new ListaEnlazada<>();
    }

    public String getPalabra() {
        return palabra;
    }

    public ListaEnlazada<Publicacion> getPublicaciones() {
        return publicaciones;
    }
}
