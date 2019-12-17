package com.kaifantech.component.service.pi.ctrl.dealer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.calculatedfun.util.AppTool;
import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.bean.taskexe.FancyTaskexeBean;
import com.kaifantech.component.dao.agv.info.AgvInfoDao;
import com.kaifantech.component.service.pi.ctrl.work.FancyPiInfoService;
import com.kaifantech.init.sys.qualifier.DefaultSystemQualifier;
import com.kaifantech.util.agv.msg.PiCommand;
import com.kaifantech.util.log.AppFileLogger;

@Component
public class KfTestPiCtrlDealer implements IPiCtrlDealer {

	@Autowired
	private FancyPiInfoService piInfoService;

	private Map<Object[], Object[]> clashAreasFull = null;

	private Map<Object[], Object[]> clashAreasRev = null;

	@Autowired
	@Qualifier(DefaultSystemQualifier.DEFAULT_AGV_INFO_DAO)
	private AgvInfoDao agvInfoDao;

	public PiCommand check2Agvs(IotClientBean one, IotClientBean another) throws Exception {
		PiCommand command = new PiCommand();
		FancyTaskexeBean piOne = piInfoService.get(one.getId());
		FancyTaskexeBean piAnother = piInfoService.get(another.getId());
		command = inInit(piOne, piAnother);
		if (!AppTool.isNull(command)) {
			return command;
		}
		if (AppTool.isAnyNull(piOne, piAnother)) {
			return null;
		}

		command = toClash(piOne, piAnother, getClashAreasFull());
		if (!AppTool.isNull(command)) {
			return command;
		}

		if (piOne.getTasktype().equals(piAnother.getTasktype())) {
			return null;
		}

		command = toClash(piOne, piAnother, getClashAreasRev());
		return command;
	}

	private PiCommand inInit(FancyTaskexeBean piOne, FancyTaskexeBean piAnother) {
		PiCommand command = new PiCommand();
		if (AppTool.isAnyNull(piOne, piAnother)) {
			return null;
		}
		if (AppTool.isAnyNull(piOne.agvInfo.getCurrentsite(), piAnother.agvInfo.getCurrentsite())) {
			return null;
		}
		if (piOne.agvInfo.getCurrentsite().equals(87) && piAnother.agvInfo.getCurrentsite().equals(28)) {
			if (!AppTool.isNull(piOne)) {
				command.d(piOne);
				command.s(piAnother);
				return command;
			}
		}
		if (piOne.agvInfo.getCurrentsite().equals(28) && piAnother.agvInfo.getCurrentsite().equals(87)) {
			if (!AppTool.isNull(piAnother)) {
				command.d(piAnother);
				command.s(piOne);
				return command;
			}
		}
		return null;
	}

	private Map<Object[], Object[]> getClashAreasFull() {
		if (AppTool.isNull(clashAreasFull)) {
			clashAreasFull = new HashMap<>();
			clashAreasFull.put(new Object[] { 65, 66, 53, 56, 61, 67, 68, 92, 88, 90, 89, 93, 59 },
					new Object[] { 58 });
			clashAreasFull.put(new Object[] { 64, 63, 62, 60 }, new Object[] { 109 });
			clashAreasFull.put(new Object[] { 64, 63, 62 }, new Object[] { 60 });
			clashAreasFull.put(new Object[] { 92 }, new Object[] { 65 });
			clashAreasFull.put(new Object[] { 66 }, new Object[] { 92 });

			clashAreasFull.put(new Object[] { 50 }, new Object[] { 108 });
			clashAreasFull.put(new Object[] { 107 }, new Object[] { 50 });
		}
		return clashAreasFull;
	}

	private Map<Object[], Object[]> getClashAreasRev() {
		if (AppTool.isNull(clashAreasRev)) {
			clashAreasRev = new HashMap<>();
			clashAreasRev.put(new Integer[] { 29, 31 }, new Integer[] { 83 });
			clashAreasRev.put(new Integer[] { 84 }, new Integer[] { 29, 31 });

			clashAreasRev.put(new Integer[] { 34, 91 }, new Integer[] { 110 });
			clashAreasRev.put(new Integer[] { 82, 91 }, new Integer[] { 34 });

			clashAreasRev.put(new Integer[] { 78, 79 }, new Integer[] { 36 });
			clashAreasRev.put(new Integer[] { 37 }, new Integer[] { 78, 79 });

			clashAreasRev.put(new Integer[] { 47, 49, 50, 54, 55, 57 }, new Integer[] { 70 });
			clashAreasRev.put(new Integer[] { 72, 50, 54, 55, 57 }, new Integer[] { 97, 47, 49 });

			clashAreasRev.put(new Integer[] { 59 }, new Integer[] { 65 });
		}
		return clashAreasRev;
	}

	public PiCommand toClash(FancyTaskexeBean piOne, FancyTaskexeBean piAnother, Map<Object[], Object[]> clashAreas) {
		PiCommand command = new PiCommand();
		for (Entry<Object[], Object[]> entry : clashAreas.entrySet()) {
			command = toClash(piOne, piAnother, entry.getKey(), entry.getValue());
			if (!AppTool.isNull(command)) {
				return command;
			}
		}
		return null;
	}

	public PiCommand toClash(FancyTaskexeBean piOne, FancyTaskexeBean piAnother, Object[] clashAreas,
			Object[] clashPoints) {
		PiCommand command = new PiCommand();

		if (AppTool.equals(piOne.agvInfo.getCurrentsite(),
				new Object[] { 65, 66, 53, 56, 61, 67, 68, 92, 88, 90, 89, 93, 59 })
				&& AppTool.equals(piAnother.agvInfo.getCurrentsite(), new Object[] { 58 })
				&& !AppTool.isNull(piAnother.nextStopSite) && AppTool.equals(piAnother.nextStopSite.getId(), 60)) {
			return null;
		}
		if (AppTool.equals(piAnother.agvInfo.getCurrentsite(),
				new Object[] { 65, 66, 53, 56, 61, 67, 68, 92, 88, 90, 89, 93, 59 })
				&& AppTool.equals(piOne.agvInfo.getCurrentsite(), new Object[] { 58 })
				&& !AppTool.isNull(piOne.nextStopSite) && AppTool.equals(piOne.nextStopSite.getId(), 60)) {
			return null;
		}

		if (AppTool.equals(piOne.agvInfo.getCurrentsite(), clashAreas)
				&& AppTool.equals(piAnother.agvInfo.getCurrentsite(), clashPoints)) {
			command.d(piAnother.getAgvId());
			command.s(piOne.getAgvId());
			String piMsg = piAnother.getAgvId() + "," + piOne.getAgvId() + "两车在冲突区域（" + Arrays.toString(clashAreas)
					+ "），停接近冲突区域车：" + piAnother.getAgvId();
			command.setInfo(piMsg);
			AppFileLogger.piLogs(piMsg);
			return command;
		}
		if (AppTool.equals(piAnother.agvInfo.getCurrentsite(), clashAreas)
				&& AppTool.equals(piOne.agvInfo.getCurrentsite(), clashPoints)) {
			command.d(piOne);
			command.s(piAnother.agvInfo.getId());
			String piMsg = piAnother.getAgvId() + "," + piOne.getAgvId() + "两车在冲突区域（" + Arrays.toString(clashAreas)
					+ "），停接近冲突区域车：" + piAnother.getAgvId();
			command.setInfo(piMsg);
			AppFileLogger.piLogs(piMsg);
			return command;
		}
		return null;
	}
}
