
/* my implementation of iterable did not work for either arrays or lists, so
   in the instance of needed to iterate, I iterated with a typical for-loop
   rather than a foreach loop. Even when overriding, I could not iterate by
   by a given index, so I left it as is.

   I wanted to do sorted insertion into arrays, but I couldn't figure it out.
   I took the code 1:1 from lab (which did work), but it didn't seem to work
   here. Instead, I made a bubble sort algorithm that is called whenever an
   object is added to the Bag. It's the same end result, I just wish I could
   have saved some lines and did insertion-sort.
*/

package assign5;
import MULTISET.*;

public class Assign5 {
    
    public Assign5 () {
        testHarnessBag(); //testing implementation of Bag
        testHarnessSet(); //testing implementation of Set
        System.out.println();
    }
    
    //both test harnesses should be obvious enough so I've little comments
    
    public final void testHarnessBag() {
        System.out.println("\nThe below are for Bag implementation of MultiSet.\n");
        MultiSet aBag = new Bag(); //bags
        MultiSet bBag = new Bag(); //bags
        Keyed cVal = new Value("a"); //value
        MultiSet dBag = new Bag(); //bags
        MultiSet fBag = new Bag(); //bags
        String[] inputArr = {"a","b","c","a","d","a","c"}; //for insertion
        for (String in : inputArr) {
            aBag.add(new Value(in)); //insertion
        }
        String[] inputBarr = {"c","f","c","d"}; //for insertion
        for (String in : inputBarr) {
            dBag.add(new Value(in)); //insertion
        }
        String[] inputCarr = {"c","f","c","d"}; //for insertion
        for (String in : inputCarr) { //insertion
            fBag.add(new Value(in));
        }
        MultiSet eBag = aBag.union(dBag); //union
        MultiSet gBag = aBag.intersection(dBag); //intersect
        System.out.println("aBag = "+aBag.printSet());
        System.out.println("bBag = "+bBag.printSet());
        System.out.println("dBag = "+dBag.printSet());
        System.out.println("fBag = "+fBag.printSet());
        System.out.println("Cardinality of aBag: "+aBag.cardinality());
        System.out.println("Multiplicity of 'a' in aBag: "+aBag.multiplicity(cVal));  
        System.out.println("Checking equality between dBag and fBag: "+dBag.equal(fBag));
        System.out.println("Checking on empty bBag: "+bBag.isEmpty());
        System.out.println("Union of aBag and dBag: "+eBag.printSet());
        System.out.println("Intersection of aBag and dBag: "+gBag.printSet());
    }
    
    public final void testHarnessSet() {
        System.out.println("\nThe below are for Set implementation of MultiSet.\n");
        MultiSet aSet = new Set(); //sets
        MultiSet bSet = new Set(); //sets
        Keyed cVal = new Value("z"); //value
        MultiSet dSet = new Set(); //sets
        MultiSet fSet = new Set(); //sets
        String[] inputArr = {"v","x","y","w","w","z","x","y","y","z","w"}; //for insertion
        for (String in : inputArr) {
            aSet.add(new Value(in)); //insertion
        }
        String[] inputBarr = {"z","z","x","w","y","t","u"}; //for insertion
        for (String in : inputBarr) {
            dSet.add(new Value(in)); //insertion
        }
        String[] inputCarr = {"z","z","x","w","y","t","u"}; //for insertion
        for (String in : inputCarr) { //insertion
            fSet.add(new Value(in));
        }
        MultiSet eSet = aSet.union(dSet); //union
        MultiSet gSet = aSet.intersection(dSet); //intersect
        System.out.println("aSet = "+aSet.printSet());
        System.out.println("bSet = "+bSet.printSet());
        System.out.println("dSet = "+dSet.printSet());
        System.out.println("fSet = "+fSet.printSet());
        System.out.println("Cardinality of aSet: "+aSet.cardinality());
        System.out.println("Multiplicity of 'z' in aSet: "+aSet.multiplicity(cVal));  
        System.out.println("Checking equality between dSet and fSet: "+dSet.equal(fSet));
        System.out.println("Checking on empty bSet: "+bSet.isEmpty());
        System.out.println("Union of aSet and dSet: "+eSet.printSet());
        System.out.println("Intersection of aSet and dSet: "+gSet.printSet());        
    }
    
    public static void main(String[] args) {
        Assign5 a = new Assign5();
    }    
}