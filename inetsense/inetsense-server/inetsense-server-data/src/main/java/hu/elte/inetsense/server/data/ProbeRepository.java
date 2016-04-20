package hu.elte.inetsense.server.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.elte.inetsense.server.data.entities.Probe;

/**
 * @author Zsolt Istvanfi
 */
@Repository
public interface ProbeRepository extends JpaRepository<Probe, Long> {

    public Probe findOneByAuthId(String id);

}