package team.dig.vtdm.transformation;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;

/**
 * Shifts the time period of a trajectory, make it starts from time t = startTime
 * 
 * @author uqdalves
 */
public class TimeShiftTransformation implements TransformationInterface {
	long startTime = 0;
	
	public TimeShiftTransformation(){}
	public TimeShiftTransformation(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * Returns a copy of the trajectory with time period shifted to 
	 * start at t = zero
	 * ?
	 */
	@Override
	public ArrayList<Point> getTransformation(ArrayList<Point> list) {
		ArrayList<Point> new_traj = new ArrayList<Point>();
		long time_t1 = list.get(0).timeStamp;
		for(int i = 0; i < list.size(); i++){
			// Makes a copy of the Point
			Point new_p = list.get(i).clone();
			// Shifts the time and set to the new list
			new_p.timeStamp = list.get(i).timeStamp - time_t1 + startTime;

//			System.out.println();
			new_traj.add(new_p);
		}
		return new_traj;
	}

	@Override
	public ArrayList<Point> getTransformation(ArrayList<Point> list,
			ArrayList<Point> escapeList) {
		// TODO Auto-generated method stub
		return null;
	}	
}
