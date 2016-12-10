package hu.elte.inetsense.server.collector.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import hu.elte.inetsense.server.data.repository.AlertConfigurationRepository;
import hu.elte.inetsense.server.data.repository.AlertLogRepository;
import hu.elte.inetsense.server.data.entities.alert.*;
import hu.elte.inetsense.server.data.entities.Measurement;
import hu.elte.inetsense.server.data.entities.probe.Probe;


public class AlertLogServiceImpl {
    @Autowired
    private AlertConfigurationRepository alertConfigurationRepository;

	
	public void process(Probe probe, Measurement measurement){
		List<AlertConfig> allConfig = alertConfigurationRepository.findAll();

		for(AlertConfig alertConfig : allConfig){
			if(alertConfig.getRelation() == "<"){
				Long downloadSpeed = measurement.getDownloadSpeed();
				Long configDownloadSpeed = alertConfig.getLimit();
				if(downloadSpeed < configDownloadSpeed){
					//valami
				}
			}else if(alertConfig.getRelation() == ">"){
				Long downloadSpeed = measurement.getDownloadSpeed();
				Long configDownloadSpeed = alertConfig.getLimit();
				if(downloadSpeed > configDownloadSpeed){
					//valami
				}
			}
		}
	}
}
