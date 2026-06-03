// Class      : ImageLoader
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: Handles all image loading for the Learning Module.
//              Searches every possible location automatically.

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * The ImageLoader class provides robust, automated utility methods to look up, 
 * read, and scale images from various local directory environments.
 */

public class ImageLoader {

    public static ImageIcon load(String filename, int width, int height) {
        // Generate a comprehensive list of potential file locations
        List<String> paths = buildSearchPaths(filename);

        // Iterate through the generated path combinations to find the file
        for (String path : paths) {
            File f = new File(path);
            if (f.exists() && f.isFile()) {
                try {
                    BufferedImage raw = ImageIO.read(f);
                    if (raw != null) {
                        // Calculate aspect ratio scaling factor to prevent image distortion
                        double scale = Math.min(
                            (double) width  / raw.getWidth(),
                            (double) height / raw.getHeight()
                        );
                        int w = (int)(raw.getWidth()  * scale);
                        int h = (int)(raw.getHeight() * scale);
                        System.out.println("[ImageLoader] Found: " + f.getAbsolutePath());
                        // Return scaled image using high-quality smooth scaling
                        return new ImageIcon(raw.getScaledInstance(
                            w, h, Image.SCALE_SMOOTH));
                    }
                } catch (Exception e) { /* try next */ }
                /* Silently catch reading errors and try the next path sequence */
            }
        }

        // Last resort: search entire working directory tree
        File found = searchDir(new File(System.getProperty("user.dir")), filename, 3);
        if (found != null) {
            try {
                BufferedImage raw = ImageIO.read(found);
                if (raw != null) {
                    // Recalculate scaling for the file found through deep search
                    double scale = Math.min(
                        (double) width  / raw.getWidth(),
                        (double) height / raw.getHeight()
                    );
                    int w = (int)(raw.getWidth()  * scale);
                    int h = (int)(raw.getHeight() * scale);
                    System.out.println("[ImageLoader] Found by search: " + found.getAbsolutePath());
                    return new ImageIcon(raw.getScaledInstance(w, h, Image.SCALE_SMOOTH));
                }
            } catch (Exception e) { /* fall through if image processing fails */ }
        }

        // Log a failure message if all lookups and searches fail
        System.out.println("[ImageLoader] NOT FOUND: " + filename);
        return null;
    }

    private static List<String> buildSearchPaths(String filename) {
        List<String> paths = new ArrayList<>();
        String s   = File.separator;                    // Dynamic OS file seperator ('/' or '\')
        String dir = System.getProperty("user.dir");    // Current project working directory
        String home = System.getProperty("user.home");  // User's operating system home directory

        // All common subfolder names
        // List of all conventional asset/image directory patterns used in the project
        String[] folders = {
            "assets" + s + "final_images",
            "assets" + s + "images",
            "assets",
            "images",
            "final_images",
            "resources",
            "res",
            "" // Empty string handles direct root filename evaluations
        };

        // Combine subfolders with various system access strategies (Relative, Absolute, Desktop, Downloads)
        for (String folder : folders) {
            String base = folder.isEmpty() ? filename : folder + s + filename;
            paths.add(base);                          // relative
            paths.add(dir + s + base);               // absolute from working dir
            paths.add(dir + s + ".." + s + base);    // one level up
            paths.add(home + s + "Desktop" + s + base);
            paths.add(home + s + "Downloads" + s + base);
        }

// Fallback for common Windows project locations and current batch execution directory
        String[] winRoots = {
            dir,                  // Dynamically points to your active project/bat directory
            home + s + "Desktop",
            home + s + "Documents",
            home + s + "Downloads"
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
