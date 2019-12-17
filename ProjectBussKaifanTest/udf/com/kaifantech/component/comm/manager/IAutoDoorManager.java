package com.kaifantech.component.comm.manager;

import com.calculatedfun.util.msg.AppMsg;

public interface IAutoDoorManager {
	public AppMsg open(int devId) throws Exception;

	public AppMsg close(int devId) throws Exception;

	public AppMsg search(int devId) throws Exception;
}
