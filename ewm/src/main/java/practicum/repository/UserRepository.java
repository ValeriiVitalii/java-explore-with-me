package practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import practicum.model.User;


import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIdIsIn(List<Long> ids, Pageable pageable);

}