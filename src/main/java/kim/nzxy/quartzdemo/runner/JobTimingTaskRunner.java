package kim.nzxy.quartzdemo.runner;

import kim.nzxy.quartzdemo.service.QuartzJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 负责项目启动时安排定时任务
 *
 * @author xuyingfa
 * @since 2022-03-10
 */
@Component
@RequiredArgsConstructor
public class JobTimingTaskRunner implements ApplicationRunner {
    private final QuartzJobService quartzJobService;

    @Override
    public void run(ApplicationArguments args) {
        quartzJobService.timingTask();
    }
}
