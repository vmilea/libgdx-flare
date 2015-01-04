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

import com.vmilea.gdx.flare.tween.Easing;
import com.vmilea.gdx.pool.AltPool;
import com.vmilea.util.Assert;

public final class EaseAction extends AbstractWrapperAction {

	private final static float EPS = 0.001f;

	private Easing easing;
	private float duration;
	private float elapsed;
	private float easedElapsed;

	public static final AltPool<EaseAction> pool = ActionPool.make(EaseAction.class);

	EaseAction() { } // internal

	public static EaseAction obtain(AbstractAction action, Easing easing) {
		EaseAction obj = pool.obtain();
		obj.action = action;
		obj.easing = easing;
		return obj;
	}

	@Override
	public void reset() {
		super.reset();
		
		easing = null;
		duration = Float.NaN;
		elapsed = 0;
		easedElapsed = 0;
	}

	@Override
	public void restore() {
		super.restore();
		
		duration = Float.NaN;
		elapsed = 0;
		easedElapsed = 0;
	}

	@Override
	public float getDuration() {
		return duration;
	}

	@Override
	protected void doPin() {
		super.doPin();

		Assert.check(Float.isNaN(duration));
		duration = action.getDuration();
	}

	@Override
	protected float doRun(float dt) {
		elapsed += dt;

		if (elapsed < duration) {
			float easedElapsed = easing.get(elapsed / duration) * duration;
			action.run(easedElapsed - this.easedElapsed);
			this.easedElapsed = easedElapsed;

			if (action.isDone()) {
				isDone = true;
				easedElapsed = elapsed = duration;
			}
			return 0;
		} else {
			float dtExcess = elapsed - duration;
			action.run(duration - easedElapsed);
			easedElapsed = elapsed = duration;

			while (!action.isDone()) {
				action.run(EPS);
			}
			isDone = true;
			return dtExcess;
		}
	}
}
