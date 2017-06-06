package com.cre8techlabs.utils.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArrayListStringEnum<T extends Enum> extends ArrayList<String> implements StringEnumCollection<T> {
	Map<String, T> map = new HashMap<>();
	private Class<T> enumType;
	
	public ArrayListStringEnum(Class<T> enumType) {
		this.enumType = enumType;
		T[] result = enumType.getEnumConstants();
		for (T t : result) {
			map.put(t.toString(), t);
		}
	}
	
	public boolean add(T e) {
		return add(e.toString());
	}
	public boolean addAllEnums(Collection<T> e) {
		List<String> c = e.stream().map(x -> x.toString()).collect(Collectors.toList());
		return addAll(c);
	}
	
	public boolean removeAllEnums(Collection<T> c) {
		List<String> es = c.stream().map(x -> x.toString()).collect(Collectors.toList());
		return super.removeAll(es);
	}
	
	public boolean remove(T o) {
		return super.remove(o.toString());
	}
	@Override
	public boolean add(String name) {
		T e = map.get(name);
		if (e != null) {
			return super.add(name);
		}
		throw new IllegalArgumentException(name + " is not an enmu of " + enumType.getSimpleName());
	}

	@Override
	public Class<T> getEnumType() {
		return enumType;
	}
}
