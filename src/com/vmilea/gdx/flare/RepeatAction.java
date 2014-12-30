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

public final class RepeatAction extends AbstractWrapperAction {

	private int currentCycle, totalCycles;
	private boolean isInterrupted;
	
	public static final AltPool<RepeatAction> pool = ActionPool.make(RepeatAction.class);
	
	RepeatAction () { } // internal
	
	public static RepeatAction obtain(AbstractAction action, int repeatCount) {
		RepeatAction obj = pool.obtain();
		obj.action = action;
		obj.totalCycles = repeatCount;
		return obj;
	}
	
	public void interrupt() {
		isInterrupted = true;
	}
	
	@Override
	public void reset() {
		super.reset();
		
		currentCycle = 0;
		totalCycles = 0;
		isInterrupted = false;
	}
	
	@Override
	public void restore() {
		super.restore();
		
		currentCycle = 0;
		isInterrupted = false;
	}
	
	@Override
	public float getDuration() {
		return totalCycles * action.getDuration();
	}
	
	@Override
	protected float doRun(float dt) {
		if (isInterrupted || currentCycle == totalCycles) {
			isDone = true;
			return dt;
		}
		
		dt = action.run(dt);
		
		if (isInterrupted) {
			isDone = true;
			return dt;
		}
		
		while (action.isDone()) {
			action.restart();
			currentCycle++;
			
			if (currentCycle == totalCycles) {
				isDone = true;
				return dt;
			}
			
			dt = action.run(dt);
						
			if (isInterrupted) {
				isDone = true;
				return dt;
			}
		}
		
		return 0;
	}
}
