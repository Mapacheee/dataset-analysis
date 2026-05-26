package org.group.analysis.model;

import org.group.analysis.structure.ListaEnlazada;

public class Usuario {

    private Long id;
    private String nombreUsuario;
    private String biografia;
    private int seguidores;
    private int seguidos;
    private ListaEnlazada<String> amigos;
    private ListaEnlazada<Publicacion> publicaciones;

    public Usuario(Long id, String nombreUsuario, String biografia, int seguidores, int seguidos) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.biografia = biografia;
        this.seguidores = seguidores;
        this.seguidos = seguidos;
        this.amigos = new ListaEnlazada<>();
        this.publicaciones = new ListaEnlazada<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getSeguidos() {
        return seguidos;
    }

    public void setSeguidos(int seguidos) {
        this.seguidos = seguidos;
    }

    public ListaEnlazada<String> getAmigos() {
        return amigos;
    }

    public void setAmigos(ListaEnlazada<String> amigos) {
        this.amigos = amigos;
    }

    public ListaEnlazada<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(ListaEnlazada<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return this.nombreUsuario.equals(usuario.nombreUsuario);
    }

    @Override
    public int hashCode() {
        return nombreUsuario.hashCode();
    }
}
