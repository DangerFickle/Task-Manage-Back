# 拯救学习委员之作业管理系统—后端部分

# 前端部分：<a href="https://github.com/DangerFickle/Task-Manage-Front">拯救学习委员之作业管理系统—前端部分</a>



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



## 功能

* 身份验证（角色分为三个）
  * 系统管理员
  * 普通管理员（通常为班委，只比系统管理员少了人员管理）
  * 普通用户（只能提交和下载自己的作业文件）
* 个人作业提交（仅user用户）
  * 只能提交和下载该批次下自己的作业
* 课程管理（仅system和normal管理员）
  * 一个课程对应一个系统中的同名文件夹，文件夹下有若干的批次文件夹
* 批次管理（仅system和normal管理员）
  * 归属于所属课程
  * 一个批次对应一个系统中的同名文件夹，存在于所属课程文件夹下，提交的作业文件都存储在这里
* 作业提交详情（仅system和normal管理员）
  * 支持一键下载指定批次全部文件
  * 支持下载单个已交人员提交的作业
  * 支持查看指定批次已交人员
  * 支持查看指定批次未交人员，可导出为Excel文件，可邮件提醒指定已绑定邮箱的用户
* 人员管理（仅system管理员）
* 个人信息管理
  * 支持修改密码和邮箱，目前不支持密码找回，如忘记密码需联系系统管理员修改

## 后续展望

* 加入群组功能，针对小组作业

## 部署

* 下载项目导入到IntelliJ Idea

* 创建数据库，运行建表SQL，<a href="https://github.com/DangerFickle/Task-Manage-Back/blob/main/src/main/resources/job_manage.sql">SQL建表脚本</a>

* **不管是本地还是服务器上记得安装Redis！**

* 修改配置文件（application-pro.yml）

  * 将数据库配置改为自己的

    ![](https://img.belongme.top/images/202304032222348.png)

  * 将邮件配置为自己的

    ![](https://img.belongme.top/images/202304032320456.png)

* 运行maven打包插件

  * 点击package![](https://img.belongme.top/images/202304032224893.png)

  * 在target目录下会生成一个以【.jar】为后缀的文件

    * 使用宝塔面板部署到服务器

    * 或命令行运行

      ~~~shell
      java -jar jar文件路径
      ~~~

    

    ![](https://img.belongme.top/images/202304032226925.png)

