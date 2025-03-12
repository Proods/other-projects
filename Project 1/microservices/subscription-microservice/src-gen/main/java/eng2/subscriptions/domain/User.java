package eng2.subscriptions.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.micronaut.serde.annotation.Serdeable;


@Entity
@Serdeable
public class User {
	
	@Id
	private Long id;

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
