package myapps;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.Serde;

public class YearColorSerdes implements Serde<YearColor> {

    @Override
    public Serializer<YearColor> serializer(){
        return new YearColorSerializer();
    }

    @Override
    public Deserializer<YearColor> deserializer()
    {
        return new YearColorDeserializer();
    }
    
}
