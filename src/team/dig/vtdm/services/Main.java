/**
 *
 */
package team.dig.vtdm.services;

import java.util.ArrayList;

import team.dig.vtdm.entities.Trajectory;

/**
 * @author uqdalves
 *
 */
public class Main {
    private static final double PI = Math.PI;

    // test parameters
    private static double[] parameters;

    // Get instance of the transformation functions
    private static final TransformationService transfService =
            new TransformationService();

    // Get instance of the distance functions
    private static final DistanceService distService =
            new DistanceService();

    /**
     * Main
     */
    public static void main(String[] args) {
        System.out.println("<<<STARTED>>>\n");

        // ## Read the original trajectory files and re-sample them.
        // ## Save the re-sampled trajectories to new files in the input folder.
        // ## 50 trajectories per file.
        System.out.println("1. Run reSampleTrajectoriesFromGeolife function");
        reSampleTrajectoriesFromGeolife();
        System.out.println();

        // ## Calculate the distances BEFORE transformation.
        // ## Run the distance functions and save the outcomes to the output folder.
        // ## Each file is one experiment (transformation/distance/parameter).
        // ## These are the 'ground truth' files for every distance function.
        System.out.println("2. Run calculatetopNBeforeTransformation function");
        calculatetopNBeforeTransformation(10);
        System.out.println();

        // ## Calculate the distances AFTER transformation.
        // ## Run the distance functions and save the outcomes to the output folder.
        // ## Run the distance functions after the transformation and save the outcomes.
        // ## Each file is one experiment (transformation/distance/parameter).
        System.out.println("3. Run calculatetopNAfterTransformation function");
        calculatetopNAfterTransformation(10);
        System.out.println();

        // ## Calculate the distances AFTER mix transformation.
        System.out.println("4. Run calculatetopNAfterMixTransformation function");
        calculatetopNAfterMixTransformation(10);

        System.out.print("\n<<<FINISHED>>>");
    }

    /**
     * Read the original trajectory files from input
     * folder.
     */
    public static void reSampleTrajectoriesFromGeolife() {
        // Read trajectory files
        ArrayList<Trajectory> trajectoryList = FileService.readOriginalTrajectoriesFromGeolife();
        System.out.println("Number of all trajectories: " + trajectoryList.size());
        FileService.saveTrajectoriesFiles(trajectoryList, 10);
    }

    /**
     * Calculate the distances BEFORE transformation.
     * Compare sample trajectories with test trajectories.
     * Run the distance functions and save the files to the output folder.
     * These are the 'ground truth' files for every distance function.
     *
     * @param k TopK
     */
    public static void calculatetopNBeforeTransformation(int k) {
        ArrayList<Trajectory> testList =
                FileService.readTestTrajectories();

        // run the distance functions
        distService.runDistanceFunctions(testList, k);
    }

    /**
     * Calculate the distances AFTER mix transformation.
     *
     * @param k TopK
     */
    public static void calculatetopNAfterMixTransformation(int k) {
        // read trajectories from folders
        ArrayList<Trajectory> testList =
                FileService.readTestTrajectories();


        // Calculate the distances for every transformation and every parameter.
        // One file for every distance function and parameter
        ArrayList<Trajectory> newTestList = null;

        // Add Points Transformation
        parameters = new double[]{0.5};
        for (double addRate : parameters) {
            newTestList = transfService.addPointsTransformation(testList, addRate);
        }

        // Delete Points Transformation
        parameters = new double[]{0.1};
        for (double deleteRate : parameters) {
            newTestList = transfService.deletePointsTransformation(newTestList, deleteRate);
        }

        // Different Time Rate Transformation
        parameters = new double[]{4};
        for (double sampleRate : parameters) {
            newTestList = transfService.diffSampleRateTransformation(newTestList, (int) sampleRate);
        }

        // Whole Scale Transformation
        parameters = new double[]{0.5};
        for (double scaleRatio : parameters) {
            newTestList = transfService.scaleTransformation(newTestList, scaleRatio);
        }

        // Time scale Transformation
        parameters = new double[]{0.25};
        for (double timeRatio : parameters) {
            newTestList = transfService.timeScaleTransformation(newTestList, timeRatio);
        }

        // Rotation Transformation
        parameters = new double[]{PI / 3, PI / 2, PI};
        for (double angle : parameters) {
            // get the transformation
            newTestList = transfService.rotationTransformation(newTestList, angle);
        }

        // Add Noise Transformation
        double[] addRate = new double[]{0.05};
        double[] noiseDist = new double[]{0.1};

        for (double addRateParam : addRate) {
            for (double noiseDistParam : noiseDist) {
                newTestList = transfService.addNoiseTransformation(newTestList, addRateParam, noiseDistParam);
            }
        }
        distService.TRANSF_NAME = "MixOfAllTransformation";
        distService.runDistanceFunctions(newTestList, k);
    }

