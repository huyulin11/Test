
package com.kaifantech.component.service.taskexe.detail.cache;

import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.calculatedfun.util.AppTool;
import com.kaifantech.bean.taskexe.TaskexeDetailBean;
import com.kaifantech.bean.tasksite.TaskSiteInfoBean;
import com.kaifantech.component.service.tasksite.info.ITaskSiteInfoService;
import com.kaifantech.init.sys.qualifier.UdfQualifier;
import com.kaifantech.util.constant.taskexe.FancyArrivedActType;
import com.kaifantech.util.constants.cmd.FancyAgvCmdConstant;

@Service
public class KfTestTaskexeDetailCacheService implements IFancyTaskexeDetailCacheService {
	@Autowired
	@Qualifier(UdfQualifier.DEFAULT_TASK_SITE_INFO_SERVICE)
	private ITaskSiteInfoService taskSiteInfoService;

	private TreeMap<Integer, TreeMap<String, TreeMap<String, String>>> map = new TreeMap<>();

	public void undoCacheCommand(Integer agvId) {
		map.remove(agvId);
	}

	public synchronized TreeMap<String, String> getCache(Integer agvId, TaskexeDetailBean current) throws Exception {
		TreeMap<String, TreeMap<String, String>> mapOfTask = map.get(agvId);
		if (mapOfTask == null) {
			mapOfTask = new TreeMap<>();
			map.put(agvId, mapOfTask);
		}
		TreeMap<String, String> mapOfDetail = mapOfTask
				.get(current.getTaskexesid() + "-" + current.getDetailsequence());
		if (AppTool.isNull(mapOfDetail)) {
			mapOfDetail = getFirstCache(current);
			addByNext(agvId, current, mapOfDetail);
			addByPoints(agvId, current, mapOfDetail);
			addByPast(agvId, current, mapOfDetail);
			addByPastAndNext(agvId, current, mapOfDetail);
			mapOfTask.put(current.getTaskexesid() + "-" + current.getDetailsequence(), mapOfDetail);
		}
		return mapOfDetail;
	}

	public void addByNext(Integer agvId, TaskexeDetailBean current, TreeMap<String, String> cache) {
		TaskexeDetailBean next = current.getNext();
		if (!AppTool.isNull(next)) {
			TaskSiteInfoBean currentInfoBean = current.site();
			TaskSiteInfoBean nextInfoBean = next.site();
			if (currentInfoBean.isBaySite()) {
				TaskexeDetailBean next2 = next.getNext();
				if (!AppTool.isNull(next2)) {
					if (nextInfoBean.isBaySite() && next2.site().isAllocSite()
							&& currentInfoBean.getLine().equals(nextInfoBean.getLine())) {
						if (nextInfoBean.getLine().equals(next2.site().getLine()))
							cache.put(HOOK, FancyAgvCmdConstant.CMD_TASK_CACHE_HOOK_UP);
					} else if (current.getSiteid().equals(1085) && next.getSiteid().equals(1087)
							&& next2.getSiteid().equals(2108)) {
						cache.put(HOOK, FancyAgvCmdConstant.CMD_TASK_CACHE_HOOK_UP);
					}
				}
			} else if (currentInfoBean.isOutSite() && nextInfoBean.isOutSite()) {
				if (FancyArrivedActType.isStopAct(next)) {
					cache.put(SPEED, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_40);
				} else {
					cache.put(SPEED, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_60);
				}
			} else if (currentInfoBean.isOutSite() && nextInfoBean.isBaySite()) {
				cache.put(SPEED, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_40);
				if (nextInfoBean.getLine().equals(-1)) {
					cache.put(CHANNEL, FancyAgvCmdConstant.CMD_TASK_CACHE_OBSTACLE_CHANNEL_CLOSE);
				}
			}

		}
	}

	public void addByPast(Integer agvId, TaskexeDetailBean current, TreeMap<String, String> cache) throws Exception {
	}

