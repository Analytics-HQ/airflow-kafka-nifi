package myapps;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;




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
        

        System.out.println("This is: " + props.getProperty(StreamsConfig.APPLICATION_ID_CONFIG));

        StreamsBuilder builder = new StreamsBuilder();
        

        KStream<byte[], UsedCars> usedCarsInputStream = 
            builder.stream("used-car-colors", Consumed.with(Serdes.ByteArray(), new UsedCarsSerdes()));
            
            //k, v => year, countof cars in year
            KTable<String,Long> yearCount = usedCarsInputStream
                //.filter((k,v)->v.getYear() >= 2010)
                .selectKey((k,v) -> v.getVin())
                .groupBy((vin, usedcar) -> Integer.toString(usedcar.getYear()))
                .count();

            //output to new topic
            yearCount
                .toStream()
                .map((k,v)->new KeyValue<>(k, v.toString()))
                .to("used-car-colors-totals-year", Produced.with(Serdes.String(), Serdes.String()));
                
            //optionally print
            // yearCount
            //     .toStream()
            //     .print(Printed.<String, Long>toSysOut().withLabel("years"));
                    

            KTable<String,Long> colorCount = usedCarsInputStream
                //.filter((k,v)->v.getYear() >= 2010)
                .selectKey((k,v) -> v.getVin())
                .groupBy((vin, usedcar) -> usedcar.getColor())
                .count();

            //output to new topic
            colorCount
                .toStream()
                .map((k,v)->new KeyValue<>(k, v.toString()))
                .to("used-car-colors-totals-color", Produced.with(Serdes.String(), Serdes.String())); 
            
            //optionally print
            // yearCount
            //     .toStream()
            //     .print(Printed.<String, Long>toSysOut().withLabel("years"));

            KTable<YearColor,Long> yearColorGroupCount = usedCarsInputStream
                //.filter((k,v)->v.getYear() >= 2010)
                .selectKey((k,v) -> new YearColor(v.getYear(), v.getColor()))
                .groupByKey(Grouped.with(new YearColorSerdes(), new UsedCarsSerdes()))
                .count();
            
            yearColorGroupCount
                .toStream()
                .mapValues((k,v)->new YearColorGrouping(k.getYear(), k.getColor(), v))
                .to("used-car-colors-yearcolor-group", Produced.with(new YearColorSerdes(),new YearColorGroupingSerdes()));
                

            //print if you want
            // yearColorGroupCount
            //     .toStream()
            //     .peek((yc, ct) -> System.out.println("year: " + yc.getYear() + " color: " + yc.getColor()));
                

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
    
}
