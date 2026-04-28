package org.group.analysis.structure;

import org.group.analysis.model.Post;

import java.util.HashMap;

public class RevertedIndex {

    private HashMap<String, LinkedList<Post>> index;

    public RevertedIndex() {
        this.index = new HashMap<>();
    }

    public void addIndex(String word, Post post) {
        index.putIfAbsent(word, new LinkedList<Post>());
        index.get(word).add(post);
    }

    public LinkedList<Post> getPosts(String word) {
        return index.get(word);
    }

}