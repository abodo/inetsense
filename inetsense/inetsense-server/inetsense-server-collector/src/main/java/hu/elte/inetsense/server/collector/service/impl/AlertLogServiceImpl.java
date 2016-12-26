package hu.elte.inetsense.server.collector.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.elte.inetsense.common.dtos.alert.AlertType;
import hu.elte.inetsense.server.collector.service.AlertLogService;
import hu.elte.inetsense.server.collector.service.ClockService;
import hu.elte.inetsense.server.data.repository.AlertConfigurationRepository;
import hu.elte.inetsense.server.data.repository.AlertLogRepository;
import hu.elte.inetsense.server.data.entities.alert.AlertLog;
import hu.elte.inetsense.server.data.entities.alert.AlertConfig;
import hu.elte.inetsense.server.data.entities.Measurement;
import hu.elte.inetsense.server.data.entities.probe.Probe;

@Component
public class AlertLogServiceImpl implements AlertLogService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

	
    @Autowired
    private AlertConfigurationRepository alertConfigurationRepository;

    @Autowired
    private AlertLogRepository alertLogRepository;
    
    @Autowired
    private ClockService clockService;
	
    @Override
	public void process(Probe probe, Measurement measurement){
		List<AlertConfig> allConfig = alertConfigurationRepository.findAll();

		for(AlertConfig alertConfig : allConfig){
			if(alertConfig.getEnabled()){
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

		AlertLog availableAlert = getAvailableAlert(alertConfig,probe);
		
		if(availableAlert == null){	
			insertNewAlert(alertConfig,probe,measurement);
		}else if(availableAlert.getSeverity() != alertConfig.getSeverity()){
			availableAlert.setEndTime(clockService.getCurrentTime());
			alertLogRepository.save(availableAlert);
			insertNewAlert(alertConfig,probe,measurement);
		}else{
			modifyAvailableAlert(availableAlert,alertConfig,probe,measurement);
		}
	}
	
	private void modifyAvailableAlert(AlertLog availableAlert,AlertConfig alertConfig,Probe probe,Measurement measurement){
		Long count = availableAlert.getCount()+1;
		availableAlert.setCount(count);
		alertLogRepository.save(availableAlert);
	}
	
	private void insertNewAlert(AlertConfig alertConfig,Probe probe,Measurement measurement){
		String alertMessage = " alertType:"+alertConfig.getAlertType()+" ,DownloadSpeed:"+measurement.getDownloadSpeed()+" UploadSpeed:"+measurement.getUploadSpeed();
		AlertLog alertLog = new AlertLog();
		alertLog.setLimit(alertConfig.getLimit());
		alertLog.setAlertMessage(alertMessage);
		alertLog.setCount(1L);
		alertLog.setStartTime(measurement.getCreatedOn());
		alertLog.setRelation(alertConfig.getRelation());
		alertLog.setProbe(probe);
		alertLog.setSeverity(alertConfig.getSeverity());
		alertLogRepository.save(alertLog);
	}
	
	private AlertLog getAvailableAlert(AlertConfig alertConfig,Probe probe){
		List<AlertLog> logList = alertLogRepository.findAll();
		for(int i = 0; i < logList.size(); ++i){
			AlertLog tmpLog = logList.get(i);
			if(tmpLog.getProbe().getId() == probe.getId() && tmpLog.getEndTime() == null){
				return tmpLog;
			}
		}
		return null;
	}
}
