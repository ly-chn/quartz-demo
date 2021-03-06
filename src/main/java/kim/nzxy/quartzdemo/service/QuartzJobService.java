package kim.nzxy.quartzdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import kim.nzxy.quartzdemo.entity.QuartzJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 定时任务在线管理
 *
 * @author jeecg-boot
 * @Version: V1.1
 * @since 2019-04-28
 */
public interface QuartzJobService extends IService<QuartzJob> {

    List<QuartzJob> findByJobClassName(String jobClassName);

    boolean saveAndScheduleJob(QuartzJob quartzJob);

    boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException;

    boolean deleteAndStopJob(QuartzJob quartzJob);

    boolean resumeJob(QuartzJob quartzJob);

    /**
     * 执行定时任务
     */
    void execute(QuartzJob quartzJob) throws Exception;

    /**
     * 暂停任务
     *
     * @throws SchedulerException
     */
    void pause(QuartzJob quartzJob);

    void timingTask();
}
