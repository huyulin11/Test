package com.kaifantech.component.controller.de;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.calculatedfun.util.AppTool;
import com.calculatedfun.util.msg.AppMsg;
import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.bean.taskexe.AgvStatusBean;
import com.kaifantech.bean.taskexe.TaskexeBean;
import com.kaifantech.cache.manager.AppCache;
import com.kaifantech.component.dao.agv.info.AgvOpChargeDao;
import com.kaifantech.component.dao.agv.info.AgvOpDeliverDao;
import com.kaifantech.component.dao.agv.info.AgvOpFetchDao;
import com.kaifantech.component.dao.agv.info.AgvOpInitDao;
import com.kaifantech.component.dao.agv.info.AgvOpTransportDao;
import com.kaifantech.component.log.AgvStatusDBLogger;
import com.kaifantech.component.service.iot.client.IIotClientService;
import com.kaifantech.component.service.status.agv.AgvStatusService;
import com.kaifantech.component.service.taskexe.add.ITaskexeAddService;
import com.kaifantech.component.service.taskexe.info.TaskexeInfoService;
import com.kaifantech.init.sys.params.AppAutoParameters;
import com.kaifantech.init.sys.params.AppSysParameters;
import com.kaifantech.init.sys.qualifier.UdfQualifier;
import com.kaifantech.util.constant.taskexe.ctrl.AgvCtrlType;
import com.kaifantech.util.constant.taskexe.ctrl.AgvMoveStatus;
import com.kaifantech.util.constant.taskexe.ctrl.AgvTaskType;
import com.kaifantech.util.log.AppFileLogger;
import com.kaifantech.util.thread.ThreadTool;

@Controller
@RequestMapping("/de/acs/")
public class KfTestRestController {
	@Autowired
	@Qualifier(UdfQualifier.DEFAULT_TASKEXE_ADD_SERVICE)
	private ITaskexeAddService taskexeAddService;

	@Autowired
	private AgvStatusService agvStatusService;

	@Autowired
	@Qualifier(UdfQualifier.DEFAULT_IOT_CLIENT_SERVICE)
	private IIotClientService iotClientService;

	@Autowired
	private TaskexeInfoService taskexeInfoService;

	@Autowired
	private AgvOpChargeDao agvOpChargeDao;

	@Autowired
	private AgvOpTransportDao agvOpTransportDao;

	@Autowired
	private AgvOpDeliverDao agvOpDeliverDao;

	@Autowired
	private AgvOpFetchDao agvOpFetchDao;

	@Autowired
	private AgvOpInitDao agvOpInitDao;

	@RequestMapping("addAcsTask")
	@ResponseBody
	public JSONObject addTaskTo(String tasktype, Integer agvId, String to) {
		try {
			if (AgvTaskType.TRANSPORT.equals(tasktype)) {
				agvOpTransportDao.command(agvId, to);
			} else if (AgvTaskType.DELIVER.equals(tasktype)) {
				agvOpDeliverDao.command(agvId, to);
			} else if (AgvTaskType.FETCH.equals(tasktype)) {
				agvOpFetchDao.command(agvId, to);
			} else if (AgvTaskType.GOTO_INIT.equals(tasktype)) {
				agvOpInitDao.command(agvId, to);
			} else {
				return new AppMsg(-1, "无法识别的任务类型:" + tasktype).toAlbbJson();
			}
			AppMsg msg = agvStatusService.addStatus(new AgvStatusBean(tasktype, agvId, 0));
			return msg.toAlbbJson();
		} catch (Exception e) {
			e.printStackTrace();
			return new AppMsg(-1, "发生错误" + e.getMessage()).toAlbbJson();
		}
	}

