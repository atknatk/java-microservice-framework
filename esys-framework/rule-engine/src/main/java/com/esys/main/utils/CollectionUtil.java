package com.esys.main.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author selcukme
 *
 * @param <T>
 */
public class CollectionUtil {

	
	public static <T>LinkedList<T> getLinkedListFromArray(T[] array) {
		LinkedList<T> list = new LinkedList<T>();
		if (array != null && array.length > 0) {
			for (T object : array) {
				list.add(object);
			}
		}
		return list; 
	}

	public static <T> ArrayList<T> getArrayListFromArray(T[] array) {
		ArrayList<T> list = new ArrayList<T>();
		if (array != null && array.length > 0) {
			for (T object : array) {
				list.add(object);
			}
		}
		return list; 
	}
	
	public static <T> T[] getArrayFromList(List<T> list, @SuppressWarnings("rawtypes") Class arrayClass) {
		@SuppressWarnings("unchecked")
		T[] array = (T[])Array.newInstance(arrayClass, list.size());
		if (list != null && list.size() > 0) {
			int i = 0;
			for (T object : list) {
				array[i++] = object;
			}
		}
		return array; 
	}
	
	public static boolean hasListEmptyValue(List<Object> list){
		boolean isOk = true;
		for (Object obj : list) {
			if(obj == null){
				isOk = false;
				break;
			}
		}
		
		return isOk;
	}
	
	public static boolean hasListTheSameTypeValue(List<Object> list,Class <?> clz){
		boolean isOk = true;
		for (Object obj : list) {
			if(obj != null && obj.getClass().getName().equals(clz.getClass().getName())){
				isOk = false;
				break;
			}
		}
		return isOk;
	}
	
	public static boolean isAllCollectionValuesTheSame(List<Object> listFrom,List<Object> listTo){
		boolean isOk = true;
		for (Object objectFrom : listFrom) {
			int countOfObjectValue = Collections.frequency(listTo, objectFrom);
			if(countOfObjectValue ==0){
				isOk = false;
				break;
			}
		}
		
		return isOk;
	}
		
}
