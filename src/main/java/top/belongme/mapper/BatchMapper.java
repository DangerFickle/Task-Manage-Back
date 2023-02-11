package top.belongme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.belongme.model.pojo.Batch;
import top.belongme.model.vo.BatchQueryVo;

/**
 * @Title: BatchMapper
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/915:29
 */
public interface BatchMapper extends BaseMapper<Batch> {
    IPage<Batch> selectPage(Page<Batch> pageParam, @Param("vo") BatchQueryVo batchQueryVo);
}
