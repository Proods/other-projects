package eng2.videos.controllers;


import java.util.Optional;

import eng2.videos.domain.Hashtag;
import eng2.videos.domain.Video;
import eng2.videos.repositories.HashtagsRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

@Controller("/hashtags")
public class HashtagsController {
	
	@Inject
	HashtagsRepository repo;
	
	@Get("/")
	public Iterable<Hashtag> list(){
		return repo.findAll();
	}
	
	// No POST method
	
	@Get("/{id}")
	public Hashtag getHashtag(Long id) {
		return repo.findById(id).orElse(null);
	}
	
	@Get("/{id}/videos")
	public Iterable<Video> getHashtagVideos(Long id) {
		Optional<Hashtag> hashtag = repo.findById(id);
		if (hashtag.isEmpty()) {
			return null;
		}
		return hashtag.get().getVideos();
	}
	
	// No PUT method
	
	// No DELETE method

}
