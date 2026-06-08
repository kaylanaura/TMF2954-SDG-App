// Interface  : Displayable
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: Defines the contract for any content that can be displayed
//              in the Learning Module. All content pages must implement this.

public interface Displayable {
    void display();                // Render the content to console / GUI
    /* this is POLYMORPHISM - the same method name behaves differently per class
        any class that implements Displayable must decide HOW to display itself. **/
    
    String getTitle();             // Return the page title
    /* purpose: provode the titile of this content page.
    // Used by the UI to show e.g. "Understanding Mental Health" as a heading.**/
    
    String getContent();           // Return the main body text (main content/home screen)
    /* Purpose     : Provide the paragraph text shown in the reading area.**/
    
    String getFactLabel();         // Return the fact box label (e.g. "Did you know?")
    /* Purpose     : Label the highlighted fact/tip box at the bottom of a page.**/
    
    String getFactText();          // Return the fact box content
    /*  Purpose     : Provide the actual content inside the highlighted fact box.**/
    
    String getIconName();          // Return icon identifier for the card
    /* Purpose     : Tell the UI which emoji icon to show for this topic card. **/
    
    String getColorTheme();        // Return hex color for the card theme
}
