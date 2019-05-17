package BigNumbers;

public interface BNum {
    public long[] grabVal();
    public int getSign();
    public BNum clone();
    public boolean isEquals ( BNum n);
    public boolean lessThan (BNum n);
    public BNum add (BNum n);
    public BNum sub (BNum n);
    public int getDigit(int i);
}