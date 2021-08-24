package myapps;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.Serde;

public class YearColorGroupingSerdes implements Serde<YearColorGrouping> {

    @Override
    public Serializer<YearColorGrouping> serializer()
    {
        return new YearColorGroupingSerializer();
    }

    @Override
    public Deserializer<YearColorGrouping> deserializer()
    {
        return new YearColorGroupingDeserializer();
    }
    
}
