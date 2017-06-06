package com.cre8techlabs.utils.collection;

import java.util.Collection;

public interface StringEnumCollection<T extends Enum> {
	public boolean add(T e);
	public boolean addAllEnums(Collection<T> e);
	public boolean removeAllEnums(Collection<T> c);
	public boolean remove(T o);
	public Class<T> getEnumType();
}
