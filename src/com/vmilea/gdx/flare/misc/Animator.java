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

package com.vmilea.gdx.flare.misc;

import static com.vmilea.gdx.flare.Actions.run;
import static com.vmilea.gdx.flare.Actions.seq;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.vmilea.gdx.flare.AbstractAction;
import com.vmilea.gdx.flare.Actions.Delegate;
import com.vmilea.util.ArgCheck;
import com.vmilea.util.Assert;

public class Animator<T extends Enum<T>> {

	protected final ObjectMap<T, AbstractAction> managedActions = new ObjectMap<T, AbstractAction>();

	public boolean isRunning(T actionId) {
		return managedActions.containsKey(actionId);
	}

	public void start(T actionId, AbstractAction action, Actor actor) {
		ArgCheck.check(action != null, "Invalid action");
		ArgCheck.check(actor != null, "Invalid actor");

		stop(actionId);

		action = seq(action, run(actionDoneHandler, actionId));
		managedActions.put(actionId, action);
		action.startOn(actor);
	}

	public boolean stop(T actionId) {
		AbstractAction action = managedActions.get(actionId);

		if (action != null) {
			action.getActor().removeAction(action);
			managedActions.remove(actionId);
			return true;
		} else {
			return false;
		}
	}

	public void stopAll() {
		for (ObjectMap.Entry<T, AbstractAction> entry : managedActions) {
			AbstractAction action = entry.value;
			action.getActor().removeAction(action);
		}
		managedActions.clear();
	}

	public boolean skipToEnd(T actionId) {
		AbstractAction action = managedActions.get(actionId);

		if (action != null) {
			action.skipToEnd(true);
			Assert.check(!managedActions.containsKey(actionId));
			return true;
		} else {
			return false;
		}
	}

	private final Delegate actionDoneHandler = new Delegate() {
		@SuppressWarnings("unchecked")
		@Override
		public void run(Actor actor, Object data) {
			T actionId = (T) data;
			Assert.check(managedActions.containsKey(actionId));
			managedActions.remove(actionId);
		}
	};
}
