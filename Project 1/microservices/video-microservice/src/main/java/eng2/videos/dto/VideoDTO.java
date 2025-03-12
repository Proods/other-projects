package eng2.videos.dto;

import java.util.Set;

import io.micronaut.serde.annotation.Serdeable;


@Serdeable
public class VideoDTO {
	
	private String title;
	private Set<String> hashtags;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Set<String> getHashtags() {
		return hashtags;
	}
	
	public void setHashtags(Set<String> hashtags) {
		this.hashtags = hashtags;
	}	
	
}
