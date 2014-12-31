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

import com.vmilea.gdx.pool.AltPool;
import com.vmilea.util.Assert;

public final class LazyAction extends AbstractAction {

	public interface Generator {
		AbstractAction call(Object data);
	}

	private Generator generator;
	private Object data;
	private AbstractAction action;

	public static final AltPool<LazyAction> pool = ActionPool.make(LazyAction.class);

	LazyAction() { } // internal

	public static LazyAction obtain(Generator generator, Object data) {
		LazyAction obj = pool.obtain();
		obj.generator = generator;
		obj.data = data;
		return obj;
	}

	@Override
	public void reset() {
		super.reset();

		generator = null;
		data = null;

		if (action != null) {
			action.recycle();
			action = null;
		}
	}

	@Override
	public void restore() {
		super.restore();

		Assert.check(action == null);
	}

	@Override
	public void pin() {
		Assert.check(!isPinned);
		Assert.check(action == null);

		action = generator.call(data);
		// keep the action's target, start it on our actor
		action.setActor(getActor());
		action.restart();

		isPinned = true;
	}

	@Override
	protected float doRun(float dt) {
		dt = action.run(dt);

		if (action.isDone()) {
			isDone = true;

			action.recycle();
			action = null;
		}

		return dt;
	}
}
