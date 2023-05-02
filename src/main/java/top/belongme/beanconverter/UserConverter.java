package top.belongme.beanconverter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import top.belongme.model.dto.UserDTO;
import top.belongme.model.pojo.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: UserConverter
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/5/21:44
 */
@Component
public class UserConverter {
    public IPage<UserDTO> convertPage(IPage<User> userIPage) {
        IPage<UserDTO> userDTOIPage = new Page<>();
        BeanUtils.copyProperties(userIPage, userDTOIPage);

        List<UserDTO> userDTOList = new ArrayList<>();

        userIPage.getRecords().forEach(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTOList.add(userDTO);
        });
        userDTOIPage.setRecords(userDTOList);
        return userDTOIPage;
    }


    public UserDTO convertPage(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}
