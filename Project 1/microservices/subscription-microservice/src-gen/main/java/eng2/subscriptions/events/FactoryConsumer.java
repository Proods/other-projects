package eng2.subscriptions.events;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;


@KafkaListener(groupId="factory")
public class FactoryConsumer {
	
	UsersRepository usersRepo;

	VideosRepository videosRepo;

	HashtagsRepository hashtagsRepo;


	@Topic("create-user")
	public void createUser(@KafkaKey Long id, User user) {

	}

	@Topic("delete-user")
	public void deleteUser(@KafkaKey Long id, User user) {

	}

	@Topic("post-video")
	public void createVideo(@KafkaKey User user, Video video) {

	}

	@Topic("update-video")
	public void updateVideo(@KafkaKey User user, Video video) {

	}

	@Topic("delete-video")
	public void deleteVideo(@KafkaKey User user, Video video) {

	}

	@Topic("create-hashtag")
	public void createHashtag(@KafkaKey Long id, Hashtag hashtag) {

	}

	@Topic("delete-hashtag")
	public void deleteHashtag(@KafkaKey Long id, Hashtag hashtag) {

	}

}
