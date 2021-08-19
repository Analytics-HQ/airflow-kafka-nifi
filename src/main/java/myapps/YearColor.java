package myapps;
import com.google.gson.annotations.SerializedName;

public class YearColor {

    @SerializedName("year")
    private Integer year;
    @SerializedName("color")
    private String color;

    public  YearColor(){}
    public YearColor(Integer year, String color)
    {
        this.year = year;
        this.color = color;
    }
    public Integer getYear(){
        return this.year;
    }
    public String getColor()
    {
        return this.color;
    }
    public void setYear(Integer year)
    {
        this.year = year;
    }
    public void setColor(String color)
    {
        this.color = color;
    }

    
}