	@RequestMapping("addCtrlTask2")
	@ResponseBody
	public JSONObject addCtrlTask(String tasktype, Integer agvId) {
		try {
			if (AgvCtrlType.AGVS_STOP_PI.equals(tasktype) || AgvCtrlType.AGVS_OPEN_PI.equals(tasktype)) {
				boolean num = AppSysParameters.setOpenPi(AgvCtrlType.AGVS_OPEN_PI.equals(tasktype));
				String msg = (AgvCtrlType.AGVS_STOP_PI.equals(tasktype) ? AgvCtrlType.AGVS_STOP_PI_STR
						: AgvCtrlType.AGVS_OPEN_PI_STR) + num;
				AppFileLogger.setWarningTips(agvId, msg, 0, AgvStatusDBLogger.MSG_LEVEL_WARNING);
				return new AppMsg(0, msg + ":更新成功！").toAlbbJson();
			}

			if (AgvCtrlType.AGVS_STOP_AUTO_TASK.equals(tasktype) || AgvCtrlType.AGVS_OPEN_AUTO_TASK.equals(tasktype)) {
				boolean num = AppAutoParameters.setAutoTask(!AgvCtrlType.AGVS_STOP_AUTO_TASK.equals(tasktype));
				String msg = (AgvCtrlType.AGVS_STOP_AUTO_TASK.equals(tasktype) ? AgvCtrlType.AGVS_STOP_AUTO_TASK_STR
						: AgvCtrlType.AGVS_OPEN_AUTO_TASK_STR) + num;
				AppFileLogger.setWarningTips(agvId, msg, 0, AgvStatusDBLogger.MSG_LEVEL_WARNING);
				return new AppMsg(0, msg + ":更新成功！").toAlbbJson();
			}

			if (AgvCtrlType.AGVS_STOP_AUTO_CHARGE.equals(tasktype)
					|| AgvCtrlType.AGVS_OPEN_AUTO_CHARGE.equals(tasktype)) {
				boolean num = AppAutoParameters.setAutoCharge(!AgvCtrlType.AGVS_STOP_AUTO_CHARGE.equals(tasktype));
				String msg = (AgvCtrlType.AGVS_STOP_AUTO_CHARGE.equals(tasktype) ? AgvCtrlType.AGVS_STOP_AUTO_CHARGE_STR
						: AgvCtrlType.AGVS_OPEN_AUTO_CHARGE_STR) + num;
				AppFileLogger.setWarningTips(agvId, msg, 0, AgvStatusDBLogger.MSG_LEVEL_WARNING);
				return new AppMsg(0, msg + ":更新成功！").toAlbbJson();
			}

			if (AgvCtrlType.AGVS_STOP_ERR_BACK.equals(tasktype) || AgvCtrlType.AGVS_OPEN_ERR_BACK.equals(tasktype)) {
				boolean num = AppAutoParameters.setErrBack(!AgvCtrlType.AGVS_STOP_ERR_BACK.equals(tasktype));
				String msg = (AgvCtrlType.AGVS_STOP_ERR_BACK.equals(tasktype) ? AgvCtrlType.AGVS_STOP_ERR_BACK_STR
						: AgvCtrlType.AGVS_OPEN_ERR_BACK_STR) + num;
				AppFileLogger.setWarningTips(agvId, msg, 0, AgvStatusDBLogger.MSG_LEVEL_WARNING);
				return new AppMsg(0, msg + ":更新成功！").toAlbbJson();
			}

			if (AgvTaskType.GOTO_CHARGE.equals(tasktype)) {
				agvOpChargeDao.commandToCharge(agvId);
			} else if (AgvTaskType.BACK_CHARGE.equals(tasktype)) {
				agvOpChargeDao.commandBackCharge(agvId);
			} else if (AgvMoveStatus.PAUSE_USER.equals(tasktype)) {
				agvOpChargeDao.pauseByUser(agvId);
				if (agvId.equals(0)) {
					for (IotClientBean agv : iotClientService.getAgvCacheList()) {
						agvOpChargeDao.pauseByUser(agv.getId());
					}
				}
			} else if (AgvMoveStatus.CONTINUE.equals(tasktype)) {
				agvOpChargeDao.startup(agvId);
			} else if (AgvMoveStatus.SHUTDOWN.equals(tasktype)) {
				TaskexeBean taskexeBean = taskexeInfoService.getNextOne(agvId);
				if (!AppTool.isNull(taskexeBean)) {
					taskexeBean.setShutdown();
				} else {
					agvOpInitDao.workOverForce(agvId);
				}
			} else if (AgvTaskType.CONFIRM.equals(tasktype)) {
				AppCache.worker().hset("CONFIRM", agvId, "TRUE");
			} else if (AgvTaskType.GOTO_INIT.equals(tasktype)) {
				agvOpInitDao.commandToInit(agvId);
			} else if (AgvTaskType.RE_PATH.equals(tasktype)) {
				TaskexeBean taskexeBean = taskexeInfoService.getNextOne(agvId);
				if (!AppTool.isNull(taskexeBean)) {
					ThreadTool.run(() -> {
						try {
							taskexeAddService.rePath(taskexeBean);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}, true);
				}
			}

			AppMsg msg = agvStatusService.addStatus(new AgvStatusBean(tasktype, agvId, 0));
			return msg.toAlbbJson();
		} catch (Exception e) {
			e.printStackTrace();
			return new AppMsg(-1, "发生错误" + e.getMessage()).toAlbbJson();
		}
	}

	@RequestMapping("addTask2")
	@ResponseBody
	public JSONObject addTask(String tasktype, Integer agvId) {
		try {
			AppMsg msg = taskexeAddService.addTask(new TaskexeBean(tasktype, agvId, 0));
			return msg.toAlbbJson();
		} catch (Exception e) {
			e.printStackTrace();
			return new AppMsg(-1, "发生错误" + e.getMessage()).toAlbbJson();
		}
	}

	@RequestMapping("addTaskById")
	@ResponseBody
	public JSONObject addTaskById(String taskid, Integer agvId) {
		try {
			JSONObject json = new JSONObject();
			json.put("taskid", taskid);
			AppMsg msg = taskexeAddService.addTask(new TaskexeBean(json, agvId, 0));
			return msg.toAlbbJson();
		} catch (Exception e) {
			e.printStackTrace();
			return new AppMsg(-1, "发生错误" + e.getMessage()).toAlbbJson();
		}
	}
}
