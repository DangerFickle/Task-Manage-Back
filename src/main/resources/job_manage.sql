/*
 Navicat Premium Data Transfer

 Source Server         : 作业管理系统
 Source Server Type    : MySQL
 Source Server Version : 50650
 Source Host           : 162.14.83.179:3306
 Source Schema         : job_manage

 Target Server Type    : MySQL
 Target Server Version : 50650
 File Encoding         : 65001

 Date: 25/03/2023 13:28:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for job_batch
-- ----------------------------
DROP TABLE IF EXISTS `job_batch`;
CREATE TABLE `job_batch`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '批次id',
  `batch_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '批次描述',
  `end_time` datetime(3) NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '批次截至时间',
  `folder_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次对应的文件夹路径',
  `belong_course_id` bigint(20) NOT NULL COMMENT '批次所属的课程',
  `creator_id` bigint(20) NOT NULL COMMENT '创建者id',
  `modifier_id` bigint(20) NOT NULL COMMENT '修改者ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `batch_name`(`batch_name`, `folder_path`) USING BTREE,
  INDEX `creator_id`(`creator_id`) USING BTREE,
  INDEX `update_by`(`modifier_id`) USING BTREE,
  INDEX `belong_course`(`belong_course_id`) USING BTREE,
  CONSTRAINT `job_batch_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `job_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `job_batch_ibfk_2` FOREIGN KEY (`modifier_id`) REFERENCES `job_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `job_batch_ibfk_3` FOREIGN KEY (`belong_course_id`) REFERENCES `job_course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1638926902552973314 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '批次表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of job_batch
-- ----------------------------
INSERT INTO `job_batch` VALUES (1627616322357637122, '测试批次', '测试批次', '1970-01-01 00:00:00.000', '/www/wwwroot/JavaProject/TaskManage-Back/JobManage/测试课程/测试批次', 1625772321090387969, 1, 1, '2023-02-20 18:29:05', '2023-03-15 15:56:24');

-- ----------------------------
-- Table structure for job_course
-- ----------------------------
DROP TABLE IF EXISTS `job_course`;
CREATE TABLE `job_course`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '课程id',
  `course_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程名',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '课程描述',
  `folder_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程对应的文件夹路径',
  `creator_id` bigint(20) NOT NULL COMMENT '创建者id',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态（1：正常 0：停用）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
  `modifier_id` bigint(20) NOT NULL COMMENT '修改者ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `course_name`(`course_name`, `folder_path`) USING BTREE,
  INDEX `creator_id`(`creator_id`) USING BTREE,
  CONSTRAINT `job_course_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `job_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1626420561745833986 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '课程表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of job_course
-- ----------------------------
INSERT INTO `job_course` VALUES (1625772321090387969, '测试课程', '测试课程', '/www/wwwroot/JavaProject/TaskManage-Back/JobManage/测试课程', 1, 1, '2023-02-15 16:21:41', '2023-02-15 16:21:41', 0, 1);

-- ----------------------------
-- Table structure for job_menu
-- ----------------------------
DROP TABLE IF EXISTS `job_menu`;
CREATE TABLE `job_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '所属上级',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '类型(0:目录,1:菜单,2:按钮)',
  `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `perms` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `sort_value` int(11) NULL DEFAULT NULL COMMENT '排序',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态(1:正常,0:禁止)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of job_menu
-- ----------------------------
INSERT INTO `job_menu` VALUES (1, 0, '作业管理', 0, 'jobManage', NULL, NULL, NULL, NULL, 1, '2023-02-07 12:52:16', '2023-03-25 13:24:43', 0);
INSERT INTO `job_menu` VALUES (2, 1, '课程管理', 1, 'courseManage', NULL, NULL, NULL, NULL, 1, '2023-02-07 12:53:04', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (3, 1, '批次管理', 1, 'batchManage', NULL, NULL, NULL, NULL, 1, '2023-02-07 12:53:19', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (4, 0, '提交详情', 0, 'submissionDetails', NULL, NULL, NULL, NULL, 1, '2023-02-07 12:54:02', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (5, 0, '人员管理', 0, 'personnelManage', NULL, NULL, NULL, NULL, 1, '2023-02-07 12:54:21', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (6, 0, '个人中心', 0, 'personal', NULL, NULL, NULL, NULL, 1, '2023-02-07 12:54:52', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (7, 2, '添加', 2, NULL, NULL, 'job:course:insert', NULL, NULL, 1, '2023-02-07 16:52:23', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (8, 2, '删除', 2, NULL, NULL, 'job:course:delete', NULL, NULL, 1, '2023-02-07 19:27:39', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (9, 2, '修改', 2, NULL, NULL, 'job:course:update', NULL, NULL, 1, '2023-02-07 19:29:58', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (10, 2, '查看', 2, NULL, NULL, 'job:course:select', NULL, NULL, 1, '2023-02-07 19:30:38', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (11, 3, '添加', 2, NULL, NULL, 'job:batch:insert', NULL, NULL, 1, '2023-02-07 20:14:37', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (12, 3, '删除', 2, NULL, NULL, 'job:batch:delete', NULL, NULL, 1, '2023-02-07 20:14:37', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (13, 3, '修改', 2, NULL, NULL, 'job:batch:update', NULL, NULL, 1, '2023-02-07 20:14:37', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (14, 3, '查看', 2, NULL, NULL, 'job:batch:select', NULL, NULL, 1, '2023-02-07 20:14:37', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (18, 4, '查看', 2, NULL, NULL, 'job:taskDetail:select', NULL, NULL, 1, '2023-02-07 20:16:45', '2023-03-25 13:24:43', 0);
INSERT INTO `job_menu` VALUES (19, 5, '添加', 2, NULL, NULL, 'job:user:insert', NULL, NULL, 1, '2023-02-07 20:17:36', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (20, 5, '删除', 2, NULL, NULL, 'job:user:delete', NULL, NULL, 1, '2023-02-07 20:17:36', '2023-03-25 13:24:43', 0);
INSERT INTO `job_menu` VALUES (21, 5, '修改', 2, NULL, NULL, 'job:user:update', NULL, NULL, 1, '2023-02-07 20:17:36', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (22, 5, '查看', 2, NULL, NULL, 'job:user:select', NULL, NULL, 1, '2023-02-07 20:17:36', '2023-03-25 13:24:42', 0);
INSERT INTO `job_menu` VALUES (25, 6, '修改', 2, NULL, NULL, 'job:personal:update', NULL, NULL, 1, '2023-02-07 20:18:35', '2023-03-25 13:24:43', 0);
INSERT INTO `job_menu` VALUES (26, 6, '查看', 2, NULL, NULL, 'job:personal:select', NULL, NULL, 1, '2023-02-07 20:18:35', '2023-03-25 13:24:43', 0);
INSERT INTO `job_menu` VALUES (27, 1, '添加', 2, NULL, NULL, 'job:task:insert', NULL, NULL, 1, '2023-02-09 16:33:42', '2023-02-10 18:16:45', 0);
INSERT INTO `job_menu` VALUES (28, 1, '删除', 2, NULL, NULL, 'job:task:delete', NULL, NULL, 1, '2023-02-09 16:33:42', '2023-02-10 18:16:45', 0);
INSERT INTO `job_menu` VALUES (30, 1, '查看', 2, NULL, NULL, 'job:task:select', NULL, NULL, 1, '2023-02-09 16:33:42', '2023-02-10 18:16:45', 0);
INSERT INTO `job_menu` VALUES (31, 0, '关闭系统', 2, NULL, NULL, 'job:system:shutdown', NULL, NULL, 1, '2023-03-25 13:24:18', '2023-03-25 13:24:18', 0);

-- ----------------------------
-- Table structure for job_role
-- ----------------------------
DROP TABLE IF EXISTS `job_role`;
CREATE TABLE `job_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `role_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of job_role
-- ----------------------------
INSERT INTO `job_role` VALUES (1, '系统管理员', 'system', NULL, '2023-02-07 12:46:09', '2023-02-07 12:46:09', 0);
INSERT INTO `job_role` VALUES (2, '普通管理员', 'normal', NULL, '2023-02-07 16:18:28', '2023-02-07 16:18:38', 0);
INSERT INTO `job_role` VALUES (3, '普通用户', 'user', NULL, '2023-02-07 16:17:45', '2023-02-07 16:18:38', 0);

-- ----------------------------
-- Table structure for job_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `job_role_menu`;
CREATE TABLE `job_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL DEFAULT 0,
  `menu_id` bigint(20) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `menu_id`(`menu_id`) USING BTREE,
  CONSTRAINT `job_role_menu_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `job_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `job_role_menu_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `job_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单' ROW_FORMAT = DYNAMIC;

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
INSERT INTO `job_role_menu` VALUES (55, 1, 31, '2023-03-25 13:26:03', '2023-03-25 13:26:03', 0);

-- ----------------------------
-- Table structure for job_task
-- ----------------------------
DROP TABLE IF EXISTS `job_task`;
CREATE TABLE `job_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '作业id',
  `file_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作业文件名，包含后缀名',
  `file_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作业文件路径',
  `uploader_id` bigint(20) NOT NULL COMMENT '姓名',
  `belong_batch_id` bigint(20) NOT NULL COMMENT '所属批次id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uploader_id`(`uploader_id`) USING BTREE,
  INDEX `belong_batch_id`(`belong_batch_id`) USING BTREE,
  CONSTRAINT `job_task_ibfk_1` FOREIGN KEY (`uploader_id`) REFERENCES `job_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `job_task_ibfk_2` FOREIGN KEY (`belong_batch_id`) REFERENCES `job_batch` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1638937266757832707 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '作业表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of job_task
-- ----------------------------

-- ----------------------------
-- Table structure for job_user
-- ----------------------------
DROP TABLE IF EXISTS `job_user`;
CREATE TABLE `job_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `student_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像Base64编码',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态（1：正常 0：停用）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
  `role_id` bigint(20) NOT NULL DEFAULT 3 COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  CONSTRAINT `job_user_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `job_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1638936829086404610 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of job_user
-- ----------------------------
INSERT INTO `job_user` VALUES (1, 'admin', '$2a$10$wfbXaljOvKTVM/FeYG3SPO/p1THg2Uds6XS.BCTob4sBi8gl7d2ju', '系统管理员', '888888888888', null, 'https://img.belongme.top/JobManage/avatar/admin.png', 0, '2023-02-07 11:43:29', '2023-03-23 23:32:24', 0, 1);

SET FOREIGN_KEY_CHECKS = 1;
