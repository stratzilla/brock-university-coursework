package everybodyintothepool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MacguffinConcreteList<E> implements MacguffinList<E> {

    private final LinkedList<LinkedList<E>> groups = new LinkedList<>();

    @Override
    public void addSingle(E item) { // O(1)
        LinkedList<E> occupants = new LinkedList<>(); //create LL for people
        occupants.add(item); //insert person(s) into LL
        groups.add(occupants); //insert LL of people into LL of groups
    }

    @Override
    public void addPair(E first, E second) { // O(1)
        LinkedList<E> occupants = new LinkedList<>(); //create LL for people
        occupants.add(first); //insert person(s) into LL
        occupants.add(second);
        groups.add(occupants); //insert LL of people into LL of groups            
    }

    @Override
    public void addTriple(E first, E second, E third) { // O(1)
        LinkedList<E> occupants = new LinkedList<>(); //create LL for people
        occupants.add(first); //insert person(s) into LL
        occupants.add(second);
        occupants.add(third);
        groups.add(occupants); //insert LL of people into LL of groups   
    }

    @Override
    public void addQuartet(E first, E second, E third, E fourth) { // O(1)
        LinkedList<E> occupants = new LinkedList<>(); //create LL for people
        occupants.add(first); //insert person(s) into LL
        occupants.add(second);
        occupants.add(third);
        occupants.add(fourth);
        groups.add(occupants); //insert LL of people into LL of groups   
    }

    @Override
    public E removeSingle() {
        /* constructor chained so other methods 
        can utilize this one at a pointed spot (ptr) */
        return this.removeSingle(0);
    }
    
    public E removeSingle(int ptr) { // O(n)
        //search for sublist of size 1
        for (int i = ptr; i < this.groups.size(); i++){
            if (this.groups.get(i).size() == 1) { //if found
                E passthrough = this.groups.get(i).get(0); //initialize temp
                this.groups.remove(i); //remove element
                return passthrough; //return element
            }
        }
        //if cannot find, throw exception
        throw new InsufficientElementsException();
    }

    @Override
    public List<E> removePair() {
        //constructor chaining
        return removePair(0);
    }
    
    public List<E> removePair(int ptr) { // O(n^2)
        //search for sublist of size 2
        for (int i = ptr; i < this.groups.size(); i++){
            if (this.groups.get(i).size() == 2) { //if found
                List<E> passthrough = this.groups.get(i); //initialize temp
                this.groups.remove(i); //remove sublist
                return passthrough; //return sublist
            } else if (this.groups.get(i).size() == 1) {
                try {
                    /* I couldn't figure out why I could not just do
                       List<E> passthrough = new List<>(); as it kept popping
                       errors on NetBeans. I've instead upcasted it to ArrayList
                       which as far as I can tell is identical functionality. */
                    List<E> passthrough = new ArrayList<>(); //temp
                    for (int j = 0; j < 2; j++) { //look for two singles
                        passthrough.add(this.removeSingle()); //append to temp               
                    }
                    return passthrough; //return new sublist
                } catch (InsufficientElementsException e) {
                    /* this does nothing because I want to prevent throwing
                       until it tries all combinations. It doesn't make
                       a difference, I could throw here, but why not? */
                }
            }
        }
        //if cannot find, throw exception
        throw new InsufficientElementsException();
    }

    @Override
    public List<E> removeTriple() {
        //constructor chaining
        return removeTriple(0);
    }
    
    public List<E> removeTriple(int ptr) { // O(n^3)
        //search for sublist of size 3
        for (int i = ptr; i < this.groups.size(); i++){
            if (this.groups.get(i).size() == 3) { //if found
                List<E> passthrough = this.groups.get(i); //initialize temp
                this.groups.remove(i); //remove sublist
                return passthrough; //return sublist
            //if not sublist of size 3, try of size 2
            } else if (this.groups.get(i).size() == 2) { //if found
                try {
                    List<E> passthrough = groups.get(i); //temp
                    passthrough.add(removeSingle(i+1)); //append to temp list               
                    return passthrough; //return new sublist
                } catch (InsufficientElementsException e) {
                    //do nothing
                }
            //if not sublist of size 2, try of size 1
            } else if (this.groups.get(i).size() == 1) { //if found
                try {
                    List<E> passthrough = groups.get(i); //temp
                    passthrough.addAll(removePair(i+1)); //append to temp list
                    return passthrough; //return new sublist
                } catch (InsufficientElementsException e) {
                    //do nothing
                }
            }
        }
        //if cannot find, throw exception
        throw new InsufficientElementsException();
    }

    @Override
    public List<E> removeQuartet() {
        //constructor chaining
        return removeQuartet(0);
    }
    
    public List<E> removeQuartet(int ptr) { // O(n^4)
        //search for sublist of size 4
        for (int i = ptr; i < this.groups.size(); i++){
            if (this.groups.get(i).size() == 4) { //if found
                List<E> passthrough = this.groups.get(i); //initialize temp
                this.groups.remove(i); //remove sublist
                return passthrough; //return new sublist
            } else if (this.groups.get(i).size() == 3) { //if found
                try {
                    List<E> passthrough = groups.get(i); //temp
                    passthrough.add(removeSingle(i+1)); //append to temp list
                    return passthrough; //return new sublist
                } catch (InsufficientElementsException e) {
                    //do nothing
                }
            } else if (this.groups.get(i).size() == 2) {
                try {
                    List<E> passthrough = groups.get(i); //temp
                    passthrough.addAll(removePair(i+1)); //append to temp list             
                    return passthrough; //return new sublist
                } catch (InsufficientElementsException e) {
                    //do nothing
                }
            } else if (this.groups.get(i).size() == 1) {
                try {
                    List<E> passthrough = groups.get(i); //temp
                    passthrough.addAll(removeTriple(i+1)); //append to temp list
                    return passthrough; //return new sublist
                } catch (InsufficientElementsException e) {
                    //do nothing
                }
            }
        }
        //if cannot find, throw exception
        throw new InsufficientElementsException();
    }
    
    @Override
    public boolean hasSingle() {
        //constructor chaining
        return hasSingle(0);
    }
    
    public boolean hasSingle(int ptr) { // O(n)
        for (int i = ptr; i < this.groups.size(); i++) { //check if group = 1
            if (this.groups.get(i).size() == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasPair() {
        //constructor chaining
        return hasPair(0);
    }
    
    public boolean hasPair(int ptr) { // O(n^2)
        for (int i = ptr; i < this.groups.size(); i++) { //check if group = 2
            if (this.groups.get(i).size() == 2) {
                return true;
            //if not, try 1+1
            } else if (this.groups.get(i).size() == 1 && hasSingle(i+1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasTriple() {
        //constructor chaining
        return hasTriple(0);
    }
    
    public boolean hasTriple(int ptr) { // O(n^3)
        for (int i = ptr; i < this.groups.size(); i++) { //check if group = 3
            if (this.groups.get(i).size() == 3) {
                return true;
            //if not, try 2+1
            } else if (this.groups.get(i).size() == 2 && hasSingle(i+1)) {
                return true;
            //if not, try 1+2
            } else if (this.groups.get(i).size() == 1 && hasPair(i+1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasQuartet() {
        //constructor chaining
        return hasQuartet(0);
    }
    
    public boolean hasQuartet(int ptr) { // O(n^4)
        for (int i = ptr; i < this.groups.size(); i++) { //check if group = 4
            if (this.groups.get(i).size() == 4) {
                return true;
            //if not, try 3+1
            } else if (this.groups.size() == 3 && hasSingle(i+1)) {
                return true;
            //if not, try 2+2
            } else if (this.groups.size() == 2 && hasPair(i+1)) {
                return true;
            //if not, try 1+3
            } else if (this.groups.size() == 1 && hasTriple(i+1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int count() { // O(n)
        int sum = 0;
        for (int i = 0; i < this.groups.size(); i++) {
            sum += this.groups.get(i).size();
        }
        return sum; //returns sum of people waiting (not groups)
    }
}