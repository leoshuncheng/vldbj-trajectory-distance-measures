package team.dig.vtdm.distance;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;

public class SigmoidDistanceCalculator implements SequenceDistanceCalculator {
	private double a;
	private int k;
	private int delta; // the temporal threshold
	
	public SigmoidDistanceCalculator(double A, int K, int Delta) {
		a = A;
		k = K;
		delta = Delta;
	}

	@Override
	public double getDistance(ArrayList<Point> r, ArrayList<Point> s) {
		double[] std = StandardDeviation(r);
		double[] sstd = StandardDeviation(s);
		for (int i = 0; i < std.length; ++i) {
			std[i] = Math.min(std[i], sstd[i]);
		}
		
		double[][] LCSSMetric = new double[r.size() + 1][s.size() + 1];
		
		for (int i = 0; i <= r.size(); i++){
			LCSSMetric[i][0] = 0;
		}
		for (int i = 0; i <= s.size(); i++){
			LCSSMetric[0][i] = 0;
		}
		
		for (int i = 1; i <= r.size(); i++){
			for (int j = 1; j <= s.size(); j++){
				if (Math.abs(i - j) < delta) {
					LCSSMetric[i][j] = SigmoidMatch(r.get(i - 1), s.get(j - 1), std)
							+ Math.max(LCSSMetric[i-1][j-1], 
									Math.max(LCSSMetric[i-1][j], LCSSMetric[i][j-1]));
				} else {
					LCSSMetric[i][j] = 0;
				}
			}
		}		
		
		return LCSSMetric[r.size()][s.size()];
	}
	
	private double SigmoidMatch(Point a, Point b, double[] std) {
		double ret = 0;
		
		for (int i = 0; i < a.dimension; ++i) {
			double l1 = Math.abs(a.coordinate[i] - b.coordinate[i]);
			if (l1 > std[i]) {
				return 0;
			} else {
				ret += Sigmoid(l1/std[i]*(2*k+1));
			}
		}
		
		return ret/2;
	}
	
	private double Sigmoid(double x) {
		return 1/(1+Math.exp(-a*(x-k-1)));
	}
	
	private double[] StandardDeviation(ArrayList<Point> r) {
		int dim = r.get(0).dimension;
		double[] sum = new double[dim];
		double[] squaresum = new double[dim];
		
		for (Point p : r) {
			for (int i = 0; i < dim; ++i) {
				sum[i] += p.coordinate[i];
				squaresum[i] += p.coordinate[i] * p.coordinate[i];
			}
		}
		
		for (int i = 0; i < dim; ++i) {
			squaresum[i] -= sum[i]*sum[i];
		}
		
		return squaresum;
	}
}
