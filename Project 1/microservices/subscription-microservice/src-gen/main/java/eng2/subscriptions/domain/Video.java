package eng2.subscriptions.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.micronaut.serde.annotation.Serdeable;


@Entity
@Serdeable
public class Video {
	
	@Id
	private Long id;

	private String title;

	private Integer likes;

	private Set<Hashtag> hashtags;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Set<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(Set<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

}
