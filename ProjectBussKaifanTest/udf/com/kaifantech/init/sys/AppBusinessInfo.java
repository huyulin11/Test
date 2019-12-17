package com.kaifantech.init.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.calculatedfun.util.AppFile;
import com.kaifantech.cache.manager.AppConf;
import com.kaifantech.component.cache.worker.AppContextStaticBeanFactory;
import com.kaifantech.component.service.iot.client.KfTestIotClientService;
import com.kaifantech.component.service.tasksite.TaskSiteLogicService;
import com.kaifantech.util.constant.taskexe.ctrl.IotDevType;

public class AppBusinessInfo extends BaseBusinessInfo {
	public static final Clients CURRENT_CLIENT = Clients.LAO;
	public static final Projects CURRENT_PROJECT = Projects.LAO_DBWY;

	public static final String SYSTEM_INIT_DB_KEY = "kf_lao_dbwy_";

	public static void createStaticFile() throws IOException {
		BaseBusinessInfo.createStaticFile();
		KfTestIotClientService iotClientService = AppContextStaticBeanFactory.getBean(KfTestIotClientService.class);
		TaskSiteLogicService taskSiteLogicService = AppContextStaticBeanFactory.getBean(TaskSiteLogicService.class);
		String info;
		info = JSONArray.toJSON(iotClientService.getList(IotDevType.ZIGBEE_AUTODOOR)).toString();
		AppFile.createFile(AppBusinessInfo.getProjJsonsPath() + "autodoor/", "autodoor" + ".json", info);
		info = JSONArray.toJSON(iotClientService.getList(IotDevType.ZIGBEE_LIFT)).toString();
		AppFile.createFile(AppBusinessInfo.getProjJsonsPath() + "lift/", "lift" + ".json", info);
		info = JSONArray.toJSON(iotClientService.getList(IotDevType.ZIGBEE_LIGHT)).toString();
		AppFile.createFile(AppBusinessInfo.getProjJsonsPath() + "light/", "light" + ".json", info);
		List<Map<String, Object>> list = AppConf.worker().getList("task_site_location");
		JSONArray values = new JSONArray();
		for (Map<String, Object> item : list) {
			JSONObject json = JSONObject.parseObject(item.get("value").toString());
			values.add(json);
		}
		info = values.toJSONString();
		AppFile.createFile(AppBusinessInfo.getProjJsonsPath() + "sites/", "taskSiteLocation" + ".json", info);
		info = JSONArray.toJSON(taskSiteLogicService.getCachedList()).toString();
		AppFile.createFile(AppBusinessInfo.getProjJsonsPath() + "sites/", "taskSiteLogic" + ".json", info);
	}
}
