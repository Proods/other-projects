package eng2.videos.clients;

import eng2.videos.domain.Hashtag;
import eng2.videos.domain.Video;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;


@Client("/hashtags")
public interface HashtagsClient {
	
	@Get("/")
	public Iterable<Hashtag> list();
	
	@Get("/{id}")
	public Hashtag getHashtag(Long id);
	
	@Get("/{id}/videos")
	public Iterable<Video> getHashtagVideos(Long id);
	
}
