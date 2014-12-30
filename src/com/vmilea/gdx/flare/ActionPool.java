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

package com.vmilea.gdx.flare;

import com.vmilea.gdx.pool.AltReflectionPool;

public class ActionPool<T extends AbstractAction> extends AltReflectionPool<T> {

	public interface Listener {

		void onActionObtained(Class<?> type);

		void onActionRecycled(Class<?> type);
	}

	public static final int DEFAULT_ACTION_POOL_CAPACITY = 4;

	private static Listener listener;
	
	public static void setListener(Listener listener) {
		ActionPool.listener = listener;
	}
	
	public static <T extends AbstractAction> ActionPool<T> make(Class<T> pooledType) {
		return make(pooledType, DEFAULT_ACTION_POOL_CAPACITY);
	}

	public static <T extends AbstractAction> ActionPool<T> make(Class<T> pooledType, int initialCapacity) {
		return new ActionPool<T>(pooledType, initialCapacity);
	}

	private ActionPool(Class<T> pooledType, int initialCapacity) {
		super(pooledType, initialCapacity);
	}

	@Override
	public synchronized T obtain() {
		T action = super.obtain();
		action.setPool(this);
		
		if (listener != null)
			listener.onActionObtained(getPooledType());
		
		return action;
	}

	@Override
	public synchronized void free(T object) {
		super.free(object);

		AbstractAction action = (AbstractAction) object;
		action.poolItemIncarnation++;
		
		if (listener != null)
			listener.onActionRecycled(getPooledType());
	}
}
