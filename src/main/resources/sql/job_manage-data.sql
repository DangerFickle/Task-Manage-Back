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

 Date: 14/02/2023 19:25:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of job_batch
-- ----------------------------

-- ----------------------------
-- Records of job_course
-- ----------------------------

-- ----------------------------
-- Records of job_menu
-- ----------------------------
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (1, 0, '作业管理', 0, 'jobManage', NULL, NULL, NULL, NULL, 0, '2023-02-07 12:52:16', '2023-02-07 19:29:28', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (2, 1, '课程管理', 1, 'courseManage', NULL, NULL, NULL, NULL, 0, '2023-02-07 12:53:04', '2023-02-07 19:29:28', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (3, 1, '批次管理', 1, 'batchManage', NULL, NULL, NULL, NULL, 0, '2023-02-07 12:53:19', '2023-02-07 19:29:28', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (4, 0, '提交详情', 0, 'submissionDetails', NULL, NULL, NULL, NULL, 0, '2023-02-07 12:54:02', '2023-02-07 19:29:28', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (5, 0, '人员管理', 0, 'personnelManage', NULL, NULL, NULL, NULL, 0, '2023-02-07 12:54:21', '2023-02-07 19:29:28', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (6, 0, '个人中心', 0, 'personal', NULL, NULL, NULL, NULL, 0, '2023-02-07 12:54:52', '2023-02-07 19:29:28', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (7, 2, '添加', 2, NULL, NULL, 'job:course:insert', NULL, NULL, 0, '2023-02-07 16:52:23', '2023-02-07 16:52:23', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (8, 2, '删除', 2, NULL, NULL, 'job:course:delete', NULL, NULL, 0, '2023-02-07 19:27:39', '2023-02-07 19:29:28', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (9, 2, '修改', 2, NULL, NULL, 'job:course:update', NULL, NULL, 0, '2023-02-07 19:29:58', '2023-02-07 19:29:58', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (10, 2, '查看', 2, NULL, NULL, 'job:course:select', NULL, NULL, 0, '2023-02-07 19:30:38', '2023-02-07 19:30:38', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (11, 3, '添加', 2, NULL, NULL, 'job:batch:insert', NULL, NULL, 0, '2023-02-07 20:14:37', '2023-02-07 20:14:37', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (12, 3, '删除', 2, NULL, NULL, 'job:batch:delete', NULL, NULL, 0, '2023-02-07 20:14:37', '2023-02-07 20:14:37', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (13, 3, '修改', 2, NULL, NULL, 'job:batch:update', NULL, NULL, 0, '2023-02-07 20:14:37', '2023-02-07 20:14:37', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (14, 3, '查看', 2, NULL, NULL, 'job:batch:select', NULL, NULL, 0, '2023-02-07 20:14:37', '2023-02-07 20:14:37', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (18, 4, '查看', 2, NULL, NULL, 'job:taskDetail:select', NULL, NULL, 0, '2023-02-07 20:16:45', '2023-02-12 20:06:37', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (19, 5, '添加', 2, NULL, NULL, 'job:user:insert', NULL, NULL, 0, '2023-02-07 20:17:36', '2023-02-07 20:17:36', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (20, 5, '删除', 2, NULL, NULL, 'job:user:delete', NULL, NULL, 0, '2023-02-07 20:17:36', '2023-02-07 20:17:36', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (21, 5, '修改', 2, NULL, NULL, 'job:user:update', NULL, NULL, 0, '2023-02-07 20:17:36', '2023-02-07 20:17:36', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (22, 5, '查看', 2, NULL, NULL, 'job:user:select', NULL, NULL, 0, '2023-02-07 20:17:36', '2023-02-07 20:17:36', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (25, 6, '修改', 2, NULL, NULL, 'job:personal:update', NULL, NULL, 0, '2023-02-07 20:18:35', '2023-02-07 20:18:35', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (26, 6, '查看', 2, NULL, NULL, 'job:personal:select', NULL, NULL, 0, '2023-02-07 20:18:35', '2023-02-07 20:18:35', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (27, 1, '添加', 2, NULL, NULL, 'job:task:insert', NULL, NULL, 1, '2023-02-09 16:33:42', '2023-02-10 18:16:45', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (28, 1, '删除', 2, NULL, NULL, 'job:task:delete', NULL, NULL, 1, '2023-02-09 16:33:42', '2023-02-10 18:16:45', 0);
INSERT INTO `job_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `perms`, `icon`, `sort_value`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (30, 1, '查看', 2, NULL, NULL, 'job:task:select', NULL, NULL, 1, '2023-02-09 16:33:42', '2023-02-10 18:16:45', 0);

-- ----------------------------
-- Records of job_role
-- ----------------------------
INSERT INTO `job_role` (`id`, `role_name`, `role_code`, `description`, `create_time`, `update_time`, `is_deleted`) VALUES (1, '系统管理员', 'system', NULL, '2023-02-07 12:46:09', '2023-02-07 12:46:09', 0);
INSERT INTO `job_role` (`id`, `role_name`, `role_code`, `description`, `create_time`, `update_time`, `is_deleted`) VALUES (2, '普通管理员', 'normal', NULL, '2023-02-07 16:18:28', '2023-02-07 16:18:38', 0);
INSERT INTO `job_role` (`id`, `role_name`, `role_code`, `description`, `create_time`, `update_time`, `is_deleted`) VALUES (3, '普通用户', 'user', NULL, '2023-02-07 16:17:45', '2023-02-07 16:18:38', 0);

-- ----------------------------
-- Records of job_role_menu
-- ----------------------------
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (2, 1, 7, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (3, 1, 8, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (4, 1, 9, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (5, 1, 10, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (6, 1, 11, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (7, 1, 12, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (8, 1, 13, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (9, 1, 14, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (13, 1, 18, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (14, 1, 19, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (15, 1, 20, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (16, 1, 21, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (17, 1, 22, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (20, 1, 25, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (21, 1, 26, '2023-02-14 14:11:27', '2023-02-14 14:11:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (26, 2, 7, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (27, 2, 8, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (28, 2, 9, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (29, 2, 10, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (30, 2, 11, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (31, 2, 12, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (32, 2, 13, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (33, 2, 14, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (37, 2, 18, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (38, 2, 22, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (39, 2, 25, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (40, 2, 26, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (41, 2, 27, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (42, 2, 28, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (44, 2, 30, '2023-02-14 14:19:57', '2023-02-14 14:19:57', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (45, 1, 30, '2023-02-14 14:26:11', '2023-02-14 14:26:11', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (46, 2, 22, '2023-02-14 14:26:36', '2023-02-14 14:26:36', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (47, 3, 25, '2023-02-14 14:29:27', '2023-02-14 14:29:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (48, 3, 26, '2023-02-14 14:29:27', '2023-02-14 14:29:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (49, 3, 27, '2023-02-14 14:29:27', '2023-02-14 14:29:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (50, 3, 28, '2023-02-14 14:29:27', '2023-02-14 14:29:27', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (51, 3, 10, '2023-02-14 14:30:04', '2023-02-14 14:30:04', 0);
INSERT INTO `job_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`) VALUES (52, 3, 14, '2023-02-14 14:30:04', '2023-02-14 14:30:04', 0);

-- ----------------------------
-- Records of job_task
-- ----------------------------

-- ----------------------------
-- Records of job_user
-- ----------------------------
INSERT INTO `job_user` (`id`, `username`, `password`, `name`, `student_number`, `email`, `avatar`, `status`, `create_time`, `update_time`, `is_deleted`, `role_id`) VALUES (1, 'admin', '$2a$10$.U861V4IdZcJ6xm.zCtbB.RElYbWrU1YuqPaMUo.Hv1rxs8DKz2d6', '系统管理员', '888888888888', null, 'http://image.imageurl.cf/JobManage/avatar/%E5%A4%B4%E5%83%8F.jpeg', 0, '2023-02-07 11:43:29', '2023-02-14 17:21:01', 0, 1);

SET FOREIGN_KEY_CHECKS = 1;
