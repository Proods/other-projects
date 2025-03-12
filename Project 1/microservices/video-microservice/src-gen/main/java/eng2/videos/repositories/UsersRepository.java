package eng2.videos.repositories;

import eng2.trending.hashtags.domain.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;


@Repository
public interface UsersRepository extends CrudRepository<User,Long>{

}
