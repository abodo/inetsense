package hu.elte.inetsense.server.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.elte.inetsense.server.data.entities.alert.*;

@Repository
public interface AlertLogRepository extends JpaRepository<AlertLog, Long>{
	List<AlertLog> findAll();
}