	public void addByPastAndNext(Integer agvId, TaskexeDetailBean current, TreeMap<String, String> cache)
			throws Exception {
		TaskexeDetailBean past = current.getPast();
		TaskexeDetailBean next = current.getNext();
		if (AppTool.isNull(past)) {
			return;
		}
		TaskSiteInfoBean pastInfoBean = past.site();
		TaskSiteInfoBean currentInfoBean = current.site();
		Integer pastSeqId = pastInfoBean.getJsonItem("SEQ", Integer.class);
		Integer currentSeqId = currentInfoBean.getJsonItem("SEQ", Integer.class);
		if (AppTool.isNull(next)) {
			if (AppTool.or(pastInfoBean.isBaySite() && currentInfoBean.isOutSite() && pastInfoBean.getLine().equals(1),
					pastInfoBean.isOutSite() && currentInfoBean.isOutSite() && pastSeqId > currentSeqId)) {
			} else {
				if (!currentInfoBean.isBaySite()) {
					cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_SWITCH_STOP);
					cache.put(SPEED_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_40);
					if (currentInfoBean.isAllocSite() && !currentInfoBean.getLine().equals(-1)) {
						cache.put(CHANNEL_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_OBSTACLE_CHANNEL_OPEN);
					}
				}
			}
		} else {
			if (currentInfoBean.isBaySite()) {
				TaskSiteInfoBean nextInfoBean = next.site();
				Integer pastSiteid = past.getSiteid();
				Integer currentSiteid = current.getSiteid();
				Integer nextSiteid = next.getSiteid();
				if (pastInfoBean.isBaySite() && nextInfoBean.isOutSite()) {
					Integer aspect = nextInfoBean.getJsonItem("ASPECT", Integer.class);
					pirouette(pastSeqId, currentSeqId, aspect, cache);
					if (!FancyArrivedActType.isStopAct(next)) {
						cache.put(SPEED_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_60);
						cache.put(CHANNEL_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_OBSTACLE_CHANNEL_OPEN);
					} else if (FancyArrivedActType.isStopAct(next)) {
						cache.put(SPEED_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_40);
						cache.put(CHANNEL_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_OBSTACLE_CHANNEL_OPEN);
					}
				} else if (pastInfoBean.isBaySite() && nextInfoBean.isAllocSite()) {
					if (pastInfoBean.getLine().equals(currentInfoBean.getLine())) {
						if (currentInfoBean.getLine().equals(4) && nextInfoBean.getLine().equals(3)) {
							pirouette(pastSeqId, currentSeqId, 1, cache);
							cache.put(SPEED_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_40);
							return;
						}
						Integer aspect = nextInfoBean.getJsonItem("ASPECT", Integer.class);
						pirouette(pastSeqId, currentSeqId, aspect, cache);
					} else if (pastSiteid.equals(1085) && currentSiteid.equals(1087)) {
						cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_RIGHT);
					} else if (pastSiteid.equals(1088) && currentSiteid.equals(1087)) {
						cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_LEFT);
					} else {
						cache.put(HOOK, FancyAgvCmdConstant.CMD_TASK_CACHE_HOOK_UP);
					}
					cache.put(SPEED_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_30);
					if (!cache.containsKey(CHANNEL_DELAY)) {
						cache.put(CHANNEL_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_OBSTACLE_CHANNEL_CLOSE);
					}
				} else if (pastInfoBean.isBaySite() && nextInfoBean.isBaySite()) {
					Integer nextSeqId = nextInfoBean.getJsonItem("SEQ", Integer.class);
					Integer pastLine = pastInfoBean.getLine();
					Integer currentLine = currentInfoBean.getLine();
					Integer nextLine = nextInfoBean.getLine();
					if (!pastLine.equals(currentLine) && currentLine.equals(nextLine)) {
						if (pastSiteid.equals(1085) && currentSiteid.equals(1087)) {
						} else if (!cache.containsKey(ASPECT)) {
							cache.put(SPEED_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_40);
							pirouetteAllBaySite(currentSeqId, nextSeqId, currentLine, pastLine, cache);
						}
					} else if (pastLine.equals(currentLine) && !currentLine.equals(nextLine)) {
						if (pastSiteid.equals(1088) && currentSiteid.equals(1087)) {
						} else {
							cache.put(SPEED_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_40);
							pirouetteAllBaySite(pastSeqId, currentSeqId, currentLine, nextLine, cache);
						}
					}
				} else if (AppTool.or(pastInfoBean.isOutSite() && nextInfoBean.isBaySite(),
						pastInfoBean.isAllocSite() && nextInfoBean.isBaySite())) {
					cache.put(SPEED_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_40);
					if (currentInfoBean.getLine().equals(nextInfoBean.getLine())) {
						Integer aspect = pastInfoBean.getJsonItem("ASPECT", Integer.class);
						Integer nextSeqId = nextInfoBean.getJsonItem("SEQ", Integer.class);
						pirouetteAspect(currentSeqId, nextSeqId, aspect, cache);
					} else if (pastSiteid.equals(2108) && nextSiteid.equals(1085)) {
						cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_LEFT);
					}
				} else if (pastInfoBean.isAllocSite() && nextInfoBean.isAllocSite()) {
					// if (AppTool.or(currentInfoBean.getLine().equals(1),
					// currentInfoBean.getLine().equals(4))) {
					// cache.put(ASPECT,
					// FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_RIGHT_REVERSE);
					// cache.put(HOOK,
					// FancyAgvCmdConstant.CMD_TASK_CACHE_HOOK_UP);
					// }
				}
			}
		}
	}

	public void addByPoints(Integer agvId, TaskexeDetailBean current, TreeMap<String, String> cache) {
		TaskexeDetailBean past = current.getPast();
		TaskexeDetailBean next = current.getNext();
		if (AppTool.isAnyNull(past, next)) {
			return;
		}
		Integer pastSiteid = past.getSiteid();
		Integer currentSiteid = current.getSiteid();
		Integer nextSiteid = next.getSiteid();
		if (AppTool.or(currentSiteid.equals(1049) && nextSiteid.equals(1050),
				currentSiteid.equals(1034) && nextSiteid.equals(1035), nextSiteid.equals(1022))) {
			cache.put(CHANNEL, FancyAgvCmdConstant.CMD_TASK_CACHE_OBSTACLE_CHANNEL_CLOSE);
		}
		if (currentSiteid.equals(1039)) {
			if (AppTool.or(pastSiteid.equals(2039) && nextSiteid.equals(2055),
					pastSiteid.equals(2055) && nextSiteid.equals(2039))) {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_RIGHT_REVERSE);
				cache.put(HOOK, FancyAgvCmdConstant.CMD_TASK_CACHE_HOOK_UP);
			}
		}
		if (AppTool.equals(currentSiteid, 1036, 1039, 1043, 1047)) {
			if (AppTool.equals(nextSiteid, 2054, 2055, 2056, 2057)) {
				cache.put(CHANNEL_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_OBSTACLE_CHANNEL_OPEN);
			}
		}

		TaskexeDetailBean past2 = past.getPast();
		if (!AppTool.isNull(past2)) {
			Integer past2Siteid = past2.getSiteid();
			if (AppTool.or(
					past2Siteid.equals(2054) && pastSiteid.equals(1036) && currentSiteid.equals(1023)
							&& nextSiteid.equals(1024),
					past2Siteid.equals(2061) && pastSiteid.equals(1058) && currentSiteid.equals(1070)
							&& nextSiteid.equals(1069))) {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_RIGHT_SWITCH_ASPECT);
				cache.put(SPEED_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_40);
			}
			TaskexeDetailBean past3 = past2.getPast();
			if (!AppTool.isNull(past3)) {
				Integer past3Siteid = past3.getSiteid();
				if (AppTool.or(
						past3Siteid.equals(2054) && past2Siteid.equals(1036) && pastSiteid.equals(1023)
								&& currentSiteid.equals(1012),
						past3Siteid.equals(2061) && past2Siteid.equals(1058) && pastSiteid.equals(1070)
								&& currentSiteid.equals(1082) && nextSiteid.equals(1081))) {
					cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_RIGHT_SWITCH_ASPECT);
					cache.put(SPEED_DELAY, FancyAgvCmdConstant.CMD_TASK_CACHE_SPEED_40);
				}
			}
		}

		TaskexeDetailBean next2 = next.getNext();
		if (!AppTool.isNull(next2)) {
			Integer next2Siteid = next2.getSiteid();
			TaskexeDetailBean next3 = next2.getNext();
			if (!AppTool.isNull(next3)) {
				Integer next3Siteid = next3.getSiteid();
				if (AppTool.or(
						pastSiteid.equals(2009) && currentSiteid.equals(1019) && nextSiteid.equals(1031)
								&& next2Siteid.equals(1045) && next3Siteid.equals(2051),
						pastSiteid.equals(2051) && currentSiteid.equals(1045) && nextSiteid.equals(1031)
								&& next2Siteid.equals(1019) && next3Siteid.equals(2009))) {
					cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_RIGHT_REVERSE);
				}
			}
		}
	}

	private void pirouette(Integer idOne, Integer idAnother, Integer aspect, TreeMap<String, String> cache) {
		if (idOne < idAnother) {
			if (aspect == 0) {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_LEFT);
			} else if (aspect == 1) {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_RIGHT);
			}
		} else {
			if (aspect == 0) {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_RIGHT);
			} else if (aspect == 1) {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_LEFT);
			}
		}
	}

	private void pirouetteAspect(Integer idOne, Integer idAnother, Integer aspect, TreeMap<String, String> cache) {
		if (idOne < idAnother) {
			if (aspect == 0) {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_RIGHT_SWITCH_ASPECT);
			} else if (aspect == 1) {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_LEFT_SWITCH_ASPECT);
			}
		} else {
			if (aspect == 0) {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_LEFT_SWITCH_ASPECT);
			} else if (aspect == 1) {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_RIGHT_SWITCH_ASPECT);
			}
		}
	}

	private void pirouetteAllBaySite(Integer seqOne, Integer seqAnother, Integer lineOne, Integer lineAnother,
			TreeMap<String, String> cache) {
		if (seqOne > seqAnother) {
			if (lineOne > lineAnother) {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_LEFT);
			} else {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_RIGHT);
			}
		} else {
			if (lineOne > lineAnother) {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_RIGHT);
			} else {
				cache.put(ASPECT, FancyAgvCmdConstant.CMD_TASK_CACHE_PIROUETTE_LEFT);
			}
		}
	}

}
