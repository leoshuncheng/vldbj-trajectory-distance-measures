package team.dig.vtdm.entities;

import java.util.ArrayList;


public class Distance {
    private ArrayList<Double> distances =
            new ArrayList<Double>();
    public String fileName = "";

    public Distance() {}
    public Distance(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<Double> getDistancesList() {
        return distances;
    }
    public void setDistancesList(ArrayList<Double> distances) {
        this.distances = distances;
    }
    /**
     * Add a distance to the list.
     * Append it to the end of the list.
     */
    public void addDistance(double distance){
        distances.add(distance);
    }

}
