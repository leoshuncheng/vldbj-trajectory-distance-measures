/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team.dig.vtdm.entities;
import java.util.ArrayList;
/**
 *
 * @author uqhsu1
 */
public class Polygon {
    
    ArrayList<Point> polygon;
    
    public Polygon(){
        polygon = new ArrayList<Point>();
    }
    
    public Polygon(ArrayList<Point> p){
        polygon = p;
    }

    /**
     * @return double Polygon area
     */
    public double getArea(){
        if(polygon.size() < 3){
            return 0;
        }
        
        double result = 0;
        for(int i = 1;i < polygon.size() - 1; i++){
            result += getArea(polygon.get(0), polygon.get(i), polygon.get(i + 1));
        }
        
        return result;
    }

    /**
     * @param x
     * @param y
     * @param z
     * @return double Triangle area
     */
    private double getArea(Point x, Point y, Point z){ //三角形面积
        double result = Math.abs(x.coordinate[0]*y.coordinate[1]
                + x.coordinate[1]*z.coordinate[0]
                + y.coordinate[0]*z.coordinate[1]
                - z.coordinate[0]*y.coordinate[1]
                - x.coordinate[0]*z.coordinate[1]
                - x.coordinate[1]*y.coordinate[0]);
        return result / 2;
    }
}
