package team.dig.vtdm.distance;

import java.util.*;

import team.dig.vtdm.entities.Point;

public interface SequenceDistanceCalculator {
	public double getDistance(ArrayList<Point> r, ArrayList<Point> s);
}
