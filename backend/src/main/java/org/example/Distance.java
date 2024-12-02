package org.example;

public class Distance {
    public static Double distance(Node node1, Node node2){

        double lat1=node1.getCoordinates()[0];
        double lat2=node2.getCoordinates()[0];
        double lon1=node1.getCoordinates()[1];
        double lon2=node2.getCoordinates()[1];

        final double R = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
