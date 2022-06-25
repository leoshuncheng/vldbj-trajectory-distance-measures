package team.dig.vtdm.services;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;
import team.dig.vtdm.entities.Trajectory;
import team.dig.vtdm.transformation.AddNoiseTransformation;
import team.dig.vtdm.transformation.AddPointsTransformation;
import team.dig.vtdm.transformation.DeletePointTransformation;
import team.dig.vtdm.transformation.DifferentSampleRateTransformation;
import team.dig.vtdm.transformation.RamerDouglasPeuchkerAlgorithm;
import team.dig.vtdm.transformation.WholeTrajectoryRotationTransformation;
import team.dig.vtdm.transformation.WholeTrajectoryScaleTransformation;
import team.dig.vtdm.transformation.WholeTrajectoryTranslationTransformation;

/**
 * Service (proxy) to put together all transformation functions.
 * 
 * @author uqdalves
 *
 */
public class TransformationService {

	// Add points transformation
	public ArrayList<Trajectory> addPointsTransformation(ArrayList<Trajectory> list, double addRate){
		ArrayList<Trajectory> newTrajectoryList = new ArrayList<>();
		AddPointsTransformation addPtsTransf = 
				new AddPointsTransformation(addRate); 
		
		System.out.println("Add Points Transformation: " + (addRate*100) + "pct");
		
		for(Trajectory traj : list){
			Trajectory newTraj = new Trajectory();
			newTraj.setPointsList(addPtsTransf.getTransformation(traj.getPointsList()));
			newTrajectoryList.add(newTraj);
		}
		System.gc();
		
		return newTrajectoryList;
	}
	
	// Delete points transformation
	public ArrayList<Trajectory> deletePointsTransformation(ArrayList<Trajectory> list, double deleteRate){
		ArrayList<Trajectory> newTrajectoryList = new ArrayList<>();
		DeletePointTransformation delPtsTransf = 
				new DeletePointTransformation(deleteRate); 
		
		System.out.println("Delete Points Transformation: " + (deleteRate*100) + "pct");
		for(Trajectory traj : list){
			// Get the scape points list (from DP heuristic)
			RamerDouglasPeuchkerAlgorithm dp = new RamerDouglasPeuchkerAlgorithm();
			ArrayList<Point> scapeList = dp.getKeyPointArrayList(traj.getPointsList());//¹Ç¼Ü½Úµã¼ÆËã

			Trajectory newTraj = new Trajectory();
			newTraj.setPointsList(delPtsTransf.getTransformation(traj.getPointsList(), scapeList));
			newTrajectoryList.add(newTraj);
		} 
		System.gc();
		
		return newTrajectoryList;
	}
	
	// Different sample rate transformation
	public ArrayList<Trajectory> diffSampleRateTransformation(ArrayList<Trajectory> list, int sampleRate){
		ArrayList<Trajectory> newTrajectoryList = new ArrayList<>();
		DifferentSampleRateTransformation difSampRateTransf = 
				new DifferentSampleRateTransformation(sampleRate);
		
		System.out.println("Dif. Sample Rate Transformation: " + sampleRate + "pts");
		
		for(Trajectory traj : list){
			Trajectory newTraj = new Trajectory();
			newTraj.setPointsList(difSampRateTransf.getTransformation(traj.getPointsList()));
			newTrajectoryList.add(newTraj);
		}
		System.gc();
		
		return newTrajectoryList;
	}
	
	// Whole trajectory time scale transformation
	public ArrayList<Trajectory> timeScaleTransformation(ArrayList<Trajectory> list, double timeRatio){
		ArrayList<Trajectory> newTrajectoryList = new ArrayList<>();
		WholeTrajectoryScaleTransformation timeScaleTransf = 
				new WholeTrajectoryScaleTransformation(timeRatio); 
		
		System.out.println("Time Scale Transformation: " + (timeRatio*100) + "pct");
		
		for(Trajectory traj : list){
			Trajectory newTraj = new Trajectory();
			newTraj.setPointsList(timeScaleTransf.getTransformation(traj.getPointsList()));
			newTrajectoryList.add(newTraj);
		}
		System.gc();
		
		return newTrajectoryList;
	}
	
	// Whole trajectory rotation transformation
	public ArrayList<Trajectory> rotationTransformation(ArrayList<Trajectory> list, double angle){
		ArrayList<Trajectory> newTrajectoryList = new ArrayList<>();
		WholeTrajectoryRotationTransformation rotationTransf = 
				new WholeTrajectoryRotationTransformation(angle); 
		
		System.out.println("Rotation Transformation: " + Math.toDegrees(angle) + "dgr");
		
		for(Trajectory traj : list){
			Trajectory newTraj = new Trajectory();
			newTraj.setPointsList(rotationTransf.getTransformation(traj.getPointsList()));
			newTrajectoryList.add(newTraj);
		}
		System.gc();
		
		return newTrajectoryList;
	}
	
	// Whole trajectory scale transformation
	public ArrayList<Trajectory> scaleTransformation(ArrayList<Trajectory> list, double scaleRatio){
		ArrayList<Trajectory> newTrajectoryList = new ArrayList<>();
		WholeTrajectoryTranslationTransformation scaleTransf = 
				new WholeTrajectoryTranslationTransformation(scaleRatio);
		
		System.out.println("Whole Scale Transformation: " + (scaleRatio*100) + "pct");
		
		for(Trajectory traj : list){
			Trajectory newTraj = new Trajectory();
			newTraj.setPointsList(scaleTransf.getTransformation(traj.getPointsList()));
			newTrajectoryList.add(newTraj);
		}
		System.gc();
		
		return newTrajectoryList;
	}
	
	// Add noise transformation
    public ArrayList<Trajectory> addNoiseTransformation(ArrayList<Trajectory> list, double addRate, double noiseDist){
    	ArrayList<Trajectory> newTrajectoryList = new ArrayList<>();
  		AddNoiseTransformation noiseTransf = 
  				new AddNoiseTransformation(addRate, noiseDist);
  		
  		System.out.println("Add Noise Transformation: " + (addRate*100) + "pct  " + (noiseDist*100) + "d");
		
  		for(Trajectory traj : list){
			Trajectory newTraj = new Trajectory();
			newTraj.setPointsList(noiseTransf.getTransformation(traj.getPointsList()));
			newTrajectoryList.add(newTraj);
		}
  		System.gc();
  		
		return newTrajectoryList;
    }

}
