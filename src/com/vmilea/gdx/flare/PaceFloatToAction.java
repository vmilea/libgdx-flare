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

import com.vmilea.gdx.flare.actor.FloatActorProperty;
import com.vmilea.gdx.pool.AltPool;
import com.vmilea.util.Assert;

public final class PaceFloatToAction extends AbstractAction {

	private FloatActorProperty property;

	private float speed;
	private float value1;

	public static final AltPool<PaceFloatToAction> pool = ActionPool.make(PaceFloatToAction.class);

	PaceFloatToAction() { } // internal

	public static PaceFloatToAction obtain(FloatActorProperty property, float value1, float speed) {
		Assert.check(speed >= 0, "Speed may not be negative");

		PaceFloatToAction obj = pool.obtain();
		obj.property = property;
		obj.value1 = value1;
		obj.speed = speed;
		return obj;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float scale) {
		Assert.check(speed >= 0, "Speed may not be negative");

		this.speed = scale;
	}

	@Override
	public void reset() {
		super.reset();
		
		property = null;
		speed = 0;
		value1 = 0;
	}

	@Override
	public void restore() {
		super.restore();
	}

	@Override
	public float getDuration() {
		Assert.check(isPinned, "PaceFloatToAction can't estimate duration unless pinned");

		// estimate based on current speed
		float delta = Math.abs(property.get(target) - value1);
		return delta / speed;
	}

	@Override
	public void pin() {
		Assert.check(!isPinned);

		isPinned = true;
	}

	@Override
	protected float doRun(float dt) {
		if (speed == 0) {
			dt = 0;
		} else {
			float value = property.get(target);
			boolean overshoot = false;

			if (value > value1) {
				// current value is over target, need to go down
				value -= dt * speed;
				if (value <= value1) {
					overshoot = true;
					dt = (value1 - value) / speed;
				}
			} else {
				// current value is less than or equal to target, need to go up
				value += dt * speed;
				if (value >= value1) {
					overshoot = true;
					dt = (value - value1) / speed;
				}
			}

			if (overshoot) {
				property.set(target, value1);
				isDone = true;
			} else {
				property.set(target, value);
				dt = 0;
			}
		}
		return dt;
	}
}
