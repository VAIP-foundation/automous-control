/**
 * 
 */
package com.autonomous.pm.memcache.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autonomous.pm.memcache.MemCache;

/**
 * 
 * 
 * select: <br>
 * key1: <br>
 * key2: <br>
 * key3: <br>
 * 
 * @author promaker
 * @param <T>
 *
 */
public class MemEntity<T> {
	public static final Logger logger = LoggerFactory.getLogger(MemEntity.class);

	public class Entity<T> {
		public Date ctime = new Date();
		public String key1;
		public String key2;
		public String key3;
		public T obj;

		public Date getCtime() {
			return ctime;
		}

		public String getKey1() {
			return key1;
		}

		public String getKey2() {
			return key2;
		}

		public String getKey3() {
			return key3;
		}

		public T getObj() {
			return obj;
		}

		@Override
		public String toString() {
			return "Entity [ctime=" + ctime + ", key1=" + key1 + ", key2=" + key2 + ", key3=" + key3 + ", obj=" + obj
					+ "]";
		}

	}

	public MemEntity(int cntKey) {
		this.cntKey = cntKey;
	}

	protected Object lock = new Object();
	public MemCache<Entity<T>> k1Map = new MemCache<Entity<T>>();

	protected int cntKey = 0;

	/**
	 * 
	 * @param ,    mandatory
	 * @param ,    mandatory
	 * @param obj, mandatory
	 */
	public void insert(String k1, T obj) {

		synchronized (lock) {

			_insert(k1, obj);

		}
	}

	/**
	 * insert 하기 앞서 key
	 * 
	 * @param ,    mandatory
	 * @param ,    mandatory
	 * @param obj, mandatory
	 */
	public void insertSafety(Long k1, T obj) {
		insertSafety(k1.toString(), obj);
	}
	public void insertSafety(String k1, T obj) {

		synchronized (lock) {

			T t1 = null;

			if (k1 != null)
				t1 = _select(k1);

			// 하나라도 다른것이 있으면 삭제한다.
			if (k1 != null) {
				_delete(k1);// k1Map
			} else {
				// do nothing
			}

			// 새로운 object를 추가한다.
			_insert(k1, obj);

		}
	}

	void _insert(String k1, T obj) {
		Entity<T> et = new Entity<T>();

		et.key1 = k1;
		et.obj = obj;

		if (k1 != null)
			k1Map.put(k1, et);

	}

	/**
	 * k1,k2,k3를 각기 key 로 저장된 data를 검색한다.<br>
	 * key를 pass 할때는 하나만 pass 한다.
	 * 
	 */
	public T select(Long k1) {
		return select(Long.toString(k1));
	}
	public T select(String k1) {

		synchronized (lock) {
			return _select(k1);
		}
	}

	public Entity<T> selectEntity(String k1) {

		synchronized (lock) {
			return _selectEntity(k1);
		}
	}

	T _select(String k1) {
		T o = null;
		Entity<T> e = null;

		e = _selectEntity(k1);

		if (e != null)
			o = e.obj;
		return o;
	}

	Entity<T> _selectEntity(String k1) {
		Entity e = null;

		if (k1 != null) {
			e = k1Map.get(k1);
		}
		return e;
	}

	/**
	 * 
	 * @param k1
	 * @return
	 */

	public T delete(Long k1) {
		return delete(Long.toString(k1));
	}
	public T delete(String k1) {
		T o = null;

		synchronized (lock) {
			o = _delete(k1);
		}
		return o;
	}

	T _delete(String k1) {
		T o = null;
		Entity<T> e = null;

		e = _selectEntity(k1);
		if (e != null) {
			if (e.key1 != null)
				k1Map.remove(e.key1);
			o = e.obj;
		}
		return o;

	}

	/**
	 * 모든 data를 삭제한다.
	 */
	public void removeAll() {
		synchronized (lock) {
			k1Map.clear();
		}
	}

	public Object getLock() {
		return lock;
	}

	public MemCache<Entity<T>> getMemCache() {
		return k1Map;
	}

	public List<T> selectAll() {
		List<T> list = new ArrayList<T>();

		synchronized (lock) {
			for (Entry<String, MemEntity<T>.Entity<T>> elem : getMemCache().getMap().entrySet()) {

//				if (logger.isDebugEnabled()) {
//					logger.debug(String.format("Key : %s, Value : %s", elem.getKey(), elem.getValue().toString()));
//				}
				list.add(elem.getValue().getObj());
			}
		}

		return list;
	}

	public List<Entity<T>> selectEntityAll() {
		List<Entity<T>> list = new ArrayList<Entity<T>>();

		synchronized (lock) {
			for (Entry<String, MemEntity<T>.Entity<T>> elem : getMemCache().getMap().entrySet()) {

//				if (logger.isDebugEnabled()) {
//					logger.debug(String.format("Key : %s, Value : %s", elem.getKey(), elem.getValue().toString()));
//				}
				list.add(elem.getValue());
			}
		}

		return list;
	}

	@Override
	public String toString() {
		return "MemEntity [\nk1Map=" + k1Map + ",\ncntKey=" + cntKey + "]";
	}

	public void clearAll() {
		synchronized (lock) {
			k1Map.clear();
		}
	}

}
