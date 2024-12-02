package org.example;
public class Dijkstra extends Astar {

    public Dijkstra() {
        super();
    }

    // Override heuristic method
    @Override
    public Double heuristic(Node n1, Node n2) {
        return 0.0;
    }

}
