package org.group.analysis.structure.hash;

public class EntradaHash {
    private String termino;
    private int contador;

    public EntradaHash(String termino, int contador) {
        this.termino = termino;
        this.contador = contador;
    }

    public String getTermino() {
        return termino;
    }

    public int getContador() {
        return contador;
    }

    public void incrementar() {
        this.contador++;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EntradaHash other = (EntradaHash) obj;
        return this.termino.equals(other.termino);
    }

    @Override
    public int hashCode() {
        return this.termino.hashCode();
    }
}
