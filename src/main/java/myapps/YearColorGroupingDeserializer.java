package myapps;

import java.nio.charset.StandardCharsets;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.kafka.common.serialization.Deserializer;

public class YearColorGroupingDeserializer implements Deserializer<YearColorGrouping>
{

    private Gson gson = 
        new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .create();
    
    @Override
    public YearColorGrouping deserialize(String nullKey, byte[] bytes)
    {
        if(bytes == null) return null;
        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), YearColorGrouping.class);

    }


}
    

