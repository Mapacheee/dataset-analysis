package org.group.analysis.model;

import org.group.analysis.structure.LinkedList;

import java.util.Objects;

public class Post {

    private String id;
    private String autor;
    private String text;
    private LinkedList<String> likes;

    public Post(String id, String autor, String text, LinkedList<String> likes) {
        this.id = id;
        this.autor = autor;
        this.text = text;
        this.likes = likes;
    }

    public String getId() {
        return id;
    }
    public String getAutor() {
        return autor;
    }
    public String getText() {
        return text;
    }
    public LinkedList<String> getLikes() {
        return likes;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Post post = (Post) obj;
        return this.id.equals(post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
