// Class      : LearningContent
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : [Member 2 Name]
// Description: Stores all data for a single educational content page.
//              Implements Displayable to provide a consistent content contract.
//              Used by LearningModulePanel to populate the learning screens.

public class LearningContent implements Displayable {

    // ── Attributes ────────────────────────────────────────────────────────────
    private String title;        // Page title shown in header
    private String content;      // Main educational body text
    private String factLabel;    // Label for the highlighted fact box
    private String factText;     // Content of the highlighted fact box
    private String iconName;     // Icon name for the topic card (e.g. "brain")
    private String colorTheme;   // Hex color for card and icon background
    private int pageNumber;      // Page number within its topic (1-based)
    private String topicName;    // Parent topic this page belongs to

    // ── Constructor ───────────────────────────────────────────────────────────
    public LearningContent(String title, String content, String factLabel,
                           String factText, String iconName, String colorTheme,
                           int pageNumber, String topicName) {
        this.title       = title;
        this.content     = content;
        this.factLabel   = factLabel;
        this.factText    = factText;
        this.iconName    = iconName;
        this.colorTheme  = colorTheme;
        this.pageNumber  = pageNumber;
        this.topicName   = topicName;
    }

    // ── Displayable interface implementation ──────────────────────────────────
    @Override
    public void display() {
        System.out.println("========================================");
        System.out.println("Topic  : " + topicName);
        System.out.println("Page   : " + pageNumber);
        System.out.println("Title  : " + title);
        System.out.println("----------------------------------------");
        System.out.println(content);
        System.out.println("----------------------------------------");
        System.out.println("[" + factLabel + "] " + factText);
        System.out.println("========================================");
    }

    // Overloaded display — print with a custom prefix label
    public void display(String prefix) {
        System.out.println(prefix + " | " + topicName + " > " + title);
    }

    // Overloaded display — print only the fact box
    public void display(boolean factOnly) {
        if (factOnly) {
            System.out.println("[" + factLabel + "] " + factText);
        } else {
            display();
        }
    }

    @Override
    public String getTitle()      { return title; }

    @Override
    public String getContent()    { return content; }

    @Override
    public String getFactLabel()  { return factLabel; }

    @Override
    public String getFactText()   { return factText; }

    @Override
    public String getIconName()   { return iconName; }

    @Override
    public String getColorTheme() { return colorTheme; }

    // ── Additional getters ────────────────────────────────────────────────────
    public int    getPageNumber() { return pageNumber; }
    public String getTopicName()  { return topicName; }

    // ── toString override ─────────────────────────────────────────────────────
    @Override
    public String toString() {
        return "LearningContent{topic='" + topicName + "', page=" + pageNumber
               + ", title='" + title + "'}";
    }
}
