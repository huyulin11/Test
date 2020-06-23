package com.kaifantech.component.controller.de;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.calculatedfun.util.msg.AppMsg;
import com.kaifantech.bean.taskexe.AgvStatusBean;
import com.kaifantech.bean.taskexe.TaskexeBean;
import com.kaifantech.component.dao.agv.info.AgvOpDeliverDao;
import com.kaifantech.component.dao.agv.info.AgvOpFetchDao;
import com.kaifantech.component.dao.agv.info.AgvOpInitDao;
import com.kaifantech.component.dao.agv.info.AgvOpTransportDao;
import com.kaifantech.component.service.iot.client.IIotClientService;
import com.kaifantech.component.service.status.agv.AgvStatusService;
import com.kaifantech.component.service.taskexe.add.IFancyTaskexeAddService;
import com.kaifantech.init.sys.qualifier.UdfQualifier;
import com.kaifantech.util.constant.taskexe.ctrl.AgvTaskType;

@Controller
@RequestMapping("/de/acs/")
public class KfTestRestController {
	@Autowired
	@Qualifier(UdfQualifier.DEFAULT_TASKEXE_ADD_SERVICE)
	private IFancyTaskexeAddService taskexeAddService;

	@Autowired
	private AgvStatusService agvStatusService;

	@Autowired
	@Qualifier(UdfQualifier.DEFAULT_IOT_CLIENT_SERVICE)
	private IIotClientService iotClientService;

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
