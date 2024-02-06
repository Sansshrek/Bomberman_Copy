package main;

import java.util.Objects;

public class Node implements Comparable<Node> {
    private int row;
    private int col;
    private int g;  // Costo effettivo dal punto di partenza
    private int h;  // Stima del costo al punto finale
    private Node parent;  // Nodo genitore nel percorso

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
        this.g = 0;
        this.h = 0;
        this.parent = null;
    }

    // Getter e Setter per i campi

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    // Override del metodo compareTo per l'utilizzo della PriorityQueue
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.g + this.h, other.g + other.h);
    }

    // Override del metodo equals per la comparazione di nodi
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return row == node.row && col == node.col;
    }

    // Override del metodo hashCode per la comparazione di nodi
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}