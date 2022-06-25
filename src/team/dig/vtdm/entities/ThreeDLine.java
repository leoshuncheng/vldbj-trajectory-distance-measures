/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team.dig.vtdm.entities;

/**
 *
 * @author Administrator
 */
public class ThreeDLine {
    Point startPoint;
    Point endPoint;
    boolean isX = false;
    boolean isY = false;
    boolean isZ = false;
    double x = 0;
    double y = 0;
    double z = 0;
    
    public ThreeDLine(){
        startPoint = new Point();
        endPoint = new Point();
    }

    /**
     *
     * @param start
     * @param end
     */
    public ThreeDLine(Point start, Point end){
        startPoint = start;
        endPoint = end;
        
        assert(startPoint.dimension == 2);
        assert(endPoint.dimension == 2);
        assert(startPoint.time != null);
        assert(endPoint.time != null);
        
        if(startPoint.coordinate[0] == endPoint.coordinate[0]){
            isX = true;
            x = startPoint.coordinate[0];
        }
        
        if(startPoint.coordinate[1] == endPoint.coordinate[1]){
            isY = true;
            x = startPoint.coordinate[1];
        }
        
        assert(startPoint.timeStamp != endPoint.timeStamp);
    }
    
    public Point getPointByTime(long t){
        if(isX && isY){
            double[] temp = new double[]{x,y};
            return new Point(temp,t);
        }else if(isX){
            double aaa = (double)(t - startPoint.timeStamp);
            double bbb = (double)(endPoint.timeStamp - startPoint.timeStamp);
            
            double yy = ((aaa) * (bbb) / (endPoint.coordinate[1] - startPoint.coordinate[1])) + startPoint.coordinate[1];
            
            double[] temp = new double[]{x,yy};
            return new Point(temp, t);
            
        }else if(isY){
            double aaa = (double)(t - startPoint.timeStamp);
            double bbb = (double)(endPoint.timeStamp - startPoint.timeStamp);
            
            double xx = ((aaa) * (bbb) / (endPoint.coordinate[0] - startPoint.coordinate[0])) + startPoint.coordinate[0];
            
            double[] temp = new double[]{xx, y};
            return new Point(temp, t);
        }else {
            double aaa = (double)(t - startPoint.timeStamp);
            double bbb = (double)(endPoint.timeStamp - startPoint.timeStamp);
            
            double yy = ((aaa) * (bbb) / (endPoint.coordinate[1] - startPoint.coordinate[1])) + startPoint.coordinate[1];
            double xx = ((aaa) * (bbb) / (endPoint.coordinate[0] - startPoint.coordinate[0])) + startPoint.coordinate[0];
         
            double[] temp = new double[]{xx,yy};
            return new Point(temp, t);
        }          
        
    }
    /*
    public double getPointLineDistance(Point p){
        double result=0;
        if(isKZero){
            return Math.abs(p.coordinate[0]-x);
        }else{
            result=Math.abs(k*p.coordinate[0]+(-1)*p.coordinate[1]+b);
            result=result/(Math.sqrt(k*k+1));
            
        }
        return result;
    }
    
    public Point getIntersection(Line l){
        if(isKZero&&l.isKZero){
            return null;
        }else if(isKZero){
        double[] temp=new double[2];
        temp[0]=x;
        temp[1]=l.k*x+l.b;
        return new Point(temp);
        }else if(l.isKZero){
            double[] temp=new double[2];
        temp[0]=l.x;
        temp[1]=k*l.x+b;
        }else if(k==l.k){
            return null;
        }
        
        double[] temp=new double[2];
        temp[0]=(l.b-b)/(k-l.k);
        temp[1]=k*temp[0]+b;
        
        return new Point(temp);
    }*/
}
