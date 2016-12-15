package hu.elte.inetsense.server.data.entities.alert;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import hu.elte.inetsense.common.dtos.alert.AlertType;
import hu.elte.inetsense.server.data.entities.probe.Probe;

import javax.persistence.FetchType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Entity
public class AlertLog implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Probe probe;
	private Date startTime;
	private Date endTime;
	private Long count;
	private Long limit;
	private Long severity;
	private String alertMessage;
	private String relation;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "probe_id", nullable = false)
	public Probe getProbe() {
		return probe;
	}

	public void setProbe(final Probe probe) {
		this.probe = probe;
	}

	@Column(name = "start_time", nullable = false)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(final Date endTime) {
		this.endTime = endTime;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(final Long count) {
		this.count = count;
	}

	@Column(name = "my_limit")
	public Long getLimit() {
		return limit;
	}

	public void setLimit(final Long limit) {
		this.limit = limit;
	}

	public Long getSeverity() {
		return severity;
	}

	public void setSeverity(final Long severity) {
		this.severity = severity;
	}

	@Column(name = "alert_message", nullable = false)
	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(final String alertMessage) {
		this.alertMessage = alertMessage;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(final String relation) {
		this.relation = relation;
	}
}
