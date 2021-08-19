package myapps;

import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

public class YearColorSerializer implements Serializer<YearColor>{

    private Gson gson = new Gson();
    @Override
    public byte[] serialize(String nullKey, YearColor uc)
    {
        if(uc == null) return null;
        return gson.toJson(uc).getBytes(StandardCharsets.UTF_8);

    } 
    
}
