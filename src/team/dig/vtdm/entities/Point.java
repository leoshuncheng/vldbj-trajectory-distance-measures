package team.dig.vtdm.entities;

import java.util.Date;

public class Point implements Cloneable {

	public Date time;
	public double[] coordinate;
	public int dimension;
	public String originalTimeString = ""; // Date to string
    public long timeStamp;

	public Point(){}

    /**
     * @param x coordinate
     * @param t time
     */
    public Point(double[] x, long t){
        coordinate = new double[x.length];
        for(int i = 0;i < x.length; i++){
            coordinate[i] = x[i];
        }
        dimension = x.length;
        timeStamp = t;
        time = new Date(timeStamp);
        originalTimeString = time.toString();
    }

    /**
     * @param x coordinate
     */
	public Point(double[] x) {
        coordinate = new double[x.length];
        for(int i = 0; i < x.length; i++){
            coordinate[i] = x[i];
        }
		dimension = x.length;
	}

    /**
     * @param d Point dimension
     */
	public void setDimension(int d){
		dimension = d;
	}

    /**
     * @param p Point
     * @return boolean whether the points are same
     */
    public boolean isSame(Point p){
        if(p.dimension != this.dimension){
            return false;
        }

        for(int i = 0; i < p.dimension; i++){
            if(this.coordinate[i] != p.coordinate[i]){
                return false;
            }
        }

        if(this.originalTimeString.compareTo(p.originalTimeString) != 0){
            return false;
        }
        return true;
    }
        
    @Override
    public String toString(){
        return coordinate[0] + "-" + coordinate[1];
    }

    /**
     * @param another
     * @return double distance to another point
     */
    public double distanceTo(Point another){
        double distance = 0.0;
        for (int count = 0, size = coordinate.length; count < size; ++count){
            distance += Math.pow(coordinate[count] - another.coordinate[count],
                    2);
        }
        return Math.sqrt(distance);
    }
        
    /**
     * Makes an identical copy of this element
     */
    @Override
    public Point clone() {
        Point p_clone = new Point(coordinate, timeStamp);
        return p_clone;
    }
}
