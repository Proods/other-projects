package eng2.videos.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.micronaut.serde.annotation.Serdeable;


@Entity
@Serdeable
public class User {
	
	@Id
	private Long id;

	private String name;

	private Set<Video> uploads;

	private Set<Video> likedVids;

	private Set<Video> dislikedVids;

	private Set<Video> views;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Video> getUploads() {
		return uploads;
	}

	public void setUploads(Set<Video> uploads) {
		this.uploads = uploads;
	}

	public Set<Video> getLikedVids() {
		return likedVids;
	}

	public void setLikedVids(Set<Video> likedVids) {
		this.likedVids = likedVids;
	}

	public Set<Video> getDislikedVids() {
		return dislikedVids;
	}

	public void setDislikedVids(Set<Video> dislikedVids) {
		this.dislikedVids = dislikedVids;
	}

	public Set<Video> getViews() {
		return views;
	}

	public void setViews(Set<Video> views) {
		this.views = views;
	}

}
