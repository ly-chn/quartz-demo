package kim.nzxy.quartzdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import kim.nzxy.quartzdemo.entity.QuartzJob;
import kim.nzxy.quartzdemo.mapper.QuartzJobMapper;
import kim.nzxy.quartzdemo.service.QuartzJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 定时任务
 *
 * @author xy
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzJobServiceImpl extends ServiceImpl<QuartzJobMapper, QuartzJob> implements QuartzJobService {

    private final Scheduler scheduler;

    private static Job getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Job) class1.newInstance();
    }

    @Override
    public List<QuartzJob> findByJobClassName(String jobClassName) {
        return this.lambdaQuery()
                .eq(QuartzJob::getJobClassName, jobClassName)
                .list();
    }

    /**
     * 保存&启动定时任务
     */
    @Override
    public boolean saveAndScheduleJob(QuartzJob quartzJob) {
        boolean success = this.save(quartzJob);
        if (success) {
            if (Boolean.TRUE.equals(quartzJob.getStarted())) {
                // 定时器添加
                this.schedulerAdd(quartzJob.getId(), quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
            }
        }
        return success;
    }

    /**
     * 恢复定时任务
     */
    @Override
    public boolean resumeJob(QuartzJob quartzJob) {
        schedulerDelete(quartzJob.getId());
        schedulerAdd(quartzJob.getId(), quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
        quartzJob.setStarted(true);
        return this.updateById(quartzJob);
    }

    /**
     * 编辑&启停定时任务
     */
    @Override
    public boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException {
        if (Boolean.TRUE.equals(quartzJob.getStarted())) {
            schedulerDelete(quartzJob.getId());
            schedulerAdd(quartzJob.getId(), quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
        } else {
            scheduler.pauseJob(JobKey.jobKey(quartzJob.getId()));
        }
        return this.updateById(quartzJob);
    }

    /**
     * 删除&停止删除定时任务
     */
    @Override
    public boolean deleteAndStopJob(QuartzJob job) {
        schedulerDelete(job.getId());
        return this.removeById(job.getId());
    }

    @Override
    public void execute(QuartzJob quartzJob) throws Exception {
        String jobName = quartzJob.getJobClassName().trim();
        Date startDate = new Date();
        String identity = LocalDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
        startDate.setTime(startDate.getTime() + 100L);
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(identity)
                .startNow()
                .build();
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobName).getClass()).withIdentity(identity).usingJobData("parameter", quartzJob.getParameter()).build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }

    @Override
    public void pause(QuartzJob quartzJob) {
        schedulerDelete(quartzJob.getId());
        quartzJob.setStarted(false);
        this.updateById(quartzJob);
    }

    @Override
    public void timingTask() {
        val list = this
                .lambdaQuery()
                .eq(QuartzJob::getStarted, true)
                .list();
        if (list == null) {
            log.error("查询定时任务列表失败");
            return;
        }
        for (QuartzJob job : list) {
            try {
                this.schedulerAdd(job.getId(), job.getJobClassName(), job.getCronExpression(), job.getParameter());
                log.info("安排定时任务: {}, cron: {}, parameter: {}", job.getJobClassName(), job.getCronExpression(), job.getParameter());
            } catch (Exception e) {
                log.error("安排定时任务失败: {}", job, e);
            }
        }
        log.info("初始化定时任务完成");
    }

    /**
     * 添加定时任务
     */
    private void schedulerAdd(String id, String jobClassName, String cronExpression, String parameter) {
        try {
            // 启动调度器
            scheduler.start();

            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(id).usingJobData("parameter", parameter).build();

            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(id).withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("创建定时任务失败", e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("后台找不到该类名：" + jobClassName, e);
        }
    }

    /**
     * 删除定时任务
     */
    private void schedulerDelete(String id) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(id));
            scheduler.unscheduleJob(TriggerKey.triggerKey(id));
            scheduler.deleteJob(JobKey.jobKey(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("删除定时任务失败");
        }
    }

}
