package lowlevel;

/**
 * Created by Jonathan on 4/26/2016.
 */
public class Symbol {
    public enum Location {
        Register, Memory
    }
    private Location loc;
    private int RegNum;
    private int MemOffset;

    public Symbol(Location loc, int regNumOrOffset) {
        this.loc = loc;
        switch(loc) {
            case Register:
                RegNum = regNumOrOffset;
                break;
            case Memory:
                MemOffset = regNumOrOffset;
                break;
        }
    }

    // accessor methods
    public Location getLoc() {
        return loc;
    }

    public int getRegNum() {
        return RegNum;
    }

    public int getMemOffset() {
        return MemOffset;
    }
}
