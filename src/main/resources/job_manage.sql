/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : job_manage

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 29/10/2024 20:22:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for job_batch
-- ----------------------------
DROP TABLE IF EXISTS `job_batch`;
CREATE TABLE `job_batch`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '批次id',
  `batch_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '批次描述',
  `end_time` datetime(3) NOT NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '批次截至时间',
  `belong_course_id` bigint NOT NULL COMMENT '批次所属的课程',
  `creator_id` bigint NOT NULL COMMENT '创建者id',
  `modifier_id` bigint NOT NULL COMMENT '修改者ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `batch_type` enum('personal','group') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次类型【personal或group】',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `creator_id`(`creator_id`) USING BTREE,
  INDEX `update_by`(`modifier_id`) USING BTREE,
  INDEX `belong_course`(`belong_course_id`) USING BTREE,
  CONSTRAINT `job_batch_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `job_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `job_batch_ibfk_2` FOREIGN KEY (`modifier_id`) REFERENCES `job_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `job_batch_ibfk_3` FOREIGN KEY (`belong_course_id`) REFERENCES `job_course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1851233882154770435 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '批次表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_batch
-- ----------------------------
INSERT INTO `job_batch` VALUES (1851233882154770434, '第一次案例分析', '第一次案例分析', '1970-01-01 00:00:00.000', 1851233740152414209, 1, 1, '2024-10-29 20:05:26', '2024-10-29 20:08:56', 'personal');

-- ----------------------------
-- Table structure for job_course
-- ----------------------------
DROP TABLE IF EXISTS `job_course`;
CREATE TABLE `job_course`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程id',
  `course_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程名',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '课程描述',
  `creator_id` bigint NOT NULL COMMENT '创建者id',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（1：正常 0：停用）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
  `modifier_id` bigint NOT NULL COMMENT '修改者ID',
  `group_max_member_size` int NULL DEFAULT NULL COMMENT '课程下的每个群组的最大人数',
  `group_max_size` int NOT NULL DEFAULT 0 COMMENT '课程下最大的 群组数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `course_name`(`course_name`) USING BTREE,
  INDEX `creator_id`(`creator_id`) USING BTREE,
  CONSTRAINT `job_course_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `job_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1851233740152414210 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '课程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_course
-- ----------------------------
INSERT INTO `job_course` VALUES (1851233740152414209, '软件过程与项目管理', '软件过程与项目管理', 1, 1, '2024-10-29 20:04:37', '2024-10-29 20:04:39', 0, 1, 6, 7);

