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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vmilea.gdx.flare.AbstractAction;
import com.vmilea.util.Assert;

public abstract class AbstractTweenAction extends AbstractAction {

	protected float duration;
	protected float elapsed;
	protected Easing easing = Easings.linear;

	protected abstract void applyRatio(float ratio);

	@Override
	public void reset() {
		super.reset();

		duration = 0;
		elapsed = 0;
		easing = Easings.linear;
	}

	@Override
	public void restore() {
		super.restore();

		elapsed = 0;
	}

	@Override
	public AbstractTweenAction target(Actor target) {
		super.target(target);

		return this;
	}

	@Override
	public AbstractTweenAction reversed() {
		Assert.fail("All tween actions should be reversible");
		return null;
	}

	@Override
	protected float doRun(float dt) {
		elapsed += dt;

		if (elapsed < duration) {
			seek(elapsed / duration);
			return 0;
		} else {
			float dtExcess = elapsed - duration;
			elapsed = duration;
			seek(1);
			isDone = true;
			return dtExcess;
		}
	}

	@Override
	public AbstractTweenAction skipToEnd(boolean removeFromActor) {
		int incarnation = poolItemIncarnation;
		seek(1);
		Assert.check(incarnation == poolItemIncarnation);
		isDone = true;

		if (removeFromActor)
			actor.removeAction(this);

		return this;
	}

	@Override
	public AbstractTweenAction skipToEnd(float dtPerStep, boolean removeFromActor) {
		return skipToEnd(removeFromActor);
	}

	@Override
	public AbstractTweenAction startOn(Actor actor) {
		super.startOn(actor);

		return this;
	}

	@Override
	public float getDuration() {
		return duration;
	}

	public float getElapsed() {
		return elapsed;
	}

	public void seek(float ratio) {
		applyRatio(easing.get(ratio));
		isDone = false;
	}

	public Easing getEasing() {
		return easing;
	}

	public AbstractTweenAction ease(Easing easing) {
		this.easing = easing;
		return this;
	}

	protected void pinPush() {
		seek(1.0f);
	}

	protected void pinPop() {
		seek(0.0f);
	}
}
