package top.belongme.beanconverter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import top.belongme.model.dto.BatchDTO;
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
    public IPage<BatchDTO> convertPage(IPage<Batch> source) {
        IPage<BatchDTO> batchDTOIPage = new Page<>();

        BeanUtils.copyProperties(source, batchDTOIPage);

        List<BatchDTO> batchDTOList = new ArrayList<>();
        for(Batch batch : source.getRecords()) {
            BatchDTO batchDTO = new BatchDTO();
            BeanUtils.copyProperties(batch, batchDTO);
            batchDTOList.add(batchDTO);
        }
        batchDTOIPage.setRecords(batchDTOList);
        return batchDTOIPage;
    }
}
