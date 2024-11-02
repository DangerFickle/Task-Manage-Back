package top.belongme.beanconverter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import top.belongme.model.vo.BatchVO;
import top.belongme.model.pojo.Batch;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: BatchConverter
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/5/20:51
 */
@Component
public class BatchConverter {
    public IPage<BatchVO> convertPage(IPage<Batch> source) {
        IPage<BatchVO> batchDTOIPage = new Page<>();

        BeanUtils.copyProperties(source, batchDTOIPage);

        List<BatchVO> batchVOList = new ArrayList<>();
        for(Batch batch : source.getRecords()) {
            BatchVO batchVO = new BatchVO();
            BeanUtils.copyProperties(batch, batchVO);
            batchVOList.add(batchVO);
        }
        batchDTOIPage.setRecords(batchVOList);
        return batchDTOIPage;
    }
}
