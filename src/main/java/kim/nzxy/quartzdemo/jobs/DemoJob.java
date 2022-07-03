package kim.nzxy.quartzdemo.jobs;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoJob implements Job {
    @Setter
    private String parameter;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("execution: {}", parameter);
    }
}
