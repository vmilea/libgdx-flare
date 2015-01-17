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
import com.vmilea.util.ArgCheck;

public final class RepeatAction extends AbstractWrapperAction {

	private int repeatCycle, repeatLimit;
	private boolean isInterrupted;

	public static final AltPool<RepeatAction> pool = ActionPool.make(RepeatAction.class);

	RepeatAction() { } // internal

	public static RepeatAction obtain(AbstractAction action, int repeatLimit) {
		ArgCheck.check(repeatLimit > 0, "Repeat limit must be greater than 0");

		RepeatAction obj = pool.obtain();
		obj.action = action;
		obj.repeatLimit = repeatLimit;
		return obj;
	}

	public void setRepeatLimit(int repeatLimit) {
		ArgCheck.check(repeatLimit > 0, "Repeat limit must be greater than 0");

		this.repeatLimit = repeatLimit;
	}

	public void interrupt() {
		isInterrupted = true;
	}

	@Override
	public void reset() {
		super.reset();

		repeatCycle = 0;
		repeatLimit = 0;
		isInterrupted = false;
	}

	@Override
	public void restore() {
		super.restore();

		repeatCycle = 0;
		isInterrupted = false;
	}

	@Override
	public float getDuration() {
		return repeatLimit * action.getDuration();
	}

	@Override
	protected float doRun(float dt) {
		if (isInterrupted) {
			isDone = true;
			return dt;
		}

		dt = action.run(dt);

		if (isInterrupted) {
			isDone = true;
			return dt;
		}

		while (action.isDone()) {
			if (++repeatCycle >= repeatLimit) {
				isDone = true;
				return dt;
			}

			action.restart();
			dt = action.run(dt);

			if (isInterrupted) {
				isDone = true;
				return dt;
			}
		}

		return 0;
	}
}
