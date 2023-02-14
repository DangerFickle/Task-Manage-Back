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

 Date: 14/02/2023 19:21:51
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
  `end_time` datetime(3) NULL DEFAULT '1970-01-01 00:00:00.000' COMMENT '批次截至时间',
  `folder_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次对应的文件夹路径',
  `belong_course_id` bigint NOT NULL COMMENT '批次所属的课程',
  `creator_id` bigint NOT NULL COMMENT '创建者id',
  `modifier_id` bigint NOT NULL COMMENT '修改者ID',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1625102331855589379 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '批次表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for job_course
-- ----------------------------
DROP TABLE IF EXISTS `job_course`;
CREATE TABLE `job_course`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程id',
  `course_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程名',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '课程描述',
  `folder_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程对应的文件夹路径',
  `creator_id` bigint NOT NULL COMMENT '创建者id',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（1：正常 0：停用）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
  `modifier_id` bigint NOT NULL COMMENT '修改者ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `course_name`(`course_name`, `folder_path`) USING BTREE,
  INDEX `creator_id`(`creator_id`) USING BTREE,
  CONSTRAINT `job_course_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `job_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1625102378873737219 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '课程表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for job_task
-- ----------------------------
DROP TABLE IF EXISTS `job_task`;
CREATE TABLE `job_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '作业id',
  `file_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作业文件名，包含后缀名',
  `file_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作业文件路径',
  `uploader_id` bigint NOT NULL COMMENT '姓名',
  `belong_batch_id` bigint NOT NULL COMMENT '所属批次id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uploader_id`(`uploader_id`) USING BTREE,
  INDEX `belong_batch_id`(`belong_batch_id`) USING BTREE,
  CONSTRAINT `job_task_ibfk_1` FOREIGN KEY (`uploader_id`) REFERENCES `job_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `job_task_ibfk_2` FOREIGN KEY (`belong_batch_id`) REFERENCES `job_batch` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1625095906332876802 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '作业表' ROW_FORMAT = Dynamic;

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
  `avatar` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像Base64编码',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（1：正常 0：停用）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
  `role_id` bigint NOT NULL DEFAULT 3 COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  CONSTRAINT `job_user_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `job_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1624393284304052227 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
