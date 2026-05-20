package org.group.analysis.structure.contacts;

import org.group.analysis.structure.LinkedList;
import org.group.analysis.structure.Node;

public class UserContactsIndex {

    private LinkedList<ContenedorContactos> index;

    public UserContactsIndex() {
        this.index = new LinkedList<>();
    }

    public void addContact(String username, String contactName) {
        Node<ContenedorContactos> curr = index.getHead();
        ContenedorContactos entrada = null;
        while (curr != null) {
            if (curr.getData().getUsername().equals(username)) {
                entrada = curr.getData();
                break;
            }
            curr = curr.getNext();
        }

        if (entrada == null) {
            entrada = new ContenedorContactos(username);
            index.add(entrada);
        }

        entrada.getAmigos().add(contactName);
    }

    public LinkedList<String> getContacts(String username) {
        Node<ContenedorContactos> curr = index.getHead();
        while (curr != null) {
            if (curr.getData().getUsername().equals(username)) {
                return curr.getData().getAmigos();
            }
            curr = curr.getNext();
        }
        return new LinkedList<String>();
    }
}
