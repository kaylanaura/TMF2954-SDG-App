// Class      : ImageLoader
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: Handles all image loading for the Learning Module.
//              Searches every possible location automatically.

// ── WHAT DOES THIS CLASS DO? ─────────────────────────────────────────────────
// ImageLoader is a UTILITY CLASS — it provides static helper methods for
// loading and scaling image files from disk.

// ── KEY CONCEPTS IN THIS FILE ────────────────────────────────────────────────
// 1. UTILITY CLASS   : Has no constructor; all methods are static.
// 2. STATIC METHODS  : Called on the class, not on an object instance.
// 3. ArrayList       : Used to build a dynamic list of paths to try.
// 4. RECURSION       : searchDir() calls ITSELF to search sub-folders.
// 5. try/catch       : Exception handling — gracefully handles missing/broken files.
// ─────────────────────────────────────────────────────────────────────────────

import java.awt.*;              // For Image, used in scaling
import java.awt.image.BufferedImage; // For reading raw pixel image data
import java.io.File;            // For representing and checking file paths
import java.util.ArrayList;     // For building the dynamic list of paths to try
import java.util.List;          // Interface type for the list
import javax.imageio.ImageIO;   // Java's built-in image file reader
import javax.swing.*;           // For ImageIcon — the Swing image wrapper used in UI labels

/**
 * The ImageLoader class provides robust, automated utility methods to look up, 
 * read, and scale images from various local directory environments.
 */

public class ImageLoader {
   // METHOD: load()
    // ── PURPOSE ───────────────────────────────────────────────────────────────
    // Given a filename and target dimensions, search all known folder locations
    // for the image file, load it, scale it to fit within width x height
    // while preserving aspect ratio, and return it as an ImageIcon. 

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

        // dir.listFiles() → returns an array of all files and subfolders in 'dir'
        // Returns null if the directory can't be read (permission error, etc.)
        File[] files = dir.listFiles();
        if (files == null) return null;
        
        // Loop through every item in the directory
        for (File f : files) {
            
            // If this item is a file and its name matches what we're looking for, return it.
            // f.getName() → just the filename (not the full path), e.g. "02_mental_health.png"
            if (f.isFile() && f.getName().equals(filename)) return f; // FOUND IT
            if (f.isDirectory()) {
                File found = searchDir(f, filename, maxDepth - 1); // RECURSIVE CALL
                if (found != null) return found; // if the recursive call found it, pas it up
            }
        }
        
        // We searched the whole directory and found nothing — return null.
        return null;
    }
}

    // ── FINAL SUMMARY OF CONCEPTS IN THIS FILE ───────────────────────────────
    // • UTILITY CLASS  : No constructor, all methods are static.
    //                    Used by calling ImageLoader.load() directly.
    // • STATIC METHODS : load(), buildSearchPaths(), searchDir() all static.
    // • ArrayList      : Dynamic list of path strings built in buildSearchPaths().
    // • try/catch      : Exception handling so broken images don't crash the app.
    // • RECURSION      : searchDir() calls itself to explore subdirectories.
    // • null           : Returned when the file is not found; caller must check for null.
    // • Math.min()     : Used to compute aspect-ratio-preserving scale factor.
    // • File.separator : Makes file paths work on both Windows (\) and Mac/Linux (/).
    // ─────────────────────────────────────────────────────────────────────────
