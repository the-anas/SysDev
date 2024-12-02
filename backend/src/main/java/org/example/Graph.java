package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Graph {
    static Set<Node> nodes = new HashSet<>(); //list of nodes, used in the nextNode function
    static HashMap<Node, HashMap<Node, Double>> graph= new HashMap<>();

    //constructor used to make a graph instance using the graph hashmap, used at the end of garphing method
    public Graph(HashMap<Node, HashMap<Node, Double>> graph){
        this.graph=graph;
    }


    public static Graph graphing() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Jsonparser json = mapper.readValue(new File("C:\\Users\\anasn\\OneDrive\\Desktop\\sysdev\\dependencies\\schleswig-holstein.json"), Jsonparser.class);

        for (Feature feature : json.features) {
            if (feature.geometry.type.equals("LineString")) {
                for (Double[] coord: feature.geometry.coordinates) {
                    Node current = new Node(coord[1], coord[0]);

                    // if node is not in the set of nodes, add it to it
                    if (!nodes.contains(current)){
                        nodes.add(current);
                        graph.put(current, new HashMap<>());
                        HashMap<Node, Double> inner = new HashMap<>();
                    }

                    //loop through every node in the linestring
                    for (Double[] coord1: feature.geometry.coordinates){
                        if (new Node(coord1[1], coord1[0]).equals(current)){
                            continue;
                        }
                        Node innerNode = new Node(coord1[1], coord1[0]);
                        HashMap<Node, Double> inner = graph.get(current);
                        inner.computeIfAbsent(innerNode, k -> Distance.distance(current, k));
                    }
                }
            }
        }
        return new Graph(graph);
    }

    public Node nextNode(double lat, double lon ){
        Node chosen = new Node(lat, lon);
        double shortest_dist = Double.POSITIVE_INFINITY;
        Node closest = null;

        for (Node currnode: this.nodes){

            if (Distance.distance(currnode, chosen)< shortest_dist){
                shortest_dist = Distance.distance(currnode, chosen);
                closest = currnode;
            }
        }
        return closest;
    }
}