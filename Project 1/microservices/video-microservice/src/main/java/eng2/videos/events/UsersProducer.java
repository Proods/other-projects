package eng2.videos.events;

import eng2.videos.domain.User;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface UsersProducer {
	
	@Topic("create-user")
	void createUser(@KafkaKey Long id, User user);
	
	@Topic("delete-user")
	void deleteUser(@KafkaKey Long id, User user);
	
}
