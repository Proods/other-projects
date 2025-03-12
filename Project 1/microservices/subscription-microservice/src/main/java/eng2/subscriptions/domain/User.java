package eng2.subscriptions.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micronaut.serde.annotation.Serdeable;

@Entity
@Serdeable
public class User {
	
	@Id
	private Long id;
	
	@JsonIgnore
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="user_subscriptions")
	private Set<Hashtag> subscriptions;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Hashtag> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Set<Hashtag> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
}
