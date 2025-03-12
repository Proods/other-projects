package eng2.subscriptions.repositories;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import eng2.subscriptions.domain.User;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UsersRepository extends CrudRepository<User,Long> {

	@Join(value="subscriptions", type=Join.Type.LEFT_FETCH)
	@Override
	Optional<User> findById(@NotNull Long id);
}
