package team.dig.vtdm.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import team.dig.vtdm.entities.*;

/**
 * Service to deal with files (open, read, save)
 *
 * @author uqdalves
 */
public class FileService {
    // Root directory of the files in the disc
    private static final String INPUT_FILE = "src/data/input/";
    private static final String OUTPUT_FILE = "src/data/output/";

    private static final String ORIGINAL_TRA_FOLDER = "Geolife Trajectories TEST/";

    private static final String CLEANED_TRA_FOLDER = "ReSampleClean/";

    private static final int DIMENSION = 2;

    /**
     * Return a list of all Trajectory objects with the coordinates read
     * from disc. The original file trajectories, without re-sampling.
     *
     * @return ArrayList<Trajectory>
     */
    public static ArrayList<Trajectory> readOriginalTrajectoriesFromGeolife() {
        ArrayList<Trajectory> trajectoryList =
                new ArrayList<Trajectory>();     //Initialize a dynamic array of type Trajectory
        try {
            // open files from folder
            File diretory = new File(INPUT_FILE + ORIGINAL_TRA_FOLDER);

            File files[] = openDirectoryFiles(diretory);
            // read files
            for (int fileId = 0; fileId < files.length; fileId++) {
                File currentFile = files[fileId];
                // read file
                BufferedReader buffer = new BufferedReader(
                        new FileReader(currentFile));
                // fields to be read from the file
                double coordinate[] = new double[DIMENSION];
                // new trajectory for this file, set features
                Trajectory trajectory = new Trajectory();
                // read file lines
                while (buffer.ready()) {
                    String line = buffer.readLine();
                    String[] tokens = line.split(",");
                    // if new trajectory
                    if (tokens.length != 7) {
                        // Add trajectories with more than 20 points only
                        continue;
                    } else {
                        // Parse the inputs
                        String timeString = tokens[5] + " " + tokens[6];
                        coordinate[1] = Double.parseDouble(tokens[0]);
                        coordinate[0] = Double.parseDouble(tokens[1]);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = null;
                        try {
                            date = format.parse(timeString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        // Convert time to timestamp
                        long timeStamp = date.getTime();
                        // create a new point from the line input, set features
                        Point point = new Point(coordinate, timeStamp);
                        trajectory.addPoint(point);
                    }
                }
                // Add trajectories with more than 20 points only
                if (trajectory.numberOfPoints() >= 20 && trajectory.numberOfPoints() <= 150) {
                    trajectoryList.add(trajectory);
                    if (trajectoryList.size() == 5000)
                        break;
                }
                // close file
                buffer.close();
            }
        } catch (IOException e) {
            System.out.println("Error opening input files.");
            e.printStackTrace();
        }
        return trajectoryList;
    }

    /**
     * Return a list of test Trajectory objects with the coordinates read
     * from disc. Previously selected random trajectories, form the test folder.
     */
    public static ArrayList<Trajectory> readTestTrajectories() {
        return readTrajectories(CLEANED_TRA_FOLDER);
    }

    /**
     * Return a list of Trajectory objects with the coordinates read
     * from disc. Read the re-sampled trajectories.
     */
    private static ArrayList<Trajectory> readTrajectories(final String folderToRead) {
        // trajectories to read
        ArrayList<Trajectory> trajectoryList =
                new ArrayList<>();

        try {
            // open files from folder
            File diretory = new File(INPUT_FILE + folderToRead);
            File files[] = openDirectoryFiles(diretory);
            // read files
            for (int fileId = 0; fileId < files.length; fileId++) {
                File currentFile = files[fileId];
//                System.out.println(files[fileId]);

                // read file
                BufferedReader buffer = new BufferedReader(
                        new FileReader(currentFile));
                // fields to be read from the file
                double coordinate[] = new double[DIMENSION];
                long timeLong;

                // new trajectory for this file, set features
                Trajectory trajectory = new Trajectory();

                // read file lines (coordinates)
                while (buffer.ready()) {
                    // each line of the file
                    String line = buffer.readLine();
                    String[] tokens = line.split(",");
                    // new trajectory start at #
                    if (tokens[0].equals("#")) {
                        if (trajectory.numberOfPoints() != 0) {
                            trajectoryList.add(trajectory);
                        }
                        // new trajectory for this file, set features
                        trajectory = new Trajectory();
                    } else {
                        // Parse the inputs
                        coordinate[0] = Double.parseDouble(tokens[1]);
                        coordinate[1] = Double.parseDouble(tokens[2]);
                        String timeStampString = tokens[0];
                        timeLong = Long.parseLong(timeStampString);
                        // create a new point from the line input, set features
                        Point point = new Point(coordinate, timeLong);
                        trajectory.addPoint(point);
                    }
                }
                // adds the last trajectory in the file
                trajectoryList.add(trajectory);
                // close file
                buffer.close();
            }

        } catch (IOException e) {
            System.out.println("Error opening input files.");
            e.printStackTrace();
        }

        return trajectoryList;
    }

    /**
     * Creates and save a file with the distances
     */
    public static void saveDistanceFile(ArrayList<Double> distancesList, String fileName, int k) {
        String script = "#\n";
        int flag = 0;
        for (Double val : distancesList) {
            flag++;
            if (Double.isNaN(val)) {
                continue;
            }
            if (flag % k == 0 && flag < distancesList.size()) {
                script += val.toString() + "\n" + "#\n";
            } else script += val.toString() + "\n";
        }
        saveFile(script, fileName, false);
    }

    /**
     * Read the files inside a directory. Recursively read
     * directories into other directory.
     *
     * @param dir File
     * @return File[] a list with the files read
     */
    private static File[] openDirectoryFiles(File dir) {
        List<File> fileList = new ArrayList<>();

        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                // add in the list the files found in 'files[i]':
                File[] recFiles = openDirectoryFiles(files[i]);
                for (int j = 0; j < recFiles.length; j++) {
                    fileList.add(recFiles[j]);
                }
            } else {
                // add in the list the file found in 'directory'
                fileList.add(files[i]);
            }
        }

        // turn the List into a File[]:
        File[] filesFound = new File[fileList.size()];
        for (int i = 0; i < fileList.size(); i++) {
            filesFound[i] = fileList.get(i);
        }

        return filesFound;
    }

