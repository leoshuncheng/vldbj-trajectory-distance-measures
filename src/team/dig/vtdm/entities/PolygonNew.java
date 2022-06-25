/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team.dig.vtdm.entities;

import java.util.ArrayList;

/**
 * @author uqhsu1
 */
public class PolygonNew {
    
    ArrayList<Point> upper;
    ArrayList<Point> lower;
    
    public PolygonNew(){
        upper = new ArrayList<Point>();
        lower = new ArrayList<Point>();
    }
    
    public PolygonNew(ArrayList<Point> ip, ArrayList<Point> low){
        upper = ip;
        lower = low;
    }
    
    public double getArea(){
        ArrayList<Line> upl = getPolyline(upper);
        ArrayList<Line> lowl = getPolyline(lower);
        
        double start = upper.get(0).coordinate[0];
        double end = upper.get(upper.size() - 1).coordinate[0];
        
        int pieces = 1000;
        
        double area = 0;
        
        for(int i = 0;i < pieces; i++){
            double x = start + (end - start) * i / pieces;
            int j = 0;
            int k = 0;
            int jj = -1;int kk = -1;
            for(; j < upl.size(); j++){
                if(upl.get(j).isInLineX(x)){
                    jj = j;
                    break;
                }
            }
            for(;k < lowl.size(); k++){
                if(lowl.get(k).isInLineX(x)){
                    kk = k;
                    break;
                }
            }
            if(jj == -1 || kk == -1){
                continue;
            }
            double upX = upl.get(jj).getYByX(x);
            double lowX = lowl.get(kk).getYByX(x);
            area += Math.abs((end - start) * (upX - lowX) / pieces);
        }
        return area;
    }

    /**
     *
     * @param r
     * @return ArrayList<Line> Convert to two line segments
     */
    private ArrayList<Line> getPolyline(ArrayList<Point> r){
        ArrayList<Line> result = new ArrayList<>();
        if(r.size() < 2){
            return result;
        }
        for(int i = 0; i < r.size() - 1; i++){
            Line tempLine = new Line(r.get(i), r.get(i + 1));
            result.add(tempLine);
        }
        return result;
    }

    public double getArea(Point x, Point y, Point z){
        double result = Math.abs(x.coordinate[0] * y.coordinate[1]
                + x.coordinate[1] * z.coordinate[0]
                + y.coordinate[0] * z.coordinate[1]
                - z.coordinate[0] * y.coordinate[1]
                - x.coordinate[0] * z.coordinate[1]
                - x.coordinate[1] * y.coordinate[0]);
        return result / 2;
    }
}
