package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.List;

public class AnyLocation {


    public static List<Node> AnyLocationAstar(double startlat, double startlon, double endlat, double endlon) throws IOException {

        //instance of the graph
        Graph graph = Graph.graphing();

        Node startnode = graph.nextNode(startlat, startlon);
        Node endnode = graph.nextNode(endlat, endlon);
        List<Node> path = new Astar().path(startnode, endnode);
        return path;

    }

    public static List<Node> AnyLocationDijkstra(double startlat, double startlon, double endlat, double endlon) throws IOException {
        Graph graph = Graph.graphing();

        Node startnode = graph.nextNode(startlat, startlon);
        Node endnode = graph.nextNode(endlat, endlon);
        List<Node> path = new Dijkstra().path(startnode, endnode);
        return path;
    }


    public static String Geojson(List<Node> path) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode geoJson = objectMapper.createObjectNode();


        ObjectNode featureCollection = objectMapper.createObjectNode();
        featureCollection.put("type", "FeatureCollection");


        ObjectNode feature = objectMapper.createObjectNode();
        feature.put("type", "Feature");


        ObjectNode properties = objectMapper.createObjectNode();
        feature.set("properties", properties);


        ObjectNode geometry = objectMapper.createObjectNode();
        geometry.put("type", "LineString");


        ArrayNode coordinates = objectMapper.createArrayNode();
        for (Node node : path) {
            if (node != null) {
                ArrayNode point = objectMapper.createArrayNode();
                point.add(node.getCoordinates()[1]);
                point.add(node.getCoordinates()[0]);
                coordinates.add(point);
            }
        }

        geometry.set("coordinates", coordinates);
        feature.set("geometry", geometry);

        featureCollection.set("features", objectMapper.createArrayNode().add(feature));
        geoJson.setAll(featureCollection);

        try {
            return objectMapper.writeValueAsString(geoJson);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }

    }

    public static String shortest_path_dij(double startlat, double startlon, double endlat, double endlon) throws IOException {
        List<Node> path = AnyLocationDijkstra(startlat, startlon, endlat, endlon);
        return Geojson(path);

    }


    public static String shortest_path_astar(double startlat, double startlon, double endlat, double endlon) throws IOException {
        List<Node> path = AnyLocationAstar(startlat, startlon, endlat, endlon);
        return Geojson(path);

    }

}
