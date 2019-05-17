package assign_1;

import BasicIO.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Assign_1 {
    
    ASCIIDataFile catalog; // infile for catalog of items
    ASCIIDataFile patrons; // infile for set of customers/patrons
    ArrayList<Item> collection; // catalog of items
    ArrayList<Customer> customers; // set of customers/patrons
    
    public Assign_1() { // main menu
        loadFiles(); // load .txt files
        prepareCollections(); // populate arraylists with values from .txt
        mainMenu(); // begin form
    }
    
    /*************************************************************************
      * regarding below: I wrote all this in Netbeans at home and used absolute
      * path for files. It worked great! But making sure it works in DrJava on the lab
      * computers doesn't work as well. I don't know default path for input in DrJava.
      * I tried just "File.txt", "\\File.txt", and ".\\File.txt" since the files
      * are in the same directories as the program itself but it opens nothing.
      * I don't know what the problem is and it's 8:17am on due date. Hopefully
      * this doesn't dock too many marks off.
      * ******************************************************************/
    
    /* method to load the .txt files into datafile types */
    void loadFiles() {
        System.out.println("Select Catalog.txt file");
        catalog = new ASCIIDataFile();
        System.out.println("Select Patrons.txt file");
        patrons = new ASCIIDataFile();
    }
    
    /* method to populate ArrayLists with the values found in the .txt
     * files provided for the assignment. The ALs is populated with objects of
     * type Item */
    void prepareCollections() {
        collection = new ArrayList<>();
        while(true) {         
            if (catalog.isEOF()) { break; } // if reached EoF, stop
            char itemType = catalog.readChar(); // find type of Item
            // the below are fields in the superclass so are in all subclasses
            double catNum = catalog.readDouble(); // item ID
            int copyNum = catalog.readInt(); // copy number
            String name = catalog.readString(); // item name
            if (itemType == 'b') { // if Book
                String author = catalog.readString(); // author name
                Book newItem = new Book(catNum, copyNum, name, author); // init
                collection.add(newItem); // append to ArrayList
            } else if (itemType == 'v') { // if Video
                char rating = catalog.readChar(); // classification
                Video newItem = new Video(catNum, copyNum, name, rating); // init
                collection.add(newItem); // append to ArrayList
            } else if (itemType == 'g') { // if Game
                char rating = catalog.readChar(); // classification
                char sys = catalog.readChar(); // system
                Game newItem = new Game(catNum, copyNum, name, rating, sys); // init
                collection.add(newItem); // append to ArrayList
            }
        }
        catalog.close(); // close the file
        customers = new ArrayList<>();
        while(true) {
            if (patrons.isEOF()) { break; } // if EoF, stop
            /* just like before, these are superclass fields and are present
             * in all subclasses */
            char customer_type = patrons.readChar(); // find type of customer
            int custId = patrons.readInt(); // customer ID
            String custName = patrons.readString(); // customer name
            int custAge = patrons.readInt(); // customer age
            if (customer_type == 'a') { // Adult
                Customer newCust = new Customer(custId, custName, custAge); // init
                customers.add(newCust); // append to AL
            } else if (customer_type == 'c') { // Child
                Child newCust = new Child(custId, custName, custAge); // init
                customers.add(newCust); // append to AL
            }
        }
        patrons.close(); // close the file
    }
    
    /* method to construct the main menu for the form */
    void mainMenu() {
        BasicForm theForm = new BasicForm(); // init form
        theForm.setTitle("St. Kitts Library"); // set title
        String[] formElements = {"Borrow", "Return", "Pay Fine", "Loaned",
                                 "Patron", "Items", "Quit"}; // menu options
        int choice = theForm.accept(formElements); // wait for input
        theForm.close(); // close form after input
        switch(choice) { // do something dependent on input
            case 0: borrowMenu(); break;
            case 1: returnMenu(); break;
            case 2: payMenu(); break;
            case 3: loanMenu(); break;
            case 4: patronMenu(); break;
            case 5: itemMenu(); break;
            case 6: break;
        }
    }
    
    /* method to generate borrow menu */
    void borrowMenu() {
        BasicForm theForm = new BasicForm();
        theForm.setTitle("St. Kitts Library - Borrow");
        String[] formElements = {"OK", "Cancel"};
        theForm.addTextField("patron_num", "Patron #", 10);
        theForm.addTextField("item_num", "Item #", 10);
        theForm.addTextField("copy_num", "Copy", 5);
        theForm.addTextField("date", "Date", 10);
        theForm.addTextArea("textarea", 10, 50);
        int choice = theForm.accept(formElements);
        switch(choice) {
            case 0: 
                /* generate message to send to console
                 * which is created in the ___do method,
                 * which does all the logic, this method is just for
                 * menu generation, the ___do method does the actual
                 * work. Print message to console in addition to the
                 * textarea */
                String message = borrowDo(theForm);
                theForm.writeString("textarea",message);
                System.out.println(message);
                theForm.accept("OK");
                /* below this it'll keep going down so don't need
                 * to call menu in this case, it'll do the 1 case too */
            case 1: 
                theForm.close(); // close form
                mainMenu(); break; // go back to main menu
        }
    }
    
    /* method which does all the work requested by
     * the borrow menu gen method; checks to see if a borrow is possible
     * and does the transaction if so.
     *
     * @return - the status message (pass/fail (and why it failed))
    */
    String borrowDo(BasicForm theForm) {
        // boolean flags to keep method moving if valid
        boolean custExists = false, itemExists = false;
        String result = ""; // result message seed
        Customer c = null; // just to keep it in scope after iteration
        for (Iterator<Customer> ic = customers.iterator(); ic.hasNext();) {
            /* holy hell this is ugly, but couldn't do it with a basic
             * foreach loop and maintain scope of c */
            c = ic.next();
            // if customer ID matches the one in the form
            if (c.getId() == theForm.readInt("patron_num")) {
                custExists = true; // mark flag as T
                break; // get out of loop
            }
        }
        // if customer has a balance, don't do transaction
        if (c.getBal() > 0.00) {
            result = "Error: Outstanding balance.";
        } else { // otherwise we can continue
            if (custExists) { // if customer was found by iteration
                for (Item i : collection) { // lovely aesthetic loop
                    // just as above, try to find the item
                    if ((i.getId() == theForm.readDouble("item_num"))
                    &&  (i.getCopy() == theForm.readInt("copy_num"))) {
                        if (i.checkAvail()) { // if loaned already
                            result = "Error: Item already rented.";
                            break;
                        }
                        // children can rent games
                        if(c instanceof Child && i instanceof Game) {
                            result = "Error: Children cannot rent games.";
                            break;
                        }
                        if(i instanceof Video || i instanceof Game) {
                            char tempClassification = ' '; // temp
                            /* this whole part is just to find classification
                             * if item is video or game. Is there a cleaner way
                             * to do this? I dunno, but it works */
                            if (i instanceof Video) { 
                                Video v = (Video)i; // temp Video obj
                                tempClassification = v.getRating();
                            } else { 
                                Game g = (Game)i;  // temp Game obj
                                tempClassification = g.getRating();
                            }
                            /* the below complex is for age checking based
                             * on customer age and item classification */
                            if (tempClassification == 'X' && c.getAge() < 21) {
                                result = "Error: Customer need be 21 to rent this.";
                                break;
                            } else if (tempClassification == 'R' && c.getAge() < 18) {
                                result = "Error: Customer need be 18 to rent this.";
                                break;
                            } else if (tempClassification == 'P' && c.getAge() < 14) {
                                result = "Error: Customer need be 14 to rent this.";
                                break;
                            }
                        }
                        // children cannot rent more than 5 items
                        if (c instanceof Child && c.getRents() >= 5) {
                            result = "Error: Customer cannot rent any more.";
                            break;
                        } else { // otherwise continue
                            itemExists = true; // item found to exist at this p.
                            // generate success message
                            result = "Item: " + theForm.readDouble("item_num") 
                                    + " loaned to " + c.getId() + ", due: " + 
                                    (int)(i.getPer() + theForm.readInt("date"));
                            i.toggleRent(); // make it loaned
                            i.setDR(theForm.readInt("date")); // set when loaned
                            c.addRental(); // customer rent++
                            break;
                        }
                    }
                    if (!itemExists) { // if item did not exist
                        result = "Error: Item does not exist.";
                    }
                }
            } else { // if customer doesn't exist
                result = "Error: Customer does not exist.";
            }
        }
        return result; // return the succ/fail message
    }
    
    /*******************************************************************
     * a lot of the below is similar enough in function to previous methods
     * please refer to those methods for details on how menu generation
     * methods function 
     * 
     * comments going forward will be more sparse than previously
     *******************************************************************/
    
    void returnMenu() {
        BasicForm theForm = new BasicForm();
        theForm.setTitle("St. Kitts Library - Return");
        String[] formElements = {"OK", "Cancel"};
        theForm.addTextField("patron_num", "Patron #", 10);
        theForm.addTextField("item_num", "Item #", 10);
        theForm.addTextField("copy_num", "Copy", 5);
        theForm.addTextField("date", "Date", 10);
        theForm.addTextArea("textarea", 10, 50);
        int choice = theForm.accept(formElements);
        switch(choice) {
            case 0: 
                String message = returnDo(theForm);
                theForm.writeString("textarea",message);
                System.out.println(message);
                theForm.accept("OK");
            case 1: 
                theForm.close();
                mainMenu(); break;
        }
    }
    
    String returnDo(BasicForm theForm) {
        boolean cust_exists = false, item_exists = false;
        String result = "";
        Customer c = null;
        for (Iterator<Customer> ic = customers.iterator(); ic.hasNext();) {
            c = ic.next();
            if (c.getId() == theForm.readInt("patron_num")) {
                cust_exists = true;
                break;
            }
        }
        if (cust_exists) {
            for (Item i : collection) {
                if ((i.getId() == theForm.readDouble("item_num"))
                &&  (i.getCopy() == theForm.readInt("copy_num"))) {
                    if (!i.checkAvail()) { // if item available
                        result = "Error: Item not currently rented.";
                    }
                    item_exists = true;
                    /* find the differences between rent date 
                     * and expected rent date */
                    double difference = theForm.readDouble("date") - 
                                        (i.getDR() + i.getPer());
                    // useless to consider a difference < 0; set a floor of 0
                    difference = (difference < 0) ? 0 : difference * i.getFee();
                    result = "Item: " + theForm.readDouble("item_num") 
                            + " returned on " + theForm.readInt("date") + 
                            " by " + c.getId() + ", fee: $" + difference;
                    i.toggleRent(); // make unrented
                    c.decRental(); // cust rentals--
                    c.setBal(c.getBal()+difference); // append fee to account
                    break;
                }
            }
            if (!item_exists) {
                result = "Error: Item does not exist.";
            }
        } else {
            result = "Error: Customer does not exist.";
        }
        return result;
    }
    
    void payMenu() {
        BasicForm theForm = new BasicForm();
        theForm.setTitle("St. Kitts Library - Pay Fine");
        String[] formElements = {"OK", "Cancel"};
        theForm.addTextField("patron_num", "Patron #", 10);
        theForm.addTextField("amount", "Amount", 10);
        theForm.addTextArea("textarea", 10, 50);
        int choice = theForm.accept(formElements);
        switch(choice) {
            case 0: 
                String message = payDo(theForm);
                theForm.writeString("textarea",message);
                System.out.println(message);
                theForm.accept("OK");
            case 1: 
                theForm.close();
                mainMenu(); break;
        }
    }
    
    String payDo(BasicForm theForm) {
        boolean cust_exists = false;
        String result = "";
        Customer c = null;
        for (Iterator<Customer> ic = customers.iterator(); ic.hasNext();) {
            c = ic.next();
            if (c.getId() == theForm.readInt("patron_num")) {
                cust_exists = true;
                break;
            }
        }
        if (cust_exists) {
            /* add to customers account the amount specified
             * its possible to have a neg- balance, maybe a customer
             * wants to accrue credit for later? */
            c.setBal(c.getBal() - theForm.readDouble("amount"));
            result = c.getId() + " paid " + theForm.readDouble("amount") +
                    " to their account, now " + c.getBal() + " due";
        } else {
            result = "Error: Customer does not exist.";
        }
        return result;
    }
    
    void loanMenu() {
        BasicForm theForm = new BasicForm();
        theForm.setTitle("St. Kitts Library - Pay Fine");
        String[] formElements = {"OK", "Cancel"};
        theForm.addTextField("patron_num", "Patron #", 10);
        theForm.addTextArea("textarea", 10, 50);
        int choice = theForm.accept(formElements);
        switch(choice) {
            case 0: 
                String message = loanDo(theForm);
                theForm.writeString("textarea",message);
                System.out.println(message);
                theForm.accept("OK");
            case 1: 
                theForm.close();
                mainMenu(); break;
        }        
    }
    
    String loanDo(BasicForm theForm) {
        boolean cust_exists = false;
        String result = "";
        Customer c = null;
        for (Iterator<Customer> ic = customers.iterator(); ic.hasNext();) {
            c = ic.next();
            if (c.getId() == theForm.readInt("patron_num")) {
                cust_exists = true;
                break;
            }
        }
        if (cust_exists) {
            result = c.getId() + " currently has " + c.getRents() +
                    " items on loan";
        } else {
            result = "Error: Customer does not exist.";
        }
        return result;
    }
    
    void patronMenu() {
        BasicForm theForm = new BasicForm();
        theForm.setTitle("St. Kitts Library - Pay Fine");
        String[] formElements = {"OK", "Cancel"};
        theForm.addTextField("patron_num", "Patron #", 10);
        theForm.addTextArea("textarea", 10, 50);
        int choice = theForm.accept(formElements);
        switch(choice) {
            case 0: 
                String message = patronDo(theForm);
                theForm.writeString("textarea",message);
                System.out.println(message);
                theForm.accept("OK");
            case 1: 
                theForm.close();
                mainMenu(); break;
        }      
    }
    
    String patronDo(BasicForm theForm) {
        boolean cust_exists = false;
        String result = "";
        Customer c = null;
        for (Iterator<Customer> ic = customers.iterator(); ic.hasNext();) {
            c = ic.next();
            if (c.getId() == theForm.readInt("patron_num")) {
                cust_exists = true;
                break;
            }
        }
        if (cust_exists) {
            result = c.getId() + ": " + c.getName() +
                    ", " + c.getAge() + "y/o. " + c.getRents() +
                    " items on loan, current balance: $" + c.getBal();
        } else {
            result = "Error: Customer does not exist.";
        }
        return result;
    }
    
    void itemMenu() {
        BasicForm theForm = new BasicForm();
        theForm.setTitle("St. Kitts Library - Pay Fine");
        String[] formElements = {"OK", "Cancel"};
        theForm.addTextField("item_num", "Item #", 10);
        theForm.addTextField("copy_num", "Copy #", 10);
        theForm.addTextArea("textarea", 10, 50);
        int choice = theForm.accept(formElements);
        switch(choice) {
            case 0: 
                String message = itemDo(theForm);
                theForm.writeString("textarea",message);
                System.out.println(message);
                theForm.accept("OK");
            case 1: 
                theForm.close();
                mainMenu(); break;
        }      
    }
    
    String itemDo(BasicForm theForm) {
        boolean item_exists = false;
        String result = "";
        Item i = null;
        for (Iterator<Item> ii = collection.iterator(); ii.hasNext();) {
            i = ii.next();
            if ((i.getId() == theForm.readDouble("item_num")
            &&  (i.getCopy() == theForm.readInt("copy_num")))) {
                item_exists = true;
                break;
            }
        }
        if (item_exists) {
            String append = ""; // you'll see in a moment
            String rentStatus = (i.checkAvail()) ? "on loan" : "available";
            if (i instanceof Book) {
                Book b = (Book)i;
                // append author for specific case where subtype is Book
                append = ", author: " + b.getAuthor();
            } else if (i instanceof Video) {
                Video v = (Video)i;
                // append rating for specific case where subtype is Video
                append = ", rating: " + v.getRating();
            } else if (i instanceof Game) {
                Game g = (Game)i;
                /* append rating and system for specific case where
                 * subtype is Game */
                append = ", rating: " + g.getRating() + ", system: " + g.getSystem();
            }
            result = i.getId() + ": " + i.getName() +
                    append + ", status: " + rentStatus;
        } else {
            result = "Error: Customer does not exist.";
        }
        return result;
    }

    public static void main(String[] args) {
        Assign_1 a = new Assign_1();
    }    
}
