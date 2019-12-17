package com.kaifantech.component.service.tasksite.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.calculatedfun.util.AppTool;
import com.kaifantech.bean.taskexe.TaskexeBean;
import com.kaifantech.bean.tasksite.TaskSiteInfoBean;
import com.kaifantech.bean.tasksite.TaskSiteLogicBean;
import com.kaifantech.component.service.tasksite.TaskSiteLogicService;
import com.kaifantech.util.log.AppFileLogger;

@Service
public class KfTestTaskexePathService extends FancyTaskexePathService {
	@Inject
	private TaskSiteLogicService taskSiteLogicService;

	public synchronized TaskSiteInfoBean getKey(TaskexeBean taskexeBean, TaskSiteInfoBean startSite,
			TaskSiteInfoBean targetSite) throws Exception {
		long timestamp = System.currentTimeMillis();
		TaskSiteInfoBean key = null;

		String aspect = "0";

		List<TaskSiteInfoBean> sitesForSearch = new ArrayList<>();
		sitesForSearch.add(startSite);

		boolean isSuccess = false;
		while (!isSuccess) {
			List<TaskSiteInfoBean> choosedSiteList = new ArrayList<>();
			for (TaskSiteInfoBean currentSite : sitesForSearch) {
				Map<String, TaskSiteInfoBean> nextSiteList = currentSite.getNexts();
				if (AppTool.isNull(nextSiteList)) {
					continue;
				}

				for (TaskSiteInfoBean nextSite : nextSiteList.values()) {
					if (targetSite.getId().equals(nextSite.getId())) {
						key = nextSite;
						isSuccess = true;
						break;
					}
				}

				for (TaskSiteInfoBean nextSite : nextSiteList.values()) {
					if (System.currentTimeMillis() - timestamp > 10000) {
						AppFileLogger.setErrorTips(0, "doFindKey", startSite.getId(), "到", targetSite.getId(),
								"路径生成耗时过长，可能出现死循环，系统中断其运行！");
						return null;
					}
					if (nextSite.getId().equals(278)) {
						if (!nextSite.getId().equals(278)) {
						}
					}
					if (targetSite.isAllocSite()) {
						Integer targetLine = targetSite.getLine();
						Integer currentLine = currentSite.getLine();
						Integer nextLine = nextSite.getLine();
						if (currentSite.isBaySite() && nextSite.isBaySite() && !currentLine.equals(nextLine)) {
							if (currentLine > targetLine) {
								if (nextLine > currentLine) {
									continue;
								}
							} else if (targetLine > currentLine) {
								if (currentLine > nextLine) {
									continue;
								}
							} else if (currentLine == targetLine) {
								continue;
							}
						} else if (currentSite.isBaySite() && nextSite.isBaySite() && currentLine.equals(nextLine)) {
							Boolean betterPath = false;
							if (currentLine > targetLine) {
								for (TaskSiteInfoBean nextSite1 : nextSiteList.values()) {
									if (currentLine > nextSite1.getLine()) {
										betterPath = true;
										break;
									}
								}
								if (betterPath) {
									continue;
								}
							} else if (targetLine > currentLine) {
								if (nextSite.getId().equals(1085)) {
									for (TaskSiteInfoBean currentSite1 : sitesForSearch) {
										if (currentSite1.getLine().equals(8)) {
											betterPath = true;
											break;
										}
									}
									if (betterPath) {
										continue;
									}
								}
								for (TaskSiteInfoBean nextSite1 : nextSiteList.values()) {
									if (AppTool.or(nextSite1.isBaySite() && nextSite1.getLine() > currentLine,
											AppTool.equals(nextSite1.getId(), 2054, 2055, 2056, 2057)
													&& nextSite1.getLine() == currentLine)) {
										betterPath = true;
										break;
									}
								}
								if (betterPath) {
									continue;
								}
							}
						} else if (currentSite.isBaySite() && nextSite.isAllocSite()) {
							if (currentLine.equals(targetLine)) {
								if (!nextSite.getId().equals(targetSite.getId())) {
									continue;
								}
							} else {
								if (!nextLine.equals(3)) {
									continue;
								} else if (currentLine.equals(nextLine) && currentLine > targetLine) {
									continue;
								} else if (currentLine > nextLine && targetLine > nextLine) {
									continue;
								}
							}
						} else if (AppTool.equals(currentSite.getId(), 2054, 2055, 2056, 2057)
								&& nextSite.isBaySite()) {
							if (currentLine == nextLine && targetLine > nextLine)
								continue;
							else if (nextLine > currentLine && nextLine > targetLine)
								continue;
						}

						if (targetLine.equals(0)) {
							if (currentSite.isOutSite() && nextSite.isOutSite()) {
								Integer currentSeqId = currentSite.getJsonItem("SEQ", Integer.class);
								Integer nextSeqId = nextSite.getJsonItem("SEQ", Integer.class);
								if (nextSeqId > currentSeqId) {
									continue;
								}
							}
						}
					} else if (targetSite.isOutSite()) {
						if (currentSite.isBaySite() && nextSite.isBaySite()
								&& !currentSite.getLine().equals(nextSite.getLine())) {
							continue;
						} else if (nextSite.isAllocSite() && !nextSite.getLine().equals(0)) {
							continue;
						} else if (currentSite.isBaySite() && nextSite.isOutSite()) {
							if (!targetSite.getLine().equals(nextSite.getLine())) {
								continue;
							}
						}
					}

					if (!startSite.getId().equals(currentSite.getId()) && !AppTool.isNull(currentSite.getPre())) {
						if (nextSite.getId().equals(currentSite.getPre().getId())) {
							continue;
						}
					}
					TaskSiteLogicBean logic = taskSiteLogicService.get(currentSite, nextSite);
					String currentAspect = logic.getJsonItem("aspect");
					if (!"0".equals(aspect) && !aspect.equals(currentAspect) && !"0".equals(currentAspect)) {
						continue;
					}
					if ("0".equals(aspect) && !aspect.equals(currentAspect)) {
						aspect = currentAspect;
					}
					if (nextSite.getId().equals(startSite.getId())) {
						continue;
					}
					nextSite.setPre(currentSite);
					choosedSiteList.add(nextSite);
				}
			}
			sitesForSearch.clear();
			sitesForSearch.addAll(choosedSiteList);
			if (sitesForSearch.size() == 0) {
				break;
			}
		}
		return key;
	}
}