    /**
     * Save the file to the disc folder.
     *
     * @param script   The content of the file
     * @param fileName Name of the file, with its extension
     */
    private static void saveFile(String script, String fileName, boolean saveToInput) {
        String pathname = OUTPUT_FILE + fileName;
        if (saveToInput) {
            pathname = INPUT_FILE + CLEANED_TRA_FOLDER + fileName;
        }
        File file = new File(pathname);
        File fileParent = file.getParentFile();

        try {
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("File '" + fileName + "' successfully created ->\n" + "(path: " + pathname +")");
            } else {
                System.out.println("File '" + fileName + "' already exists ->\n" + "(path: " + pathname +")");
            }

            BufferedWriter buffer =
                    new BufferedWriter(new PrintWriter(file));
            buffer.write(script);
            //buffer.flush();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save this list of trajectories to disc files,
     * save every trajPerFile trajectories into a different file.
     */
    public static void saveTrajectoriesFiles(ArrayList<Trajectory> trajectoryList, int trajPerFile) {
        int count = 1;
        String script = "";
        int fileCount = trajPerFile;
        for (Trajectory traj : trajectoryList) {
            script += "#\n";
            for (Point p : traj.getPointsList()) {
                // timeStamp, x, y, timeString
                script += p.time.getTime() + "," +
                        p.coordinate[0] + "," +
                        p.coordinate[1] + "," +
                        p.originalTimeString + "\n";
            }
            if (count == trajPerFile) {
                count = 1;
                // save the script into a file
                saveFile(script, "" + fileCount, true);
                fileCount += trajPerFile;
                script = "";
            } else {
                count++;
            }
        }
        System.out.println("Number of redundant trajectories: "+ (count-1));
        System.out.println("Number of test trajectories: " + (fileCount-trajPerFile));
    }

}
