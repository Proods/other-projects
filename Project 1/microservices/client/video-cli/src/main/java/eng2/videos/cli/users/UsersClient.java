package eng2.videos.cli.users;

import eng2.videos.cli.domain.User;
import eng2.videos.cli.domain.Video;
import eng2.videos.cli.dto.UserDTO;
import eng2.videos.cli.dto.VideoDTO;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;


@Client("${users.url:`http://localhost:8080/users`}")
public interface UsersClient {
	
	@Get("/")
	public Iterable<User> list();
	
	@Post("/")
	public HttpResponse<Void> createUser(@Body UserDTO userDetails);
	
	@Get("/{id}")
	public User getUser(Long id);
	
	@Put("/{id}")
	public HttpResponse<String> updateUser(Long id, @Body UserDTO userDetails);
	
	@Delete("/{id}")
	public HttpResponse<String> deleteUser(Long id);
	
	@Get("/{id}/uploads")
	public Iterable<Video> getUploads(Long id);
	
	@Get("/{id}/views")
	public Iterable<Video> getViewedVideos(Long id);
	
	@Get("/{id}/likes")
	public Iterable<Video> getLikedVideos(Long id);
	
	@Get("/{id}/dislikes")
	public Iterable<Video> getDislikedVideos(Long id);
	
	@Post("/{id}/videos")
	public HttpResponse<Void> createVideo(Long id, @Body VideoDTO videoDetails);
	
	@Put("/{creator_id}/videos/{video_id}")
	public HttpResponse<String> updateVideo(Long creator_id, Long video_id, @Body VideoDTO videoDetails);
	
	@Delete("/{creator_id}/videos/{video_id}")
	public HttpResponse<String> deleteVideo(Long creator_id, Long video_id);
	
}
