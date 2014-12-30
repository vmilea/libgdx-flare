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

public final class ParallelAction extends AbstractGroupAction {

	public static final AltPool<ParallelAction> pool = ActionPool.make(ParallelAction.class);

	ParallelAction() { } // internal

	public static ParallelAction obtain() {
		ParallelAction obj = pool.obtain();
		return obj;
	}

	@Override
	public ParallelAction add(AbstractAction action) {
		super.add(action);
		return this;
	}

	@Override
	public ParallelAction addAll(Iterable<AbstractAction> actions) {
		super.addAll(actions);
		return this;
	}
	
	@Override
	public void reset() {
		super.reset();
	}
	
	@Override
	public void restore() {
		super.restore();
	}

	@Override
	public ParallelAction reversed() {
		// assumes all actions have the same duration!
		ParallelAction reversed = obtain();
		
		reversed.actions.ensureCapacity(actions.size);
		for (int i = 0; i <= actions.size; i++) {
			reversed.actions.add(actions.get(i).reversed());
		}
		return reversed;
	}
	
	@Override
	public float getDuration() {
		float duration = 0;
		for (AbstractAction action : actions) {
			duration = Math.max(duration, action.getDuration());
		}
		return duration;
	}

	@Override
	public void pin() {
		Assert.check(!isPinned);
		
		for (AbstractAction action : actions) {
			action.pin();
		}
		isPinned = true;
	}

	@Override
	protected float doRun(float dt) {
		boolean allDone = true;
		float excessDt = Float.MAX_VALUE;

		for (int i = 0, n = actions.size; i < n; i++) {
			AbstractAction action = actions.get(i);

			if (!action.isDone()) {
				excessDt = Math.min(action.run(dt), excessDt);

				if (allDone && !action.isDone())
					allDone = false;
			}
		}

		if (allDone) {
			isDone = true;
			return excessDt;
		} else {
			return 0;
		}
	}
}
