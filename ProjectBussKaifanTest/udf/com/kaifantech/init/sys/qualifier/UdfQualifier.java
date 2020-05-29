package com.kaifantech.init.sys.qualifier;

public interface UdfQualifier extends FatherQualifier {
	public static final String DEFAULT_SERVICE_PREFIX = KfTestQualifier.DEFAULT_SERVICE_PREFIX;

	public static final String DEFAULT_TASKEXE_MODULE = FancyQualifier.TASKEXE_MODULE;
	public static final String DEFAULT_CTRL_MODULE = KfTestQualifier.CTRL_MODULE;

	public static final String DEFAULT_AGV_CLIENT_WORKER = KfTestQualifier.AGV_CLIENT_WORKER;
	public static final String DEFAULT_AGV_SERVER_WORKER = KfTestQualifier.AGV_SERVER_WORKER;

	public static final String DEFAULT_TASKEXE_ADD_SERVICE = FancyQualifier.TASKEXE_ADD_SERVICE;

	public static final String DEFAULT_ALLOC_CHECK_SERVICE = WmsQualifier.ALLOC_CHECK_SERVICE;
	public static final String DEFAULT_ALLOC_INFO_SERVICE = WmsQualifier.ALLOC_INFO_SERVICE;

	public static final String DEFAULT_TASKEXE_CHECK_SERVICE = FancyQualifier.TASKEXE_CHECK_SERVICE;

	public static final String DEFAULT_PI_WORK_TODO_SERVICE = AcsQualifier.PI_WORK_TODO_SERVICE;

	public static final String DEFAULT_AUTO_DOOR_WORKER = KfTestQualifier.AUTO_DOOR_CLIENT_WORKER;
	public static final String DEFAULT_LIFT_WORKER = KfTestQualifier.LIFT_CLIENT_WORKER;
	public static final String DEFAULT_LIGHT_WORKER = KfTestQualifier.LIGHT_CLIENT_WORKER;

	public static final String DEFAULT_IOT_CLIENT_SERVICE = KfTestQualifier.IOT_CLIENT_SERVICE;
	public static final String DEFAULT_TASKEXE_DETAIL_JOBS_SERVICE = KfTestQualifier.TASKEXE_DETAIL_JOBS_SERVICE;

	public static final String DEFAULT_TASKEXE_WATCH_SERVICE = FancyQualifier.TASKEXE_WATCH_SERVICE;
	public static final String DEFAULT_TASKEXE_DEALER = FancyQualifier.TASKEXE_DEALER;

	public static final String DEFAULT_FETCH_OP_SERVICE = AcsQualifier.FETCH_OP_SERVICE;
	public static final String DEFAULT_DELIVER_OP_SERVICE = AcsQualifier.DELIVER_OP_SERVICE;
	public static final String DEFAULT_ALLOC_AMOUNT_SERVICE = WmsQualifier.ALLOC_AMOUNT_SERVICE;
	public static final String DEFAULT_TASK_SITE_INFO_SERVICE = AcsQualifier.TASK_SITE_INFO_SERVICE;
	public static final String DEFAULT_TASKEXE_DETAIL_DEALER = AcsQualifier.TASKEXE_DETAIL_DEALER;

	public static final String DEFAULT_CHARGE_WORKER = CsyQualifier.CHARGE_WORKER;

	public static final String DEFAULT_LAP_INFO_SERVICE = BaseQualifier.LAP_INFO_SERVICE;
	public static final String DEFAULT_PAPER_SERVICE = BaseQualifier.PAPER_SERVICE;
	public static final String DEFAULT_APP_CONF_SERVICE = BaseQualifier.APP_CONF_SERVICE;
	public static final String DEFAULT_AGV_INFO_DAO = BaseQualifier.AGV_INFO_DAO;
}
