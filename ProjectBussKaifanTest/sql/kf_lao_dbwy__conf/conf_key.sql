-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.3.11-MariaDB - mariadb.org binary distribution
-- 服务器OS:                        Win64
-- HeidiSQL 版本:                  10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table kf_lao_dbwy__conf.conf_key
DROP TABLE IF EXISTS `conf_key`;
CREATE TABLE IF NOT EXISTS `conf_key` (
  `key` varchar(100) NOT NULL,
  `value` varchar(500) NOT NULL,
  `updatetime` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table kf_lao_dbwy__conf.conf_key: ~24 rows (大约)
DELETE FROM `conf_key`;
/*!40000 ALTER TABLE `conf_key` DISABLE KEYS */;
INSERT INTO `conf_key` (`key`, `value`, `updatetime`) VALUES
	('ACS_CONTROL', '[{"id":"PAUSE_USER","name":"暂停所有"},{"id":"togglePiBtn","name":"交通管制"},{"id":"autoTaskBtn","name":"自动任务"},{"id":"autoChargeBtn","name":"自动充电"},{"id":"sysLocation","name":"即时地图"}]', '2019-02-19 10:45:58'),
	('AGV_CONTROL', '[{"id":"PAUSE_USER","name":"暂停"},{"id":"CONTINUE","name":"继续"},{"id":"GOTO_CHARGE","name":"前往充电"},{"id":"TRANSPORT","name":"运输"},{"id":"DELIVER_INIT","name":"原料库送料"},{"id":"DELIVER_STEREOTYPE","name":"定型暂存区出发"},{"id":"DELIVER_PACK","name":"包装暂存区出发"},{"id":"GOTO_STEREOTYPE","name":"前往定型暂存区"},{"id":"GOTO_PACK","name":"前往包装暂存区"},{"id":"CONFIRM","name":"原料确认","color":"red"},{"id":"GOTO_INIT","name":"前往原料库","color":"red"},{"id":"SHUTDOWN","name":"取消任务","color":"red"}]', '2019-11-23 08:53:53'),
	('AGV_GENERAL_SEARCH_INTEVAL', '200', '2019-01-22 13:44:38'),
	('AGV_JUDGED_TIMES_INTEVAL', '1200', '2019-02-22 18:55:06'),
	('DETA_JUDGE_SITE', '500', '2019-08-29 09:13:29'),
	('DETA_TIMES', '500', '2019-01-19 11:25:58'),
	('DISTANCE_TARGET', '5000', '2019-08-29 09:13:29'),
	('DISTANCE_WAITING', '8000', '2019-08-29 09:13:29'),
	('EXPIRE_TIME', '1575678990635', '2019-10-08 08:36:30'),
	('IS_ALLOW_CACHE_TASK', 'FALSE', '2019-08-29 09:13:34'),
	('IS_AUTO_CHARGE', 'FALSE', '2019-02-22 10:38:23'),
	('IS_AUTO_TASK', 'FALSE', '2019-02-22 10:38:23'),
	('IS_CHECK_FROM_STATUS_WHEN_UPDATE_ALLOC', 'FALSE', '2019-10-08 10:44:20'),
	('IS_CHECK_SQL', 'FALSE', '2019-08-29 09:13:26'),
	('IS_ERR_BACK', 'FALSE', '2019-02-22 10:38:23'),
	('IS_LOCAL_TEST', 'TRUE', '2019-09-12 10:27:34'),
	('IS_ONE_USER_ONE_LOGIN', 'FALSE', '2019-08-29 09:20:22'),
	('IS_ONE_USER_TICK_OFF', 'FALSE', '2019-08-29 09:20:22'),
	('IS_OPEN_PI', 'TRUE', '2019-01-13 09:29:28'),
	('NGINX_PATH', 'D:/acs/nginx-1.10.1/html/static/s/jsons/', '2019-09-12 14:54:41'),
	('NGINX_PORT', '99', '2019-02-14 09:07:36'),
	('PROJECT_INFO', '{"projectName":"AGV调度系统","fullProjectName":"吉林袜业AGV调度系统","corporation":{"fullname":"合肥凯钒信息科技有限公司","shortname":"山东省济南市档案局","address":"地址：安徽省合肥市经开区百鸟路石柱路5F创客空间","telephone":"电话：15077923697"},"client":{"tips":"AHYF","fullname":"凯钒科技","shortname":"凯钒科技","address":""}}', '2019-10-30 14:41:12'),
	('SEND_TO_WMS', 'FALSE', '2019-11-01 16:01:11'),
	('TIMEOUT_MILL_SECONDS', '86400000', '2019-10-28 13:33:33');
/*!40000 ALTER TABLE `conf_key` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
