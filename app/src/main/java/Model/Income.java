package Model;

/**
 * Created by isaarikan on 18.04.2017.
 */



public class Income {
    public Income(){ }
    public Income(String name, String type, int miktar) {
        this.name = name;
        this.type = type;
        this.miktar = miktar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMiktar() {
        return miktar;
    }

    public void setMiktar(int miktar) {
        this.miktar = miktar;
    }

    String name,type;
    int miktar;

}
