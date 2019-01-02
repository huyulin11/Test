package com.kaifantech.bean.msg.csy.agv;

import com.kaifantech.util.constants.cmd.AgvCmdConstant;
import com.ytgrading.util.AppArraysUtil;

public class CsyAgvCacheCommand extends CsyAgvCommand {
	public CsyAgvCacheCommand(Integer agvId, Integer tasksite, String... cmds) {
		super(agvId, tasksite,
				AppArraysUtil.addInFirst(cmds, AgvCmdConstant.CMD_TASK_CACHE, CsyAgvCmdBean.getSitecode(tasksite)));
	}

	public String toString() {
		return "agvId:" + getAgvId() + "tasksite:" + getTasksite() + "cmds" + getCmds();
	}
}
