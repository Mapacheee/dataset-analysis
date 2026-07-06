package org.group.analysis.structure.hash;

import org.group.analysis.structure.ListaEnlazada;
import org.group.analysis.structure.Nodo;

public class TablaHash {
    private ListaEnlazada<EntradaHash>[] tabla;
    private int tamanoM;
    private int cantTerminosN;

    public TablaHash(int sizeM) {
        this.tamanoM = sizeM;
        this.tabla = new ListaEnlazada[sizeM];
        for (int i = 0; i < sizeM; i++) {
            this.tabla[i] = new ListaEnlazada<>();
        }
        this.cantTerminosN = 0;
    }

    /**
     * hash(0) = 5381
     * hash(i) = hash(i-1) * 33 + c[i]
     */
    private int obtenerIndice(String termino) {
        long hash = 5381;
        for (int i = 0; i < termino.length(); i++) {
            hash = ((hash << 5) + hash) + termino.charAt(i);
        }
        int idx = (int) (hash % tamanoM);
        if (idx < 0) {
            idx += tamanoM;
        }
        return idx;
    }

    public void insertarOIncrementar(String termino) {
        int idx = obtenerIndice(termino);
        ListaEnlazada<EntradaHash> lista = tabla[idx];

        Nodo<EntradaHash> actual = lista.getCabeza();
        while (actual != null) {
            if (actual.getDato().getTermino().equals(termino)) {
                actual.getDato().incrementar();
                return;
            }
            actual = actual.getSiguiente();
        }

        lista.agregar(new EntradaHash(termino, 1));
        cantTerminosN++;
    }

    public int getCantTerminosN() {
        return cantTerminosN;
    }

    public int getTamanoM() {
        return tamanoM;
    }

    /**
     * retorna el número total de colisiones
     * si una celda tiene K elementos, aporta K-1 colisiones
     */
    public int getTotalColisiones() {
        int colisiones = 0;
        for (int i = 0; i < tamanoM; i++) {
            int tam = tabla[i].tamano();
            if (tam > 1) {
                colisiones += (tam - 1);
            }
        }
        return colisiones;
    }

    /**
     * retorna el largo máximo de una cadena de colisiones
     */
    public int getLargoMaximoCadena() {
        int max = 0;
        for (int i = 0; i < tamanoM; i++) {
            int tam = tabla[i].tamano();
            if (tam > max) {
                max = tam;
            }
        }
        return max;
    }

    /**
     * retorna el largo promedio de las cadenas de colisiones ocupadas
     */
    public double getLargoPromedioCadena() {
        int totalElementos = 0;
        int celdasOcupadas = 0;
        for (int i = 0; i < tamanoM; i++) {
            int tam = tabla[i].tamano();
            if (tam > 0) {
                totalElementos += tam;
                celdasOcupadas++;
            }
        }
        return celdasOcupadas == 0 ? 0.0 : (double) totalElementos / celdasOcupadas;
    }

    /**
     * recupera todas las entradas de la tabla hash
     */
    public ListaEnlazada<EntradaHash> obtenerTodasLasEntradas() {
        ListaEnlazada<EntradaHash> todas = new ListaEnlazada<>();
        for (int i = 0; i < tamanoM; i++) {
            Nodo<EntradaHash> actual = tabla[i].getCabeza();
            while (actual != null) {
                todas.agregar(actual.getDato());
                actual = actual.getSiguiente();
            }
        }
        return todas;
    }

    /**
     * retorna los N terminos más frecuentes
     */
    public EntradaHash[] obtenerTopN(int n) {
        ListaEnlazada<EntradaHash> todas = obtenerTodasLasEntradas();
        int total = todas.tamano();
        EntradaHash[] arreglo = new EntradaHash[total];

        Nodo<EntradaHash> actual = todas.getCabeza();
        int idx = 0;
        while (actual != null) {
            arreglo[idx++] = actual.getDato();
            actual = actual.getSiguiente();
        }

        // ordenamiento Insertion Sort descendente
        for (int i = 1; i < total; i++) {
            EntradaHash clave = arreglo[i];
            int j = i - 1;
            while (j >= 0 && arreglo[j].getContador() < clave.getContador()) {
                arreglo[j + 1] = arreglo[j];
                j--;
            }
            arreglo[j + 1] = clave;
        }

        int limite = Math.min(n, total);
        EntradaHash[] resultado = new EntradaHash[limite];
        System.arraycopy(arreglo, 0, resultado, 0, limite);
        return resultado;
    }

    /**
     * verifica si es primo
     */
    public static boolean esPrimo(int num) {
        if (num <= 1) return false;
        if (num <= 3) return true;
        if (num % 2 == 0 || num % 3 == 0) return false;
        for (int i = 5; i * i <= num; i += 6) {
            if (num % i == 0 || num % (i + 2) == 0) return false;
        }
        return true;
    }

    /**
     * retorna el siguiente número primo >= min
     */
    public static int siguientePrimo(int min) {
        int candidato = min;
        if (candidato % 2 == 0) {
            candidato++;
        }
        while (!esPrimo(candidato)) {
            candidato += 2;
        }
        return candidato;
    }
}
