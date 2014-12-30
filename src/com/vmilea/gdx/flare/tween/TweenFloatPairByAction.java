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
import com.vmilea.gdx.flare.actor.FloatPairActorProperty;
import com.vmilea.gdx.pool.AltPool;
import com.vmilea.util.Assert;

public class TweenFloatPairByAction extends AbstractTweenAction {

	protected FloatPairActorProperty property;
	protected float aDelta, bDelta;
	protected float a0 = Float.NaN, b0 = Float.NaN;
	
	public static final AltPool<TweenFloatPairByAction> pool = ActionPool.make(TweenFloatPairByAction.class);
	
	TweenFloatPairByAction () { } // internal
	
	public static TweenFloatPairByAction obtain(FloatPairActorProperty property, float aDelta, float bDelta, float duration) {
		TweenFloatPairByAction obj = pool.obtain();
		obj.property = property;
		obj.aDelta = aDelta;
		obj.bDelta = bDelta;
		obj.duration = duration;
		return obj;
	}
	
	@Override
	public void reset() {
		super.reset();
		
		property = null;
		aDelta = 0;
		bDelta = 0;
		a0 = Float.NaN;
		b0 = Float.NaN;
	}
	
	@Override
	public void restore() {
		super.restore();
		
		a0 = Float.NaN;
		b0 = Float.NaN;
	}
	
	@Override
	public boolean isReversible() {
		return true;
	}
	
	@Override
	public TweenFloatPairByAction reversed() {
		TweenFloatPairByAction action = obtain(property, -aDelta, -bDelta, duration);
		action.ease(easing.reversed());
		return action;
	}
	
	@Override
	public void pin() {
		Assert.check(!isPinned);
		Assert.check(Float.isNaN(a0) && Float.isNaN(b0));
		
		a0 = property.getA(target);
		b0 = property.getB(target);
		isPinned = true;
	}
	
	@Override
	protected void applyRatio(float ratio) {
		property.set(target,
				a0 + ratio * aDelta,
				b0 + ratio * bDelta);
	}
}
