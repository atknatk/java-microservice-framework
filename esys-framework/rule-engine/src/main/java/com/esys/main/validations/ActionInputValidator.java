package com.esys.main.validations;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.esys.main.controller.input.action.CreateActionInput;
import com.esys.main.controller.input.action.GetActionListInput;
import com.esys.main.controller.input.action.RemoveActionInput;
import com.esys.main.controller.input.action.UpdateActionInput;
import com.esys.main.dto.ActionDTO;
import com.esys.main.enums.SeverityEnum;
import com.esys.main.utils.StringUtil;

public class ActionInputValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> clazz) {
		return  clazz.asSubclass(ActionDTO.class) != null ;
	}
	@Override
	public void validate(Object target, Errors errors) {
		
		ActionDTO input = (ActionDTO) target;
		
		if(target instanceof CreateActionInput) {
			if(StringUtil.isEmpty(input.getActionName())){
				rejectValue(errors,"createaction.actionname.isnotnull");
			}
			if(StringUtil.isEmpty(input.getActionType())){
				rejectValue(errors,"createaction.actiontypenotnull.isnotnull");
			}
		}else if(target instanceof GetActionListInput) {
			
		}else if(target instanceof RemoveActionInput) {
			
		}else if(target instanceof UpdateActionInput) {
			
		}
		

	}
	private void rejectValue(Errors errors,String message) {
		rejectValue(errors,message,null);
	}
	private void rejectValue(Errors errors,String message,Object[] args) {
		if (args!=null) {
			errors.rejectValue("",message,args,""); 
		}else {
			errors.rejectValue("",message,new Object[]{SeverityEnum.ERROR},"");
		}
	}

}