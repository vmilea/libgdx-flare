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
import com.badlogic.gdx.utils.Array;
import com.vmilea.util.ArgCheck;

public abstract class AbstractGroupAction extends AbstractAction {

	protected final Array<AbstractAction> actions = new Array<AbstractAction>(true, 8);

	public AbstractGroupAction add(AbstractAction action) {
		ArgCheck.check(isDone, "May not be add actions to a group while it is running");
		ArgCheck.check(action != null, "Action may not be null");

		actions.add(action);
		return this;
	}

	public AbstractGroupAction addAll(Iterable<AbstractAction> actions) {
		for (AbstractAction action : actions) {
			add(action);
		}
		return this;
	}

	@Override
	public void setActor(Actor actor) {
		super.setActor(actor);

		if (actor != null) {
			for (AbstractAction action : actions) {
				action.setActor(target);
			}
		}
	}

	@Override
	public void setTarget(Actor target) {
		super.setTarget(target);

		for (AbstractAction action : actions) {
			action.setTarget(target);
		}
	}

	@Override
	public void reset() {
		super.reset();

		for (AbstractAction action : actions) {
			action.recycle();
		}
		actions.clear();
	}

	@Override
	public void restore() {
		super.restore();

		for (AbstractAction action : actions) {
			action.restore();
		}
	}

	@Override
	public boolean isReversible() {
		for (AbstractAction action : actions) {
			if (!action.isReversible())
				return false;
		}
		return true;
	}
}
