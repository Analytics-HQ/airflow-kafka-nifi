package myapps;
import com.google.gson.annotations.SerializedName;
//{'vin': 'SCFEFBBC8AG265167', 
// 'make': 'GMC', 
// 'model': 'Sonoma', 
// 'year': 1997, 
// 'color': 'Pink', 
// 'sale_price': '$32064.89', 
// 'city': 'Shawnee Mission', 
// 'state': 'Kansas', 
// 'zip_code': '66276'}
public class UsedCars {
    
    @SerializedName("vin")
    private String vin;
    @SerializedName("make")
    private String make;
    @SerializedName("model")
    private String model;
    @SerializedName("year")
    private Integer year;
    @SerializedName("color")
    private String color;
    //@SerializedName("salePrice")
    private float salePrice;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    //@SerializedName("zipCode")
    private Integer zipCode;

    public UsedCars(){}
    public UsedCars(String vin, String make, String model, Integer year, String color, float salePrice, String city, String state, Integer zipCode)
    {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.salePrice = salePrice;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String getVin() {
        return this.vin;
    }
    public String getMake() {
        return this.make;
    }
    public String getModel() {
        return this.model;
    }
    public int getYear() {
        return this.year;
    }
    public String getColor() {
        return this.color;
    }
    public float getSalePrice() {
        return this.salePrice;
    }
    public String getCity() {
        return this.city;
    }
    public String getState() {
        return this.state;
    }
    public int getZipCode() {
        return this.zipCode;
    }
    public void setVin(String vin) {
        this.vin = vin;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

}
