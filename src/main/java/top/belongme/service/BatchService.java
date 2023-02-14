package top.belongme.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.belongme.model.pojo.Batch;
import top.belongme.model.result.Result;
import top.belongme.model.vo.BatchQueryVo;

import java.util.List;

/**
 * @Title: BatchService
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/915:30
 */
public interface BatchService extends IService<Batch> {
    Result addBatchAndFolderPath(Batch batch);

    IPage<Batch> selectPage(Page<Batch> pageParam, BatchQueryVo batchQueryVo);

    Result updateBatchAndFolderPath(Batch batch);

    Result deleteBatchAndFolderPath(String batchId);

    Result updateStatus(String batchId, Integer status);

    IPage<Batch> selectPageIsCommit(Page<Batch> pageParam, BatchQueryVo batchQueryVo);
    IPage<Batch> selectPageIsCommitAndCount(Page<Batch> pageParam, BatchQueryVo batchQueryVo);

}
