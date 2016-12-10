package hu.elte.inetsense.server.data.repository;

import java.util.List;

import hu.elte.inetsense.server.data.entities.Configuration;
import hu.elte.inetsense.server.data.entities.alert.AlertConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertConfigurationRepository extends JpaRepository<AlertConfig, String>{
	List<AlertConfig> findAll();
}