package org.group.analysis.structure.contactos;

import org.group.analysis.structure.ListaEnlazada;
import org.group.analysis.structure.Nodo;

public class IndiceContactos {

    private ListaEnlazada<ContenedorContactos> indice;

    public IndiceContactos() {
        this.indice = new ListaEnlazada<>();
    }

    public void agregarContacto(String nombreUsuario, String nombreContacto) {
        Nodo<ContenedorContactos> actual = indice.getCabeza();
        ContenedorContactos entrada = null;
        while (actual != null) {
            if (actual.getDato().getNombreUsuario().equals(nombreUsuario)) {
                entrada = actual.getDato();
                break;
            }
            actual = actual.getSiguiente();
        }

        if (entrada == null) {
            entrada = new ContenedorContactos(nombreUsuario);
            indice.agregar(entrada);
        }

        entrada.getAmigos().agregar(nombreContacto);
    }

    public ListaEnlazada<String> obtenerContactos(String nombreUsuario) {
        Nodo<ContenedorContactos> actual = indice.getCabeza();
        while (actual != null) {
            if (actual.getDato().getNombreUsuario().equals(nombreUsuario)) {
                return actual.getDato().getAmigos();
            }
            actual = actual.getSiguiente();
        }
        return new ListaEnlazada<String>();
    }
}
