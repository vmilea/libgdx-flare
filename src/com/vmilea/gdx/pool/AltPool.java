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

import java.util.ArrayDeque;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.vmilea.util.ArgCheck;
import com.vmilea.util.Assert;

// Pool replacement with better stat tracking
//
public abstract class AltPool<T extends Poolable> extends com.badlogic.gdx.utils.Pool<T> {

	public static final int DEFAULT_CAPACITY = 16;

	public static final Array<AltPool<?>> registeredPools = new Array<AltPool<?>>();

	public static void logPoolStats() {
		registeredPools.sort(new Comparator<AltPool<?>>() {
			@Override
			public int compare(AltPool<?> a, AltPool<?> b) {
				int aCount = a.getFree() + a.getLoose();
				int bCount = b.getFree() + b.getLoose();
				return (aCount < bCount ? 1 : (aCount > bCount ? -1 : 0));
			}
		});

		int maxTypeNameLength = 0;
		StringBuilder sb = new StringBuilder();

		for (AltPool<?> pool : registeredPools)
			maxTypeNameLength = Math.max(maxTypeNameLength,
					pool.getPooledType().getSimpleName().length());

		for (AltPool<?> pool : registeredPools) {
			sb.append(pool.getPooledType().getSimpleName());
			for (int k = maxTypeNameLength - sb.length(); k > 0; k--)
				sb.append(' ');

			Gdx.app.log("POOL", String.format("%s : f+l: %4d, f: %4d, l: %4d; peak f: %4d, peak l: %4d",
					sb,
					pool.getFree() + pool.getLoose(), pool.getFree(), pool.getLoose(),
					pool.getPeakFree(), pool.getPeakLoose()));
			sb.setLength(0);
		}
	}

	private final ArrayDeque<T> freeObjects;
	private int looseCount;
	private int peakLooseCount;
	private final Class<T> pooledType;

	abstract protected T newObject();

	public AltPool(Class<T> pooledType) {
		this(pooledType, DEFAULT_CAPACITY);
	}

	public AltPool(Class<T> pooledType, int initialCapacity) {
		this.pooledType = pooledType;

		freeObjects = new ArrayDeque<T>(initialCapacity);
		reserve(initialCapacity);

		registeredPools.add(this);
	}

	public Class<?> getPooledType() {
		return pooledType;
	}

	public void reserve(int capacity) {
		while (freeObjects.size() < capacity)
			freeObjects.addFirst(newObject());
	}

	@Override
	public synchronized int getFree() {
		return freeObjects.size();
	}

	public synchronized int getLoose() {
		return looseCount;
	}

	public synchronized int getPeakFree() {
		return peak;
	}

	public synchronized int getPeakLoose() {
		return peakLooseCount;
	}

	public synchronized void breakAway(T object) {
		if (object != null) {
			if (object instanceof PoolItem) {
				PoolItem poolItem = (PoolItem) object;
				Object pool = poolItem.getPool();

				if (pool == null) {
					ArgCheck.fail("Can't break away object of type '%s', it's not attached to any pool",
							object.getClass().getSimpleName());
				} else if (pool != this) {
					ArgCheck.fail("Can't break away object of type '%s', it's from a different pool",
							object.getClass().getSimpleName());
				}

				poolItem.setPool(null);
			}

			Assert.check(looseCount > 0);
			looseCount--;
		}
	}

	@Override
	public synchronized T obtain() {
		looseCount++;
		peakLooseCount = Math.max(looseCount, peakLooseCount);

		T object = freeObjects.pollFirst();
		if (object == null)
			object = newObject();

		if (object instanceof PoolItem)
			((PoolItem) object).setPool(this);

		return object;
	}

	@SuppressWarnings("unchecked")
	public <R extends T> R obtainAs() {
		return (R) obtain();
	}

	@Override
	public synchronized void free(T object) {
		if (object != null) {
			if (object instanceof PoolItem) {
				PoolItem poolItem = (PoolItem) object;
				Object pool = poolItem.getPool();

				if (pool == null) {
					ArgCheck.fail("Can't recycle object of type '%s', already returned to pool",
							object.getClass().getSimpleName());
				} else if (pool != this) {
					ArgCheck.fail("Can't recycle object of type '%s', it's from a different pool",
							object.getClass().getSimpleName());
				}

				poolItem.setPool(null);
			}

			object.reset();
			freeObjects.addFirst(object);
			peak = Math.max(peak, freeObjects.size());

			Assert.check(looseCount > 0);
			looseCount--;
		}
	}

	@Override
	public synchronized void freeAll(Array<T> objects) {
		freeAll((Iterable<T>) objects);
	}

	public synchronized void freeAll(Iterable<T> objects) {
		for (T object : objects) {
			free(object);
		}
	}

	@Override
	public synchronized void clear() {
		freeObjects.clear();
		peak = 0;
		peakLooseCount = looseCount;
	}

	public synchronized void resetPeakStats() {
		peak = 0;
		peakLooseCount = looseCount;
	}
}
