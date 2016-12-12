package hu.elte.inetsense.server.collector.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.elte.inetsense.common.dtos.alert.AlertType;
import hu.elte.inetsense.server.data.repository.AlertConfigurationRepository;
import hu.elte.inetsense.server.data.repository.AlertLogRepository;
import hu.elte.inetsense.server.data.entities.alert.AlertLog;
import hu.elte.inetsense.server.data.entities.alert.AlertConfig;
import hu.elte.inetsense.server.data.entities.Measurement;
import hu.elte.inetsense.server.data.entities.probe.Probe;

@Component
public class AlertLogServiceImpl {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

	
    @Autowired
    private AlertConfigurationRepository alertConfigurationRepository;

    @Autowired
    private AlertLogRepository alertLogRepository;
	
	public void process(Probe probe, Measurement measurement){
		List<AlertConfig> allConfig = alertConfigurationRepository.findAll();

		for(AlertConfig alertConfig : allConfig){
			if(alertConfig.getAlertType() == AlertType.DOWNLOAD){
				if(alertConfig.getRelation().equals("<")){
					Long downloadSpeed = measurement.getDownloadSpeed();
					Long configDownloadSpeed = alertConfig.getLimit();
					if(downloadSpeed < configDownloadSpeed){
						insertAlert(alertConfig,probe,measurement);
					}
				}else if(alertConfig.getRelation().equals(">")){
					Long downloadSpeed = measurement.getDownloadSpeed();
					Long configDownloadSpeed = alertConfig.getLimit();
					if(downloadSpeed > configDownloadSpeed){
						insertAlert(alertConfig,probe,measurement);
					}
				}
			}else if(alertConfig.getAlertType() == AlertType.UPLOAD){
				if(alertConfig.getRelation().equals("<")){
					Long uploadSpeed = measurement.getUploadSpeed();
					Long configUploadSpeed = alertConfig.getLimit();
					if(uploadSpeed < configUploadSpeed){
						insertAlert(alertConfig,probe,measurement);
					}
				}else if(alertConfig.getRelation().equals(">")){
					Long uploadSpeed = measurement.getUploadSpeed();
					Long configUploadSpeed = alertConfig.getLimit();
					if(uploadSpeed > configUploadSpeed){
						insertAlert(alertConfig,probe,measurement);
					}
				}
			}
		}
	}
	
	private void insertAlert(AlertConfig alertConfig,Probe probe,Measurement measurement){
		String logForConsole = "message:"+alertConfig.getAlertMessage()+
				" count: 1"+
				" startTime:"+measurement.getCreatedOn()+
				" endTime:"+measurement.getCreatedOn()+
				" relation:"+alertConfig.getRelation()+
				" probeId:"+probe.getId()+
				" severity:"+alertConfig.getSeverity()+
				" alertType:"+alertConfig.getAlertType()+
				" downloadSpeed:"+measurement.getDownloadSpeed()+
				" uploadSpeed:"+measurement.getUploadSpeed()+
				" configSpeed:"+alertConfig.getLimit();
		log.info(logForConsole);

		AlertLog alert_log = new AlertLog();
		alert_log.setId((long)101);
		alert_log.setAlertMessage(alertConfig.getAlertMessage());
		alert_log.setCount((long)1);
		alert_log.setStartTime(measurement.getCreatedOn());
		alert_log.setEndTime(measurement.getCreatedOn());
		alert_log.setRelation(alertConfig.getRelation());
		alert_log.setProbe(probe);
		alert_log.setSeverity((long)1);
		alertLogRepository.save(alert_log);
	}
}
