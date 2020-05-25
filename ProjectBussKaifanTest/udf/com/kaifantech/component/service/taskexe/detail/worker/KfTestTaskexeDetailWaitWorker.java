package com.kaifantech.component.service.taskexe.detail.worker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculatedfun.util.AppTool;
import com.calculatedfun.util.msg.AppMsg;
import com.kaifantech.bean.msg.fancy.agv.FancyAgvMsgBean;
import com.kaifantech.bean.taskexe.TaskexeBean;
import com.kaifantech.bean.taskexe.TaskexeDetailBean;
import com.kaifantech.bean.taskexe.TaskexeDetailWorksBean;
import com.kaifantech.bean.tasksite.TaskSiteInfoBean;
import com.kaifantech.cache.manager.AppCache;
import com.kaifantech.component.dao.agv.info.AgvOpWaitDao;
import com.kaifantech.init.sys.params.AppBaseParameters;

@Service
public class KfTestTaskexeDetailWaitWorker extends KfTestTaskexeDetailBaseWorker {
	@Autowired
	private AgvOpWaitDao agvOpWaitDao;

	public void work(FancyAgvMsgBean fancyAgvMsgBean, TaskexeBean taskexeBean, TaskexeDetailBean taskexeDetail,
			List<TaskexeDetailWorksBean> works) throws Exception {
		if (taskexeDetail.matchThisSite(fancyAgvMsgBean.currentSite())) {
			if (taskexeDetail.isNew()) {
				agvOpWaitDao.reach(taskexeBean.getAgvId());
				taskexeDetailOpService.send(taskexeDetail);
			} else if (taskexeDetail.isSend()) {
				if (AppBaseParameters.flag(AppCache.worker().get("CONFIRM", taskexeBean.getAgvId()))) {
					TaskexeDetailBean pastDetail = taskexeDetail.getPast();
					TaskexeDetailBean nextDetail = taskexeDetail.getNext();
					if (AppTool.isNull(nextDetail) && !AppTool.isNull(pastDetail)) {
						AppCache.worker().hset("CONFIRM", taskexeBean.getAgvId(), false);
						taskexeDetailOpService.over(taskexeDetail);
						agvOpWaitDao.over(taskexeBean.getAgvId());
					}
					if (AppTool.isAnyNull(pastDetail, nextDetail)) {
						return;
					}

					TaskSiteInfoBean pastInfoBean = taskSiteInfoService.getBean(pastDetail.getSiteid());
					TaskSiteInfoBean currentInfoBean = taskSiteInfoService.getBean(taskexeDetail.getSiteid());
					TaskSiteInfoBean nextInfoBean = taskSiteInfoService.getBean(nextDetail.getSiteid());
					Integer currentSeqId = currentInfoBean.getJsonItem("SEQ", Integer.class);
					Integer nextSeqId = nextInfoBean.getJsonItem("SEQ", Integer.class);
					if (currentInfoBean.isOutSite() && nextInfoBean.isOutSite()) {
						if (currentSeqId > nextSeqId) {
							AppMsg appMsg = ctrlModule.startupSwitch(taskexeBean.getAgvId());
							if (!appMsg.isSuccess()) {
								return;
							}
						}
					} else if (currentInfoBean.isOutSite() && nextInfoBean.isBaySite()) {
						if (pastInfoBean.isBaySite()) {
							AppMsg appMsg = ctrlModule.startupSwitch(taskexeBean.getAgvId());
							if (!appMsg.isSuccess()) {
								return;
							}
						}
					}
					AppCache.worker().hset("CONFIRM", taskexeBean.getAgvId(), false);
					taskexeDetailOpService.over(taskexeDetail);
					agvOpWaitDao.over(taskexeBean.getAgvId());
				}
			}
		}
	}

}
