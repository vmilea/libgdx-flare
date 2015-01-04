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
import com.vmilea.gdx.flare.actor.FloatActorProperty;
import com.vmilea.gdx.pool.AltPool;
import com.vmilea.util.Assert;

public class TweenFloatByAction extends AbstractTweenAction {

	protected FloatActorProperty property;
	protected float delta;
	protected float value0 = Float.NaN;

	public static final AltPool<TweenFloatByAction> pool = ActionPool.make(TweenFloatByAction.class);

	TweenFloatByAction() { } // internal

	public static TweenFloatByAction obtain(FloatActorProperty property, float delta, float duration) {
		TweenFloatByAction obj = pool.obtain();
		obj.property = property;
		obj.delta = delta;
		obj.duration = duration;
		return obj;
	}

	@Override
	public void reset() {
		super.reset();

		property = null;
		delta = 0;
		value0 = Float.NaN;
	}

	@Override
	public void restore() {
		super.restore();

		value0 = Float.NaN;
	}

	@Override
	public boolean isReversible() {
		return true;
	}

	@Override
	public TweenFloatByAction reversed() {
		TweenFloatByAction reversed = obtain(property, -delta, duration);

		reversed.target = target;
		reversed.ease(easing.reversed());
		return reversed;
	}

	@Override
	protected void doPin() {
		Assert.check(Float.isNaN(value0));

		value0 = property.get(target);
	}

	@Override
	protected void applyRatio(float ratio) {
		property.set(target, value0 + ratio * delta);
	}
}
