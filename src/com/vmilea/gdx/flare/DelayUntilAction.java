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

import com.badlogic.gdx.utils.Predicate;
import com.vmilea.gdx.pool.AltPool;
import com.vmilea.util.Assert;

public final class DelayUntilAction<T> extends AbstractAction {

	private Predicate<T> predicate;
	private T argument;

	public static final AltPool<DelayUntilAction<?>> pool = ActionPool.make(DelayUntilAction.class);

	DelayUntilAction() { } // internal

	public static <T> DelayUntilAction<T> obtain(Predicate<T> predicate, T argument) {
		DelayUntilAction<T> obj = pool.obtainAs();
		obj.predicate = predicate;
		obj.argument = argument;
		return obj;
	}

	public T getArgument() {
		return argument;
	}

	public void setArgument(T argument) {
		this.argument = argument;
	}

	@Override
	public void reset() {
		super.reset();

		predicate = null;
		argument = null;
	}

	@Override
	public void restore() {
		if (predicate == null)
			throw new UnsupportedOperationException("DelayUntilAction may not be restarted");

		super.restore();
	}

	@Override
	public void pin() {
		Assert.check(!isPinned);

		isPinned = true;
	}

	@Override
	protected float doRun(float dt) {
		if (predicate.evaluate(argument)) {
			isDone = true;
			return dt;
		} else {
			return 0;
		}
	}
}
