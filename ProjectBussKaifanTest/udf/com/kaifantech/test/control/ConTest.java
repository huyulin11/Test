package com.kaifantech.test.control;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.calculatedfun.util.msg.AppMsg;
import com.kaifantech.bean.msg.fancy.agv.FancyAgvCacheCommand;
import com.kaifantech.bean.tasksite.TaskSiteInfoBean;
import com.kaifantech.component.comm.manager.agv.IFancyAgvManager;
import com.kaifantech.component.service.iot.client.IIotClientService;
import com.kaifantech.component.service.tasksite.path.IFancyTaskexePathService;
import com.kaifantech.init.sys.qualifier.UdfQualifier;
import com.kaifantech.util.constants.cmd.FancyAgvCmdConstant;
import com.kaifantech.util.log.AppFileLogger;
import com.kaifantech.util.thread.ThreadTool;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-shiro.xml", "classpath:spring-application.xml",
		"classpath:spring-mvc-scan.xml" })
public class ConTest {
	private MockMvc mockMvc;

	@Autowired
	private IFancyTaskexePathService taskexePathService;

	@Autowired
	private IFancyAgvManager agvManager;

	@Autowired
	@Qualifier(UdfQualifier.DEFAULT_IOT_CLIENT_SERVICE)
	private IIotClientService iotClientService;

	@Before
	public void Ctest() throws Exception {
		ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/de/acs/wms/addAcsTask").param(
				"{\"Type\":\"2\",\"ShipmentId\":\"A-2-b89732a3434c4972b44179438bfa535e\",\"Wicket\":\"ck2\",\"ItemList\":[{\"AllocId\":\"002-F2-02\"}],\"WarehouseId\":\"B\"}"));
		MvcResult mvcResult = resultActions.andReturn();
		String result = mvcResult.getResponse().getContentAsString();
		AppFileLogger.warning("=====客户端获得反馈数据:" + result);
		// 也可以从response里面取状态码，header,cookies...
		// AppFileLogger.warning(mvcResult.getResponse().getStatus());
	}

	public void testObstacle() {
		agvManager.incaseofObstacleNormal(3);
		ThreadTool.sleep(5000);
	}

	@Test
	public void testPath() {
		try {
			List<TaskSiteInfoBean> path = taskexePathService.doGetPath(49, 67);
			Collections.reverse(path);
			AppFileLogger.warning(path);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testCacheSite() {
		try {
			// ser.incaseofObstacleCorner(1);
			// ser.incaseofObstacleNormal(1);
			// for (int i = 500; i >= 1; i--) {
			// ser.cacheTask(new CsyAgvCacheCommand(2, i,
			// AgvCmdConstant.CMD_TASK_CACHE_STOP));
			// }
			AppMsg msg = agvManager.cacheTask(new FancyAgvCacheCommand(3, 21,
					FancyAgvCmdConstant.CMD_TASK_CACHE_TURN_RIGHT, FancyAgvCmdConstant.CMD_TASK_CACHE_TEST_LOW_SPEED));
			AppFileLogger.warning(msg.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}