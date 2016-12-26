package hu.elte.inetsense.server.data.converter;

import hu.elte.inetsense.common.dtos.alert.AlertLogDTO;
import hu.elte.inetsense.common.dtos.probe.ProbeDTO;
import hu.elte.inetsense.server.data.entities.alert.AlertLog;
import hu.elte.inetsense.server.data.entities.probe.Probe;

public class AlertLogConverter extends AbstractConverter<AlertLogDTO, AlertLog> {

	private ProbeConverter probeConverter;
	
	public AlertLogConverter(ProbeConverter probeConverter) {
		this.probeConverter = probeConverter;
	}

	@Override
	public AlertLog convertToEntity(AlertLogDTO alertLogDto){
		Probe probe = probeConverter.convertToEntity(alertLogDto.getProbeDto());
		AlertLog alertLog = new AlertLog();
		alertLog.setId(alertLogDto.getId());
		alertLog.setProbe(probe);
		alertLog.setStartTime(alertLogDto.getStartTime());
		alertLog.setEndTime(alertLogDto.getEndTime());
		alertLog.setCount(alertLogDto.getCount());
		alertLog.setLimit(alertLogDto.getLimit());
		alertLog.setSeverity(alertLogDto.getSeverity());
		alertLog.setAlertMessage(alertLogDto.getAlertMessage());
		alertLog.setRelation(alertLogDto.getRelation());
		return alertLog;
	}

	@Override
	public AlertLogDTO convertToDto(AlertLog alertLog){
		AlertLogDTO alertLogDto = new AlertLogDTO();
		ProbeDTO probeDto = probeConverter.convertToDto(alertLog.getProbe());
		alertLogDto.setAlertMessage(alertLog.getAlertMessage());
		alertLogDto.setCount(alertLog.getCount());
		alertLogDto.setEndTime(alertLog.getEndTime());
		alertLogDto.setId(alertLog.getId());
		alertLogDto.setLimit(alertLog.getLimit());
		alertLogDto.setProbeDto(probeDto);
		alertLogDto.setRelation(alertLog.getRelation());
		alertLogDto.setSeverity(alertLog.getSeverity());
		alertLogDto.setStartTime(alertLog.getStartTime());
		return alertLogDto;
	}
}
