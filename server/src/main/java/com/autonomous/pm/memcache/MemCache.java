package com.autonomous.pm.memcache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemCache<T> {

	public static final Logger logger = LoggerFactory.getLogger(MemCache.class);

	ArrayList<T> list = new ArrayList<T>();
	ConcurrentHashMap<String, T> map = new ConcurrentHashMap<String, T>();

	public MemCache() {
	}

	public void clear() {
		list.clear();
		map.clear();
	}

	public void addAll(List<T> c) {
		list = new ArrayList<T>();
		list.addAll(c);
	}

	public List<T> getList() {
		return list;
	}

	/**
	 * return data list from Map data
	 * 
	 * @return
	 */
	public List<T> getValueList() {
		List<T> valueList = new ArrayList<T>(map.values());
		return valueList;
	}

	/**
	 * return key list from key of Map
	 * 
	 * @return
	 */
	public List<String> getKeyList() {
		List<String> keysList = new ArrayList<String>(map.keySet());
		return keysList;
	}

	public ConcurrentHashMap<String, T> getMap() {
		return map;
	}

	Object lock = new Object();

	public void put(String key, T obj) {
		synchronized (lock) {
			T t = null;
			if ((t = map.get(key)) != null) {
				map.replace(key, obj);
			} else {
				map.putIfAbsent(key, obj);
			}
		}
	}

	public T get(String key) {
		synchronized (lock) {
			return map.get(key);
		}
	}

	public void remove(String key) {
		synchronized (lock) {
			T t = map.get(key);
			map.remove(key);
			list.remove(t);
		}
	}

	@Override
	public String toString() {
		String str = "";
		int i = 0;
		for (Map.Entry<String, T> s : this.map.entrySet()) {
			str += "[" + i++ + "][Key:" + s.getKey() + ", Value:" + s.getValue().toString() + "]\n";
		}
		return "MemCache [map=\n" + str + "]";
	}

}
