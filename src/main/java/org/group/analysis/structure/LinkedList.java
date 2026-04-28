package org.group.analysis.structure;

public class LinkedList<T> {

    private Node<T> head;

    public LinkedList() {
        this.head = null;
    }

    public void add(T data) {
        if (head == null) {
            head = new Node<T>(data);
            return;
        }

        Node<T> actual = head;

        while (actual.getNext() != null) {
            if (actual.getData().equals(data)) {
                return;
            }
            actual = actual.getNext();
        }

        actual.setNext(new Node<T>(data));
    }
}
