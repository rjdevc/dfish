package com.rongji.dfish.misc.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.rongji.dfish.base.util.LogUtil;

/**
 * XML工具类,使用此工具类需要导入dom4j.jar
 * 
 * @author DFish Team
 * 
 */
public class XMLUtil {

	private String fileFullName;
	private Document doc;
	private static final String ENCODING = "UTF-8";

	/**
	 * 获得文件全名
	 * @return String
	 */
	public String getFileFullName() {
		return fileFullName;
	}

	/**
	 * 设置文件全名
	 * @param fileFullName String
	 */
	public void setFileFullName(String fileFullName) {
		this.fileFullName = fileFullName;
	}

	/**
	 * 获得 Document
	 * @return Document
	 */
	public Document getDoc() {
		return doc;
	}

	/**
	 * 设置Document
	 * @param doc Document
	 */
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	
	/**
	 * 创建对象:默认会构建一个空的Document
	 */
	public XMLUtil() {
		doc = DocumentHelper.createDocument();
	}

	/**
	 * 创建对象
	 * @param doc Document
	 */
	public XMLUtil(Document doc) {
		this.doc = doc;
	}

	/**
	 * 创建对象
	 * @param inputStream 输入流
	 * @throws DocumentException 文档异常
	 */
	public XMLUtil(InputStream inputStream) throws DocumentException {
		readDoc(inputStream);
	}

	/**
	 * 创建对象
	 * @param file 文件
	 * @throws DocumentException 文档异常
	 */
	public XMLUtil(File file) throws DocumentException {
		if (file == null) {
			throw new UnsupportedOperationException("The input File can not be null.");
		}
		fileFullName = file.getAbsolutePath();
		readDoc(file);
	}

	/**
	 * 创建对象
	 * @param fileFullName 文件完整名
	 * @throws DocumentException 文档异常
	 */
	public XMLUtil(String fileFullName) throws DocumentException {
		this.fileFullName = fileFullName;
		readDoc();
	}

	private synchronized void readDoc() {
		readDoc(fileFullName);
	}
	
	private synchronized void readDoc(String fileFullName) {
		readDoc(new File(fileFullName));
	}
	
	/**
	 * 读取文件内容
	 */
	private synchronized void readDoc(File file) {
		if (file == null) {
			throw new UnsupportedOperationException("The input File can not be null.");
		}
		try {
			doc = new SAXReader().read(file);
		} catch (DocumentException ex) {
			LogUtil.error("读取XML文件[" + fileFullName + "]失败,使用空内容代替", ex);
//			DocumentFactory f = new DocumentFactory();
//			doc = f.createDocument(f.createElement("datas"));
			doc = DocumentHelper.createDocument();
			doc.add(DocumentHelper.createElement("datas"));
		}
	}
	
	/**
	 * 读取文件内容
	 */
	private synchronized void readDoc(InputStream inputStream) {
		try {
			doc = new SAXReader().read(inputStream);
		} catch (DocumentException ex) {
			LogUtil.error("读取XML文件[" + fileFullName + "]失败,使用空内容代替", ex);
//			DocumentFactory f = new DocumentFactory();
//			doc = f.createDocument(f.createElement("datas"));
			doc = DocumentHelper.createDocument();
			doc.add(DocumentHelper.createElement("datas"));
		}
	}

