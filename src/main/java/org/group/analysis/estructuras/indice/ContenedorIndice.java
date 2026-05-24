package org.group.analysis.estructuras.indice;

import org.group.analysis.modelos.Publicacion;
import org.group.analysis.estructuras.ListaEnlazada;

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
