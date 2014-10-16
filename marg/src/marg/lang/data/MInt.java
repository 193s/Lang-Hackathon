package marg.lang.data;

public class MInt implements IType {
    public MInt(){}
    public MInt(int value) {
        set(value);
    }

    private int value;

    @Override
    public void set(Object value) {
        this.value = (Integer) value;
    }

    @Override
    public Integer get() {
        return value;
    }
}
