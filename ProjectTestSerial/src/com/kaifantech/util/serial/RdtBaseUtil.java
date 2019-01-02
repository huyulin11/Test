package com.kaifantech.util.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

import com.kaifantech.bean.msg.csy.agv.CsyAgvCmdBean;
import com.kaifantech.bean.msg.csy.agv.CsyAgvMsgBean;
import com.kaifantech.util.hex.AppByteUtil;
import com.kaifantech.util.thread.ThreadTool;
import com.ytgrading.util.AppTool;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class RdtBaseUtil implements SerialPortEventListener {

	protected static CommPortIdentifier portid = null; // 通讯端口标识符
	protected static SerialPort comPort = null; // 串行端口
	protected int BAUD = 9600; // 波特率
	protected int DATABITS = SerialPort.DATABITS_8; // 数据位
	protected int STOPBITS = SerialPort.STOPBITS_1; // 停止位
	protected int PARITY = SerialPort.PARITY_NONE; // 奇偶检验
	public static OutputStream oStream; // 输出流
	public static InputStream iStream; // 输入流

	private static Map<String, RdtBaseUtil> map = new HashMap<>();

	public static RdtBaseUtil get(String com) {
		RdtBaseUtil o = map.get(com);
		if (AppTool.isNull(o)) {
			o = new RdtBaseUtil(com);
			map.put(com, o);
		}
		return map.get(com);
	}

	private RdtBaseUtil(String com) {
		setSerialPortNumber(com);
	}

	/**
	 * 读取所有串口名字
	 */
	public void listPortChoices() {
		CommPortIdentifier portId;
		Enumeration<?> en = CommPortIdentifier.getPortIdentifiers();
		// iterate through the ports.
		while (en.hasMoreElements()) {
			portId = (CommPortIdentifier) en.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				System.out.println(portId.getName());
			}
		}
	}

	/**
	 * 设置串口号
	 *
	 * @param
	 * @return
	 */
	public void setSerialPortNumber(String osName) {
		String osname = System.getProperty("os.name", "").toLowerCase();
		if (osname.startsWith("windows")) {
		} else if (osname.startsWith("linux")) {
			osName = "/dev/ttyS1";
		}
		System.out.println(osName);
		try {
			portid = CommPortIdentifier.getPortIdentifier(osName);
			// portid = CommPortIdentifier.getPortIdentifier(Port);
			if (portid.isCurrentlyOwned()) {
				System.out.println("端口在使用");
			} else {
				comPort = (SerialPort) portid.open(this.getClass().getName(), 1000);
			}
		} catch (PortInUseException e) {
			System.out.println("端口被占用");
			e.printStackTrace();
		} catch (NoSuchPortException e) {
			System.out.println("端口不存在");
			e.printStackTrace();
		}

		try {
			iStream = comPort.getInputStream(); // 从COM1获取数据
			oStream = comPort.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			comPort.addEventListener(this); // 给当前串口增加一个监听器
			comPort.notifyOnDataAvailable(true); // 当有数据是通知
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}

		try {
			// 设置串口参数依次为(波特率,数据位,停止位,奇偶检验)
			comPort.setSerialPortParams(this.BAUD, this.DATABITS, this.STOPBITS, this.PARITY);
		} catch (UnsupportedCommOperationException e) {
			System.out.println("端口操作命令不支持");
			e.printStackTrace();
		}
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:// 当有可用数据时读取数据,并且给串口返回数据
			break;
		}
	}

	public static final String LIGHT_SHOW = "FD004103" + "FB" + "440A" + "01C6" + "89" + "FE";
	public static final String AGV_MOVE = "FD00" + "410C" + "FB" + "410C" + "0181" + "CD" + "FE";

	public String sendToDevAndPrint(String address, String cmd) throws IOException {
		String finalCmd = "FD00" + address + "FB" + address + "01" + cmd + "FE";
		oStream.write(AppByteUtil.hexStringToBytes(finalCmd));
		ThreadTool.sleepOneSecond();
		String ss = AppByteUtil.getHexStrFrom(iStream);
		return "发送：" + finalCmd + ";接收" + "：" + ss;
	}

	public String sendToDev(String address, String cmd) throws IOException {
		String finalCmd = "FD00" + address + "FB" + address + "01" + cmd + "FE";
		oStream.write(AppByteUtil.hexStringToBytes(finalCmd));
		ThreadTool.sleepOneSecond();
		String ss = AppByteUtil.getHexStrFrom(iStream);
		return ss;
	}

	public String sendToAgv(Integer agvId, String address, String cmd) throws IOException {
		timer();
		String finalCmd = "FD00" + address + CsyAgvCmdBean.getTaskCmd(agvId, cmd);
		oStream.write(AppByteUtil.hexStringToBytes(finalCmd));
		ThreadTool.sleepOneSecond();
		String ss = AppByteUtil.getHexStrFrom(iStream);
		return ss;
	}

	public String sendToAgvAndPrint(Integer agvId, String address, String cmd) throws IOException {
		String finalCmd = "FD00" + address + CsyAgvCmdBean.getTaskCmd(agvId, cmd);
		oStream.write(AppByteUtil.hexStringToBytes(finalCmd));
		ThreadTool.sleepOneSecond();
		String ss = AppByteUtil.getHexStrFrom(iStream);
		return "发送：" + finalCmd + ";接收" + "：" + ss;
	}

	private boolean isInTimer = false;

	private void timer() {
		if (!isInTimer) {
			while (true) {
				isInTimer = true;
				try {
					String msg = sendToAgv(1, "4103", "02");
					if (AppTool.isNull(msg)) {
						continue;
					}
					CsyAgvMsgBean msgBean = new CsyAgvMsgBean(msg);
					if (!msgBean.isValid()) {
						continue;
					}
					Integer currentSite = msgBean.currentSite();
					if (AppTool.equals(currentSite, 5599)) {
						sendToDevAndPrint("4202", "B1F0");
						ThreadTool.sleep(5000);
						sendToAgvAndPrint(1, "4103", "05");
					}
					if (AppTool.equals(currentSite, 5523)) {
						sendToDevAndPrint("4202", "B2F3");
					}
					if (AppTool.equals(currentSite, 5693)) {
						sendToDevAndPrint("4202", "B2F3");
					}
					if (AppTool.equals(currentSite, 5967)) {
						sendToDevAndPrint("4301", "A3E0");
						ThreadTool.sleep(60000);
						sendToDevAndPrint("4301", "A2E1");
					}
					if (AppTool.equals(currentSite, 5838)) {
						sendToDevAndPrint("4301", "A3E0");
					}
					if (AppTool.equals(currentSite, 5968)) {
						sendToDevAndPrint("4202", "B1F0");
						ThreadTool.sleep(5000);
						sendToAgvAndPrint(1, "4103", "05");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				ThreadTool.sleep(1000);
			}
		}

	}

	public static void main(String[] args) {
		RdtBaseUtil app = new RdtBaseUtil("COM1");
		try {
			app.sendToAgv(1, "4103", "05");
		} catch (IOException e) {
			System.out.println("发送数据异常");
			e.printStackTrace();
		}
	}
}
