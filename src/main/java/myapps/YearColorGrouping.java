package myapps;
import com.google.gson.annotations.SerializedName;

public class YearColorGrouping extends YearColor {

    @SerializedName("count")
    private Long count;
    

    public  YearColorGrouping(){}
    public YearColorGrouping(Integer year, String color, Long count)
    {
        super(year, color);
        this.count = count;
    }
    public Long getCount(){
        return this.count;
    }
    
    public void setCount(Long count)
    {
        this.count = count;
    }
    

    
}
