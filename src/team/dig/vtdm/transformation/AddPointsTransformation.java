    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team.dig.vtdm.transformation;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;
/**
 *
 * @author uqhsu1
 */
public class AddPointsTransformation implements TransformationInterface{
    public double addRate = 0.25;
    
    public AddPointsTransformation(){}
    
    public AddPointsTransformation(double AddPointsRate){
        addRate=AddPointsRate;
    }
    
    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list){
        ArrayList<Point> result = new ArrayList<Point>();
        
        int addPointCount = (int)(list.size()*addRate);
        
        if(list.size() < 2){
            return list;
        }
        if(addPointCount < 1){
            addPointCount = 1;
        }
        if(addPointCount >= list.size()){
            addPointCount = list.size()-1;
        }
        
        int[] valueList = topN(list.size() - 1, addPointCount);//随机生成三个点的index 并按照从小到大排列
        
        for(int i = 0; i < list.size(); i++){
            result.add((Point)list.get(i));
            for(int j = 0;j < valueList.length; j++){
                if(valueList[j] == i){
                    Point temp = getMidPoint((Point)list.get(i),(Point)list.get(i+1));
                    result.add(temp);
                }
            }
        }
        
        return result;
    }
    
    public ArrayList<Point> getTransformation(ArrayList<Point> list,ArrayList<Point> escapeList){
        return null;
    }
    
    private Point getMidPoint(Point p, Point q){
        
        double[] pCood = p.coordinate;
        double[] qCood = q.coordinate;
        
        double[] cood = new double[p.dimension];
        
        for(int i = 0;i < cood.length; i++){
            cood[i] = (pCood[i] + qCood[i]) / 2;
        }
        
        long t1 = p.timeStamp;
        long t2 = q.timeStamp;
        
        if(t1 > t2){
            long temp = t1;
            t1 = t2;
            t2 = temp;
        }
        
        long timeLong = t1 + (t2 - t1) / 2;
        
        Point result = new Point(cood,timeLong);
        
        return result;
    }
    
    
    
    private int[] topN(int allSize,int N){
        int[] result=new int[N];
        
        double[] valueList=new double[allSize];
        for(int i=0;i<valueList.length;i++){
            valueList[i]=Math.random();
        }
        int[] allSizeList=sort(valueList);
        
        for(int i=0;i<N;i++){
            result[i]=allSizeList[i];
        }
        
        for(int i=0;i<N;i++){
            int min=result[i];
            int minIndex=i;
            for(int j=i+1;j<N;j++){
                if(min>result[j]){
                    min=result[j];
                    minIndex=j;
                }
            }
            int temp=result[i];
            result[i]=min;
            result[minIndex]=temp;
        }
        
        return result;
    }
    
    
    private int[] sort(double[] list){ //从大到小排序，输出索引
        int[] result=new int[list.length];
        boolean[] mark=new boolean[list.length];
        
        for(int i=0;i<mark.length;i++){
            mark[i]=true;
            result[i]=-1;
        }
        int count=0;
        for(int i=0;i<list.length;i++){
            double max=-1;
            int index=-1;
            for(int j=0;j<list.length;j++){
                if(mark[j]){
                    if(max==-1){
                        max=list[j];
                        index=j;
                    }else if(max<list[j]){
                        max=list[j];
                        index=j;
                    }
                }
            }
            mark[index]=false;
            result[count]=index;
            count++;
        }
        return result;
    }
}
