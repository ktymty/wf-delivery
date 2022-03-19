package adapter.jpa.repository;

import adapter.jpa.entity.ErrorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorRepository extends JpaRepository<ErrorEntity, String> {
}
