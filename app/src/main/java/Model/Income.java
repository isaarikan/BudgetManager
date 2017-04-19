package Model;

/**
 * Created by isaarikan on 18.04.2017.
 */



public class Income {
   private String name,type;
  private  String miktar;
    public Income(){ }
    public Income(String name, String type, String miktar) {
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

    public String getMiktar() {
        return miktar;
    }

    public void setMiktar(String miktar) {
        this.miktar = miktar;
    }



}
