package org.group.analysis.structure;

public class ListaEnlazada<T> {

    private Nodo<T> cabeza;

    public ListaEnlazada() {
        this.cabeza = null;
    }

    public void agregar(T dato) {
        if (cabeza == null) {
            cabeza = new Nodo<T>(dato);
            return;
        }

        Nodo<T> actual = cabeza;

        while (actual.getSiguiente() != null) {
            if (actual.getDato().equals(dato)) {
                return;
            }
            actual = actual.getSiguiente();
        }

        if (actual.getDato().equals(dato)) {
            return;
        }

        actual.setSiguiente(new Nodo<T>(dato));
    }

    public int tamano() {
        int contador = 0;
        Nodo<T> actual = cabeza;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }

    public boolean contiene(T dato) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public Nodo<T> getCabeza() {
        return cabeza;
    }
}
