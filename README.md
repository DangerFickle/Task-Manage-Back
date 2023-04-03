# 拯救学习委员之作业管理系统—后端部分

# 前端部分：<a href="https://github.com/DangerFickle/Task-Manage-Front" target="_blank">拯救学习委员之作业管理系统—前端部分</a>



## 环境

**基于SpringBoot2**



## 涉及技术

* **Spring**
* **SpringMVC**
* **SpringSecurity**
* **MyBatisPlus**
* **Redis**
* **Druid**
* **Java IO**



## 部署

* 下载项目导入到IntelliJ Idea

* **不管是本地还是服务器上记得安装Redis！**

* 修改配置文件（application-pro.yml）

  * 将数据库配置改为自己的

    ![](https://img.belongme.top/images/202304032222348.png)

  * 将邮件配置为自己的

    ![](https://img.belongme.top/images/202304032320456.png)

* 运行maven打包插件

  * 点击package![](https://img.belongme.top/images/202304032224893.png)

  * 在target目录下会生成一个以【.jar】为后缀的文件，后续部署到服务器时会使用

    ![](https://img.belongme.top/images/202304032226925.png)

