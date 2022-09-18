package translate.vaporm;


public class RTPair {
    Register register;
    String temporary;

    public RTPair(String name, Register register) {
        this.register = register;
        this.temporary = name;
    }
}
