package org.group.analysis.structure.indice;

import org.group.analysis.model.Publicacion;
import org.group.analysis.structure.ListaEnlazada;
import org.group.analysis.structure.Nodo;

public class IndiceInvertido {

    private ListaEnlazada<ContenedorIndice> indice;

    public IndiceInvertido() {
        this.indice = new ListaEnlazada<>();
    }

    public void agregarIndice(String palabra, Publicacion publicacion) {
        Nodo<ContenedorIndice> actual = indice.getCabeza();
        ContenedorIndice entrada = null;
        while (actual != null) {
            if (actual.getDato().getPalabra().equalsIgnoreCase(palabra)) {
                entrada = actual.getDato();
                break;
            }
            actual = actual.getSiguiente();
        }

        if (entrada == null) {
            entrada = new ContenedorIndice(palabra);
            indice.agregar(entrada);
        }

        if (!entrada.getPublicaciones().contiene(publicacion)) {
            entrada.getPublicaciones().agregar(publicacion);
        }
    }

    public ListaEnlazada<Publicacion> obtenerPublicaciones(String palabra) {
        Nodo<ContenedorIndice> actual = indice.getCabeza();
        while (actual != null) {
            if (actual.getDato().getPalabra().equalsIgnoreCase(palabra)) {
                return actual.getDato().getPublicaciones();
            }
            actual = actual.getSiguiente();
        }
        return new ListaEnlazada<Publicacion>();
    }

    public ListaEnlazada<Publicacion> buscarInterseccion(ListaEnlazada<String> terminos) {
        ListaEnlazada<Publicacion> resultado = new ListaEnlazada<>();
        if (terminos == null || terminos.getCabeza() == null) {
            return resultado;
        }

        String primerTermino = terminos.getCabeza().getDato();
        ListaEnlazada<Publicacion> candidatos = obtenerPublicaciones(primerTermino);
        if (candidatos == null || candidatos.getCabeza() == null) {
            return resultado;
        }

        Nodo<Publicacion> actualPub = candidatos.getCabeza();
        while (actualPub != null) {
            Publicacion publicacion = actualPub.getDato();
            boolean coincideConTodos = true;

            Nodo<String> actualTerm = terminos.getCabeza().getSiguiente();
            while (actualTerm != null) {
                String termino = actualTerm.getDato();
                ListaEnlazada<Publicacion> publicacionesDelTermino = obtenerPublicaciones(termino);
                if (publicacionesDelTermino == null || !publicacionesDelTermino.contiene(publicacion)) {
                    coincideConTodos = false;
                    break;
                }
                actualTerm = actualTerm.getSiguiente();
            }

            if (coincideConTodos) {
                resultado.agregar(publicacion);
            }
            actualPub = actualPub.getSiguiente();
        }

        return resultado;
    }

    public int getTamanoVocabulario() {
        return this.indice.tamano();
    }
}
