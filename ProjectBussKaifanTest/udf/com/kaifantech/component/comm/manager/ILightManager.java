package com.kaifantech.component.comm.manager;

import com.calculatedfun.util.msg.AppMsg;

public interface ILightManager {
	public AppMsg yellow(int devId, boolean isRev) throws Exception;

	public AppMsg red(int devId, boolean isRev) throws Exception;

	public AppMsg green(int devId, boolean isRev) throws Exception;

	public AppMsg close(int devId, boolean isRev) throws Exception;

}
