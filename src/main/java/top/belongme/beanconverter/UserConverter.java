package top.belongme.beanconverter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import top.belongme.model.vo.UserVO;
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
    public IPage<UserVO> convertPage(IPage<User> userIPage) {
        IPage<UserVO> userDTOIPage = new Page<>();
        BeanUtils.copyProperties(userIPage, userDTOIPage);

        List<UserVO> userVOList = new ArrayList<>();

        userIPage.getRecords().forEach(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVOList.add(userVO);
        });
        userDTOIPage.setRecords(userVOList);
        return userDTOIPage;
    }


    public UserVO convertPage(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
