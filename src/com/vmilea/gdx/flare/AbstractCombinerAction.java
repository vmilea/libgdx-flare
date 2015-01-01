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

abstract class AbstractCombinerAction extends AbstractAction { // internal

	protected AbstractAction action1;
	protected AbstractAction action2;

	@Override
	public void setActor(Actor actor) {
		super.setActor(actor);

		if (actor != null) {
			action1.setActor(actor);
			action2.setActor(actor);
		}
	}

	@Override
	public void setTarget(Actor target, boolean replaceSubactionsTarget) {
		super.setTarget(target, replaceSubactionsTarget);

		if (action1.getTarget() == null || replaceSubactionsTarget)
			action1.setTarget(target, replaceSubactionsTarget);
		if (action2.getTarget() == null || replaceSubactionsTarget)
			action2.setTarget(target, replaceSubactionsTarget);
	}

	@Override
	public void reset() {
		super.reset();

		action1.recycle();
		action1 = null;
		action2.recycle();
		action2 = null;
	}

	@Override
	public void restore() {
		super.restore();

		action1.restore();
		action2.restore();
	}

	@Override
	public boolean isReversible() {
		return action1.isReversible() && action2.isReversible();
	}
}
