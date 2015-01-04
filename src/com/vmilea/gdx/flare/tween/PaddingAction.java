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
import com.vmilea.gdx.pool.AltPool;

public final class PaddingAction extends AbstractTweenAction {

	private AbstractTweenAction action; // not owned
	private float fixedRatio;

	public static final AltPool<PaddingAction> pool = ActionPool.make(PaddingAction.class);

	PaddingAction() { } // internal

	public static PaddingAction obtain(AbstractTweenAction action, float fixedRatio, float duration) {
		PaddingAction obj = pool.obtain();
		obj.action = action;
		obj.fixedRatio = fixedRatio;
		obj.duration = duration;
		return obj;
	}

	@Override
	public void reset() {
		super.reset();
		action = null;
		fixedRatio = 0;
	}

	@Override
	public boolean isReversible() {
		return true;
	}

	@Override
	public PaddingAction reversed() {
		return obtain(action, fixedRatio, duration);
	}

	@Override
	protected void doPin() {
	}

	@Override
	protected void applyRatio(float ratio) {
		action.applyRatio(fixedRatio);
	}
}
