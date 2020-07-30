package com.kaifantech.component.service.iot.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.calculatedfun.util.AppTool;
import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.init.sys.qualifier.KfTestQualifier;
import com.kaifantech.util.constant.taskexe.ctrl.IotDevType;

@Service(KfTestQualifier.IOT_CLIENT_SERVICE)
public class KfTestIotClientService extends AcsIotClientService {
	private List<IotClientBean> agvCacheList = new ArrayList<>();

	public synchronized List<IotClientBean> getAgvCacheList() {
		if (AppTool.isNull(agvCacheList)) {
			agvCacheList = getList(IotDevType.AGV_FANCY_IP);
			agvCacheList.sort((a1, a2) -> {
				return a2.getId() - a1.getId();
			});
		}
		return agvCacheList;
	}
}