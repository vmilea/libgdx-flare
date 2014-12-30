/*******************************************************************************
 * Copyright 2014 Valentin Milea <valentin.milea@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.vmilea.gdx.pool;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vmilea.util.Assert;

public class AltReflectionPool<T extends Poolable> extends AltPool<T> {

	private final Constructor constructor;
	
	public AltReflectionPool(Class<T> pooledType) {
		this(pooledType, DEFAULT_CAPACITY);
	}
	
	public AltReflectionPool(Class<T> pooledType, int initialCapacity) {
		super(pooledType, 0);
		constructor = findConstructor(pooledType);
		reserve(initialCapacity);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected T newObject() {
		try {
			return (T) constructor.newInstance((Object[]) null);
		} catch (ReflectionException e) {
			Assert.check(false, String.format("failed to create new instance of %s",
					constructor.getDeclaringClass().getSimpleName()));
			return null;
		}
	}
	
	private Constructor findConstructor(Class<T> pooledType) {
		Constructor constructor = null;
		
		try {
			constructor = ClassReflection.getConstructor(pooledType, (Class[]) null);
		} catch (Exception e) {
			try {
				constructor = ClassReflection.getDeclaredConstructor(pooledType, (Class[]) null);
				constructor.setAccessible(true);
			} catch (ReflectionException re) {
				Assert.check(false);
			}
		}
		
		return constructor;
	}
}
