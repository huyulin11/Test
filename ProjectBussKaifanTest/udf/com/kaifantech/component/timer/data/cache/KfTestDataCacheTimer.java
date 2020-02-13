package com.kaifantech.component.timer.data.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.calculatedfun.util.AppFile;
import com.calculatedfun.util.AppSet;
import com.calculatedfun.util.AppTool;
import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.component.service.iot.client.KfTestIotClientService;
import com.kaifantech.component.service.sysdic.SysDicService;
import com.kaifantech.init.sys.UdfBusinessInfo;
import com.kaifantech.init.sys.params.AppConfParameters;
import com.kaifantech.util.constant.taskexe.ctrl.IotDevType;
import com.kaifantech.util.msg.iot.AutoDoorMsgGetter;
import com.kaifantech.util.msg.iot.LiftMsgGetter;
import com.kaifantech.util.msg.iot.LightMsgGetter;
import com.kaifantech.util.thread.ThreadTool;

@Component
@Lazy(false)
public class KfTestDataCacheTimer extends FancyDataCacheTimer {
	@Autowired
	private KfTestIotClientService iotClientService;

	@Autowired
	private SysDicService sysDicService;

	public KfTestDataCacheTimer() {
		super();
	}

	@Scheduled(cron = "0/2 * * * * ?")
	public void devsInfos() {
		try {
			autoDoorInfos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			liftInfos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			lightInfos();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void keepSearch() {
		ThreadTool.run(() -> {
			while (true) {
				try {
					for (IotClientBean agvBean : iotClientService.getAgvCacheList()) {
						agvManager.generalSearch(agvBean.getId());
						ThreadTool.sleep(AppConfParameters.getGeneralSearchInteval());
					}
				} catch (Exception e) {
				}
			}
		});
	}

	private void autoDoorInfos() throws IOException {
		List<Map<String, Object>> infos = new ArrayList<>();
		for (IotClientBean obj : iotClientService.getList(IotDevType.ZIGBEE_AUTODOOR)) {
			Map<String, Object> single = new HashMap<>();
			String status = AutoDoorMsgGetter.getStatus(obj.getId());
			single = AppSet.bean2Map(obj);
			single.put("info", sysDicService.getDicValue("LAO_AUTO_DOOR_STATUS", status));
			infos.add(single);
		}
		if (!AppTool.isNull(infos)) {
			AppFile.createFile(UdfBusinessInfo.getProjJsonsPath() + "autoDoor/", "autodoor.json",
					JSONArray.toJSON(infos).toString());
		}
	}

	private void liftInfos() throws IOException {
		List<Map<String, Object>> infos = new ArrayList<>();
		for (IotClientBean obj : iotClientService.getList(IotDevType.ZIGBEE_LIFT)) {
			Map<String, Object> single = new HashMap<>();
			String status = LiftMsgGetter.getStatus(obj.getId());
			single = AppSet.bean2Map(obj);
			single.put("info", sysDicService.getDicValue("LAO_LIFT_STATUS", status));
			infos.add(single);
		}
		if (!AppTool.isNull(infos)) {
			AppFile.createFile(UdfBusinessInfo.getProjJsonsPath() + "lift/", "lift.json",
					JSONArray.toJSON(infos).toString());
		}
	}

	private void lightInfos() throws IOException {
		List<Map<String, Object>> infos = new ArrayList<>();
		for (IotClientBean obj : iotClientService.getList(IotDevType.ZIGBEE_LIGHT)) {
			Map<String, Object> single = new HashMap<>();
			String status = LightMsgGetter.getStatus(obj.getId());
			single = AppSet.bean2Map(obj);
			single.put("info", sysDicService.getDicValue("LAO_LIGHT_STATUS", status));
			infos.add(single);
		}
		if (!AppTool.isNull(infos)) {
			AppFile.createFile(UdfBusinessInfo.getProjJsonsPath() + "light/", "light.json",
					JSONArray.toJSON(infos).toString());
		}
	}

}
