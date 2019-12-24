package com.esys.bpm.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.esys.bpm.common.BpmLookup;

public class CollectionUtil {

	public static <T> LinkedList<T> getLinkedListFromArray(T[] array) {
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
		T[] array = (T[]) Array.newInstance(arrayClass, list.size());
		if (list != null && list.size() > 0) {
			int i = 0;
			for (T object : list) {
				array[i++] = object;
			}
		}
		return array;
	}

	public static boolean hasListEmptyValue(List<Object> list) {
		boolean isOk = true;
		for (Object obj : list) {
			if (obj == null) {
				isOk = false;
				break;
			}
		}

		return isOk;
	}

	public static <T> boolean isNullOrEmpty(List<T> list) {
		return (list == null || list.isEmpty());
	}

	public static boolean hasListTheSameTypeValue(List<Object> list, Class<?> clz) {
		boolean isOk = true;
		for (Object obj : list) {
			if (obj != null && obj.getClass().getName().equals(clz.getClass().getName())) {
				isOk = false;
				break;
			}
		}
		return isOk;
	}

	public static boolean isAllCollectionValuesTheSame(List<Object> listFrom, List<Object> listTo) {
		boolean isOk = true;
		for (Object objectFrom : listFrom) {
			int countOfObjectValue = Collections.frequency(listTo, objectFrom);
			if (countOfObjectValue == 0) {
				isOk = false;
				break;
			}
		}

		return isOk;
	}

	public static <T> List<T> safeList(List<T> list) {
		return list == null ? Collections.emptyList() : list;
	}

	private static Map<String, String> getMapFromString(String mapString) {
		if (StringUtil.isNotNullAndNotEmpty(mapString) && !mapString.equals("{}")) {
			// String s = "{key1=value1, key2=value2, key3=value3}";
			mapString = mapString.substring(1, mapString.length() - 1);
			Map<String, String> map = new HashMap<String, String>();

			String[] pairs = mapString.split(", ");
			for (int i = 0; i < pairs.length; i++) {
				String pair = pairs[i];
				String[] keyValue = pair.split("=");
				map.put(keyValue[0], keyValue[1]);
			}
			return map;
		} else {
			return null;
		}
	}

	private static String getStringFromMap(Map<String, String> map) {
		if (map != null)
			return map.toString();
		return null;
	}

	public static List<BpmLookup> getLookupListFromString(String mapString) {
		Map<String, String> map = getMapFromString(mapString);
		List<BpmLookup> list = new ArrayList<BpmLookup>();

		if (map != null) {
			for (Entry<String, String> entry : map.entrySet()) {
				BpmLookup lookup = new BpmLookup(entry.getKey(), entry.getValue());
				list.add(lookup);
			}
		}
		return list;
	}

	public static String getStringFromLookupList(List<BpmLookup> lookupList) {
		if (lookupList != null) {
			Map<String, String> map = new HashMap<String, String>();
			for (BpmLookup lookup : lookupList) {
				map.put(lookup.getCode(), lookup.getValue());
			}
			return getStringFromMap(map);
		}
		return null;
	}
}
