package com.rongji.dfish.framework.singletonimpl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

/**
 * 用于将配置存成JSON文件。
 * @author DFish Team
 *
 */
public class JsonConfigHelper {
	private static final Log LOG=LogFactory.getLog(JsonConfigHelper.class);
	public static final String FILE_ENCODING="UTF-8";
	private File file;
	private static JSONObject json;//如果file为空则临时存储在内存中，jvm重启丢失。
	public JsonConfigHelper(File file){
		this.file=file;
	}
	protected synchronized void writeToFile(JSONObject o) {
		if(file==null){
			return;
		}
		OutputStream out = null;
		boolean error = false;
		File tempFile = null;
		try {
			File pfile = file.getParentFile();
			if (!pfile.exists()) {
				pfile.mkdirs();
			}
			tempFile = new File(file.getParentFile(), file.getName().concat(".tmp"));
			out = new BufferedOutputStream(new FileOutputStream(tempFile));
			String json=JSON.toJSONString(o, true);
			out.write(json.getBytes(FILE_ENCODING));// 把o的内容，做成人易于阅读的格式并储存起来。
		} catch (Exception e) {
			e.printStackTrace();
			error = true;
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
				error = true;
			}
		}
		if (!error) {
			file.delete();
			tempFile.renameTo(file);
		}
	}
	protected synchronized JSONObject readContent(){
		String context="{}";
		if(file==null||!file.exists()){
			if(json==null){
				json=JSON.parseObject(context,JSONObject.class);
			}
			return json;
		}
		FileInputStream fis=null;
		try {
			byte[] buff=new byte[1024*64];
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			fis=new FileInputStream(file);
			int readed=-1;
			while((readed=fis.read(buff))>0){
				baos.write(buff, 0, readed);
			}
			context=new String(baos.toByteArray(),FILE_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					LOG.error(null,e);
				}
			}
		}
		JSONObject json=JSON.parseObject(context,JSONObject.class,Feature.OrderedField);//必须是有序的防止下次编辑的时候位置变更
		return json;
	}
	/**
	 * 设置属性，只支持字符串的属性设置
	 * 属性名可以用点号(.)进行分割成多层，JSON格式中也会分成多层
	 * @param name 属性名
	 * @param value 属性值
	 */
	public void setProperty(String name,String value){
		String[] path=name.split("[.]");
		JSONObject root= this.readContent();
		JSONObject cur=root;
		for(int i=0;i<path.length;i++){
			String seg=path[i];
			boolean isLeaf=i==path.length-1;
			if(isLeaf){
				if(value==null||value.equals("")){
					cur.remove(seg);
				}else{
					cur.put(seg, value);
				}
			}else if(cur.containsKey(seg)){
				try{
					cur=cur.getJSONObject(seg);
				}catch (JSONException je){
					throw new RuntimeException(name+" can NOT use as a path, because "+seg +" has its value ["+cur.getString(seg)+"]");
				}
			}else{
				JSONObject sub=new JSONObject();
				cur.put(seg, sub);
				cur=sub;
			}
		}
		writeToFile(root);
	}
	/**
	 * 读取属性，只支持字符串的属性读取
	 * 属性名可以用点号(.)进行分割成多层，JSON格式中也会分成多层
	 * @param name 属性名
	 * @return 属性值
	 */
	public String getProperty(String name){
		String[] path=name.split("[.]");
		JSONObject root= this.readContent();
		JSONObject cur=root;
		for(int i=0;i<path.length;i++){
			String seg=path[i];
			boolean isLeaf=i==path.length-1;
			if(isLeaf){
				return cur.getString(seg);
			}else if(cur.containsKey(seg)){
				try{
					cur=cur.getJSONObject(seg);
				}catch (JSONException je){
					throw new RuntimeException(name+" can NOT use as a path, because "+seg +" has its value ["+cur.getString(seg)+"]");
				}
			}else{
				break;
			}
		}
		return null;
	}

}
