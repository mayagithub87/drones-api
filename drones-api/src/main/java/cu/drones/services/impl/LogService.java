package cu.drones.services.impl;

import cu.drones.persistence.Log;
import cu.drones.repositories.LogRepository;
import cu.drones.services.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService implements ILogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public void logMessage(String message) {
        if (message != null && !message.isEmpty()) {
            Log log = new Log(message);
            logRepository.save(log);
        }
    }
}
