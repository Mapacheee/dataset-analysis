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

        if (actual.getData().equals(data)) {
            return;
        }

        actual.setNext(new Node<T>(data));
    }

    public int size() {
        int count = 0;
        Node<T> actual = head;
        while (actual != null) {
            count++;
            actual = actual.getNext();
        }
        return count;
    }

    public boolean contains(T data) {
        Node<T> actual = head;
        while (actual != null) {
            if (actual.getData().equals(data)) {
                return true;
            }
            actual = actual.getNext();
        }
        return false;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public Node<T> getHead() {
        return head;
    }
}
