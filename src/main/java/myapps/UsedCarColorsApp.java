package myapps;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.common.utils.Bytes;

import myapps.*;


public class UsedCarColorsApp 
{
    public static void main(String[] args)
    {
        System.out.println("Starting up used-car-colors-app.");
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "used-car-colors-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put("auto.offset.reset", "earliest");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        

        StreamsBuilder builder = new StreamsBuilder();
        

        KStream<byte[], UsedCars> usedCarsInputStream = 
            builder.stream("used-car-colors", Consumed.with(Serdes.ByteArray(), new UsedCarsSerdes()));

            System.out.println(usedCarsInputStream.toString());
   
            
            
            
            //k, v => year, countof cars in year
            KTable<String,Long> yearCount = usedCarsInputStream
                .filter((k,v)->v.getYear() > 2010)
                .selectKey((k,v) -> v.getVin())
                .groupBy((key, value) -> Integer.toString(value.getYear()))
                .count();

            

            KTable<String,Long> colorCount = usedCarsInputStream
                .filter((k,v)->v.getYear() > 2010)
                .selectKey((k,v) -> v.getVin())
                .groupBy((key, value) -> value.getColor())
                .count();

            usedCarsInputStream
                .filter((k,v)->v.getYear() > 2010)
                .selectKey((k,v) -> new YearColor(v.getYear(), v.getColor()))
                .groupByKey(Grouped.with(new YearColorSerdes(), new UsedCarsSerdes()))
                .count()
                .toStream()
                .peek((yc, ct) -> System.out.println("year: " + yc.getYear() + " color: " + yc.getColor() 
                + " count: " + ct));

            
                
                // .foreach((year,count)->{

                //     usedCarsInputStream
                //         .filter((k,car)->Integer.toString(car.getYear())==year)
                //         .selectKey((k,car) -> car.getColor())
                //         .groupBy((k,car)->car.getColor())
                //         .count()
                //         .toStream()
                //         .print(Printed.<String, Long>toSysOut().withLabel(year.toString()));
                    
                // });


        List<String> years = new ArrayList<String>();
        yearCount.toStream().foreach((y, c)->{years.add(y.toString());});

            yearCount.toStream().print(Printed.<String, Long>toSysOut().withLabel("years"));
            colorCount.toStream().print(Printed.<String, Long>toSysOut().withLabel("colors"));

            





            
                    // usedCarsInputStream
                    //     .filter((k,car)->Integer.toString(car.getYear())==y)
                    //     .selectKey((k,car) -> car.getColor())
                    //     .groupBy((k,car)->car.getColor())
                    //     .count()
                    //     .toStream()
                    //     .print(Printed.<String, Long>toSysOut().withLabel(y.toString()));

            

               
            System.out.println("done");
            
            // usedCarsInputStream
            //     .filter((k,v)->v.getYear() > 2005)
            //     // Set key to title and value to ticket value
            //     .map((k, v) -> new KeyValue<>((String) v.getYear(), v.getTicketTotalValue()))
            //     // Group by title
            //     .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
            //     // Apply SUM aggregation
            //     .reduce(Integer::sum)
            //     // Write to stream specified by outputTopic
            //     .toStream()
                
                

            //add to the used-car-colors-output topic
            //yearCount.toStream().to("used-car-colors-output",  Produced.with(Serdes.String(), Serdes.Long())); 
            //print to stdout as well
      //yearCount.print(Printed.<String, Long>toSysOut().withLabel("yearcount")); 


        final Topology topology = builder.build();
        KafkaStreams streams = new KafkaStreams(topology, props);
        

        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.cleanUp();
            streams.start();
            System.out.println(streams.toString());
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);

    }    
    private static JsonNode newCount(JsonNode transaction, JsonNode balance) {
        // create a new balance json object
        ObjectNode newCount = JsonNodeFactory.instance.objectNode();
        newCount.put("count", balance.get("count").asInt() + 1);
        newCount.put("balance", balance.get("balance").asInt() + transaction.get("amount").asInt());


        return newCount;
    }
}
