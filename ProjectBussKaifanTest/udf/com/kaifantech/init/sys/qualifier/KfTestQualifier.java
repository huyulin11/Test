package com.kaifantech.init.sys.qualifier;

import com.kaifantech.init.sys.ServicePrefix;

public interface KfTestQualifier extends FancyQualifier {
	public static final String DEFAULT_SERVICE_PREFIX = ServicePrefix.LAO_DBWY;

	public static final String TASKEXE_MODULE = DEFAULT_SERVICE_PREFIX + "TaskexeModule";
	public static final String CTRL_MODULE = DEFAULT_SERVICE_PREFIX + "CtrlModule";

	public static final String AGV_CLIENT_WORKER = DEFAULT_SERVICE_PREFIX + "AgvClientWorker";
	public static final String AUTO_DOOR_CLIENT_WORKER = DEFAULT_SERVICE_PREFIX + "AutoDoorClientWorker";
	public static final String LIFT_CLIENT_WORKER = DEFAULT_SERVICE_PREFIX + "LiftClientWorker";
	public static final String LIGHT_CLIENT_WORKER = DEFAULT_SERVICE_PREFIX + "LightClientWorker";

	public static final String IOT_CLIENT_SERVICE = DEFAULT_SERVICE_PREFIX + "IotClientService";

	public static final String TASKEXE_DETAIL_JOBS_SERVICE = DEFAULT_SERVICE_PREFIX + "TaskexeDetailJobsService";
	public static final String LOCK_WATCHER = DEFAULT_SERVICE_PREFIX + "LockWatcher";
}
