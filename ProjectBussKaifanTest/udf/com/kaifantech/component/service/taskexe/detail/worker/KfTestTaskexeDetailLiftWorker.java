package com.kaifantech.component.service.taskexe.detail.worker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculatedfun.util.AppTool;
import com.calculatedfun.util.msg.AppMsg;
import com.kaifantech.bean.msg.agv.FancyAgvMsgBean;
import com.kaifantech.bean.taskexe.TaskexeBean;
import com.kaifantech.bean.taskexe.TaskexeDetailBean;
import com.kaifantech.bean.taskexe.TaskexeDetailWorksBean;
import com.kaifantech.bean.tasksite.TaskSiteInfoBean;
import com.kaifantech.cache.manager.AppCache;
import com.kaifantech.component.comm.manager.ILiftManager;
import com.kaifantech.component.comm.manager.agv.IFancyAgvManager;
import com.kaifantech.component.dao.agv.info.AgvOpLiftDao;
import com.kaifantech.init.sys.params.AppSysParameters;
import com.kaifantech.init.sys.params.KfTestCacheKeys;
import com.kaifantech.util.thread.ThreadTool;

@Service
public class KfTestTaskexeDetailLiftWorker extends KfTestTaskexeDetailBaseWorker {
	public void work(FancyAgvMsgBean fancyAgvMsgBean, TaskexeBean taskexeBean, TaskexeDetailBean thisDetail,
			List<TaskexeDetailWorksBean> works) throws Exception {
		if (thisDetail.matchThisSite(fancyAgvMsgBean.currentSite())) {
			AppSysParameters.setTaskstop(taskexeBean.getAgvId(), true);
			TaskexeDetailBean nextDetail = thisDetail.getNext();
			TaskSiteInfoBean thisSite = taskSiteInfoService.getBean(thisDetail);
			TaskSiteInfoBean nextSite = taskSiteInfoService.getBean(nextDetail);
			int devId = taskSiteDevService.getLiftIdFromSite(fancyAgvMsgBean.currentSite());
			// ThreadTool.run(() -> {
			// loopSearch(taskexeBean, devId);
			// });
			AppMsg appMsg = AppMsg.fail();
			String thisLayer = thisSite.getJsonItem("layer");
			String nextLayer = nextSite.getJsonItem("layer");
			if (!thisLayer.equals(IN_LIFT) && nextLayer.equals(IN_LIFT)) {
				if (thisDetail.isNew()) {
					taskexeDetailOpService.send(thisDetail);
				} else if (thisDetail.isSend()) {
					agvOpLiftDao.reach(taskexeBean.getAgvId());
					appMsg = devManager.gotoLayerNum(devId, thisLayer);
					if (!appMsg.isSuccess()) {
						return;
					}

					if (AppTool.equals(nextSite.getId(), 67)) {
						appMsg = agvManager.incaseofObstacleClose(taskexeBean.getAgvId());
						if (!appMsg.isSuccess()) {
							return;
						}
					}

					taskexeDetailOpService.over(thisDetail);
					agvOpLiftDao.over(taskexeBean.getAgvId());
					AppSysParameters.setTaskstop(taskexeBean.getAgvId(), false);
				}
			} else if (thisLayer.equals(IN_LIFT)) {
				if (thisDetail.isNew()) {
					taskexeDetailOpService.send(thisDetail);
				} else if (thisDetail.isSend()) {
					agvOpLiftDao.reach(taskexeBean.getAgvId());
					String currentLayer = AppCache.worker().get(KfTestCacheKeys.liftLayer(), devId);
					boolean isClose = IN_LIFT.equals(currentLayer);
					boolean isTrans = AppSysParameters.flag(AppCache.worker().get(KfTestCacheKeys.liftTrans(), devId));
					if (!isClose && !isTrans) {
						appMsg = devManager.close(devId);
						if (!appMsg.isSuccess()) {
							return;
						}
						AppCache.worker().hset(KfTestCacheKeys.liftTrans(), devId, AppSysParameters.flag(true));
						taskexeDetailOpService.liftClose(thisDetail);
					}
				} else if (thisDetail.isLiftClose()) {
					String currentLayer = AppCache.worker().get(KfTestCacheKeys.liftLayer(), devId);
					if (!currentLayer.equals("" + nextLayer)) {
						appMsg = devManager.gotoLayerNum(devId, nextLayer);
						if (!appMsg.isSuccess()) {
							return;
						}
					}

					if (AppTool.equals(thisSite.getId(), 67)) {
						appMsg = agvManager.incaseofObstacleClose(taskexeBean.getAgvId());
						if (!appMsg.isSuccess()) {
							return;
						}
					}

					appMsg = agvManager.startup(taskexeBean.getAgvId());
					taskexeDetailOpService.over(thisDetail);
					agvOpLiftDao.over(taskexeBean.getAgvId());
					AppSysParameters.setTaskstop(taskexeBean.getAgvId(), false);
					AppCache.worker().hset(KfTestCacheKeys.liftTrans(), devId, AppSysParameters.flag(false));
					ThreadTool.run(() -> {
						closeLift(taskexeBean, devId);
					});

				}
			}
		}
	}

	private void closeLift(TaskexeBean taskexeBean, Integer devId) {
		while (true) {
			try {
				if (!isLiftOccupy()) {
					AppMsg appMsg = devManager.close(devId);
					if (appMsg.isSuccess()) {
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			ThreadTool.sleep(5000);
		}
	}

	private static final String IN_LIFT = "0";
	@Autowired
	protected IFancyAgvManager agvManager;
	@Autowired
	private ILiftManager devManager;
	@Autowired
	private AgvOpLiftDao agvOpLiftDao;
}
