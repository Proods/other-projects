package eng2.videos.clients;

import eng2.videos.domain.User;
import eng2.videos.domain.Video;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;


@Client("/videos")
public interface VideosClient {
	
	@Get("/")
	public Iterable<Video> list();
	
	@Get("/{id}")
	public Video getVideo(Long id);
	
	@Get("/{id}/viewers")
	public Iterable<User> getViewers(Long id);
	
	@Get("/{id}/likes")
	public Iterable<User> getLikes(Long id);
	
	@Get("/{id}/dislikes")
	public Iterable<User> getDislikes(Long id);
	
	@Put("/{video_id}/viewers/{viewer_id}")
	public HttpResponse<String> addView(Long video_id, Long viewer_id);
	
	@Put("/{video_id}/likes/{viewer_id}")
	public HttpResponse<String> addLike(Long video_id, Long viewer_id);
	
	@Put("/{video_id}/dislikes/{viewer_id}")
	public HttpResponse<String> addDislike(Long video_id, Long viewer_id);
	
	@Delete("/{video_id}/likes/{viewer_id}")
	public HttpResponse<String> removeLike(Long video_id, Long viewer_id);
	
	@Delete("/{video_id}/dislikes/{viewer_id}")
	public HttpResponse<String> removeDislike(Long video_id, Long viewer_id);
	
}
