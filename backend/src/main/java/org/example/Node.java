package org.example;

import java.util.Objects;

public class Node {

    private Double Lat;
    private Double Lon;

    public Node(Double Lat, Double Lon) {
        this.Lat = Lat;
        this.Lon = Lon;
    }

    public Double[] getCoordinates() {
        return new Double[]{this.Lat, this.Lon};
    }

    public void setCoordinates(Double Lat, Double Lon) {
        this.Lat = Lat;
        this.Lon = Lon;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node){
            if (Objects.equals(this.Lat, ((Node) obj).Lat) && (Objects.equals(this.Lon, ((Node) obj).Lon))){
                return true; // Same object
            }

        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Lat, Lon);
    }

    @Override
    public String toString() {
        return "[" + Lat + ", " + Lon +"]";
    }

}