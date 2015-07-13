package cn.cemobile.infointerface.util;

public class CommonData {

	/** 网络参数：访问返回的状态 */
	public final static String HTTP_STATUS = "status";
	/** 网络参数：访问返回的信息 */
	public final static String HTTP_RESULT = "result";
	/** 网络参数：Socket超时时间(20s) */
	public final static int HTTP_TIMEOUT_SOCKET = 20 * 1000;
	/** 网络参数：连接超时时间(30s) */
	public final static int HTTP_TIMEOUT_CONNECT = 30 * 1000;
	public final static int UPDATE_MAIN = 0;
	public final static int UPDATE = 1;
	public final static int NOFINDERRORLENGTH = 770;
	public final static String SECRET = "vstime1203";
	public final static String PROVINCEID = "1";
	public final static int STRING_CONTENT = 1;
	public final static int INT_CONTENT = 0;

	// 访问网络成功的访问码
	public final static String VITIST_SUCCESS = "0000";

	// 对话框选择标志
	public final static int SELECT_ITEM_FIRST = 1;
	public final static int SELECT_ITEM_SECOND = 2;

	public static String PARTURL = "http://112.5.144.241:8008/law/";
	// public static String SERVERURL = PARTURL + "if/consulting/list";
	
	/**
	 * 咨询列表
	 */
	public static final String INFO_LIST = "http://112.5.144.241:8008/law/if/consulting/list";

}
