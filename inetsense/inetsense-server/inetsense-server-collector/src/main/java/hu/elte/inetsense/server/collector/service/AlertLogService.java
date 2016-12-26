package hu.elte.inetsense.server.collector.service;

import hu.elte.inetsense.server.data.entities.Measurement;
import hu.elte.inetsense.server.data.entities.probe.Probe;

public interface AlertLogService {
	void process(Probe probe, Measurement measurement);
}
