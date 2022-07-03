package kim.nzxy.quartzdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 定时任务
 *
 * @author xy
 */
@Data
public class QuartzJob implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 任务类名
     */
    private String jobClassName;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 参数
     */
    private String parameter;
    /**
     * 为true表示正常运行
     */
    private Boolean started;

}
