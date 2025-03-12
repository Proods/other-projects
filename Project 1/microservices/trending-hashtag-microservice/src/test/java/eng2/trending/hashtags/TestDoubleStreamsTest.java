package eng2.trending.hashtags;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.Test;

import eng2.trending.hashtags.domain.Hashtag;
import eng2.trending.hashtags.events.HashtagsStream;
import io.micronaut.configuration.kafka.serde.SerdeRegistry;
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;


@MicronautTest(environments="no_streams")
public class TestDoubleStreamsTest {

	@Inject
	private SerdeRegistry serdeRegistry;

	@Inject
	private HashtagsStream streams;

	
	@Test
	public void topologyCheck() {
		
		final ConfiguredStreamBuilder builder = new ConfiguredStreamBuilder(new Properties());
		streams.trendingHashtag(builder);
		
		try (TopologyTestDriver testDriver = new TopologyTestDriver(builder.build())) {
			
			TestInputTopic<Hashtag, Integer> inputTopic = testDriver.createInputTopic("top-liked-hashtags", serdeRegistry.getSerializer(Hashtag.class), new IntegerSerializer());

			long id = 14;
			Hashtag hashtag = new Hashtag();
			hashtag.setId(id);
			hashtag.setName("#Hashtag" + id);
						
			inputTopic.pipeInput(hashtag, 1);
			inputTopic.pipeInput(hashtag, 1);

			TestOutputTopic<Hashtag, Integer> outputTopic = testDriver.createOutputTopic("trending", serdeRegistry.getDeserializer(Hashtag.class), new IntegerDeserializer());

			List<KeyValue<Hashtag, Integer>> keyValues = outputTopic.readKeyValuesToList();
			assertFalse(keyValues.isEmpty());
			
			KeyValue<Hashtag, Integer> firstKeyValue = keyValues.get(0);
			assertEquals(id, firstKeyValue.key.getId());
			assertEquals(1, firstKeyValue.value);

			KeyValue<Hashtag, Integer> lastKeyValue = keyValues.get(keyValues.size() - 1);
			assertEquals(id, lastKeyValue.key.getId());
			assertEquals(2, lastKeyValue.value);
		}
	}
	
}
