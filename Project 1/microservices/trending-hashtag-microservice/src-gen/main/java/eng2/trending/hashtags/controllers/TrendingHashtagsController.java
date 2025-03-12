package eng2.trending.hashtags.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;


@Controller("/trending-hashtags")
public class TrendingHashtagsController {
	
	private HashtagsRepository repo;

	
	@Get("/")
	public Iterable<Hashtag> getTrendingHashtags() {

	}

	
	private <T> List<T> iterableToList(Iterable<T> iterable) {

	}

}
