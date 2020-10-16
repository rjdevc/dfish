package com.rongji.dfish.framework.plugin.exception;

import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.base.util.DateUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.mvc.controller.FrameworkController;
import com.rongji.dfish.framework.mvc.response.JsonResponse;
import com.rongji.dfish.framework.plugin.exception.core.ExceptionManager;
import com.rongji.dfish.framework.plugin.exception.core.ExceptionTypeInfo;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionRecord;
import com.rongji.dfish.framework.plugin.exception.service.PubExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public abstract class AbstractExceptionViewerController extends FrameworkController {

	@Autowired
	protected PubExceptionService pubExceptionService;

	protected abstract boolean accept(HttpServletRequest request);

	@RequestMapping("/list")
	@ResponseBody
	public Object list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!accept(request)){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		int limit=50;
		int offset=0;
		Long typeId=null;
		Long beginTime=null;
		Long endTime=null;
		String limitStr=request.getParameter("limit");
		if(Utils.notEmpty(limitStr)){
			try{
				limit=Integer.parseInt(limitStr);
			}catch (Exception ex){}
		}
		String offsetStr=request.getParameter("offset");
		if(Utils.notEmpty(offsetStr)){
			try{
				offset=Integer.parseInt(offsetStr);
			}catch (Exception ex){}
		}
		Pagination page=new Pagination();
		page.setOffset(offset);
		page.setLimit(limit);
		String typeIdStr=request.getParameter("typeId");
		if(Utils.notEmpty(typeIdStr)){
			try{
				typeId=Long.parseLong(typeIdStr);
			}catch (Exception ex){}
		}
		String beginTimeStr=request.getParameter("beginTime");
		if(Utils.notEmpty(beginTimeStr)){
			try{
				beginTime=DateUtil.parse(beginTimeStr).getTime();
			}catch (Exception ex){}
		}
		String endTimeStr=request.getParameter("endTime");
		if(Utils.notEmpty(endTimeStr)){
			try{
				endTime=DateUtil.parse(endTimeStr).getTime();
			}catch (Exception ex){}
		}

		// 临时补一个方法,作为入口地址,以免其他系统已经调用了还要改入口地址,此方法与showAsLog相同,下次做改造的时候需要验证测试,并将showAsLog方法去除
		List<PubExceptionRecord> records=pubExceptionService.findRecords(typeId,beginTime, endTime, page);
		JsonResponse<List<PubExceptionRecordDto>> json= new JsonResponse<>(toDTO(records));
		json.setPagination(page);
		return json;
	}

	private List<PubExceptionRecordDto> toDTO(List<PubExceptionRecord> records) {
		if(records==null){
			return Collections.EMPTY_LIST;
		}
		List<PubExceptionRecordDto> dtos=new ArrayList<>(records.size());
		for(PubExceptionRecord rec:records){
			dtos.add(toDTO(rec));
		}
		return dtos;
	}
	private PubExceptionRecordDto toDTO(PubExceptionRecord record) {
		if(record==null){
			return null;
		}
		PubExceptionRecordDto dto=new PubExceptionRecordDto();
		Utils.copyPropertiesExact(dto,record);
		dto.setEventTime(new Date(record.getEventTime()));
		ExceptionTypeInfo typeInfo=ExceptionManager.getInstance().getExceptionType(record.getTypeId());
		dto.setTypeName(typeInfo==null?null:typeInfo.getClassName());
		return dto;
	}

	@RequestMapping("/get")
	@ResponseBody
	public Object get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!accept(request)){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		String recId=request.getParameter("recId");
		PubExceptionRecordDto dto=new PubExceptionRecordDto();

		PubExceptionRecord record=pubExceptionService.get(recId);
		Utils.copyPropertiesExact(dto,record);
		dto.setEventTime(new Date(record.getEventTime()));
		ExceptionTypeInfo typeInfo=ExceptionManager.getInstance().getExceptionType(record.getTypeId());
		dto.setTypeName(typeInfo==null?null:typeInfo.getClassName());
		dto.setStack(ExceptionManager.getInstance().getStackAsString(record.getTypeId()));
		return new JsonResponse<>(dto);
	}

}
