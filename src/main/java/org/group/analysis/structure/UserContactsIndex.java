package org.group.analysis.structure;

import java.util.HashMap;

public class UserContactsIndex {

    private HashMap<String, LinkedList<String>> index;

    public UserContactsIndex() {
        this.index = new HashMap<>();
    }

    public void addContact(String username, String contactName) {
        index.putIfAbsent(username, new LinkedList<String>());
        index.get(username).add(contactName);
    }

    public LinkedList<String> getContacts(String username) {
        LinkedList<String> list = index.get(username);
        if (list == null) {
            return new LinkedList<String>();
        }
        return list;
    }
}
