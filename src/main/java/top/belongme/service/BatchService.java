package top.belongme.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.belongme.model.pojo.Batch;
import top.belongme.model.result.Result;
import top.belongme.model.dto.BatchQueryDTO;

/**
 * @Title: BatchService
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/915:30
 */
public interface BatchService extends IService<Batch> {
    Result addBatch(Batch batch);

    IPage<Batch> selectPage(Page<Batch> pageParam, BatchQueryDTO batchQueryDTO);

    Result updateBatch(Batch batch);

    Result deleteBatch(String batchId);

    Result updateStatus(String batchId, Integer status);

    IPage<Batch> selectPageIsCommit(Page<Batch> pageParam, BatchQueryDTO batchQueryDTO);
    IPage<Batch> selectPageIsCommitAndCount(Page<Batch> pageParam, BatchQueryDTO batchQueryDTO);

}
