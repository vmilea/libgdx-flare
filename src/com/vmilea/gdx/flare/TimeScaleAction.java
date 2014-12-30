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

import com.vmilea.gdx.pool.AltPool;

public final class TimeScaleAction extends AbstractWrapperAction {

	private float scale;
	
	public static final AltPool<TimeScaleAction> pool = ActionPool.make(TimeScaleAction.class);
	
	TimeScaleAction () { } // internal
	
	public static TimeScaleAction obtain(AbstractAction action, float scale) {
		if (scale <= 0)
			throw new IllegalArgumentException("Scale must be positive");
		
		TimeScaleAction obj = pool.obtain();
		obj.action = action;
		obj.scale = scale;
		return obj;
	}

	public float getScale() {
		return scale;
	}

	public void setTimeScale(float scale) {
		this.scale = scale;
	}
	
	@Override
	public void reset() {
		super.reset();
		
		scale = 0;
	}
	
	@Override
	public float getDuration() {
		// estimate based on current time scale
		return scale * action.getDuration();
	}
	
	@Override
	protected float doRun(float dt) {
		if (scale == 0) {
			action.run(0);
			dt = 0;
		} else {
			dt = action.run(dt * scale) / scale;
		}
		
		if (action.isDone())
			isDone = true;
		
		return dt;
	}
}
