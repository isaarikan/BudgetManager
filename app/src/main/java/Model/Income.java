package Model;




public class Income {
   private String name,type;
  private  String miktar;
 private   String id;
 private   String date;



    public Income(){ }



    public Income(String name,String miktar, String type, String id,String date) {
        this.date=date;
        this.name = name;
        this.type = type;
        this.miktar = miktar;
        this.id=id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
