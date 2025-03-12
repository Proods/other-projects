package eng2.subscriptions.events;

import eng2.subscriptions.domain.Hashtag;
import eng2.subscriptions.domain.User;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface SubscriptionsProducer {
	
	@Topic("subscribe-hashtag")
	void subscribe(@KafkaKey User user, Hashtag hashtag);
	
	@Topic("unsubscribe-hashtag")
	void unsubscribe(@KafkaKey User user, Hashtag hashtag);

}
