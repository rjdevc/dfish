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
	 * 指定用这个schema编号所对应的schema 将src返回的内容解析成dfish的格式。
	 * @return String
	 */
	String getSchema();
	/**
	 * 指定用这个schema编号所对应的schema 将src返回的内容解析成dfish的格式。
	 * @param schema String
	 * @return this
	 */
	T setSchema(String schema);
}
