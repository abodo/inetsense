package hu.elte.inetsense.common.dtos.probe;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Zsolt Istvanfi
 */
public class MeasurementDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;
	private Date createdOn;
    @JsonFormat(timezone = "UTC")
    @Past(message = "Completed on time must be in the past!")
    private Date completedOn;
    
    @Range(min = -90, max = 90, message = "Latitude value must be between -90 and 90!")
    private Double lat;
    
    @Range(min = -180, max = 180, message = "Longitude value must be between -180 and 180!")
    private Double lng;
    
    @Min(value = 0, message = "Download speed cannot be negative!")
    private Long downloadSpeed;
    
    @Min(value = 0, message = "Upload speed cannot be negative!")
    private Long uploadSpeed;
    
    private String isp;
    private String downloadTarget;

    public Long getId() {
		return id;
	}
    
    public void setId(Long id) {
		this.id = id;
	}
    
    public Date getCreatedOn() {
		return createdOn;
	}
    
    public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
    
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(final Date completedOn) {
        this.completedOn = completedOn;
    }

    public Long getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(Long downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public Long getUploadSpeed() {
        return uploadSpeed;
    }

    public void setUploadSpeed(Long uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
    }
    
    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }
    
    public String getDownloadTarget() {
		return downloadTarget;
	}
    
    public void setDownloadTarget(String downloadTarget) {
		this.downloadTarget = downloadTarget;
	}

}
