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

-- Dumping structure for table kf_lao_foxiconn_agv.task_site_dev
DROP TABLE IF EXISTS `task_site_dev`;
CREATE TABLE IF NOT EXISTS `task_site_dev` (
  `siteid` int(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `devid` int(16) NOT NULL DEFAULT 0 COMMENT '设备ID',
  `json` varchar(500) DEFAULT NULL COMMENT '站点所在楼层',
  `createtime` datetime DEFAULT current_timestamp() COMMENT '创建时间',
  `delflag` int(1) DEFAULT 0 COMMENT '删除标志（DELFLAG）',
  PRIMARY KEY (`siteid`,`devid`)
) ENGINE=InnoDB AUTO_INCREMENT=503 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='任务站点信息表';

-- Dumping data for table kf_lao_foxiconn_agv.task_site_dev: ~20 rows (approximately)
DELETE FROM `task_site_dev`;
/*!40000 ALTER TABLE `task_site_dev` DISABLE KEYS */;
INSERT INTO `task_site_dev` (`siteid`, `devid`, `json`, `createtime`, `delflag`) VALUES
	(37, 201, NULL, '2019-01-30 11:55:11', 0),
	(59, 202, NULL, '2019-01-30 11:55:21', 0),
	(60, 203, NULL, '2019-01-30 11:55:32', 0),
	(61, 301, NULL, '2019-01-30 11:55:41', 0),
	(64, 203, NULL, '2019-01-30 11:55:49', 0),
	(67, 301, NULL, '2019-01-30 11:55:58', 0),
	(68, 301, NULL, '2019-01-30 11:56:09', 0),
	(79, 201, NULL, '2019-01-30 11:56:19', 0),
	(92, 202, NULL, '2019-01-30 11:56:37', 0),
	(93, 301, NULL, '2019-01-30 11:56:27', 0),
	(137, 204, NULL, '2019-04-10 17:14:13', 0),
	(139, 206, NULL, '2019-04-10 17:14:34', 0),
	(140, 206, NULL, '2019-04-10 17:14:37', 0),
	(141, 305, NULL, '2019-04-10 17:14:40', 0),
	(142, 305, NULL, '2019-04-10 17:14:42', 0),
	(146, 305, NULL, '2019-04-10 17:14:45', 0),
	(147, 204, NULL, '2019-04-10 17:14:16', 0),
	(149, 302, NULL, '2019-04-10 17:14:23', 0),
	(150, 302, NULL, '2019-04-10 17:14:26', 0),
	(151, 302, NULL, '2019-04-10 17:14:29', 0);
/*!40000 ALTER TABLE `task_site_dev` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
