// Interface  : Displayable
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : [Member 2 Name]
// Description: Defines the contract for any content that can be displayed
//              in the Learning Module. All content pages must implement this.

public interface Displayable {
    void display();                // Render the content to console / GUI
    String getTitle();             // Return the page title
    String getContent();           // Return the main body text
    String getFactLabel();         // Return the fact box label (e.g. "Did you know?")
    String getFactText();          // Return the fact box content
    String getIconName();          // Return icon identifier for the card
    String getColorTheme();        // Return hex color for the card theme
}
