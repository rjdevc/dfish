package com.rongji.dfish.ui;

/**
 * 指明当前对象有一部分内容是通过延迟加载而产生的。
 * 可能是其主要内容，也可能是其子内容。
 *  <p>部分组件，如图片也是有src属性，并不属于该接口</p>
 *
 * @param <T> 当前对象类型
 */
public interface HasSrc<T extends HasSrc<T>> {
	/**
	 * 加载 具体内容 的 url。访问这个url 时应当返回一个 json 字串。
	 * 如果没有template 这个字符串应该是dfish的格式。
	 * 如果有template 那么template 讲把这个字符串解析成dfish需要的格式。
	 * @return String
	 */
	String getSrc();
	/**
	 * 加载 具体内容 的 url。访问这个url 时应当返回一个 json 字串。
	 * 如果没有template 这个字符串应该是dfish的格式。
	 * 如果有template 那么template 讲把这个字符串解析成dfish需要的格式。
	 * @param src url
	 * @return this
	 */
	T setSrc(String src);

	/**
	 * 加载 url 的时候使用同步模式，所属父节点(可能是xsrc/view/leaf等)动作将会等待 src内容装载完毕后再触发。
	 * @return String
	 */
	Boolean getSync();
	/**
	 * 加载 url 的时候使用同步模式，所属父节点(可能是xsrc/view/leaf等)动作将会等待 src内容装载完毕后再触发。
	 * @param sync Boolean
	 * @return this
	 */
	T setSync(Boolean sync);
	/**
	 * 指定用这个编号所对应的模板 将src返回的内容解析成dfish的格式。
	 * @return String
	 */
	String getTemplate();
	/**
	 * 指定用这个编号所对应的模板 将src返回的内容解析成dfish的格式。
	 * @param template String
	 * @return this
	 */
	T setTemplate(String template);


	/**
	 * 在获取服务器的响应数据失败后调用的函数。支持一个变量，<b>$ajax</b>(Ajax实例)
	 * @param error javascript
	 * @return this
	 */
	T setError(String error);

	/**
	 * 在获取服务器的响应数据失败后调用的函数。支持一个变量，<b>$ajax</b>(Ajax实例)
	 * @return String
	 */
	String getError();

	/**
	 * 在得到服务器的响应后调用的函数(不论成功失败都会执行)。支持两个变量，<b>$value</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)
	 * @param complete javascript
	 * @return this
	 */
	T setComplete(String complete);

	/**
	 * 在得到服务器的响应后调用的函数(不论成功失败都会执行)。支持两个变量，<b>$value</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)
	 * @return String
	 */
	String getComplete();
	/**
	 * 在成功获取服务器的响应数据并执行返回的命令之后调用的函数。如果设置了本参数，引擎将不会执行后台返回的命令，由业务自行处理。支持两个变量，<b>$value</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)
	 * @param success  javascript
	 * @return this
	 */
	T setSuccess(String success);

	/**
	 * 在成功获取服务器的响应数据并执行返回的命令之后调用的函数。如果设置了本参数，引擎将不会执行后台返回的命令，由业务自行处理。支持两个变量，<b>$value</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例)
	 * @return String
	 */
	String getSuccess();

	/**
	 * 在获取服务器的响应数据后调用的函数。本语句应当 return 一个命令JSON。支持两个变量，<b>$value</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例
	 * @param filter javascript
	 * @return this
	 */
	T setFilter(String filter);

	/**
	 * 在获取服务器的响应数据后调用的函数。本语句应当 return 一个命令JSON。支持两个变量，<b>$value</b>(服务器返回的JSON对象), <b>$ajax</b>(Ajax实例
	 * @return String
	 */
	String getFilter();
}
