package myapps;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.Serde;

public class UsedCarsSerdes implements Serde<UsedCars> {

    @Override
    public Serializer<UsedCars> serializer(){
        return new UsedCarsSerializer();
    }

    @Override
    public Deserializer<UsedCars> deserializer()
    {
        return new UsedCarsDeserializer();
    }
    
}
