package org.group.analysis.model;

import org.group.analysis.structure.LinkedList;

public class User {

    private Long id;
    private String username;
    private String biography;
    private int followers;
    private int following;
    private LinkedList<String> friends;
    private LinkedList<Post> posts;

    public User(Long id, String username, String biography, int followers, int following) {
        this.id = id;
        this.username = username;
        this.biography = biography;
        this.followers = followers;
        this.following = following;
        this.friends = new LinkedList<>();
        this.posts = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public LinkedList<String> getFriends() {
        return friends;
    }

    public void setFriends(LinkedList<String> friends) {
        this.friends = friends;
    }

    public LinkedList<Post> getPosts() {
        return posts;
    }

    public void setPosts(LinkedList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return this.username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
