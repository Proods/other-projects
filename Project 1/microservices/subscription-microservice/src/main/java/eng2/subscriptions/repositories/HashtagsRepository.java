package eng2.subscriptions.repositories;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import eng2.subscriptions.domain.Hashtag;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface HashtagsRepository extends CrudRepository<Hashtag,Long>{

	@Join(value="videos", type=Join.Type.LEFT_FETCH)
	@Override
	Optional<Hashtag> findById(@NotNull Long id);
}
