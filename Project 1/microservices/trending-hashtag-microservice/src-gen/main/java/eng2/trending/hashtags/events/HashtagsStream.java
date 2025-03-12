package eng2.trending.hashtags.events;

import java.util.Properties;

import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;


@Factory
public class HashtagsStream {
	
	private SerdeRegistry serdeRegistry;

	
	@Singleton
	public KStream<Hashtag,Integer> trendingHashtag(ConfiguredStreamBuilder builder){
		
	}

}
