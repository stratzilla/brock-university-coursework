package MULTISET;

import MULTISET.Bag.NotValidTypeException;

public class Value implements Keyed {

    private String value;

    public Value() {
        throw new NotValidTypeException();
    }

    public Value(String s) {
        this.value = s;
    }

    @Override
    public String getKey() {
        return this.value;
    }        
}