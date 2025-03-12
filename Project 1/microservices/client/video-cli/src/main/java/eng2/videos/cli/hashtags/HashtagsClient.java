package eng2.videos.cli.hashtags;

import eng2.videos.cli.domain.Hashtag;
import eng2.videos.cli.domain.Video;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;


@Client("${hashtags.url:`http://localhost:8080/hashtags`}")
public interface HashtagsClient {
	
	@Get("/")
	public Iterable<Hashtag> list();
	
	@Get("/{id}")
	public Hashtag getHashtag(Long id);
	
	@Get("/{id}/videos")
	public Iterable<Video> getHashtagVideos(Long id);
	
}
