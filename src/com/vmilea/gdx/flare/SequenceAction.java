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

public final class SequenceAction extends AbstractGroupAction {

	private int currentActionIndex;

	public static final AltPool<SequenceAction> pool = ActionPool.make(SequenceAction.class);

	SequenceAction() { } // internal

	public static SequenceAction obtain() {
		SequenceAction obj = pool.obtain();
		return obj;
	}

	@Override
	public SequenceAction add(AbstractAction action) {
		super.add(action);

		return this;
	}

	@Override
	public SequenceAction addAll(Iterable<AbstractAction> actions) {
		super.addAll(actions);

		return this;
	}

	@Override
	public void reset() {
		super.reset();

		currentActionIndex = 0;
	}

	@Override
	public void restore() {
		super.restore();

		currentActionIndex = 0;
	}

	@Override
	public SequenceAction reversed() {
		SequenceAction reversed = obtain();

		reversed.actions.ensureCapacity(actions.size);
		for (int i = actions.size - 1; i >= 0; i--) {
			reversed.actions.add(actions.get(i).reversed());
		}

		reversed.target = target;
		return reversed;
	}

	@Override
	public float getDuration() {
		float duration = 0;
		for (AbstractAction action : actions) {
			duration += action.getDuration();
		}
		return duration;
	}

	@Override
	protected void doPin() {
		Assert.check(currentActionIndex == 0);

		actions.get(0).pin();
	}

	@Override
	protected boolean supportsRemoveWhileRunning() {
		return true;
	}

	@Override
	protected float doRun(float dt) {
		int incarnation = poolItemIncarnation;

		AbstractAction currentAction = actions.get(currentActionIndex);

		dt = currentAction.run(dt);

		// quit immediately if recycled while running
		if (incarnation != poolItemIncarnation)
			return -1;

		while (currentAction.isDone()) {
			if (++currentActionIndex == actions.size) {
				isDone = true;
				return dt;
			}

			currentAction = actions.get(currentActionIndex);
			currentAction.pin();
			dt = currentAction.run(dt);

			// quit immediately if recycled while running
			if (incarnation != poolItemIncarnation)
				return -1;
		}
		return 0;
	}
}
