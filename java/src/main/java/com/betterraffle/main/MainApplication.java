package com.betterraffle.main;

import com.betterraffle.main.db.SQLHelper;
import com.betterraffle.main.entities.TrainingData;
import com.betterraffle.main.helper.Constants;
import com.betterraffle.main.storage.StorageProperties;
import com.betterraffle.main.storage.StorageService;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MainApplication {
    // Load library
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static int RUN_MODE = Constants.RUN_MODE_MAIN;

    public static void main(String[] args) {
        SQLHelper.getInstance();
           // startConsole();
        if (RUN_MODE == Constants.RUN_MODE_MAIN) {
            startWebServer(args);
        }
    }

    private static void startWebServer(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    private static void startConsole() {
        String baseInputDirPath = Constants.BASE_RESOURCE_PATH + Constants.INPUT_DIR_PATH;
        String baseOutputDirPath = Constants.BASE_RESOURCE_PATH + Constants.OUTPUT_DIR_PATH;
        String baseImagesDirPath = Constants.BASE_RESOURCE_PATH + Constants.IMAGES_DIR_PATH;

        CascadeClassifier classifier = new CascadeClassifier(
                Constants.BASE_RESOURCE_PATH + Constants.CLASSIFIER_ALT);

        // Crop images and extract faces from images put in flat structure in /input
        if (RUN_MODE == Constants.RUN_MODE_EXTRACT) {
            List<String> imageNames = Training.getFileNamesInDirectory(baseInputDirPath);
            for (String imageName : imageNames) {
                String inputImagePath = Training.getImagePath(imageName, baseInputDirPath, true);
                Mat inputImage = Training.getImageMat(inputImagePath);
                List<Mat> faces = Training.findAndCropFace(inputImage, classifier);
                Training.writeFacesToImgFiles(faces, imageName, baseOutputDirPath);
            }
        }

        TrainingData trainingData = Training.prepareTrainingData(baseImagesDirPath);
        if (RUN_MODE == Constants.RUN_MODE_MAIN) {
            Training.trainRecognizer(trainingData, baseImagesDirPath);
            String imageName = "1.jpg";
            Training.recognize(imageName, Constants.BASE_RESOURCE_PATH + Constants.UPLOAD_DIR_PATH);
        }
    }
}
