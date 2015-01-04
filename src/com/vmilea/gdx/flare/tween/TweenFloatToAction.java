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
import com.vmilea.util.StateCheck;

public class TweenFloatToAction extends AbstractTweenAction {

	protected FloatActorProperty property;

	protected float value1;
	protected float value0 = Float.NaN;

	public static final AltPool<TweenFloatToAction> pool = ActionPool.make(TweenFloatToAction.class);

	TweenFloatToAction() { } // internal

	public static TweenFloatToAction obtain(FloatActorProperty property, float value1, float duration) {
		TweenFloatToAction obj = pool.obtain();
		obj.property = property;
		obj.value1 = value1;
		obj.duration = duration;
		return obj;
	}

	@Override
	public void reset() {
		super.reset();

		property = null;
		value1 = 0;
		value0 = Float.NaN;
	}

	@Override
	public void restore() {
		super.restore();

		value0 = Float.NaN;
	}

	@Override
	public boolean isReversible() {
		return isPinned;
	}

	@Override
	public TweenFloatToAction reversed() {
		if (!isReversible())
			StateCheck.fail("%s can't be reversed unless pinned", getClass().getSimpleName());

		TweenFloatToAction reversed = obtain(property, value0, duration);

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
		property.set(target,
				value0 + ratio * (value1 - value0));
	}
}
