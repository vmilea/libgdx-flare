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

import com.vmilea.gdx.flare.Actions.Delegate;
import com.vmilea.gdx.pool.AltPool;
import com.vmilea.gdx.pool.PoolItem;

// Runs once, then disposes the delegate.
//
public final class OneShotAction extends AbstractAction {

	private Delegate delegate;
	private Object data;

	public static final AltPool<OneShotAction> pool = ActionPool.make(OneShotAction.class);

	OneShotAction() { } // internal

	public static OneShotAction obtain(Delegate delegate, Object data) {
		OneShotAction obj = pool.obtain();
		obj.delegate = delegate;
		obj.data = data;
		return obj;
	}

	@Override
	public void reset() {
		super.reset();

		// recycle delegate here in case action wasn't run
		if (delegate instanceof PoolItem)
			((PoolItem) delegate).recycle();

		delegate = null;
		data = null;
	}

	@Override
	public void restore() {
		if (delegate == null)
			throw new UnsupportedOperationException("OneShotAction may not be restarted");

		super.restore();
	}

	@Override
	protected void doPin() {

	}

	@Override
	public float getDuration() {
		return 0;
	}

	@Override
	protected boolean supportsRemoveWhileRunning() {
		return true;
	}

	@Override
	protected float doRun(float dt) {
		delegate.run(target, data);

		if (delegate instanceof PoolItem)
			((PoolItem) delegate).recycle();
		delegate = null;

		isDone = true;
		return dt;
	}
}
