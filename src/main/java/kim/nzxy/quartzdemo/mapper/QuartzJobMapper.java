package kim.nzxy.quartzdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import kim.nzxy.quartzdemo.entity.QuartzJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务在线管理
 *
 * @author jeecg-boot
 * @since 2019-01-02
 */
@Mapper
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

}
