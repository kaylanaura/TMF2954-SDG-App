// Class      : LearningContent
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: Stores all data for a single educational content page.
//              Implements Displayable to provide a consistent content contract.
//              Used by LearningModulePanel to populate the learning screens.

// LearningContent is a DATA MODEL class.
// It holds all the information for ONE page of educational content

// ── KEY OOP CONCEPTS IN THIS FILE ────────────────────────────────────────────
// 1. ENCAPSULATION     : All fields are private. External code can only read
//                        them through public getter methods — not modify them directly.
// 2. IMPLEMENTS        : Signs the Displayable interface contract.
// 3. CONSTRUCTOR       : Initialises all fields when an object is created.

public class LearningContent implements Displayable {

    // ── Attributes ────────────────────────────────────────────────────────────
    // ── ENCAPSULATION ─────────────────────────────────────────────────────────
    // 'private' means these fields CANNOT be accessed directly from outside this class.
    // This is ENCAPSULATION: protecting internal data and controlling access.
    
    private String title;        // Page title shown in header
    private String content;      // Main educational body text
    private String factLabel;    // Label for the highlighted fact box
    private String factText;     // Content of the highlighted fact box
    private String iconName;     // Icon name for the topic card (e.g. "brain")
    private String colorTheme;   // Hex color for card and icon background
    private int pageNumber;      // Page number within its topic (1-based)
    private String topicName;    // Parent topic this page belongs to
    private String imagePath;    // Relative path to the SVG image asset

    // ── Constructor ───────────────────────────────────────────────────────────
    // 'public' → any class can create a LearningContent object.
    
    public LearningContent(String title, String content, String factLabel,
                           String factText, String iconName, String colorTheme,
                           int pageNumber, String topicName, String imagePath) {

        // 'this.title' = the FIELD (private attribute declared above)
        // 'title'      = the PARAMETER passed into the constructor
        // 'this.' is used to distinguish between the two when they share the same name.
        
        this.title       = title;        // Store the passed-in title into the object's private title field
        this.content     = content;      // Store the body text
        this.factLabel   = factLabel;    // Store the fact box label
        this.factText    = factText;     // Store the fact box content
        this.iconName    = iconName;     // Store the icon name identifier    
        this.colorTheme  = colorTheme;   // Store the hex colour string
        this.pageNumber  = pageNumber;   // Store which page number this is
        this.topicName   = topicName;    // Store which topic this page belongs to
        this.imagePath   = imagePath;    // Store the file path to the image asset
    }

    // ── Displayable interface implementation ──────────────────────────────────
    /**
     * Outputs a complete, structured text representation of the learning page 
     * to the console console window for debugging or text-mode rendering.
     * @Override is an ANNOTATION (not code — it's a label for the compiler).
     * If you misspell the method name, Java will give a compile error instead of
     */
    @Override
    public void display() {
        System.out.println("========================================");
        System.out.println("Topic  : " + topicName);        // e.g. "Mental Health"
        System.out.println("Page   : " + pageNumber);       // e.g. 1
        System.out.println("Title  : " + title);            // "Understanding Mental Health"
        System.out.println("----------------------------------------");
        System.out.println(content);                        // Full body paragraph text
        System.out.println("----------------------------------------");
        System.out.println("[" + factLabel + "] " + factText);
        System.out.println("========================================");
    }

    // Overloaded display — print with a custom prefix label
    // 'String prefix' is an extra parameter that lets the caller add a label.
    public void display(String prefix) {
        System.out.println(prefix + " | " + topicName + " > " + title);
    }

    // Overloaded display — print only the fact box
    // controls the output mode
    public void display(boolean factOnly) {
        if (factOnly) {
            // only print the fact box label and text
            System.out.println("[" + factLabel + "] " + factText);
        } else {
            // call the no-parameter version of display() - the full output
            display();
        }
    }

    /** GETTER METHODS (Part of Encapsulation)
    * ── WHAT IS A GETTER? ────────────────────────────────────────────────────
    * Because all fields are 'private', the only way to READ them from another
    * class is through these public getter methods.
    */
    @Override
    public String getTitle()      { return title; } // return the page heading text

    @Override
    public String getContent()    { return content; } // return the educational body paragraph text
    
    @Override
    public String getFactLabel()  { return factLabel; } // returns the fact box label

    @Override
    public String getFactText()   { return factText; } // returns the fact box body text

    @Override
    public String getIconName()   { return iconName; } // returns the icon identifier string

    @Override
    public String getColorTheme() { return colorTheme; } // returns hex colour string

    // ── Additional getters (not part of interface) ────────────────────────────────────────────────────
    // 'int' retrun type - page numbers are whole numbers
    public int    getPageNumber() { return pageNumber; } // Returns which page number this is (1-based)
    public String getTopicName()  { return topicName; } // Returns the parent topic name, e.g. "Mental Health"
    public String getImagePath()  { return imagePath; } // Returns the file path to the PNG image

    // ── toString override (is inherited from Java's Object class (every class has it)) ─────────────────────────────────────────────────────
        @Override
    public String toString() {
        // Returns a compact readable summary, e.g.: LearningContent{topic='Mental Health', page=1, title='Understanding Mental Health'}
        return "LearningContent{topic='" + topicName + "', page=" + pageNumber
               + ", title='" + title + "'}";
    }
}

// ── FINAL SUMMARY OF OOP CONCEPTS IN THIS FILE ───────────────────────────
    // • ENCAPSULATION  : All 9 fields are private. Only readable via public getters.
    //                    External classes cannot accidentally modify them.
    // • IMPLEMENTS     : Signs the Displayable interface contract — must provide
    //                    all 7 methods declared in Displayable.java.
    // • CONSTRUCTOR    : Single constructor initialises all 9 fields at object creation.
    //                    Uses 'this.' to distinguish fields from parameters.
    // • OVERLOADING    : display() has 3 versions with different parameter lists.
    //                    Java calls the right version based on what you pass.
    // • @Override      : Used on getters and display() to fulfil the interface.
    //                    Also used on toString() to replace Object's default.
    // • POLYMORPHISM   : display() behaves differently based on which version is called.
    // ─────────────────────────────────────────────────────────────────────────
