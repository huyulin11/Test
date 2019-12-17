-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- Server version:               10.3.7-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL 版本:                  9.5.0.5442
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table kf_acs.sys_dictionary_info
DROP TABLE IF EXISTS `sys_dictionary_info`;
CREATE TABLE IF NOT EXISTS `sys_dictionary_info` (
  `typeCode` varchar(20) NOT NULL COMMENT '字典类型代码',
  `key` int(50) NOT NULL COMMENT '字典编码',
  `value` varchar(40) DEFAULT NULL COMMENT '字典值',
  `alias` varchar(40) DEFAULT NULL COMMENT '字典值别名',
  `valueUs` varchar(20) DEFAULT NULL COMMENT '字典值英文',
  `remark` varchar(50) DEFAULT NULL COMMENT '字典说明',
  `delflag` varchar(1) DEFAULT '0' COMMENT '删除标志-1表示删除，0代表未删除',
  `normalUser` varchar(1) DEFAULT '1' COMMENT '普通用户标志-1表示是，0代表不是（即后台操作员）',
  `defaultValue` int(1) DEFAULT 0 COMMENT '默认选项',
  `orderby` int(11) DEFAULT 0 COMMENT '标准的排序方式',
  PRIMARY KEY (`typeCode`,`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='常规字典表明细表';

-- Dumping data for table kf_acs.sys_dictionary_info: ~12 rows (approximately)
DELETE FROM `sys_dictionary_info`;
/*!40000 ALTER TABLE `sys_dictionary_info` DISABLE KEYS */;
INSERT INTO `sys_dictionary_info` (`typeCode`, `key`, `value`, `alias`, `valueUs`, `remark`, `delflag`, `normalUser`, `defaultValue`, `orderby`) VALUES
	('CANGZHOU_TASK_TYPE', 0, '不受控', NULL, NULL, NULL, '0', '1', 0, 0),
	('CANGZHOU_TASK_TYPE', 1, '送料', NULL, NULL, NULL, '0', '1', 0, 0),
	('CANGZHOU_TASK_TYPE', 2, '上料', NULL, NULL, NULL, '0', '1', 0, 0),
	('CANGZHOU_TASK_TYPE', 3, '送光轴', NULL, NULL, NULL, '0', '1', 0, 0),
	('TASKEXE_STATUS', -1, '初始状态', NULL, NULL, NULL, '0', '1', 0, 0),
	('TASKEXE_STATUS', 0, '部分准备就绪', NULL, NULL, NULL, '0', '1', 0, 0),
	('TASKEXE_STATUS', 1, '执行送料中', NULL, NULL, NULL, '0', '1', 0, 0),
	('TASKEXE_STATUS', 2, '送料结束', NULL, NULL, NULL, '0', '1', 0, 0),
	('TASKEXE_STATUS', 3, '执行上料中', NULL, NULL, NULL, '0', '1', 0, 0),
	('TASKEXE_STATUS', 4, '上料结束', NULL, NULL, NULL, '0', '1', 0, 0),
	('TASKEXE_STATUS', 5, '执行送光轴中', NULL, NULL, NULL, '0', '1', 0, 0),
	('TASKEXE_STATUS', 6, '送光轴结束', NULL, NULL, NULL, '0', '1', 0, 0);
/*!40000 ALTER TABLE `sys_dictionary_info` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