	/**
	 * 写入文件内容
	 * 
	 * @param encoding String 编码
	 */
	public synchronized void writeDoc(String encoding) {
		OutputStream out = null;
		boolean error = false;
		File tempFile = null;
		File file = new File(fileFullName);
		try {
			File pfile = file.getParentFile();
			if (!pfile.exists()) {
				pfile.mkdirs();
			}
			tempFile = new File(file.getParentFile(), file.getName().concat(".tmp"));
			out = new BufferedOutputStream(new FileOutputStream(tempFile));
			
			OutputFormat outputFormat = getOutputFormat(encoding);
			XMLWriter outputter = new XMLWriter(out, outputFormat);
			doc.setXMLEncoding(encoding);
			outputter.write(doc);
		} catch (Exception e) {
			LogUtil.error("写入文件异常", e);
			error = true;
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				LogUtil.error("关闭流异常", e);
				error = true;
			}
		}
		if (!error) {
			file.delete();
			tempFile.renameTo(file);
		}
	}
	
	/**
	 * 获取XML的格式,以换行、易看懂的格式
	 *
	 * @param encoding 编码
	 * @return 设置好的XML格式
	 */
	public static OutputFormat getOutputFormat(String encoding) {
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		outputFormat.setIndent(" ");
		outputFormat.setIndentSize(1);
		outputFormat.setExpandEmptyElements(false);
		outputFormat.setLineSeparator("\r\n");
		outputFormat.setEncoding(encoding);
		outputFormat.setLineSeparator(System.getProperty("xml.line.separator", "\n"));
		return outputFormat;
	}
	
	/**
	 * 格式化XML
	 * 
	 * @param doc Document
	 * @param encoding 编码
	 * @return String
	 * @throws IOException XML异常
	 * @return String XML文本
	 */
	public static String formatXML(Document doc, String encoding)
			throws IOException {
		// 创建输出(目标)
		StringWriter stringWriter = new StringWriter();
		// 创建输出流
		XMLWriter xmlWriter = new XMLWriter(stringWriter, getOutputFormat(encoding));
		// 输出格式化的串到目标中，执行后。格式化后的串保存在stringWriter中。
		try {
			xmlWriter.write(doc);
		} catch (IOException e) {
			throw e;
		} finally {
			if (xmlWriter != null) {
				xmlWriter.close();
			}
		}

		return stringWriter.toString();
	}

	/**
	 * 获取根节点下的所有子节点
	 * 
	 * @return 根节点下的所有子节点列表
	 */
	public List<Element> getTopElements() {
		return getChildElements(doc.getRootElement());
	}

	/**
	 * 获取根节点下的所有子节点名称
	 * 
	 * @return List
	 */
	public List<String> getTopElementNames() {
		return getChildElementNames(doc.getRootElement());
	}

	/**
	 * 根据父节点获取其所有子节点
	 * 
	 * @param element
	 *            父节点
	 * @return 所有子节点列表
	 */
	@SuppressWarnings("unchecked")
	public List<Element> getChildElements(Element element) {
		List<Element> dataList = new ArrayList<>();
		List<Element> childList = element.elements();
		if (childList != null) {
			for (Element child : childList) {
				dataList.add(child);
			}
		}
		return dataList;
	}

	/**
	 * 根据父节点获取其子节点名称
	 * 
	 * @param element
	 *            父节点
	 * @return 所有子节点名称列表
	 */
	@SuppressWarnings("unchecked")
	public List<String> getChildElementNames(Element element) {
		List<String> dataList = new ArrayList<>();
		List<Element> childList = element.elements();
		if (childList != null) {
			for (Element child : childList) {
				dataList.add(child.getName());
			}
		}
		return dataList;
	}

	/**
	 * 根据父节点获取其子节点值
	 * 
	 * @param element
	 *            父节点
	 * @return 子节点值的列表
	 */
	@SuppressWarnings("unchecked")
	public List<String> getChildElementValues(Element element) {
		List<String> dataList = new ArrayList<>();
		List<Element> childList = element.elements();
		if (childList != null) {
			for (Element child : childList) {
				dataList.add(child.getStringValue());
			}
		}
		return dataList;
	}

	/**
	 * 创建文档实例
	 * 
	 * @param dataMap
	 *            数据集合(key对应的是根结点下的子结点(类结点),value对应的此结点下的所有数据)
	 * @param rootName
	 *            根结点的名称
	 * @param singleObjName
	 *            (单个对象结点的名称)
	 * @return 创建好的文档实例
	 */
	public Document createDocument(Map<String, List<String[]>> dataMap, String rootName, String singleObjName) {
		Element rootElement = doc.getRootElement();
		if (dataMap != null) {
			for (Entry<String, List<String[]>> entry : dataMap.entrySet()) {
				if (rootElement == null) {
					rootElement = doc.addElement(rootName);
				}
				List<String[]> dataList = entry.getValue();
				String cateName = entry.getKey();
				if (dataList != null && dataList.size() > 1) {
					String[] nodeNames = dataList.get(0);
					Element cate = rootElement.addElement(cateName);
					for (int i = 1; i < dataList.size(); i++) {
						Element singleElement = cate.addElement(singleObjName);
						String[] data = dataList.get(i);
						for (int j = 0; j < data.length; j++) {
							Element node = singleElement.addElement(nodeNames[j]);
							String text = data[j];
							text = (text == null) ? "" : text;
							node.setText(text);
						}
					}
				}
			}
		}
		return doc;
	}

	/**
	 * 创建文档实例,默认根结点的名称为"root"
	 * 
	 * @param dataMap
	 *            数据集合(key对应的是根结点下的子结点(类结点),value对应的此结点下的所有数据)
	 * @param singleObjName
	 *            (单个对象结点的名称)
	 * @return 创建好的文档实例
	 */
	public Document createDocument(Map<String, List<String[]>> dataMap, String singleObjName) {
		return createDocument(dataMap, "root", singleObjName);
	}

	/**
	 * 取得节点值
	 * 
	 * @param name
	 *            String
	 * @return String
	 */
	public String getProperty(String name) {
		name = name.replace('.', '/');
		String[] propName = parsePropertyName(name);
		Element root = doc.getRootElement();
		Element element = root;
		for (int i = 0; i < propName.length; i++) {
			if (propName[i] == null || "".equals(propName[i])
					|| (i == 0 && propName[i].equals(root.getQName().getName()))
					|| (i == 1 && propName[i].equals(root.getQName().getName()))
							&& (propName[0] == null || "".equals(propName[0]))) {
				// 如果XPath形式，不管是/root 还是 root 都要忽略掉
				element = root;
			} else if (propName[i].startsWith("@")) { // 加入对属性的支持
				return element.attributeValue(propName[i].substring(1));
			} else {
				element = element.element(propName[i]);
				if (element == null) {
					return null;
				}
			}
		}

		String value = element.getText();

		if (value == null || "".equals(value)) {
			return value;
		} else {
			value = value.trim();
			return value;
		}
	}

	/**
	 * 取得子节点名
	 * 
	 * @param parent
	 *            String
	 * @return String[]
	 */
	public String[] getChildrenProperties(String parent) {
		String[] propName = parsePropertyName(parent);
		Element element = doc.getRootElement();

		for (String prop:propName) {
			element = element.element(prop);
			if (element == null) {
				return new String[0];
			}
		}
		List children = element.elements();
		int childCount = children.size();
		String[] childrenNames = new String[childCount];

		for (int i = 0; i < childCount; i++) {
			childrenNames[i] = ((Element) children.get(i)).getName();
		}

		return childrenNames;
	}

	/**
	 * 设置节点值
	 * 
	 * @param name
	 *            String
	 * @param value
	 *            String
	 */
	public void setProperty(String name, String value) {
		name = name.replace('.', '/');
		if (value == null || "".equals(value)) {
			this.deleteProperty(name);
			return;
		}

		String[] propName = parsePropertyName(name);
		Element root = doc.getRootElement();
		Element element = root;
		Node target = null;
		for (int i = 0; i < propName.length; i++) {
			if (propName[i] == null || "".equals(propName[i])
					|| (i == 0 && propName[i].equals(root.getQName().getName()))
					|| (i == 1 && propName[i].equals(root.getQName().getName()))
							&& (propName[0] == null || "".equals(propName[0]))) {
				// 如果XPath形式，不管是/root 还是 root 都要忽略掉
				element = root;
			} else if (propName[i].startsWith("@")) { // 加入对属性的支持
				target = element.attribute(propName[i].substring(1));
				if (target == null) {
					target = element.addAttribute(propName[i].substring(1), value);
				}
			} else {
				target = element.element(propName[i]);
				if (target == null) {
					element = element.addElement(propName[i]);
					target = element;
				} else {
					element = (Element) target;
				}
			}
		}
		target.setText(value);
		writeDoc(ENCODING);
	}

	/**
	 * 删除某个节点
	 * 
	 * @param name
	 *            String
	 */
	public void deleteProperty(String name) {
		name = name.replace('.', '/');
		String[] propName = parsePropertyName(name);
		Element root = doc.getRootElement();
		Element element = root;

		for (int i = 0; i < (propName.length - 1); i++) {
			if (propName[i] == null || "".equals(propName[i])
					|| (i == 0 && propName[i].equals(root.getQName().getName()))
					|| (i == 1 && propName[i].equals(root.getQName().getName()))
							&& (propName[0] == null || "".equals(propName[0]))) {
				// 如果XPath形式，不管是/root 还是 root 都要忽略掉
				element = root;
			} else {
				element = element.element(propName[i]);
				if (element == null) {
					return;
				}
			}
		}
		if (propName[propName.length - 1].startsWith("@")) { // 加入对属性的支持
			element.remove(element.attribute(propName[propName.length - 1].substring(1)));
		} else {
			element.remove(element.element(propName[propName.length - 1]));
		}
		writeDoc(ENCODING);
	}

	/**
	 * 解析路径
	 * 
	 * @param name
	 *            String
	 * @return String[]
	 */
	private static String[] parsePropertyName(String name) {
		return name.split("[./]"); // JDK1.4 +
	}

}
