package top.belongme.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Title: SystemController
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/3/2512:09
 */
@Controller
@RequestMapping("/system")
public class SystemController {

    @PostMapping("/shutdown")
    @PreAuthorize("hasAuthority('job:system:shutdown')")
    public String shutDown() {
        return "forward:/actuator/shutdown";
    }

}
