package com.esys.main.controller.output;

import java.util.ArrayList;
import java.util.List;

import com.esys.main.enums.SeverityEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class OutputDTO<T>{
	
private T outputData;
	
private List<ResultMessages> messages= new ArrayList<ResultMessages>(0);
	
	private static final String GLOBALMESSAGE = "globalMessage";
	
	public void addSuccessMessage(String message) {
		ResultMessages resultmessage=new ResultMessages();
		resultmessage.setSeverity(SeverityEnum.INFO.value());
		resultmessage.setType(GLOBALMESSAGE);
		resultmessage.setMessageText(message);
		messages.add(resultmessage);
	}
	
	public void addErrorMessage(String message) {
		ResultMessages resultmessage=new ResultMessages();
		resultmessage.setSeverity(SeverityEnum.ERROR.value());
		resultmessage.setType(GLOBALMESSAGE);
		resultmessage.setMessageText(message);
		messages.add(resultmessage);
	}
	
	public void addWarningMessage(String message) {
		ResultMessages resultmessage=new ResultMessages();
		resultmessage.setSeverity(SeverityEnum.WARNING.value());
		resultmessage.setType(GLOBALMESSAGE);
		resultmessage.setMessageText(message);
		messages.add(resultmessage);
	}
	
	public void addSuccessFieldMessage(String message,String compName) {
		ResultMessages resultmessage=new ResultMessages();
		resultmessage.setSeverity(SeverityEnum.INFO.value());
		resultmessage.setType(GLOBALMESSAGE);
		resultmessage.setField(compName);
		resultmessage.setMessageText(message);
		messages.add(resultmessage);
	}
	
	public void addErrorFieldMessage(String message,String compName) {
		ResultMessages resultmessage=new ResultMessages();
		resultmessage.setSeverity(SeverityEnum.ERROR.value());
		resultmessage.setType(GLOBALMESSAGE);
		resultmessage.setField(compName);
		resultmessage.setMessageText(message);
		messages.add(resultmessage);
	}
	
	public void addWarningFieldMessage(String message,String compName) {
		ResultMessages resultmessage=new ResultMessages();
		resultmessage.setSeverity(SeverityEnum.WARNING.value());
		resultmessage.setType(GLOBALMESSAGE);
		resultmessage.setField(compName);
		resultmessage.setMessageText(message);
		messages.add(resultmessage);
	}
	
	public boolean isThereMessage(){
		return !messages.isEmpty();
	}
}
