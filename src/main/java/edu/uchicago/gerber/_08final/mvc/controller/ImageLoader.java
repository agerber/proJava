package edu.uchicago.gerber._08final.mvc.controller;

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

    public static Map<String, BufferedImage> IMAGE_MAP = null;
    //load all images prior to runtime in the static context
    static {
        if (LOAD_IMAGES_IN_STATIC_CONTEXT){
            Path rootDirectory = Paths.get("src/main/resources/imgs");
            Map<String, BufferedImage> localMap = null;
            try {
                localMap = loadPngImages(rootDirectory);
            } catch (IOException e) {
                e.fillInStackTrace();
            }
            IMAGE_MAP = localMap;
            System.out.println("loading:");
            if (IMAGE_MAP != null)
                IMAGE_MAP.forEach( (k, v) ->  System.out.println(k));
        }
    }

    //used to load raster graphics
    public static BufferedImage getImage(String imagePath) {
        if (LOAD_IMAGES_IN_STATIC_CONTEXT){
            return IMAGE_MAP.get(imagePath.toLowerCase());
        }
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
                            //substring(18) removes "src/main/resources" so that keys are consistent with non-static
                            pngImages.put(file.toString().toLowerCase().substring(18), bufferedImage);
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
