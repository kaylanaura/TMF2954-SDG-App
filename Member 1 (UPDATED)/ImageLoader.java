// Class      : ImageLoader
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : [Member 2 Name]
// Description: Handles all image loading for the Learning Module.
//              Searches every possible location automatically.

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageLoader {

    public static ImageIcon load(String filename, int width, int height) {
        List<String> paths = buildSearchPaths(filename);

        for (String path : paths) {
            File f = new File(path);
            if (f.exists() && f.isFile()) {
                try {
                    BufferedImage raw = ImageIO.read(f);
                    if (raw != null) {
                        double scale = Math.min(
                            (double) width  / raw.getWidth(),
                            (double) height / raw.getHeight()
                        );
                        int w = (int)(raw.getWidth()  * scale);
                        int h = (int)(raw.getHeight() * scale);
                        System.out.println("[ImageLoader] Found: " + f.getAbsolutePath());
                        return new ImageIcon(raw.getScaledInstance(
                            w, h, Image.SCALE_SMOOTH));
                    }
                } catch (Exception e) { /* try next */ }
            }
        }

        // Last resort: search entire working directory tree
        File found = searchDir(new File(System.getProperty("user.dir")), filename, 3);
        if (found != null) {
            try {
                BufferedImage raw = ImageIO.read(found);
                if (raw != null) {
                    double scale = Math.min(
                        (double) width  / raw.getWidth(),
                        (double) height / raw.getHeight()
                    );
                    int w = (int)(raw.getWidth()  * scale);
                    int h = (int)(raw.getHeight() * scale);
                    System.out.println("[ImageLoader] Found by search: " + found.getAbsolutePath());
                    return new ImageIcon(raw.getScaledInstance(w, h, Image.SCALE_SMOOTH));
                }
            } catch (Exception e) { /* fall through */ }
        }

        System.out.println("[ImageLoader] NOT FOUND: " + filename);
        return null;
    }

    private static List<String> buildSearchPaths(String filename) {
        List<String> paths = new ArrayList<>();
        String s   = File.separator;
        String dir = System.getProperty("user.dir");
        String home = System.getProperty("user.home");

        // All common subfolder names
        String[] folders = {
            "assets" + s + "final_images",
            "assets" + s + "images",
            "assets",
            "images",
            "final_images",
            "resources",
            "res",
            ""
        };

        for (String folder : folders) {
            String base = folder.isEmpty() ? filename : folder + s + filename;
            paths.add(base);                          // relative
            paths.add(dir + s + base);               // absolute from working dir
            paths.add(dir + s + ".." + s + base);    // one level up
            paths.add(home + s + "Desktop" + s + base);
            paths.add(home + s + "Downloads" + s + base);
        }

        // Hardcoded fallback for common Windows project locations
        String[] winRoots = {
            home + s + "Desktop",
            home + s + "Documents",
            home + s + "Downloads",
            "C:\\Users\\User\\Desktop\\FONG EIK\\SE\\YEAR 2\\SEM 2\\JAVA PROGRAMMING\\JAVA PROJECT",
        };
        for (String root : winRoots) {
            paths.add(root + s + "assets" + s + "final_images" + s + filename);
            paths.add(root + s + "assets" + s + filename);
            paths.add(root + s + filename);
        }

        return paths;
    }

    // Recursively search a directory up to maxDepth levels deep
    private static File searchDir(File dir, String filename, int maxDepth) {
        if (maxDepth < 0 || !dir.exists() || !dir.isDirectory()) return null;
        File[] files = dir.listFiles();
        if (files == null) return null;
        for (File f : files) {
            if (f.isFile() && f.getName().equals(filename)) return f;
            if (f.isDirectory()) {
                File found = searchDir(f, filename, maxDepth - 1);
                if (found != null) return found;
            }
        }
        return null;
    }
}