-- ----------------------------
-- Table structure for job_group
-- ----------------------------
DROP TABLE IF EXISTS `job_group`;
CREATE TABLE `job_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '群组id',
  `leader` bigint NOT NULL COMMENT '群主',
  `belong_course` bigint NULL DEFAULT NULL COMMENT '所属课程id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `already_member` int NULL DEFAULT 1 COMMENT '已有人数',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群组的名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `job_group___fk`(`leader`) USING BTREE,
  INDEX `job_group_job_course_id_fk`(`belong_course`) USING BTREE,
  CONSTRAINT `job_group___fk` FOREIGN KEY (`leader`) REFERENCES `job_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `job_group_job_course_id_fk` FOREIGN KEY (`belong_course`) REFERENCES `job_course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1851112060289650690 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_group
-- ----------------------------

-- ----------------------------
-- Table structure for job_menu
-- ----------------------------
DROP TABLE IF EXISTS `job_menu`;
CREATE TABLE `job_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '所属上级',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `type` tinyint NOT NULL DEFAULT 0 COMMENT '类型(0:目录,1:菜单,2:按钮)',
  `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `perms` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `sort_value` int NULL DEFAULT NULL COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(1:正常,0:禁止)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_menu
-- ----------------------------
INSERT INTO `job_menu` VALUES (1, 0, '作业管理', 0, 'jobManage', NULL, NULL, NULL, NULL, 1, '2023-02-07 12:52:16', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (2, 0, '课程管理', 1, 'courseManage', NULL, NULL, NULL, NULL, 1, '2023-02-07 12:53:04', '2023-12-08 19:04:41', 0);
INSERT INTO `job_menu` VALUES (3, 0, '批次管理', 1, 'batchManage', NULL, NULL, NULL, NULL, 1, '2023-02-07 12:53:19', '2023-12-08 19:04:41', 0);
INSERT INTO `job_menu` VALUES (4, 0, '提交详情', 0, 'submissionDetails', NULL, NULL, NULL, NULL, 1, '2023-02-07 12:54:02', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (5, 0, '人员管理', 0, 'personnelManage', NULL, NULL, NULL, NULL, 1, '2023-02-07 12:54:21', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (6, 0, '个人中心', 0, 'personal', NULL, NULL, NULL, NULL, 1, '2023-02-07 12:54:52', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (7, 2, '添加', 2, NULL, NULL, 'job:course:insert', NULL, NULL, 1, '2023-02-07 16:52:23', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (8, 2, '删除', 2, NULL, NULL, 'job:course:delete', NULL, NULL, 1, '2023-02-07 19:27:39', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (9, 2, '修改', 2, NULL, NULL, 'job:course:update', NULL, NULL, 1, '2023-02-07 19:29:58', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (10, 2, '查看', 2, NULL, NULL, 'job:course:select', NULL, NULL, 1, '2023-02-07 19:30:38', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (11, 3, '添加', 2, NULL, NULL, 'job:batch:insert', NULL, NULL, 1, '2023-02-07 20:14:37', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (12, 3, '删除', 2, NULL, NULL, 'job:batch:delete', NULL, NULL, 1, '2023-02-07 20:14:37', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (13, 3, '修改', 2, NULL, NULL, 'job:batch:update', NULL, NULL, 1, '2023-02-07 20:14:37', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (14, 3, '查看', 2, NULL, NULL, 'job:batch:select', NULL, NULL, 1, '2023-02-07 20:14:37', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (18, 4, '查看', 2, NULL, NULL, 'job:taskDetail:select', NULL, NULL, 1, '2023-02-07 20:16:45', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (19, 5, '添加', 2, NULL, NULL, 'job:user:insert', NULL, NULL, 1, '2023-02-07 20:17:36', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (20, 5, '删除', 2, NULL, NULL, 'job:user:delete', NULL, NULL, 1, '2023-02-07 20:17:36', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (21, 5, '修改', 2, NULL, NULL, 'job:user:update', NULL, NULL, 1, '2023-02-07 20:17:36', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (22, 5, '查看', 2, NULL, NULL, 'job:user:select', NULL, NULL, 1, '2023-02-07 20:17:36', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (25, 6, '修改', 2, NULL, NULL, 'job:personal:update', NULL, NULL, 1, '2023-02-07 20:18:35', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (26, 6, '查看', 2, NULL, NULL, 'job:personal:select', NULL, NULL, 1, '2023-02-07 20:18:35', '2023-03-25 13:24:53', 0);
INSERT INTO `job_menu` VALUES (27, 1, '添加', 2, NULL, NULL, 'job:task:insert', NULL, NULL, 1, '2023-02-09 16:33:42', '2023-02-10 18:16:45', 0);
INSERT INTO `job_menu` VALUES (28, 1, '删除', 2, NULL, NULL, 'job:task:delete', NULL, NULL, 1, '2023-02-09 16:33:42', '2023-02-10 18:16:45', 0);
INSERT INTO `job_menu` VALUES (30, 1, '查看', 2, NULL, NULL, 'job:task:select', NULL, NULL, 1, '2023-02-09 16:33:42', '2023-02-10 18:16:45', 0);
INSERT INTO `job_menu` VALUES (31, 0, '关闭系统', 2, NULL, NULL, 'job:system:shutdown', NULL, NULL, 1, '2023-03-25 12:15:48', '2023-03-25 12:15:48', 0);
INSERT INTO `job_menu` VALUES (32, 0, '导出信息Excel', 2, NULL, NULL, 'job:userInfo:export', NULL, NULL, 1, '2023-03-26 17:36:38', '2023-03-26 17:36:38', 0);

-- ----------------------------
-- Table structure for job_role
-- ----------------------------
DROP TABLE IF EXISTS `job_role`;
CREATE TABLE `job_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `role_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_role
-- ----------------------------
INSERT INTO `job_role` VALUES (1, '系统管理员', 'system', NULL, '2023-02-07 12:46:09', '2023-02-07 12:46:09', 0);
INSERT INTO `job_role` VALUES (2, '班级委员', 'normal', NULL, '2023-02-07 16:18:28', '2024-04-08 22:58:25', 0);
INSERT INTO `job_role` VALUES (3, '普通同学', 'user', NULL, '2023-02-07 16:17:45', '2024-04-08 22:58:47', 0);

-- ----------------------------
-- Table structure for job_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `job_role_menu`;
CREATE TABLE `job_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL DEFAULT 0,
  `menu_id` bigint NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `menu_id`(`menu_id`) USING BTREE,
  CONSTRAINT `job_role_menu_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `job_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `job_role_menu_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `job_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_role_menu
