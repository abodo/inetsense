package hu.elte.inetsense.domain.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Zsolt Istvanfi
 */
@Entity
public class Probe implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long              id;
    private Date              createdOn;
    private String            authId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "probe_id")
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false)
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(final Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "auth_id", length = 8, unique = true, nullable = false)
    public String getAuthId() {
        return authId;
    }

    public void setAuthId(final String authId) {
        this.authId = authId;
    }

}