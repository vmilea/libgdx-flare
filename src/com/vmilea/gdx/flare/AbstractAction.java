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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vmilea.gdx.pool.PoolItem;
import com.vmilea.util.Assert;

public abstract class AbstractAction extends com.badlogic.gdx.scenes.scene2d.Action implements PoolItem {

	protected boolean isDone = true;
	protected boolean isPinned = false;

	// used to detect if the action has been recycled while running
	protected int poolItemIncarnation = 1;

	public abstract void pin();

	protected abstract float doRun(float dt);

	@SuppressWarnings("unchecked")
	@Override
	public void recycle() {
		if (getPool() != null)
			getPool().free(this);
	}

	@Override
	public void reset() {
		// don't restart!
		super.setPool(null);
		actor = null;
		target = null;
		isDone = true;
		isPinned = false;
	}

	@Override
	public void restart() {
		restore();
		pin();
	}

	@Override
	public boolean act(float dt) {
		run(dt);
		return isDone;
	}

	public AbstractAction target(Actor target) {
		setTarget(target);
		return this;
	}

	public AbstractAction startOn(Actor actor) {
		Assert.check(this.actor == null);
		Assert.check(isDone);

		actor.addAction(this);
		restart();
		return this;
	}

	public void skipToEnd(boolean removeFromActor) {
		skipToEnd(0.2f, removeFromActor);
	}

	public void skipToEnd(float dtPerStep, boolean removeFromActor) {
		int incarnation = poolItemIncarnation;

		// act until done; won't work for perpetual actions!
		while (incarnation == poolItemIncarnation && !isDone)
			act(dtPerStep);

		if (incarnation == poolItemIncarnation && removeFromActor)
			actor.removeAction(this);
	}

	public boolean isDone() {
		return isDone;
	}

	public boolean isReversible() {
		return false;
	}

	public AbstractAction reversed() {
		throw new UnsupportedOperationException(getClass().getSimpleName() + " doesn't support reversal");
	}

	public float getDuration() {
		throw new UnsupportedOperationException(getClass().getSimpleName() + " doesn't support duration");
	}

	protected void restore() {
		Assert.check(isDone, "May not restart action while it is running");

		isDone = false;
		isPinned = false; // _don't_ keep isPinned
	}

	protected boolean supportsRemoveWhileRunning() {
		return false;
	}

	protected float run(float dt) {
		if (isDone)
			Assert.check(false, String.format("action %s can't act() because it's finished!", toString()));
		if (!isPinned)
			Assert.check(false, String.format("action %s can't act() because it's not pinned!", toString()));

		int incarnation = poolItemIncarnation;
		float excessDt = doRun(dt);

		if (incarnation != poolItemIncarnation) {
			if (!supportsRemoveWhileRunning())
				Assert.check(false, String.format("action %s was removed while running!", toString()));
			return -1;
		} else {
			Assert.check(excessDt == 0 || isDone); // (excessDt > 0) implies isDone
			return excessDt;
		}
	}
}
