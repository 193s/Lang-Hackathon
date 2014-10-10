package marg.lang.type;

public class MBool implements IType {
    public MBool() {}
    public MBool(boolean bool) {
        set(bool);
    }

    public static MBool TRUE = new MBool(true);
    public static MBool FALSE = new MBool(false);

    private boolean value;

    @Override
    public void set(Object bool) {
        value = (Boolean) bool;
    }

    @Override
    public Boolean get() {
        return value;
    }
}
