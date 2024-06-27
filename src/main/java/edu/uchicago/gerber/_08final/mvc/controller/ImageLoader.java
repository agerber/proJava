package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.Sprite;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

/*
Place all .png image assets in this directory src/main/resources/imgs or its subdirectories.
 */
public class ImageLoader {

//"src/main/resources/imgs"
//src/main/resources/imgs/fal/falcon125_thr.png
    private static final boolean LOAD_IMAGES_IN_STATIC_CONTEXT = false;

    public static final Map<String, BufferedImage> IMAGES;
    //load all images prior to runtime in the static context
    static {
        Path rootDirectory = Paths.get("src/main/resources/imgs");
        Map<String, BufferedImage> localMap = null;
        try {
            localMap = loadPngImages(rootDirectory);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
        IMAGES = localMap;

    }

    //used to load raster graphics
    public static BufferedImage getImage(String imagePath) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(Objects.requireNonNull(ImageLoader.class.getResourceAsStream(imagePath)));
        }
        catch (IOException e) {
            e.fillInStackTrace();
            bufferedImage = null;
        }
        return bufferedImage;
    }


    private static Map<String, BufferedImage> loadPngImages(Path rootDirectory) throws IOException {
        Map<String, BufferedImage> pngImages = new HashMap<>();
        Files.walkFileTree(rootDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().toLowerCase().endsWith(".png")
                        && !file.toString().toLowerCase().contains("do_not_load.png")) {
                    try {
                        BufferedImage bufferedImage = ImageIO.read(file.toFile());
                        if (bufferedImage != null) {
                            pngImages.put(file.toString().toLowerCase(), bufferedImage);
                        }
                    } catch (IOException e) {
                        e.fillInStackTrace();
                    }
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                // Handle the error here if necessary
                return FileVisitResult.CONTINUE;
            }
        });
        return pngImages;
    }
}
