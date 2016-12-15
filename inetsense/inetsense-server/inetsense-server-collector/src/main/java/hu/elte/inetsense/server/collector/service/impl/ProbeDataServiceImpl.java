package hu.elte.inetsense.server.collector.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.elte.inetsense.common.dtos.probe.MeasurementDTO;
import hu.elte.inetsense.common.dtos.probe.ProbeDataDTO;
import hu.elte.inetsense.server.collector.service.AlertLogService;
import hu.elte.inetsense.server.collector.service.ClockService;
import hu.elte.inetsense.server.collector.service.ProbeDataService;
import hu.elte.inetsense.server.collector.service.message.ProbeDataReceiver;
import hu.elte.inetsense.server.data.converter.MeasurementConverter;
import hu.elte.inetsense.server.data.entities.Measurement;
import hu.elte.inetsense.server.data.entities.probe.Probe;
import hu.elte.inetsense.server.data.repository.MeasurementRepository;
import hu.elte.inetsense.server.data.repository.ProbeRepository;

/**
 * @author Zsolt Istvanfi
 */
@Service
@Transactional(readOnly = true)
public class ProbeDataServiceImpl implements ProbeDataService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private ProbeRepository probeRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private MeasurementConverter measurementConverter;

    @Autowired ClockService clockService;
    
    @Autowired AlertLogService alertLogService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    @Transactional(readOnly = true)
    public void saveProbeData(ProbeDataDTO probeData) {
        validateProbeData(probeData);
    	jmsTemplate.convertAndSend(ProbeDataReceiver.DESTINATION, probeData);
    }

	@Override
    @Transactional(readOnly = false)
	public void processProbeData(ProbeDataDTO probeData) {
		Probe probe = getProbe(probeData.getProbeAuthId());
		for (MeasurementDTO measurementDTO : probeData.getMeasurements()) {
          processMeasurement(probe, measurementDTO);
		}
	}

	private void processMeasurement(Probe probe, MeasurementDTO measurementDTO) {
		Measurement measurement = createMeasurement(probe, measurementDTO);
		alertLogService.process(probe, measurement);
        measurementRepository.save(measurement);
	}

	private Measurement createMeasurement(Probe probe, MeasurementDTO measurementDTO) {
		Measurement measurement = measurementConverter.convertToEntity(measurementDTO);
		measurement.setCreatedOn(clockService.getCurrentTime());
		measurement.setProbe(probe);
		return measurement;
	}

	private void validateProbeData(final ProbeDataDTO probeData) {
		getProbe(probeData.getProbeAuthId());
	}
	
    private Probe getProbe(final String authId) {
        Optional<Probe> optionalProbe = probeRepository.findOneByAuthId(authId);
        Probe probe = optionalProbe.orElseThrow(() -> {
            log.error("Unable to find probe based on auth id. Measurements can be saved only for an existing probe! auth ID: " + authId);
            return new NoSuchElementException("Unable to find probe based on auth id. Measurements can be saved only for an existing probe!");
        });
        return probe;
    }

}
