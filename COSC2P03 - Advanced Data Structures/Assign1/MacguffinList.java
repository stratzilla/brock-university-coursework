
package everybodyintothepool;

import java.util.List;

public interface MacguffinList<E> {
    public void addSingle(E item);
    public void addPair(E first, E second);
    public void addTriple(E first, E second, E third);
    public void addQuartet(E first, E second, E third, E fourth);
    public E removeSingle();
    public List<E> removePair();
    public List<E> removeTriple();
    public List<E> removeQuartet();
    public boolean hasSingle();
    public boolean hasPair();
    public boolean hasTriple();
    public boolean hasQuartet();
    public int count(); //Total # of items waiting (NOT # of groups!)
}