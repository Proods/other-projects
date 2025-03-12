package eng2.trending.hashtags.events;

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;

import eng2.trending.hashtags.domain.Hashtag;
import io.micronaut.configuration.kafka.serde.SerdeRegistry;
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;


@Factory
public class HashtagsStream {
	
	@Inject
	private SerdeRegistry serdeRegistry;
	
	@Singleton
	public KStream<Hashtag,Integer> trendingHashtag(ConfiguredStreamBuilder builder){
		
		Properties props = builder.getConfiguration();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "top-hashtags");
//		props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
		
		KStream<Hashtag,Integer> hashtagStream = builder.stream("top-liked-hashtags", Consumed.with(serdeRegistry.getSerde(Hashtag.class), Serdes.Integer()));
		
		KStream<Hashtag,Integer> stream = hashtagStream.groupByKey()
				.windowedBy(TimeWindows.of(Duration.ofHours(1)).advanceBy(Duration.ofHours(1)))
				.reduce((aggValue, newValue) -> aggValue + newValue)
				.toStream()
				.selectKey((key,value) -> key.key());
		
		stream.to("trending", Produced.with(serdeRegistry.getSerde(Hashtag.class), Serdes.Integer()));
		
		return stream;
	}
	
}
