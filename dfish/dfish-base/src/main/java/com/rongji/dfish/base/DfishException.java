package com.rongji.dfish.base;
/**
 * 
 * @author DFish Team
 *
 */
public class DfishException extends Exception{
	//公共类
	/**
	 * 未知的错误类型
	 */
	public static final String UNKNOWN_EXCEPTION="DFISH-00000";
	/**
	 * 关键字重复
	 */
	public static final String KEY_DUPLICATION="DFISH-00001";
	/**
	 * 数据字段不能为空
	 */
	public static final String KEY_EMPTY="DFISH-00002";
	
	/**
	 * 数据过大/过长
	 */
	public static final String COLUMN_TOO_LONG="DFISH-00003";

	/**
	 * 指定的关键字不存在
	 */
	public static final String KEY_MISSING="DFISH-00004";

	/**
	 * 未知的服务错误
	 */
	public static final String SERVICE_EXCEPTION="DFISH-10000";
	
	/**
	 * 服务账号不可用
	 */
	public static final String SERVICE_ACCOUNT_UNAVAILABLE="DFISH-10001";
	
	/**
	 * 提供的服务账号/密码错误
	 */
	public static final String SERVICE_PASSWORD_ERROR="DFISH-10002";
	/**
	 * 提供的服务账号没有权限
	 */
	public static final String SERVICE_ACCOUNT_NO_PERMISSION="DFISH-10003";

	//统一用户
	/**
	 * 统一用户错误
	 */
	public static final String USER_MANAGE_EXCEPTION="DFISH-11000";
	
	/**
	 * 统一用户密码错误
	 */
	public static final String USER_PASSWORD_ERROR="DFISH-11001";
	/**
	 * 统一用户账号不存在
	 */
	public static final String LOGIN_NAME_DOES_NOT_EXIST="DFISH-11002";

	/**
	 * 统一用户账号重复
	 */
	public static final String USER_ACCOUNT_REPEAT="DFISH-11003";

	/**
	 * 人员编号为空
	 */
	public static final String USER_ID_EMPTY = "DFISH-11004";
	/**
	 * 人员名称为空
	 */
	public static final String USER_NAME_EMPTY = "DFISH-11005";
	/**
	 * 同级机构人员名称重复
	 */
	public static final String USER_NAME_REPEAT = "DFISH-11006";
	/**
	 * 登录名为空
	 */
	public static final String LOGIN_NAME_EMPTY = "DFISH-11007";
	/**
	 * 登录名重复
	 */
	public static final String LOGIN_NAME_REPEAT = "DFISH-11008";
	
	/**
	 * 身份证重复
	 */
	public static final String ID_CARD_REPEAT = "DFISH-11009";
	
	/**
	 * 角色关键字为空
	 */
	public static final String ROLE_CODE_EMPTY = "DFISH-11010";
	
	/**
	 * 角色名称为空
	 */
	public static final String ROLE_NAME_EMPTY = "DFISH-11011";
	
	/**
	 * 角色下包含下级角色
	 */
	public static final String ROLE_CONTAIN_SUBORDINATE_ROLE = "DFISH-11012";
	
	/**
	 * 角色下包含下级角色
	 */
	public static final String ROLE_CONTAIN_SUBORDINATE_USER = "DFISH-11013";

	/**
	 * 角色编号为空
	 */
	public static final String ROLE_ID_EMPTY = "DFISH-11014";
	
	/**
	 * 角色名称重复
	 */
	public static final String ROLE_NAME_REPEAT = "DFISH-11015";

	/**
	 * 角色不存在
	 */
	public static final String ROLE_DOES_NOT_EXIST = "DFISH-11016";
	

	//权限管理
	/**
	 * 未知的赋权鉴权错误
	 */
	public static final String PEMISSION_EXCEPTION = "DFISH-12000";
	
	/**
	 * 类别关键字为空
	 */
	public static final String CATEGORY_CODE_EMPTY = "DFISH-12001";
	
	/**
	 * 类别编号为空
	 */
	public static final String CATEGORY_ID_EMPTY = "DFISH-12002";
	
	/**
	 * 类别名称为空
	 */
	public static final String CATEGORY_NAME_EMPTY = "DFISH-12003";

	/**
	 * 类别关键字重复
	 */
	public static final String CATEGORY_CODE_REPEAT = "DFISH-12004";
	
	/**
	 * 类别不存在
	 */
	public static final String CATEGORY_DOES_NOT_EXIST = "DFISH-12005";

	/**
	 * 操作关键字为空
	 */
	public static final String OPER_CODE_EMPTY = "DFISH-12006";
	
	/**
	 * 操作编号为空
	 */
	public static final String OPER_ID_EMPTY = "DFISH-12007";
	
	/**
	 * 操作名称为空
	 */
	public static final String OPER_NAME_EMPTY = "DFISH-12008";

	/**
	 * 操作关键字为空
	 */
	public static final String OPER_CODE_REPEAT = "DFISH-12009";
	
	/**
	 * 操作不存在
	 */
	public static final String OPER_DOES_NOT_EXIST = "DFISH-12010";

	/**
	 * 资源关键字为空
	 */
	public static final String RESOURCE_CODE_EMPTY = "DFISH-12011";
	
	/**
	 * 资源编号为空
	 */
	public static final String RESOURCE_ID_EMPTY = "DFISH-12012";
	
	/**
	 * 资源名称为空
	 */
	public static final String RESOURCE_NAME_EMPTY = "DFISH-12013";

	/**
	 * 资源关键字重复
	 */
	public static final String RESOURCE_CODE_REPEAT = "DFISH-12014";
	
	/**
	 * 资源不存在
	 */
	public static final String RESOURCE_DOES_NOT_EXIST = "DFISH-12015";
	

	//消息
	/**
	 * 消息引擎错误
	 */
	public static final String MSG_ENGINE_EXCEPTION = "DFISH-20100";
	/**
	 *  发送给(msgSendto)不能为空
	 */
	public static final String MSGSENDTO_EMPTY = "DFISH-20101";

	/**
	 *  发送给(msgContent)不能为空
	 */
	public static final String MSGCONTENT_EMPTY = "DFISH-20102";

	
	private static final long serialVersionUID = 7079476050905181817L;
	private String exceptionCode;

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public DfishException(){
		super();
	}
	public DfishException(String msg){
		super(msg);
	}
	
	public DfishException(Throwable cause) {
		super(cause);
	}
	
	public DfishException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public DfishException(String message, String exceptionCode){
		super(message);
		this.exceptionCode = exceptionCode;
	}
}
