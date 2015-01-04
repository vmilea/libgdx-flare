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

package com.vmilea.gdx.flare.tween;

import com.vmilea.gdx.flare.ActionPool;
import com.vmilea.gdx.pool.AltPool;

public final class TweenSequenceAction extends AbstractTweenCombinerAction {

	private float fenceRatio;
	private AbstractTweenAction currentAction;

	public static final AltPool<TweenSequenceAction> pool = ActionPool.make(TweenSequenceAction.class);

	TweenSequenceAction() { } // internal

	public static TweenSequenceAction obtain(AbstractTweenAction action1, AbstractTweenAction action2) {
		TweenSequenceAction obj = pool.obtain();

		obj.action1 = action1;
		obj.action2 = action2;
		obj.duration = action1.duration + action2.duration;

		obj.fenceRatio = (obj.duration == 0 ? 0 : action1.duration / obj.duration);
		obj.currentAction = action1;
		return obj;
	}

	@Override
	public void reset() {
		super.reset();

		fenceRatio = 0;
		currentAction = null;
	}

	@Override
	public void restore() {
		super.restore();

		currentAction = action1;
	}

	@Override
	public TweenSequenceAction reversed() {
		TweenSequenceAction reversed = obtain(action2.reversed(), action1.reversed());

		reversed.target = target;
		reversed.ease(easing.reversed());
		return reversed;
	}

	@Override
	public float getDuration() {
		return action1.getDuration() + action2.getDuration();
	}

	@Override
	protected void doPin() {
		action1.pin();
		action1.pinPush();
		action2.pin();
		action1.pinPop();
	}

	@Override
	protected void applyRatio(float ratio) {
		if (fenceRatio == 0) {
			if (ratio == 0) {
				if (currentAction == action2) {
					action2.applyRatio(0);
					currentAction = action1;
				}
				action1.applyRatio(0);
			} else {
				if (currentAction == action1) {
					action1.applyRatio(1);
					currentAction = action2;
				}
				action2.applyRatio(duration == 0 ? 1 : ratio);
			}
		} else {
			if (ratio >= fenceRatio) {
				if (currentAction == action1) {
					action1.applyRatio(1);
					currentAction = action2;
				}
				action2.applyRatio(fenceRatio == 1 ? 1 : (ratio - fenceRatio) / (1 - fenceRatio));
			} else {
				if (currentAction == action2) {
					action2.applyRatio(0);
					currentAction = action1;
				}
				action1.applyRatio(ratio / fenceRatio);
			}
		}
	}
}
