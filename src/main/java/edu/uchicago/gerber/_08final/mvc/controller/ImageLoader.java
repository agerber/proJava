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

    /*
        The default setting for the image-loader is NOT to load images before runtime (LOAD_IMAGES_IN_STATIC_CONTEXT =
        false). You should keep this setting in most game implementations. However, if your game uses a lot of raster graphics,
        you may consider setting the LOAD_IMAGES_IN_STATIC_CONTEXT flag to true. This will load all image
        assets in the static context prior to runtime, thereby increasing runtime performance.
     */
    private static final boolean LOAD_IMAGES_IN_STATIC_CONTEXT = false;

    private static Map<String, BufferedImage> IMAGE_MAP = null;
    //If LOAD_IMAGES_IN_STATIC_CONTEXT is true, load all images prior to runtime in the static context
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



    /*
     Walks the directory and sub-directories at root src/main/resources/imgs and returns a Map<String, BufferedImage>
     of images in that file hierarcy.
     */
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
                            //substring(18) removes "src/main/resources" so that keys/paths are consistent with
                            // static and non-static
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


    //If LOAD_IMAGES_IN_STATIC_CONTEXT is true, fetch the image from existing static map, otherwise
    // load the image at runtime. This is the only public method of this class.
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


}
