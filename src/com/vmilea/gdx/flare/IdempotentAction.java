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

import com.vmilea.gdx.flare.Actions.IdempotentDelegate;
import com.vmilea.gdx.flare.tween.AbstractTweenAction;
import com.vmilea.gdx.pool.AltPool;
import com.vmilea.util.Assert;

// Expected to have the same effect every time it runs. May be restarted & reversed.
//
public final class IdempotentAction extends AbstractTweenAction {

	private IdempotentDelegate delegate;
	private Object data;
	private boolean skipWhilePinning;
	
	public static final AltPool<IdempotentAction> pool = ActionPool.make(IdempotentAction.class);
	
	IdempotentAction () { } // internal
	
	public static IdempotentAction obtain(IdempotentDelegate delegate, Object data) {
		IdempotentAction obj = pool.obtain();
		obj.delegate = delegate;
		obj.data = data;
		return obj;
	}
	
	public IdempotentAction skipWhilePinning() {
		skipWhilePinning = true;
		return this;
	}
	
	@Override
	public void reset() {
		super.reset();
		delegate = null;
		data = null;
	}
	
	@Override
	public boolean isReversible() {
		return true;
	}
	
	@Override
	public IdempotentAction reversed() {
		return obtain(delegate, data);
	}
	
	@Override
	public void pin() {
		Assert.check(!isPinned);
		
		isPinned = true;
	}
	
	@Override
	protected void applyRatio(float ratio) {
		delegate.run(target, data);
	}
	
	@Override
	protected void pinPush() {
		if (!skipWhilePinning)
			super.pinPush();
	}
	
	@Override
	protected void pinPop() {
		if (!skipWhilePinning)
			super.pinPop();
	}
}
