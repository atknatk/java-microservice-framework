package com.esys.bpm.common;

public interface MessageText {
	// Component names
	String NAME = "Name";
	String DESCRIPTION = "Description";

	// GENERAL MESSAGES
	String START = "Start";
	String END = "End";
	String EMPTY = "Please enter value!";
	String DESCRIPTION_EMPTY_WARNING = "It is recommended to enter description for task!";
	String INVALID_URL = "Please enter valid service url!";
	String INVALID_METHOD_NAME = "Please enter valid service method name!";
	String INVALID_HTTP_HEADER_KEY = "Please enter valid http header key!";
	String INVALID_HTTP_HEADER_VALUE = "Please enter valid http header value!";
	String INVALID_HTTP_PARAMETER_KEY = "Please enter valid http parameter key!";
	String INVALID_HTTP_PARAMETER_VALUE = "Please enter valid http parameter value!";
	String EMPTY_ASSIGNEE = "User task must have at least one assignee!";
	String EMPTY_NOTIFICATION = "Task does not have anyone to be notified!";
	String INVALID_MAIL = "Please enter valid email!";
	String INVALID_SMS = "Please enter valid phone number!";
	String NO_DATA_FOUND = "No data found with specified parameters!";
	String INVALID_TIMER_TYPE = "Please choose a timer type!";
	String INVALID_SQL_TYPE = "Please choose an sql type!";
	String INVALID_SQL_FUNCTION = "Please choose a valid sql function!";
	String INVALID_SQL_NATIVE = "Please enter valid HSQL command!";
	String INVALID_TIMER_INTERVAL = "Please enter a valid timer interval!";
	String INVALID_TIMER_SPECIFIC_DATE = "Please enter a valid timer specific date!";
	String INVALID_TIMER_SCHEDULE = "Please enter a valid timer schedule!";
	String INVALID_NOTIFICATION_DETAIL = "Please enter at least one notification detail!";
	String INVALID_SCRIPT_DETAIL = "Please enter at least one script detail!";
	String INVALID_GATEWAY_TYPE = "Please enter a valid gateway type!";
	String INVALID_NOTIFICATIN_TYPE = "Notification Type not found!";

	// PROCESS COMPILE MESSAGES
	String COMPILE_SUCCESS = "Process compiled successfully!";
	String EXACTLY_ONE_START = "A process must have at exactly one Start!";
	String AT_LEAST_ONE_TASK = "A process must have at least one task!";
	String AT_LEAST_ONE_END = "A process must have at least one End!";
	String PROCESS_SAVE_SUCCESS = "Process saved successfully! Process Name : ";
	String DRAFT_SAVE_SUCCESS = "Draft saved successfully! Draft Name : ";
	String INVALID_SEQUENCE_INCOMING = "Task must have at least one incoming flow : ";
	String INVALID_TASK_SEQUENCE_OUTGOING = "Task must have exactly one outgoing flow : ";
	String INVALID_GATEWAY_SEQUENCE_OUTGOING = "Gateway must have at least one outgoing flow : ";
	String INVALID_END_SEQUENCE_OUTGOING = "End cannot have outgoing flow : ";
	String USER_TASK_EMPTY_NAME = "User task must have a unique name!";
	String BUSINESS_RULE_TASK_EMPTY_NAME = "Business Rule task must have a unique name!";
	String SERVICE_TASK_EMPTY_NAME = "Service task must have a unique name!";
	String SCRIPT_TASK_EMPTY_NAME = "Script task must have a unique name!";
	String SQL_TASK_EMPTY_NAME = "Sql task must have a unique name!";
	String TIMER_TASK_EMPTY_NAME = "Timer task must have a unique name!";
	String GATEWAY_EMPTY_NAME = "Gateway must have a unique name!";
	String EMPTY_NOTIFICATION_TYPE = "Notification type must be selected!";
	String EMPTY_FROM_SOURCE = "From source must be selected!";
	String EMPTY_TO_TARGET = "At least one to target must be selected!";
	String EMPTY_SUBJECT = "It is recommended to enter subject for notification!";
	String EMPTY_BODY = "Please enter notification body!";
	String EMPTY_ROLE_TYPE = "Please select a role type!";
	String EMPTY_EXPRESSION_TYPE = "Please select an expression type!";
	String EMPTY_PARTICIPANT_NAME = "Please enter participant name!";
	String EMPTY_SCRIPT_TASK_VARIABLE_NAME = "Please enter value for Script Task variable name!";
	String EMPTY_SCRIPT_TASK_NEW_VALUE = "Please enter value for Script Task new value!";

}