
package assign_1;

// the base class to which the others extend
class Item {
    /* fields marked private to prevent access from outside
     * member methods. */
    private double catalog_number;
    private int copy_number;
    private String item_name;
    private double fee;
    private int loan_period;
    private boolean rented;
    private int date_rented;
    Item(double id, int copy, String name, double fee, int per) {
        this.catalog_number = id;
        this.copy_number = copy;
        this.item_name = name;
        this.fee = fee;
        this.loan_period = per;
        this.rented = false;
        this.date_rented = 0;
    } // constructor
    // methods used to access fields
    String getName() {
        return this.item_name;
    }
    void setDR(int in) {
        this.date_rented = in;
    }
    double getId() {
        return this.catalog_number;
    }
    int getCopy() {
        return this.copy_number;
    }
    int getDR() {
        return this.date_rented;
    }
    void toggleRent() {
        this.rented = !this.rented;
    }
    double getFee() {
        return this.fee;
    }
    int getPer() {
        return this.loan_period;
    }
    boolean checkAvail() {
        return this.rented;
    }
}

/* subclass extends superclass Item; inherits all of the fields found in
 * that superclass. Has exclusive field */
class Video extends Item {
    private char classification;
    Video(double id, int copy, String name, char rate) {
        /* call superclass constructor, adding specific
         * values from subclass. */
        super(id, copy, name, 0.5, 2);
        this.classification = rate;
    } // constructor
    char getRating() {
        return this.classification;
    }
}

class Book extends Item {
    private String author_name;
    Book(double id, int copy, String name, String auth) {
        super(id, copy, name, 0.25, 21);
        this.author_name = auth;
    }
    String getAuthor() {
        return this.author_name;
    }
}

class Game extends Item {
    private char classification;
    private char system;
    Game(double id, int copy, String name, char rate, char sys) {
        super(id, copy, name, 2.00, 3);
        this.classification = rate;
        this.system = sys;
    }
    char getRating() {
        return this.classification;
    }
    char getSystem() {
        return this.system;
    }
}

class Customer {
    private int customer_id;
    private String customer_name;
    private int age;
    private int rentals;
    private double balance;
    Customer(int id, String name, int age) {
        this.rentals = 0;
        this.balance = 0.00;
        this.customer_id = id;
        this.customer_name = name;
        this.age = age;
    }
    int getId() {
        return this.customer_id;
    }
    String getName() {
        return this.customer_name;
    }
    int getAge() {
        return this.age;
    }
    int getRents() {
        return this.rentals;
    }
    void addRental() {
        this.rentals++;
    }
    void decRental() {
        this.rentals--;
    }
    void setBal(double in) {
        this.balance = in;
    }
    double getBal() {
        return this.balance;
    }
}

class Child extends Customer {
    Child(int id, String name, int age) {
        super(id, name, age);
    }
}