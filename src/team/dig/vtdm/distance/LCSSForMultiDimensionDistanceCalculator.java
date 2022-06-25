package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.services.DistanceService;

public class LCSSForMultiDimensionDistanceCalculator implements SequenceDistanceCalculator{

	double Theta=0;
	double Ebosilon=0;
	
	public LCSSForMultiDimensionDistanceCalculator(double t, double e){
		Theta=t;
		Ebosilon=e;
	}
	
	@Override
	public double getDistance(ArrayList<Point> r, ArrayList<Point> s){
		 // make sure the original objects will not be changed
		ArrayList<Point> r_clone = DistanceService.clonePointsList(r);
		ArrayList<Point> s_clone = DistanceService.clonePointsList(s);
		
		return getLCSS(r_clone, s_clone); 
	}
	
	private double getLCSS(ArrayList<Point> r, ArrayList<Point> s){

		double[][] LCSSForMultiDimensionMetric = new double[r.size() + 1][s.size() + 1];
		
		for (int i = 0; i <= r.size(); i++){
			LCSSForMultiDimensionMetric[i][0] = 0;
		}
		for (int i = 0; i <= s.size(); i++){
			LCSSForMultiDimensionMetric[0][i] = 0;
		}
		
		
		LCSSForMultiDimensionMetric[0][0] = 0;
		
		
		for (int i = 1; i <= r.size(); i++){
			for (int j = 1; j <= s.size(); j++){
				if (subcost(r.get(i - 1), s.get(j - 1),i,j) == 0){
					LCSSForMultiDimensionMetric[i][j] = LCSSForMultiDimensionMetric[i - 1][j - 1] + 1;
				}else{
					LCSSForMultiDimensionMetric[i][j] = max(LCSSForMultiDimensionMetric[i][j - 1], LCSSForMultiDimensionMetric[i - 1][j]);
				}
				
			}
		}		
		
		return LCSSForMultiDimensionMetric[r.size()][s.size()];
	}
	
	private double max(double a, double b){
		if (a >= b){
			return a;
		}else{
			return b;
		}
	}
	
	private int subcost(Point p1, Point p2,int i, int j){
		if(Math.abs(i-j)>Theta){
			return 1;
		}
		boolean isSame=true;
		for(int index=0;index<p1.dimension;index++){
			if(Math.abs(p1.coordinate[index]-p2.coordinate[index])>Ebosilon){
				isSame=false;
				return 1;
			}
		}
				
		return 0;
	}
	
}
