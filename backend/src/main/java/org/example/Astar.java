package org.example;
import java.util.*;
import static org.example.Graph.graph;

public class Astar {

    public Astar() {

    }




      public List<Node> path(Node source, Node end) {


        // Initialize distance map, previous node map, and priority queue
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>((n1, n2) -> {
            double f1 = distances.get(n1) + heuristic(n1, end);
            double f2 = distances.get(n2) + heuristic(n2, end);
            return Double.compare(f1, f2);
        });



        // Initialize distances to infinity and previous nodes to null
        for (Node node : graph.keySet()) {
            distances.put(node, Double.POSITIVE_INFINITY);
            previous.put(node, null);
        }

        distances.put(source, 0.0);
        pq.offer(source);

        // Main loop
        while (!pq.isEmpty()) {

            Node currNode = pq.poll();
            double currDistance = distances.get(currNode);

            // If the current node is the end node, reconstruct and return the shortest path
            if (currNode.equals(end)) {
                return reconstructPath(previous, end);
            }

            // Iterate through neighbors of current node
            for (Map.Entry<Node, Double> neighborEntry : graph.get(currNode).entrySet()) {
                Node neighbor = neighborEntry.getKey();
                double edgeWeight = neighborEntry.getValue();
                double newDistance = currDistance + edgeWeight;

                // If new distance is shorter, update distance and previous node
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, currNode);
                    pq.offer(neighbor);
                }
            }
        }

        return new ArrayList<>();
    }


    private static List<Node> reconstructPath(Map<Node, Node> previous, Node end) {
        List<Node> path = new ArrayList<>();
        Node currNode = end;
        // Reconstruct path by following previous nodes from end to start
        while (currNode != null) {
            path.add(currNode);
            currNode = previous.get(currNode);
        }
        // Reverse the path to get it from start to end
        Collections.reverse(path);
        return path;
    }


    public Double heuristic(Node n1, Node n2) {
        return Distance.distance(n1, n2);

    }


}

