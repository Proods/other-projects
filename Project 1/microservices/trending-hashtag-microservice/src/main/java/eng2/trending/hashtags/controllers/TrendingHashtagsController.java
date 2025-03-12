package eng2.trending.hashtags.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eng2.trending.hashtags.domain.Hashtag;
import eng2.trending.hashtags.repositories.HashtagsRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;


@Controller("/trending-hashtags")
public class TrendingHashtagsController {
	
	@Inject
	HashtagsRepository repo;
	
	
	@Get("/")
	public Iterable<Hashtag> getTrendingHashtags(){
		
		List<Hashtag> hashtags = iterableToList(repo.findAll());
		
		Collections.sort(hashtags, (a,b) -> {
			return b.getNumOfLikes().compareTo(a.getNumOfLikes());
		});
		
		if (hashtags.size() > 10) {
			return hashtags.subList(0, 10);
		}
		
		return hashtags;
	}
	
	private <T> List<T> iterableToList(Iterable<T> iterable) {
		List<T> l = new ArrayList<>();
		iterable.forEach(l::add);
		return l;
	}

}
