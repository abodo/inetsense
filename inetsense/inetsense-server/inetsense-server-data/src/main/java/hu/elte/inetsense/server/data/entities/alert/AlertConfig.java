package hu.elte.inetsense.server.data.entities.alert;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import hu.elte.inetsense.common.dtos.alert.AlertType;

@Entity
public class AlertConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private AlertType alertType;
	private Long limit;
	private Long severity;
	private String alertMessage;
	private Boolean enabled;
	private String relation;

	@Id
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "alert_type_id", nullable = false)
	public AlertType getAlertType() {
		return alertType;
	}

	public void setAlertType(final AlertType alertType) {
		this.alertType = alertType;
	}

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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(final Boolean enabled) {
		this.enabled = enabled;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(final String relation) {
		this.relation = relation;
	}

}
