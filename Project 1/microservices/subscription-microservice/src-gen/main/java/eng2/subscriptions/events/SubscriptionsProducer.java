package eng2.subscriptions.events;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;


@KafkaClient
public interface SubscriptionsProducer {
	
	@Topic("subscribe-hashtag")
	public void subscribe(@KafkaKey User user, Hashtag hashtag);

	@Topic("unsubscribe-hashtag")
	public void unsubscribe(@KafkaKey User user, Hashtag hashtag);

}
