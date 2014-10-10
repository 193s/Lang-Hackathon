package marg.lang.type;

public class MNull implements IType {
    @Override
    public void set(Object o) {
        throw new NullPointerException();
    }

    @Override
    public Object get() {
        return null;
    }
}
