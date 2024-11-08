package top.belongme.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.beanconverter.BatchConverter;
import top.belongme.model.vo.BatchVO;
import top.belongme.model.pojo.Batch;
import top.belongme.model.result.Result;
import top.belongme.model.dto.BatchQueryDTO;
import top.belongme.service.BatchService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @Title: BatchController
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/915:27
 */
@RestController
@RequestMapping("batch")
public class BatchController {

    @Resource
    private BatchService batchService;

    @Resource
    private BatchConverter batchConverter;

    /**
     * TODO 获取批次列表，分页查询 + 模糊查询
     *
     * @Author DengChao
     * @Date 2023/2/9 18:51
     */
    @PreAuthorize("hasAuthority('job:batch:select')")
    @GetMapping("listPage/{page}/{limit}")
    public Result<IPage<BatchVO>> getBatchList(@PathVariable Long page,
                                               @PathVariable Long limit,
                                               BatchQueryDTO batchQueryDTO) {
        //创建page对象
        Page<Batch> pageParam = new Page<>(page, limit);
        //调用service方法
        IPage<Batch> pageModel = batchService.selectPage(pageParam, batchQueryDTO);

        // 将Batch转换为BatchDTO
        IPage<BatchVO> batchDTOIPage = batchConverter.convertPage(pageModel);
        return new Result<>(200, "请求成功", batchDTOIPage);
    }

    /**
     * TODO 获取批次列表，并设置该用户是否已提交对应批次，分页查询 + 模糊查询
     * 用户提交作业时用
     * @Author DengChao
     * @Date 2023/2/10 21:09
     */
    @PreAuthorize("hasAuthority('job:batch:select')")
    @GetMapping("listPageIsCommit/{page}/{limit}")
    public Result<IPage<BatchVO>> getBatchListIsCommit(@PathVariable Long page,
                                                       @PathVariable Long limit,
                                                       BatchQueryDTO batchQueryDTO) {
        //创建page对象
        Page<Batch> pageParam = new Page<>(page, limit);
        //调用service方法
        IPage<Batch> pageModel = batchService.selectPageIsCommit(pageParam, batchQueryDTO);
        // 将Batch转换为BatchDTO
        IPage<BatchVO> batchDTOIPage = batchConverter.convertPage(pageModel);
        return new Result<>(200, "请求成功", batchDTOIPage);
    }

    /**
     * TODO 获取批次列表，并设置该用户是否已提交对应批次，人数情况，分页查询 + 模糊查询
     *
     * @Author DengChao
     * @Date 2023/2/14 11:04
     */
    @PreAuthorize("hasAuthority('job:batch:select')")
    @GetMapping("listPageIsCommitAndCount/{page}/{limit}")
    public Result<IPage<BatchVO>> getBatchListIsCommitAndCount(@PathVariable Long page,
                                                               @PathVariable Long limit,
                                                               BatchQueryDTO batchQueryDTO) {
        //创建page对象
        Page<Batch> pageParam = new Page<>(page, limit);
        //调用service方法
        IPage<Batch> pageModel = batchService.selectPageIsCommitAndCount(pageParam, batchQueryDTO);
        // 将Batch转换为BatchDTO
        IPage<BatchVO> batchDTOIPage = batchConverter.convertPage(pageModel);
        return new Result<>(200, "请求成功", batchDTOIPage);
    }

    /**
     * TODO 根据batchId获取批次
     *
     * @Author DengChao
     * @Date 2023/2/9 18:50
     */
    @PreAuthorize("hasAuthority('job:batch:select')")
    @GetMapping("/get/{batchId}")
    public Result<BatchVO> getBatch(@PathVariable String batchId) {
        Batch batch = batchService.getById(batchId);
        BatchVO batchVO = new BatchVO();
        BeanUtils.copyProperties(batch, batchVO);
        return new Result<>(200, "请求成功", batchVO);
    }

    /**
     * TODO 添加批次
     *
     * @Author DengChao
     * @Date 2023/2/9 12:24
     */
    @PreAuthorize("hasAuthority('job:batch:insert')")
    @PostMapping("add")
    public Result addBatch(@RequestBody @Valid Batch batch, BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return batchService.addBatch(batch);
    }


    /**
     * TODO 修改批次
     *
     * @Author DengChao
     * @Date 2023/2/9 12:24
     */
    @PreAuthorize("hasAuthority('job:batch:update')")
    @PutMapping("update")
    public Result updateBatch(@RequestBody @Valid Batch batch, BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return batchService.updateBatch(batch);
    }

    /**
     * TODO 根据id删除批次
     *
     * @Author DengChao
     * @Date 2023/2/9 22:18
     */
    @PreAuthorize("hasAuthority('job:batch:delete')")
    @DeleteMapping("delete/{batchId}")
    public Result deleteBatch(@PathVariable String batchId) {
        return batchService.deleteBatch(batchId);
    }

    /**
     * TODO 根据id修改批次状态
     *
     * @Author DengChao
     * @Date 2023/2/9 22:30
     */
    @PreAuthorize("hasAuthority('job:batch:update')")
    @PutMapping("updateStatus/{batchId}/{status}")
    public Result updateStatus(@PathVariable String batchId,
                               @PathVariable Integer status) {
        return batchService.updateStatus(batchId, status);
    }
}
