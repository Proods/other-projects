package eng2.videos.events;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;


@KafkaClient
public interface UsersProducer {
	
	@Topic("create-user")
	public void createUser(@KafkaKey Long id, User user);

	@Topic("delete-user")
	public void deleteUser(@KafkaKey Long id, User user);

}
