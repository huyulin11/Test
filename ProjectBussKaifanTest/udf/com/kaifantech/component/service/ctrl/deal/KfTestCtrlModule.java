
package com.kaifantech.component.service.ctrl.deal;

import org.springframework.stereotype.Service;

import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.bean.msg.fancy.agv.FancyAgvMsgBean;
import com.kaifantech.component.cache.worker.AcsBeanFactory;
import com.kaifantech.init.sys.qualifier.KfTestQualifier;
import com.kaifantech.util.constant.taskexe.ctrl.AgvMoveStatus;
import com.kaifantech.util.thread.ThreadTool;

/***
 * 描述任务从用户下达到发送AGV执行前的逻辑
 ***/
@Service(KfTestQualifier.CTRL_MODULE)
public class KfTestCtrlModule extends FancyCtrlModule {
	protected void control(FancyAgvMsgBean agvMsgBean, IotClientBean agvBean) {
		if (!AgvMoveStatus.CONTINUE.equals(AcsBeanFactory.agvInfoDao().getMoveStatus(agvBean.getId()))) {
			pause(agvMsgBean.agvId());
		} else {
			if (!agvMsgBean.isAgvDriving()) {
				ThreadTool.sleep(2000);
				startup(agvBean.getId());
			}
		}
	}
}
