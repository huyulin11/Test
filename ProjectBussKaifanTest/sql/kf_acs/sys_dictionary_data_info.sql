-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- Server version:               10.3.11-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL 版本:                  10.1.0.5464
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table kf_acs.sys_dictionary_data_info
DROP TABLE IF EXISTS `sys_dictionary_data_info`;
CREATE TABLE IF NOT EXISTS `sys_dictionary_data_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dictype` varchar(50) NOT NULL COMMENT '字典类型代码',
  `dickey` varchar(50) NOT NULL COMMENT '字典编码',
  `dicvalue` varchar(40) DEFAULT NULL COMMENT '字典值',
  `dicalias` varchar(40) DEFAULT NULL COMMENT '字典值别名',
  `dicvalueus` varchar(20) DEFAULT NULL COMMENT '字典值英文',
  `remark` varchar(50) DEFAULT NULL COMMENT '字典说明',
  `delflag` varchar(1) DEFAULT '0' COMMENT '删除标志-1表示删除，0代表未删除',
  `dicnormaluser` varchar(1) DEFAULT '1' COMMENT '普通用户标志-1表示是，0代表不是（即后台操作员）',
  `dicdefau` int(1) DEFAULT 0 COMMENT '默认选项',
  `sortflag` int(11) DEFAULT 0 COMMENT '标准的排序方式',
  PRIMARY KEY (`dictype`,`dickey`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=466 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='常规字典表明细表';

-- Dumping data for table kf_acs.sys_dictionary_data_info: ~108 rows (approximately)
DELETE FROM `sys_dictionary_data_info`;
/*!40000 ALTER TABLE `sys_dictionary_data_info` DISABLE KEYS */;
INSERT INTO `sys_dictionary_data_info` (`id`, `dictype`, `dickey`, `dicvalue`, `dicalias`, `dicvalueus`, `remark`, `delflag`, `dicnormaluser`, `dicdefau`, `sortflag`) VALUES
	(3, 'ACS_CACHE_CABLE', '15', '1号窗口', NULL, NULL, NULL, '0', '1', 0, 0),
	(4, 'ACS_CACHE_CABLE', '16', '2号窗口', NULL, NULL, NULL, '0', '1', 0, 0),
	(5, 'ACS_CACHE_CABLE', '17', '3号窗口', NULL, NULL, NULL, '0', '1', 0, 0),
	(357, 'ACS_STATUS', '-1', '已删除', '', NULL, NULL, '1', '1', 0, 0),
	(6, 'ACS_STATUS', '1', '新建', NULL, NULL, NULL, '0', '1', 0, 0),
	(7, 'ACS_STATUS', '2', '下达', NULL, NULL, NULL, '0', '1', 0, 0),
	(8, 'ACS_STATUS', '3', '执行中', NULL, NULL, NULL, '0', '1', 0, 0),
	(9, 'ACS_STATUS', '4', '执行结束', NULL, NULL, NULL, '0', '1', 0, 0),
	(20, 'ALLOC_ITEM_STATUS', '1', '空', NULL, NULL, NULL, '0', '1', 0, 0),
	(21, 'ALLOC_ITEM_STATUS', '2', '锁定', NULL, NULL, NULL, '0', '1', 0, 0),
	(22, 'ALLOC_ITEM_STATUS', '3', '有货', NULL, NULL, NULL, '0', '1', 0, 0),
	(402, 'ARRIVED_SITE_ACT_TYPE', '0', '启动', NULL, NULL, NULL, '0', '1', 0, 0),
	(383, 'ARRIVED_SITE_ACT_TYPE', '1', '放货', '', NULL, NULL, '0', '1', 0, 0),
	(393, 'ARRIVED_SITE_ACT_TYPE', '10', '减速', '', NULL, NULL, '0', '1', 0, 0),
	(384, 'ARRIVED_SITE_ACT_TYPE', '2', '取货', '', NULL, NULL, '0', '1', 0, 0),
	(385, 'ARRIVED_SITE_ACT_TYPE', '3', '扫描', '', NULL, NULL, '0', '1', 0, 0),
	(386, 'ARRIVED_SITE_ACT_TYPE', '4', '充电', '', NULL, NULL, '0', '1', 0, 0),
	(388, 'ARRIVED_SITE_ACT_TYPE', '5', '窗口放货', '', NULL, NULL, '0', '1', 0, 0),
	(389, 'ARRIVED_SITE_ACT_TYPE', '6', '窗口取货', NULL, NULL, NULL, '0', '1', 0, 0),
	(391, 'ARRIVED_SITE_ACT_TYPE', '7', '左转', '', NULL, NULL, '0', '1', 0, 0),
	(392, 'ARRIVED_SITE_ACT_TYPE', '8', '右转', '', NULL, NULL, '0', '1', 0, 0),
	(387, 'ARRIVED_SITE_ACT_TYPE', '9', '停车', '', NULL, NULL, '0', '1', 0, 0),
	(417, 'ARRIVED_SITE_ACT_TYPE', 'B', '开自动门', NULL, NULL, NULL, '0', '1', 0, 0),
	(418, 'ARRIVED_SITE_ACT_TYPE', 'C', '调升降机', NULL, NULL, NULL, '0', '1', 0, 0),
	(419, 'ARRIVED_SITE_ACT_TYPE', 'D', '交通灯变红', NULL, NULL, NULL, '0', '1', 0, 0),
	(463, 'ARRIVED_SITE_ACT_TYPE', 'E', '挂钩升起', NULL, NULL, NULL, '0', '1', 0, 0),
	(464, 'ARRIVED_SITE_ACT_TYPE', 'F', '挂钩下落', NULL, NULL, NULL, '0', '1', 0, 0),
	(465, 'ARRIVED_SITE_ACT_TYPE', 'S', '停车', NULL, NULL, NULL, '0', '1', 0, 0),
	(23, 'BOOLEAN', '0', '否', NULL, 'no', NULL, '0', '1', 0, 2),
	(24, 'BOOLEAN', '1', '是', NULL, 'yes', NULL, '0', '1', 0, 1),
	(62, 'DE_STATUS', '0', '新建', '', '', '', '0', '1', 0, 0),
	(63, 'DE_STATUS', '1', '开始处理', '', '', '', '0', '1', 0, 0),
	(64, 'DE_STATUS', '2', '处理成功', '', '', '', '0', '1', 0, 0),
	(65, 'DE_STATUS', '3', '处理失败', '', '', '', '0', '1', 0, 0),
	(66, 'DE_STATUS', '4', '不再处理', '', '', '', '0', '1', 0, 0),
	(413, 'est', '1', '1', '2', NULL, NULL, '0', '1', 0, 0),
	(103, 'ISOK', '0', '不可用', '', '', '', '0', '1', 0, 0),
	(104, 'ISOK', '1', '可用', '', '', '', '0', '1', 0, 0),
	(426, 'LAO_AUTO_DOOR_STATUS', '21', '开门到位状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(427, 'LAO_AUTO_DOOR_STATUS', '22', '关门到位状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(428, 'LAO_AUTO_DOOR_STATUS', '23', '允许人员进入状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(429, 'LAO_AUTO_DOOR_STATUS', '24', '不允许人员进入状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(421, 'LAO_AUTO_DOOR_STATUS', 'B1', '开门指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(422, 'LAO_AUTO_DOOR_STATUS', 'B2', '关门指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(423, 'LAO_AUTO_DOOR_STATUS', 'B3', '允许人员进入指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(424, 'LAO_AUTO_DOOR_STATUS', 'B4', '不允许人员进入指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(425, 'LAO_AUTO_DOOR_STATUS', 'B6', '查询指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(436, 'LAO_LIFT_STATUS', '21', '到1F开门到位状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(437, 'LAO_LIFT_STATUS', '22', '到2F开门到位状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(438, 'LAO_LIFT_STATUS', '23', '到3F开门到位状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(439, 'LAO_LIFT_STATUS', '31', '开门到位状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(440, 'LAO_LIFT_STATUS', '32', '关门到位状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(441, 'LAO_LIFT_STATUS', '51', '自动模式状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(442, 'LAO_LIFT_STATUS', '52', '手动模式状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(430, 'LAO_LIFT_STATUS', 'A1', '到1F开门指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(431, 'LAO_LIFT_STATUS', 'A2', '到2F开门指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(432, 'LAO_LIFT_STATUS', 'A3', '到3F开门指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(433, 'LAO_LIFT_STATUS', 'A4', '关门指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(434, 'LAO_LIFT_STATUS', 'A5', '询问模式指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(435, 'LAO_LIFT_STATUS', 'A6', '询问状态指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(453, 'LAO_LIGHT_STATUS', '21', '黄灯亮状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(454, 'LAO_LIGHT_STATUS', '22', '绿灯亮状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(455, 'LAO_LIGHT_STATUS', '23', '红灯亮状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(456, 'LAO_LIGHT_STATUS', '24', '无灯亮状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(457, 'LAO_LIGHT_STATUS', '31', 'B面黄灯亮状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(458, 'LAO_LIGHT_STATUS', '32', 'B面绿灯亮状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(459, 'LAO_LIGHT_STATUS', '33', 'B面红灯亮状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(460, 'LAO_LIGHT_STATUS', '34', 'B面无灯亮状态', NULL, NULL, NULL, '0', '1', 0, 0),
	(443, 'LAO_LIGHT_STATUS', 'C1', '黄灯指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(444, 'LAO_LIGHT_STATUS', 'C2', '绿灯指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(445, 'LAO_LIGHT_STATUS', 'C3', '红灯指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(446, 'LAO_LIGHT_STATUS', 'C4', '关灯指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(447, 'LAO_LIGHT_STATUS', 'C6', '询问状态指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(448, 'LAO_LIGHT_STATUS', 'D1', 'B面黄灯指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(449, 'LAO_LIGHT_STATUS', 'D2', 'B面绿灯指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(450, 'LAO_LIGHT_STATUS', 'D3', 'B面红灯指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(451, 'LAO_LIGHT_STATUS', 'D4', 'B面关灯指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(452, 'LAO_LIGHT_STATUS', 'D6', 'B面询问状态指令', NULL, NULL, NULL, '0', '1', 0, 0),
	(377, 'SOCKET_DEV_TYPE', '0', 'AGV', '', NULL, NULL, '0', '1', 0, 0),
	(373, 'SOCKET_DEV_TYPE', '1', 'AGV上取货机械手', '', NULL, NULL, '0', '1', 0, 0),
	(374, 'SOCKET_DEV_TYPE', '2', 'AGV上竹节', '', NULL, NULL, '0', '1', 0, 0),
	(375, 'SOCKET_DEV_TYPE', '3', 'AGV上扫码枪', '', NULL, NULL, '0', '1', 0, 0),
	(378, 'SOCKET_DEV_TYPE', '4', 'AGV上PLC', '', NULL, NULL, '0', '1', 0, 0),
	(398, 'SOCKET_DEV_TYPE', '5', '自动充电站', '', NULL, NULL, '0', '1', 0, 0),
	(399, 'SOCKET_DEV_TYPE', '6', '存取货窗口', '', NULL, NULL, '0', '1', 0, 0),
	(376, 'SOCKET_DEV_TYPE', '9', '上位机机械手', '', NULL, NULL, '0', '1', 0, 0),
	(420, 'TASK_SITE_TYPE', '0', '普通站点', NULL, NULL, NULL, '0', '1', 0, 0),
	(394, 'TASK_SITE_TYPE', '1', '货位位置站点', '', NULL, NULL, '0', '1', 0, 0),
	(395, 'TASK_SITE_TYPE', '2', '初始位置站点', '', NULL, NULL, '0', '1', 0, 0),
	(396, 'TASK_SITE_TYPE', '3', '取货位置站点', '', NULL, NULL, '0', '1', 0, 0),
	(397, 'TASK_SITE_TYPE', '4', '充电位置站点', '', NULL, NULL, '0', '1', 0, 0),
	(400, 'TASK_SITE_TYPE', '5', '出站主干道站点', '', NULL, NULL, '0', '1', 0, 0),
	(401, 'TASK_SITE_TYPE', '6', '回归主干道站点', '', NULL, NULL, '0', '1', 0, 0),
	(408, 'TASK_SITE_TYPE', '7', '港湾站点', '', NULL, NULL, '0', '1', 0, 0),
	(414, 'TASK_SITE_TYPE', 'B', '自动门控制点', NULL, NULL, NULL, '0', '1', 0, 0),
	(415, 'TASK_SITE_TYPE', 'C', '升降机控制点', NULL, NULL, NULL, '0', '1', 0, 0),
	(416, 'TASK_SITE_TYPE', 'D', '交通灯控制点', NULL, NULL, NULL, '0', '1', 0, 0),
	(461, 'TASK_SITE_TYPE', 'E', '挂钩升起', NULL, NULL, NULL, '0', '1', 0, 0),
	(462, 'TASK_SITE_TYPE', 'F', '挂钩下落', NULL, NULL, NULL, '0', '1', 0, 0),
	(379, 'TASK_TYPE', '0', '静态任务', '激光导航中在AGV端配置的任务', NULL, NULL, '0', '1', 0, 0),
	(380, 'TASK_TYPE', '1', '橙色云-入库任务', '色带视觉导航', NULL, NULL, '0', '1', 0, 0),
	(381, 'TASK_TYPE', '2', '橙色云-出库任务', '色带视觉导航', NULL, NULL, '0', '1', 0, 0),
	(382, 'TASK_TYPE', '3', '橙色云-盘点任务', '色带视觉导航', NULL, NULL, '0', '1', 0, 0),
	(406, 'TASK_TYPE', '6', '橙色云-前往充电', NULL, NULL, NULL, '0', '1', 0, 0),
	(407, 'TASK_TYPE', '7', '橙色云-结束充电返回', NULL, NULL, NULL, '0', '1', 0, 0),
	(410, 'WMS_INVENTORY_TYPE', 'COLUMN', '单纵盘', NULL, NULL, NULL, '0', '1', 0, 0),
	(412, 'WMS_INVENTORY_TYPE', 'FULL', '全盘', NULL, NULL, NULL, '0', '1', 0, 0),
	(411, 'WMS_INVENTORY_TYPE', 'LINE', '单行盘', NULL, NULL, NULL, '0', '1', 0, 0);
/*!40000 ALTER TABLE `sys_dictionary_data_info` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
