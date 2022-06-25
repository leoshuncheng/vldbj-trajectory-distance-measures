package team.dig.vtdm.entities;

import java.util.ArrayList;

/**
 * @author uqdalves
 */
public class Trajectory {
	// the list of Points that compose the trajectory
	private ArrayList<Point> pointsList = 
			new ArrayList<Point>();
	
	/**
	 * The list of Points of this trajectory.
	 */
	public ArrayList<Point> getPointsList() {
		return pointsList;
	}
	public void setPointsList(ArrayList<Point> pointsList) {
		this.pointsList = pointsList;
	}
	
	/**
	 *  Add a Point to the trajectory (end). 
	 */
	public void addPoint(Point point){
		pointsList.add(point);
	}
	
	/**
	 * The number of points in this trajectory.
	 */
	public int numberOfPoints(){
		return pointsList.size();
	}
	
    /**
     * Makes an identical copy of this element
     */
    @Override
    public Trajectory clone() {
		Trajectory t_clone = new Trajectory();
		
		for(Point p : pointsList){
			Point new_p = p.clone();
			t_clone.addPoint(new_p);
		}

		return t_clone;
    }
}
