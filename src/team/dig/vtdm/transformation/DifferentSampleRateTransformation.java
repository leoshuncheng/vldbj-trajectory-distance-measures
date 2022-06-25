package team.dig.vtdm.transformation;

import java.util.ArrayList;

import team.dig.vtdm.entities.Point;

/**
 * Re-sample the trajectory.
 * Select every sampleRate point from the list.
 *
 * @author uqdalves
 */
public class DifferentSampleRateTransformation implements TransformationInterface {
    public int sampleRate = 0; // every sampleRate points

    public DifferentSampleRateTransformation() {
    }

    public DifferentSampleRateTransformation(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list) {
        return getDiffSampleRateTransf(list);
    }

    /**
     * Re-sample the trajectory.
     */
    private ArrayList<Point> getDiffSampleRateTransf(ArrayList<Point> list) {
        ArrayList<Point> new_sample = new ArrayList<>();

        // get every sampleRate points
        for (int i = 0; i < list.size(); i += sampleRate) {
            new_sample.add(list.get(i));
        }
        return new_sample;
    }

    @Override
    public ArrayList<Point> getTransformation(ArrayList<Point> list,
                                              ArrayList<Point> escapeList) {
        return null;
    }


}
