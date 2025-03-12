package eng2.videos.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;


@Controller("/hashtags")
public class HashtagsController {
	
	private HashtagsRepository repo;

	
	@Get("/")
	public Iterable<Hashtag> list() {

	}

	@Get("/{id}")
	public Hashtag getHashtag(Long id) {

	}

	@Get("/{id}/videos")
	public Iterable<Video> getHashtagVideos(Long id) {

	}

	
}
