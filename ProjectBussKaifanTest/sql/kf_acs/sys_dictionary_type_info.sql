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

-- Dumping structure for table kf_acs.sys_dictionary_type_info
DROP TABLE IF EXISTS `sys_dictionary_type_info`;
CREATE TABLE IF NOT EXISTS `sys_dictionary_type_info` (
  `dictype` varchar(20) NOT NULL COMMENT '字典类型代码',
  `remark` varchar(50) DEFAULT NULL COMMENT '字典说明',
  PRIMARY KEY (`dictype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='常规字典表明细表';

-- Dumping data for table kf_acs.sys_dictionary_type_info: ~5 rows (approximately)
DELETE FROM `sys_dictionary_type_info`;
/*!40000 ALTER TABLE `sys_dictionary_type_info` DISABLE KEYS */;
INSERT INTO `sys_dictionary_type_info` (`dictype`, `remark`) VALUES
	('ACS_CACHE_CABLE', '缓存柜信息'),
	('ACS_STATUS', '调度系统中出入库单的状态'),
	('SOCKET_DEV_TYPE', '系统中需要控制设备的类型'),
	('TASK_SITE_TYPE', '站点类型'),
	('TASK_TYPE', '系统任务类型');
/*!40000 ALTER TABLE `sys_dictionary_type_info` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
