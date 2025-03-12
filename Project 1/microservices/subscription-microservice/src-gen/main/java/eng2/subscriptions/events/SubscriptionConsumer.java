package eng2.subscriptions.events;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;


@KafkaListener(groupId="subscription")
public class SubscriptionConsumer {
	
	VideosRepository videosRepo;

	HashtagsRepository hashtagsRepo;


	@Topic("like-video")
	public void likeVideo(@KafkaKey Video video, User user) {

	}

	@Topic("remove-like")
	public void removeLike(@KafkaKey Video video, User user) {

	}

	@Topic("add-hashtag-video")
	public void addHashtagVideo(@KafkaKey Hashtag hashtag, Video video) {

	}

	@Topic("remove-hashtag-video")
	public void removeHashtagVideo(@KafkaKey Hashtag hashtag, Video video) {

	}

}
