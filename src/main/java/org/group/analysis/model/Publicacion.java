package org.group.analysis.model;

import org.group.analysis.structure.ListaEnlazada;

import java.util.Objects;

public class Publicacion {

    private Long id;
    private String autor;
    private String texto;
    private ListaEnlazada<String> likes;

    public Publicacion(Long id, String autor, String texto, ListaEnlazada<String> likes) {
        this.id = id;
        this.autor = autor;
        this.texto = texto;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public String getAutor() {
        return autor;
    }

    public String getTexto() {
        return texto;
    }

    public ListaEnlazada<String> getLikes() {
        return likes;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Publicacion publicacion = (Publicacion) obj;
        return this.id.equals(publicacion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
