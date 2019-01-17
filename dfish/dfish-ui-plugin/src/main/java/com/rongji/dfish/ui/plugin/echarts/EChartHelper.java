package com.rongji.dfish.ui.plugin.echarts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.abel533.echarts.code.Baseline;
import com.github.abel533.echarts.style.NodeStyle;
import com.github.abel533.echarts.style.TextStyle;
import com.github.abel533.echarts.style.itemstyle.Emphasis;

//import com.rongji.dfish.ui.plugin.echarts.json.Emphasis;
//import com.rongji.dfish.ui.plugin.echarts.json.NodeStyle;
//import com.rongji.dfish.ui.plugin.echarts.json.TextStyle;

public class EChartHelper {

	public static Map<Integer,List<Integer[]>> nodePositionMap = new HashMap<Integer,List<Integer[]>>();
	
	public static List<Integer[]>  nodePositionList = new ArrayList<Integer[]>();
	
	public static List<Integer[]> annularRadiusList = new ArrayList<Integer[]>();
	
	
	
	static{
		ArrayList<Integer[]> posList = null;
		for(int i=1;i<=6;i++){
			posList = new ArrayList<Integer[]>();
			if(i == 1){
				posList.add(new Integer[]{150,240});
			}else if(i == 2){
				posList.add(new Integer[]{150,240});
				posList.add(new Integer[]{520,240});
			}else if(i == 3){
				posList.add(new Integer[]{340,60});
				posList.add(new Integer[]{170,300});
				posList.add(new Integer[]{500,300});
			}else if(i == 4){
				posList.add(new Integer[]{340,60});
				posList.add(new Integer[]{170,200});
				posList.add(new Integer[]{510,200});
				posList.add(new Integer[]{340,330});
			}else if(i == 5){
				posList.add(new Integer[]{340,60});
				posList.add(new Integer[]{140,220});
				posList.add(new Integer[]{540,220});
				posList.add(new Integer[]{220,340});
				posList.add(new Integer[]{480,340});
			}else{
				posList.add(new Integer[]{220,70});
				posList.add(new Integer[]{480,70});
				posList.add(new Integer[]{140,220});
				posList.add(new Integer[]{540,220});
				posList.add(new Integer[]{220,340});
				posList.add(new Integer[]{480,340});
			}
			nodePositionMap.put(i, posList);
		}
		
		
		nodePositionList.add(new Integer[]{600,110});
		nodePositionList.add(new Integer[]{330,220});
		nodePositionList.add(new Integer[]{460,100});
		nodePositionList.add(new Integer[]{340,90});
		nodePositionList.add(new Integer[]{460,220});
		nodePositionList.add(new Integer[]{470,330});
		nodePositionList.add(new Integer[]{580,240});
		nodePositionList.add(new Integer[]{585,350});
		nodePositionList.add(new Integer[]{250,330});
		nodePositionList.add(new Integer[]{360,340});
		
		
		
		annularRadiusList.add(new Integer[]{125, 150});
		annularRadiusList.add(new Integer[]{100, 125});
		annularRadiusList.add(new Integer[]{75, 100});
		annularRadiusList.add(new Integer[]{50,75});
		annularRadiusList.add(new Integer[]{25,50});
		annularRadiusList.add(new Integer[]{0,25});
	}
	
	
	
	public static  List<Integer[]> getNodePositionList(){
//		List<Integer[] > nodePositionList = new ArrayList<Integer[]>();
//		nodePositionList.add(new Integer[]{600,110});
//		nodePositionList.add(new Integer[]{330,220});
//		nodePositionList.add(new Integer[]{460,100});
//		nodePositionList.add(new Integer[]{340,90});
//		nodePositionList.add(new Integer[]{460,220});
//		nodePositionList.add(new Integer[]{470,330});
//		nodePositionList.add(new Integer[]{580,240});
//		nodePositionList.add(new Integer[]{585,350});
//		nodePositionList.add(new Integer[]{250,330});
//		nodePositionList.add(new Integer[]{360,340});
		
		return nodePositionList;
	}
	
	public static  List<Integer[]> getAnnularRadiusList(){
		return annularRadiusList;
	}
	
	
	/**   
	 *@Description: 力导向图中心点位置
	 *@Author: chenwei
	 *@CreateDate: 2015-7-7
	 *@return
	 */
	public static Integer[] getCenterNodePosition(){
		return new Integer[]{340,200};
	}
	
	
	/**   
	 *@Description: 力导向图中心点大小
	 *@Author: chenwei
	 *@CreateDate: 2015-7-7
	 *@return
	 */
	public static Integer[] getCenterNodeSize(){
		return new Integer[]{50,50};
	}
	
	
	/**   
	 *@Description:力导向图其余节点大小
	 *@Author: chenwei
	 *@CreateDate: 2015-7-7
	 *@return
	 */
	public static Integer[] getOtherNodeSize(){
		return new Integer[]{40,40};
	}
	
	
	/**   
	 *@Description: 根据节点个数获得节点位置
	 *@Author: chenwei
	 *@CreateDate: 2015-7-7
	 *@param nodesNum
	 *@return
	 */
	public static List<Integer[]> getNodePositonList(int nodesNum){
		return nodePositionMap.get(nodesNum);
	}

	
	/**   
	 *@Description: 获得文本字体样式
	 *@Author: chenwei
	 *@CreateDate: 2015-7-10
	 *@return
	 */
	public static TextStyle getTextStyle(Integer fontSize,String baseline){
		TextStyle text  = new TextStyle();
		text.setColor("#666");
		text.setFontFamily("Microsoft YaHei,微软雅黑,MicrosoftJhengHei,华文细黑,STHeiti,MingLiu");
		text.setFontSize(fontSize);
//		text.setBaseline(baseline);
		if("top".equals(baseline)){
			text.setBaseline(Baseline.top);
		}else if("middle".equals(baseline)){
			text.setBaseline(Baseline.middle);
		}else if("bottom".equals(baseline)){
			text.setBaseline(Baseline.bottom);
		}

		return text;
	}
	
	
	/**   
	 *@Description: 获得颜色
	 *@Author: chenwei
	 *@CreateDate: 2015-7-10
	 *@return
	 */
	public static String[] getColor(){
		return new String[]{"#fae9b1","#4373c3","#22d3b6","#f0cc44","#74c1f5","#fa4d4a"};
	}
	
	public static Emphasis getNodeEmphasis(){
		Emphasis emphasis = new Emphasis();
		NodeStyle emphasisNodeStyle = new NodeStyle();
		emphasisNodeStyle.setBorderWidth(2);
		emphasisNodeStyle.setBorderColor("#BFBFBF");
		emphasis.setNodeStyle(emphasisNodeStyle);
		return emphasis;
	}
	
	
	public static boolean isIELowVer(String ieVer){
		if("6".equals(ieVer) || "7".equals(ieVer) || "8".equals(ieVer) ){
			return true;
		}
		return false;
	}
	
}
