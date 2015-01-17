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

import com.badlogic.gdx.utils.FloatArray;
import com.vmilea.gdx.flare.ActionPool;
import com.vmilea.gdx.flare.Actions;
import com.vmilea.gdx.flare.actor.ComplexActorProperty;
import com.vmilea.gdx.pool.AltPool;
import com.vmilea.util.Assert;

public final class TweenByAction extends AbstractTweenAction {

	private ComplexActorProperty property;
	private final FloatArray delta = new FloatArray(4);
	private final FloatArray value0 = new FloatArray(4);

	public static final AltPool<TweenByAction> pool = ActionPool.make(TweenByAction.class);

	TweenByAction() { } // internal

	public static <T> TweenByAction obtain(ComplexActorProperty property, float[] delta, float duration) {
		TweenByAction obj = pool.obtain();
		obj.property = property;

		int count = property.getCount();
		obj.delta.ensureCapacity(count);
		System.arraycopy(delta, 0, obj.delta.items, 0, count);
		obj.value0.ensureCapacity(count);
		Actions.tmpFloatArray.ensureCapacity(count - Actions.tmpFloatArray.size);

		obj.duration = duration;
		return obj;
	}

	@Override
	public void reset() {
		super.reset();

		property = null;
		delta.size = 0;
		value0.size = 0;
	}

	@Override
	public void restore() {
		super.restore();

		value0.size = 0;
	}

	@Override
	public boolean isReversible() {
		return true;
	}

	@Override
	public TweenByAction reversed() {
		float[] tmpItems = Actions.tmpFloatArray.items;
		for (int i = 0, n = property.getCount(); i < n; i++) {
			tmpItems[i] = -delta.items[i];
		}

		TweenByAction reversed = obtain(property, tmpItems, duration);

		reversed.target = target;
		reversed.ease(easing.reversed());
		return reversed;
	}

	@Override
	protected void doPin() {
		Assert.check(value0.size == 0);

		property.get(target, value0.items);
		value0.size = property.getCount();
	}

	@Override
	protected void applyRatio(float ratio) {
		float[] tmpItems = Actions.tmpFloatArray.items;
		for (int i = 0, n = property.getCount(); i < n; i++) {
			tmpItems[i] = value0.items[i] + ratio * delta.items[i];
		}

		property.set(target, tmpItems);
	}
}
