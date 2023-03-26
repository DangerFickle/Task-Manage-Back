package top.belongme.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Title: User
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/3/2619:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExcel {
    @ExcelProperty("学号")
    private String studentNumber;
    @ExcelProperty("姓名")
    private String name;
}
