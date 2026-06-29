package org.group.analysis.structure.grafo;

import org.group.analysis.model.Usuario;
import org.group.analysis.structure.ListaEnlazada;

public class Vertice {
    private Usuario usuario;
    private ListaEnlazada<Vertice> vecinos;

    public Vertice(Usuario usuario) {
        this.usuario = usuario;
        this.vecinos = new ListaEnlazada<>();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public ListaEnlazada<Vertice> getVecinos() {
        return vecinos;
    }

    public void agregarVecino(Vertice vecino) {
        if (!this.vecinos.contiene(vecino)) {
            this.vecinos.agregar(vecino);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Vertice other = (Vertice) obj;
        return this.usuario.getNombreUsuario().equals(other.usuario.getNombreUsuario());
    }

    @Override
    public int hashCode() {
        return this.usuario.getNombreUsuario().hashCode();
    }
}