-- ----------------------------
INSERT INTO `job_role_menu` VALUES (2, 1, 7, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (3, 1, 8, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (4, 1, 9, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (5, 1, 10, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (6, 1, 11, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (7, 1, 12, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (8, 1, 13, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (9, 1, 14, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (13, 1, 18, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (14, 1, 19, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (15, 1, 20, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (16, 1, 21, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (17, 1, 22, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (20, 1, 25, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (21, 1, 26, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` VALUES (26, 2, 7, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (27, 2, 8, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (28, 2, 9, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (29, 2, 10, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (30, 2, 11, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (31, 2, 12, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (32, 2, 13, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (33, 2, 14, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (37, 2, 18, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (38, 2, 22, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (39, 2, 25, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (40, 2, 26, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (41, 2, 27, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (42, 2, 28, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (44, 2, 30, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` VALUES (45, 1, 30, '2023-02-14 14:26:11', '2023-02-14 14:26:11', 0);
INSERT INTO `job_role_menu` VALUES (46, 2, 22, '2023-02-14 14:26:36', '2023-02-14 14:26:36', 0);
INSERT INTO `job_role_menu` VALUES (47, 3, 25, '2023-02-14 14:29:27', '2023-02-14 14:29:27', 0);
INSERT INTO `job_role_menu` VALUES (48, 3, 26, '2023-02-14 14:29:27', '2023-02-14 14:29:27', 0);
INSERT INTO `job_role_menu` VALUES (49, 3, 27, '2023-02-14 14:29:27', '2023-02-14 14:29:27', 0);
INSERT INTO `job_role_menu` VALUES (50, 3, 28, '2023-02-14 14:29:27', '2023-02-14 14:29:27', 0);
INSERT INTO `job_role_menu` VALUES (51, 3, 10, '2023-02-14 14:30:04', '2023-02-14 14:30:04', 0);
INSERT INTO `job_role_menu` VALUES (52, 3, 14, '2023-02-14 14:30:04', '2023-02-14 14:30:04', 0);
INSERT INTO `job_role_menu` VALUES (53, 1, 31, '2023-03-25 12:16:12', '2023-03-25 12:16:12', 0);
INSERT INTO `job_role_menu` VALUES (54, 1, 32, '2023-03-26 17:36:58', '2023-03-26 17:36:58', 0);
INSERT INTO `job_role_menu` VALUES (55, 2, 32, '2023-03-26 21:11:34', '2023-03-26 21:11:34', 0);
INSERT INTO `job_role_menu` VALUES (56, 3, 30, '2023-03-26 22:11:35', '2023-03-26 22:11:35', 0);
INSERT INTO `job_role_menu` VALUES (57, 3, 22, '2023-12-17 20:36:39', '2023-12-17 20:36:39', 0);

-- ----------------------------
-- Table structure for job_task
-- ----------------------------
DROP TABLE IF EXISTS `job_task`;
CREATE TABLE `job_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '作业id',
  `file_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作业文件名，包含后缀名',
  `file_sha256` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件的Sha256值',
  `uploader_id` bigint NOT NULL COMMENT '上传者id，userId或groupId',
  `belong_batch_id` bigint NOT NULL COMMENT '所属批次id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uploader_id`(`uploader_id`) USING BTREE,
  INDEX `belong_batch_id`(`belong_batch_id`) USING BTREE,
  CONSTRAINT `job_task_ibfk_2` FOREIGN KEY (`belong_batch_id`) REFERENCES `job_batch` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1851234374570254338 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '作业表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_task
-- ----------------------------

-- ----------------------------
-- Table structure for job_user
-- ----------------------------
DROP TABLE IF EXISTS `job_user`;
CREATE TABLE `job_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `student_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif' COMMENT '头像Base64编码',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0：正常 1：停用）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `role_id` bigint NOT NULL DEFAULT 3 COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  CONSTRAINT `job_user_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `job_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1851234208719085572 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_user
-- ----------------------------
INSERT INTO `job_user` VALUES (1, 'admin', '$2a$10$o78pTOieHqOj06WbKigU5.AjB1DQjpdcJfTQS1.Eb64hjqyB1gt82', '系统管理员', '888888888888', '1975037337@qq.com', '\r\nhttps://img.belongme.top/JobManage/avatar/admin.png', 0, '2023-02-07 11:43:29', '2024-10-27 16:54:13', 1);
INSERT INTO `job_user` VALUES (1851090312785674242, '5320240677', '$2a$10$FITTYBdcknVk2cPFJRaze.wAqNAI1a5YL9XXfrYVhAzgOrV97IUE.', '吴祖乐', '5320240677', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:56', '2024-10-29 10:34:56', 3);
INSERT INTO `job_user` VALUES (1851090312785674243, '5320240700', '$2a$10$rZD/xU9Ml0GmAiQpHTVxjeWhKVXaiJfDWNC3n/BOUw0l8VCNJvw0e', '晏俊骐', '5320240700', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:56', '2024-10-29 10:34:56', 3);
INSERT INTO `job_user` VALUES (1851090312785674244, '5320240702', '$2a$10$jEbYeuI8rcI9TB2rDsYr0u3SKO3wL.jTDh7hBuZxrzvBL3b653Ojy', '邓超', '5320240702', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:56', '2024-10-29 17:06:06', 2);
INSERT INTO `job_user` VALUES (1851090312785674245, '5320240693', '$2a$10$pZc5xw9vDJJ0s1/2gZsHDe1n5QQPLvpzXE5Jexw1Tfn0qsHeUEsiu', '唐洪悦', '5320240693', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312785674246, '5320240687', '$2a$10$6DUC4cViGAL7O0CsoATSKuuJchPq.Dvvz.cstwMIeBkLR8ZF6dpsa', '李美琦', '5320240687', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312785674247, '5320240705', '$2a$10$iiEydiwNvLfGVDaA.lkX2uDpXlPOB/FV12oOnDWPvePDHQHG.7noW', '周李强', '5320240705', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312785674248, '5320240686', '$2a$10$wgQYcn0k2yZGH.ZPe32/.uR.VTbcfyZKpdSWyn8s09ELH3G23wWjq', '吴勉', '5320240686', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034369, '5320240683', '$2a$10$rwew6OW/xW96SulrArDKqO2oO.d4T72pqLLETsLwsxd0j9ka6ND4y', '郭思雨', '5320240683', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034370, '5320240701', '$2a$10$WTPPRXyWBPQz5OoLXXMvzuUv6PK6/oh5JoWaZcqq/P58aGc4cgjRO', '吴宏煜', '5320240701', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034371, '5320240703', '$2a$10$2c9Q5H4pC8LsjYIxQWRDOO5j.Dhq94CplBjMn0YaMAc9b5daS7QMW', '张霆宇', '5320240703', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034372, '5320240689', '$2a$10$d9heUdyvjcv85ExQSCL7G.jz3OOcqvN2x0d2aXordnwVBGY4.26IG', '袁林', '5320240689', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034373, '5320240692', '$2a$10$Mvyw2FAm2J0W6P5LHcuzcuN33qs2xpaX6rWpwQ37qCytC5kZW5a4q', '朱玉明', '5320240692', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034374, '5320240688', '$2a$10$OtcsUYiEoxgv4ZZ4h2NuL.HBWIXKNxORu9iOKzzOmcVolqxSmoWb6', '罗松松', '5320240688', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034375, '5320240681', '$2a$10$fdzrKZfZY/wptl4V5zAOZusjoUby/p73hdrddh9Jh.dJXD1BZnN1e', '侯志伟', '5320240681', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034376, '5320240698', '$2a$10$lUb0QytIB8TEc5iiidXRK.Rn63s80gYt9/63WM1xw0/8QD9he3mU6', '杨潇', '5320240698', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034377, '5320240679', '$2a$10$C8Fa2NLgIIw8.UkeQ.5auO47KR8FTcwZoSdEyLPc6vwz1R7HfRQk.', '许文强', '5320240679', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034378, '5320240678', '$2a$10$lo7Q//3y/OnqP1IOQOXQneLfYdAPGSikzayTaoSj9asGBUvUHSuwC', '吴心芸', '5320240678', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034379, '5320240696', '$2a$10$pqUygY0NeT2.DQaadil5zO8DjEWTc8y.Gf4SVoIdMI7NCyKSw6EeG', '魏定清', '5320240696', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034380, '5320240694', '$2a$10$Fjmsc4BiCZUQ46nzxmhiNu9y/ZAtYG4VRg1jhPB1p3cMVINDbXMo6', '杨邦涛', '5320240694', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034381, '5320240706', '$2a$10$ckkkz5gMAdH5pxtXbDnO5.W4uEBzqBjAvbVSGVA1MNcRBthngGiSG', '田明杰', '5320240706', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034382, '5320240691', '$2a$10$wIBPdIAW/akEG2Ncrb.qCueeSu.sNoesDB/2BTo42qIWXJHSGCUDy', '李德超', '5320240691', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034383, '5320240690', '$2a$10$/d3QCac4Udt5JSjvd4pYz.flhGb5D39bL.fLycSD4v00g8Pay6BgS', '冉婷婷', '5320240690', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034384, '5320240704', '$2a$10$3o4/KZWSXQ3RPyTlhzU9xOmRu.i7hBCl/VR.v.LCF0NGkgH9tienu', '蒋盛懋', '5320240704', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034385, '5320240676', '$2a$10$OvaFmbWXruGerONHvk8v.OvoXYoPN/Kpxn2dFn7dkXyoxK9TMfmRa', '兰凡', '5320240676', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034386, '5320240697', '$2a$10$DRLGvYlZZGbRpJLRrldi2uvqocl6S/jnVTwp/3zLkAXFTogtI9Ala', '杨文龙', '5320240697', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034387, '5320240699', '$2a$10$fT6e0qYkzNxz0AC0lMowlOv.wsgNjRBKRG0BMJel67sQPCuN99/6q', '查伟', '5320240699', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034388, '5320240684', '$2a$10$CwgzVBkX1QEAy4altXcKRu.dfYSaK81s947WKTfBtBpbZ3EbopmgC', '徐孟韬', '5320240684', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034389, '5320240695', '$2a$10$sn.mruul7wtclI/UqCjEAuEXtPWLIJHNn3/kc9qinaCTxcfsdDbuO', '苏林杰', '5320240695', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034390, '5320240680', '$2a$10$Ek8pNXDvfPfogU/Nsae2Hui3I3L6rIcPvfkQ9FE8eYpilBIgU3b1W', '陈沁', '5320240680', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034391, '5320240682', '$2a$10$Wm6N7yBxjuD/QYm1DQD13eU6bOGtYOyHpnJzOzwGbkspBvEyPaM.q', '杨洋', '5320240682', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851090312815034392, '5320240685', '$2a$10$PRyHyGulc7llwqnpNJMJce730PmSEAZpoGpXWmNAaA9Ul1YGbCGmS', '李钰', '5320240685', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 10:34:57', '2024-10-29 10:34:57', 3);
INSERT INTO `job_user` VALUES (1851234208660365313, '123', '$2a$10$xVzOs627Nb53oleQiJXgOuof86mGluu92O3HcTK6QtjvJrBaRBJtq', '123', '123', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 20:06:44', '2024-10-29 20:06:44', 3);
INSERT INTO `job_user` VALUES (1851234208660365314, '456', '$2a$10$0PAjWQqv2ZCStbao2621vOe37B7Us5nxHejxerUL4c0JgWwWVo85q', '456', '456', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 20:06:44', '2024-10-29 20:06:44', 3);
INSERT INTO `job_user` VALUES (1851234208660365315, '789', '$2a$10$nECDiWHRtOXpUtNncjOHaOpgYbd5Ugcf1qiO1Z3cBuR/LPr3RU7fC', '789', '789', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 20:06:44', '2024-10-29 20:06:44', 3);
INSERT INTO `job_user` VALUES (1851234208660365316, '321', '$2a$10$dzBJKMnZdzWvQdQyxNa77OCuCJSYNt9mdj1o6JNIZBDZDLzn3NvwK', '321', '321', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 20:06:44', '2024-10-29 20:06:44', 3);
INSERT INTO `job_user` VALUES (1851234208660365317, '654', '$2a$10$Y1mO9nt1yXSFK6TmKbHUxOyWmMaHBW4nECLYaB6EnLA9ywi5/0heS', '645', '654', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 20:06:44', '2024-10-29 20:06:44', 3);
INSERT INTO `job_user` VALUES (1851234208660365318, '987', '$2a$10$f6rnyUCxZA3lzYVRVE7Bce932QNbe09PPdIOdde9h1msnmsaP9ONa', '987', '987', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 20:06:44', '2024-10-29 20:06:44', 3);
INSERT INTO `job_user` VALUES (1851234208660365319, '213', '$2a$10$59pS9Mv/8Z6t9HutLc1vW.f7k.y6NzAp9mRtEPzdZu7.Ari6lbsiS', '213', '213', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 20:06:44', '2024-10-29 20:06:44', 3);
INSERT INTO `job_user` VALUES (1851234208719085569, '231', '$2a$10$f618tbaOnkvwFVXa4EBcr.jGytxIQxc7y1511hdO9TTvyhXQ0T3wS', '231', '231', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 20:06:44', '2024-10-29 20:06:44', 3);
INSERT INTO `job_user` VALUES (1851234208719085570, '546', '$2a$10$KxqMlJ9S8JGcbGqvbO8YPujEvX4LSQvdGZs1gqZ2hluZ3XO60QqoS', '546', '546', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 20:06:44', '2024-10-29 20:06:44', 3);
INSERT INTO `job_user` VALUES (1851234208719085571, '657', '$2a$10$24VnbvrV0LhtlwaUBGYwvOGdoEfzP7b7dU2w.FsdA7KZhk/lWve.C', '657', '657', NULL, 'https://img.zcool.cn/community/01ed7955430a6e0000019ae9e3bdf3.gif', 0, '2024-10-29 20:06:44', '2024-10-29 20:06:44', 3);

-- ----------------------------
-- Table structure for job_user_group
-- ----------------------------
DROP TABLE IF EXISTS `job_user_group`;
CREATE TABLE `job_user_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `group_id` bigint NULL DEFAULT NULL COMMENT '群组id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '成员加入群组的时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `job_user_group_job_group_id_fk`(`group_id`) USING BTREE,
  INDEX `job_user_group_job_user_id_fk`(`user_id`) USING BTREE,
  CONSTRAINT `job_user_group_job_group_id_fk` FOREIGN KEY (`group_id`) REFERENCES `job_group` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `job_user_group_job_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `job_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1851112060470005762 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_user_group
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
