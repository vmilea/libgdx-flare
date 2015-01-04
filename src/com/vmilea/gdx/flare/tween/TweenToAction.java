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
import com.vmilea.gdx.flare.actor.InterpolatableActorProperty;
import com.vmilea.gdx.pool.AltPool;
import com.vmilea.util.Assert;
import com.vmilea.util.StateCheck;

@SuppressWarnings("unchecked")
public class TweenToAction extends AbstractTweenAction {

	@SuppressWarnings("rawtypes")
	protected InterpolatableActorProperty property;

	protected Object value1;
	protected Object value0 = null;

	public static final AltPool<TweenToAction> pool = ActionPool.make(TweenToAction.class);

	TweenToAction() { } // internal

	public static <T> TweenToAction obtain(InterpolatableActorProperty<T> property, T value1, float duration) {
		TweenToAction obj = pool.obtain();
		obj.property = property;
		obj.value1 = property.obtain(value1);
		obj.duration = duration;
		return obj;
	}

	@Override
	public void reset() {
		super.reset();
		property.recycle(value1);
		property.recycle(value0);
		property = null;
		value1 = null;
		value0 = null;
	}

	@Override
	public void restore() {
		super.restore();
		property.recycle(value0);
		value0 = null;
	}

	@Override
	public boolean isReversible() {
		return isPinned;
	}

	@Override
	public TweenToAction reversed() {
		if (!isReversible())
			StateCheck.fail("%s can't be reversed unless pinned", getClass().getSimpleName());

		TweenToAction reversed = obtain(property, value0, duration);

		reversed.target = target;
		reversed.ease(easing.reversed());
		return reversed;
	}

	@Override
	protected void doPin() {
		Assert.check(value0 == null);

		value0 = property.obtain(property.get(target));
	}

	@Override
	protected void applyRatio(float ratio) {
		property.setBetween(target, value0, value1, ratio);
	}
}
