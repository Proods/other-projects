package eng2.videos.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micronaut.serde.annotation.Serdeable;

@Entity
@Serdeable
public class User {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable=false)
	private String name;
	
	@JsonIgnore
	@OneToMany(mappedBy="creator", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Set<Video> uploads;
	
	@JsonIgnore
	@ManyToMany(mappedBy="likes", fetch=FetchType.EAGER)
	private Set<Video> likedVids;
	
	@JsonIgnore
	@ManyToMany(mappedBy="dislikes", fetch=FetchType.EAGER)
	private Set<Video> dislikedVids;
	
	@JsonIgnore
	@ManyToMany(mappedBy="viewers", fetch=FetchType.EAGER)
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