    /**
     * Calculate the distances AFTER transformation.
     * Compare sample trajectories with test trajectories for every transformation and parameter.
     * Run the distance functions and save the files to the output folder.
     *
     * @param k
     */
    public static void calculatetopNAfterTransformation(int k) {
        // read sample and test trajectories from folders

        ArrayList<Trajectory> testList =
                FileService.readTestTrajectories();

        // Calculate the distances for every transformation and every parameter.
        // One file for every distance function and parameter
        ArrayList<Trajectory> newTestList;

        // Add Points Transformation
        parameters = new double[]{0.25, 0.5, 1.0};

        for (double addRate : parameters) {
            testList = FileService.readTestTrajectories();
            // get the transformation
            newTestList = transfService.addPointsTransformation(testList, addRate);
            // calculate distances to transformed trajectories
            distService.TRANSF_NAME = "_add_pts_" + (int) (addRate * 100) + "pct";
            distService.runDistanceFunctions(newTestList, k);
        }

        // Delete Points Transformation
        parameters = new double[]{0.1, 0.2, 0.4};
        for (double deleteRate : parameters) {
            testList = FileService.readTestTrajectories();
            // get the transformation
            newTestList = transfService.deletePointsTransformation(testList, deleteRate);
            // calculate distances to transformed trajectories
            distService.TRANSF_NAME = "_delete_pts_" + (int) (deleteRate * 100) + "pct";
            distService.runDistanceFunctions(newTestList, k);
        }

        // Different Time Rate Transformation
        parameters = new double[]{2, 4};
        for (double sampleRate : parameters) {
            testList = FileService.readTestTrajectories();

            // get the transformation
            newTestList = transfService.diffSampleRateTransformation(testList, (int) sampleRate);
            // calculate distances to transformed trajectories
            distService.TRANSF_NAME = "_dif_time_rate_" + (int) (sampleRate) + "pts";
            distService.runDistanceFunctions(newTestList, k);
        }

        // Whole Scale Transformation
        parameters = new double[]{0.5, 2.0};
        for (double scaleRatio : parameters) {
            testList = FileService.readTestTrajectories();
            // get the transformation
            newTestList = transfService.scaleTransformation(testList, scaleRatio);
            // calculate distances to transformed trajectories
            distService.TRANSF_NAME = "_scale_" + (int) (scaleRatio * 100) + "pct";
            distService.runDistanceFunctions(newTestList, k);
        }

        // Time scale Transformation
        parameters = new double[]{0.5, 2.0};
        for (double timeRatio : parameters) {
            testList = FileService.readTestTrajectories();
            // get the transformation
            newTestList = transfService.timeScaleTransformation(testList, timeRatio);
            // calculate distances to transformed trajectories
            distService.TRANSF_NAME = "_time_scale_" + (int) (timeRatio * 100) + "pct";
            distService.runDistanceFunctions(newTestList, k);//相当于只对TestList做变换，sampleList不变，两个再来比较
        }

        // Rotation Transformation
        parameters = new double[]{PI / 3, PI / 2, PI};
        for (double angle : parameters) {

            testList = FileService.readTestTrajectories();
            // get the transformation
            newTestList = transfService.rotationTransformation(testList, angle);
            // calculate distances to transformed trajectories
            distService.TRANSF_NAME = "_rotation_" + (int) Math.round(Math.toDegrees(angle)) + "dgr";
            distService.runDistanceFunctions(newTestList, k);
        }

        // Add Noise Transformation
        double[] addRate = new double[]{0.01, 0.05, 0.1};
        double[] noiseDist = new double[]{0.05, 0.1};

        for (double addRateParam : addRate) {
            for (double noiseDistParam : noiseDist) {

                testList = FileService.readTestTrajectories();
                // get the transformation
                newTestList = transfService.addNoiseTransformation(testList, addRateParam, noiseDistParam);

                // calculate distances to transformed trajectories
                distService.TRANSF_NAME = "_add_noise_" + (int) (addRateParam * 100) +
                        "pct_" + (int) (noiseDistParam * 100) + "d";
                distService.runDistanceFunctions(newTestList, k);
            }
        }
    }

}
