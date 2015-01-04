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
import com.vmilea.util.ArgCheck;
import com.vmilea.util.Assert;
import com.vmilea.util.StateCheck;

public abstract class AbstractAction extends com.badlogic.gdx.scenes.scene2d.Action implements PoolItem {

	protected boolean isDone = true;
	protected boolean isPinned = false;

	// used to detect if the action has been recycled while running
	protected int poolItemIncarnation = 1;

	protected abstract void doPin();

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

	@Override
	public void setTarget(Actor target) {
		setTarget(target, false);
	}

	public void setTarget(Actor target, boolean replaceSubactionsTarget) {
		this.target = target;
	}

	public AbstractAction target(Actor target) {
		setTarget(target);
		return this;
	}

	public void pin() {
		if (isPinned)
			StateCheck.fail("%s is already pinned", getClass().getSimpleName());

		doPin();
		isPinned = true;
	}

	public AbstractAction startOn(Actor actor) {
		if (this.actor != null)
			ArgCheck.fail("%s already bound to an actor", getClass().getSimpleName());
		if (!isDone)
			StateCheck.fail("%s is already running", getClass().getSimpleName());

		actor.addAction(this);
		restart();
		return this;
	}

	public AbstractAction skipToEnd(boolean removeFromActor) {
		return skipToEnd(0.2f, removeFromActor);
	}

	public AbstractAction skipToEnd(float dtPerStep, boolean removeFromActor) {
		int incarnation = poolItemIncarnation;

		// act until done; won't work for perpetual actions!
		while (incarnation == poolItemIncarnation && !isDone)
			act(dtPerStep);

		if (incarnation == poolItemIncarnation && removeFromActor)
			actor.removeAction(this);

		return this;
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
		if (!isDone)
			StateCheck.fail("May not restart %s while it is running", getClass().getSimpleName());

		isDone = false;
		isPinned = false; // _don't_ keep isPinned
	}

	protected boolean supportsRemoveWhileRunning() {
		return false;
	}

	protected float run(float dt) {
		if (isDone)
			Assert.fail("Action %s can't act() because it's finished!", toString());
		if (!isPinned)
			Assert.fail("Action %s can't act() because it's not pinned!", toString());

		int incarnation = poolItemIncarnation;
		float excessDt = doRun(dt);

		if (incarnation != poolItemIncarnation) {
			if (!supportsRemoveWhileRunning())
				Assert.fail("Action %s was removed while running!", toString());
			return -1;
		} else {
			Assert.check(excessDt == 0 || isDone); // (excessDt > 0) implies isDone
			return excessDt;
		}
	}
}
