package stepper.step.api;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LoggerImpl implements Logger{
    private String log;
    private LocalTime logTime;

    public LoggerImpl(String log) {
        this.log=log;
        logTime = LocalTime.now();
    }

    @Override
    public String getLogTimeAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return logTime.format(formatter);
    }
    @Override
    public String getLog() {
        return log;
    }
}
