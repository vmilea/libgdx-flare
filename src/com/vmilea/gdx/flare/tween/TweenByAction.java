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

@SuppressWarnings("unchecked")
public class TweenByAction extends AbstractTweenAction {

	@SuppressWarnings("rawtypes")
	protected InterpolatableActorProperty property;
	
	protected Object delta;
	protected Object value0 = null;
	protected boolean negateDelta;
	
	public static final AltPool<TweenByAction> pool = ActionPool.make(TweenByAction.class);
	
	TweenByAction () { } // internal
	
	public static <T> TweenByAction obtain(InterpolatableActorProperty<T> property, T delta, boolean negateDelta, float duration) {
		TweenByAction obj = pool.obtain();
		obj.property = property;
		obj.delta = property.obtain(delta);
		obj.negateDelta = negateDelta;
		obj.duration = duration;
		return obj;
	}
	
	@Override
	public void reset() {
		super.reset();
		
		property.recycle(delta);
		property.recycle(value0);
		property = null;
		delta = null;
		negateDelta = false;
		value0 = null;
	}
	
	@Override
	public void restore() {
		super.restore();
		
		value0 = null;
	}
	
	@Override
	public boolean isReversible() {
		return true;
	}
	
	@Override
	public TweenByAction reversed() {
		TweenByAction action = obtain(property, delta, !negateDelta, duration);
		action.ease(easing.reversed());
		return action;
	}
	
	@Override
	public void pin() {
		Assert.check(!isPinned);
		Assert.check(value0 == null);
		
		value0 = property.obtain(property.get(target));
		isPinned = true;
	}
	
	@Override
	protected void applyRatio(float ratio) {
		property.setRelative(target, value0, delta, (negateDelta ? -ratio : ratio));
	}
}
