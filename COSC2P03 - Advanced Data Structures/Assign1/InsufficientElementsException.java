
package everybodyintothepool;

class InsufficientElementsException extends RuntimeException {

    public InsufficientElementsException() {
        super("There are no elements which fit the criteria for that method.");
    }    
}