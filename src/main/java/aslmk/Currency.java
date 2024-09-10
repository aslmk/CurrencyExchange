package aslmk;

public class Currency {
    private int id;
    private String code;
    private String fullName;
    private String sign;

    public Currency(int id, String code,String sign, String fullName) {
        this.id = id;
        this.code = code;
        this.sign = sign;
        this.fullName = fullName;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id='"+ id +'\'' +
                ", code='" + code + '\'' +
                ", fullName='" + fullName + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
