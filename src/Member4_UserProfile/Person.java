// ============================================================
// Class      : Person
// Creator    : Kayla Binti Mohamad (102641)
// Tester     : Victoria Ngui Fong Eik (106647)
// Description: Base class that stores a person's name and ID.
//              UserProfile extends this class (INHERITANCE).
//              Demonstrates: INHERITANCE, GETTERS, toString() OVERRIDE
// ============================================================


public class Person {


    // --- Attributes -------------------------------------------
    // protected: accessible in this class AND child classes (like UserProfile)
    // Enables INHERITANCE - child classes can access directly
    protected String name;  // Stores the person's full name
    
    // protected: accessible in this class AND child classes
    // Used for student ID or user ID identification
    protected String id;    // Stores the person's student/user ID


    // --- Constructor ------------------------------------------
    // public: accessible from any class
    // Constructor: initializes new Person objects
    // Parameters: name (String), id (String)
    // this.name = name: assigns parameter 'name' to attribute 'this.name'
    // this.id = id: assigns parameter 'id' to attribute 'this.id'
    public Person(String name, String id) {
        this.name = name;
        this.id   = id;
    }


    // --- Getters ----------------------------------------------
    // public: accessible from any class
    // Getter method: returns the 'name' attribute (ENCAPSULATION)
    // Returns: String containing the person's name
    public String getName() {
        return name;
    }


    // public: accessible from any class
    // Getter method: returns the 'id' attribute (ENCAPSULATION)
    // Returns: String containing the person's ID
    public String getId() {
        return id;
    }


    // --- toString ---------------------------------------------
    // @Override: indicates this method overrides parent class's toString()
    // public: accessible from any class
    // toString(): returns String representation of the object
    // OVERRIDE: UserProfile class will override this again to add more details
    // Returns: String like "Person{name='Alice', id='123'}"
    @Override
    public String toString() {
        return "Person{name='" + name + "', id='" + id + "'}";
    }
}