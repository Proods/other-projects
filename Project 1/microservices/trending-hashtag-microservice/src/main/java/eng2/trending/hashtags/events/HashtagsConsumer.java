package eng2.trending.hashtags.events;


import javax.transaction.Transactional;

import eng2.trending.hashtags.domain.Hashtag;
import eng2.trending.hashtags.repositories.HashtagsRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;


@KafkaListener(groupId="trending-hashtags")
public class HashtagsConsumer {
	
	@Inject
	HashtagsRepository repo;
	
	@Topic("trending")
	@Transactional
	public void trendingHashtag(@KafkaKey Hashtag hashtag, Integer numOfLikes) {	

		if (repo.existsById(hashtag.getId())) {
			Hashtag h = repo.findById(hashtag.getId()).get();
			h.setNumOfLikes(numOfLikes);
			repo.update(h);
		} else {
			hashtag.setNumOfLikes(numOfLikes);
			repo.save(hashtag);
		}
	}
	
	@Topic("delete-hashtag")
	public void deleteHashtag(@KafkaKey Long id, Hashtag hashtag) {
		repo.deleteById(id);
	}

}
