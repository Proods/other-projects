package eng2.videos.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micronaut.serde.annotation.Serdeable;

@Entity
@Serdeable
public class Video {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@ManyToOne
	private User creator;
	
	@Column(nullable=false)
	private String title;
	
	@JsonIgnore
	@ManyToMany
	private Set<Hashtag> hashtags;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name="video_user_likes")
	private Set<User> likes;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name="video_user_dislikes")
	private Set<User> dislikes;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name="video_user_views")
	private Set<User> viewers;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(Set<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

	public Set<User> getLikes() {
		return likes;
	}

	public void setLikes(Set<User> likes) {
		this.likes = likes;
	}

	public Set<User> getDislikes() {
		return dislikes;
	}

	public void setDislikes(Set<User> dislikes) {
		this.dislikes = dislikes;
	}

	public Set<User> getViewers() {
		return viewers;
	}

	public void setViewers(Set<User> viewers) {
		this.viewers = viewers;
	}
	
}
