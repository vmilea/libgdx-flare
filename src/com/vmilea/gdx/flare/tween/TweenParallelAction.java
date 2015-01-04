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
import com.vmilea.gdx.flare.Actions;
import com.vmilea.gdx.pool.AltPool;

public final class TweenParallelAction extends AbstractTweenCombinerAction {

	private float durationRatio;

	public static final AltPool<TweenParallelAction> pool = ActionPool.make(TweenParallelAction.class);

	TweenParallelAction() { } // internal

	public static TweenParallelAction obtain(AbstractTweenAction action1, AbstractTweenAction action2) {
		TweenParallelAction obj = pool.obtain();

		if (action1.duration <= action2.duration) {
			obj.action1 = action1;
			obj.action2 = action2;
		} else {
			// swap so action2 is always longer or equal to action1
			obj.action1 = action2;
			obj.action2 = action1;
		}
		obj.duration = obj.action2.duration;

		obj.durationRatio = (obj.duration == 0 ? -1 : obj.action1.duration / obj.duration);
		return obj;
	}

	@Override
	public void reset() {
		super.reset();

		durationRatio = 0;
	}

	@Override
	public TweenParallelAction reversed() {
		TweenParallelAction reversed;

		if (action1.duration == action2.duration) {
			reversed = obtain(action1.reversed(), action2.reversed());
		} else {
			reversed = obtain(
					Actions.tpaddedLeft(action1.reversed(), action2.duration - action1.duration),
					action2.reversed());
		}

		reversed.target = target;
		reversed.ease(easing.reversed());
		return reversed;
	}

	@Override
	public float getDuration() {
		return Math.max(action1.getDuration(), action2.getDuration());
	}

	@Override
	protected void doPin() {
		action1.pin();
		action2.pin();
	}

	@Override
	protected void applyRatio(float ratio) {
		if (ratio == 0) {
			action1.applyRatio(0);
			action2.applyRatio(0);
		} else {
			if (durationRatio == -1) {
				action1.applyRatio(1);
				action2.applyRatio(1);
			} else {
				action1.applyRatio(ratio > durationRatio ? 1 : ratio / durationRatio);
				action2.applyRatio(ratio);
			}
		}
	}
}
