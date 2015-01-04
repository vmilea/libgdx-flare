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

abstract class AbstractWrapperAction extends AbstractAction { // internal

	protected AbstractAction action;

	AbstractWrapperAction() { } // internal

	@Override
	public void setActor(Actor actor) {
		super.setActor(actor);

		if (actor != null)
			action.setActor(actor);
	}

	@Override
	public void setTarget(Actor target, boolean replaceSubactionsTarget) {
		super.setTarget(target, replaceSubactionsTarget);

		if (action.getTarget() == null || replaceSubactionsTarget)
			action.setTarget(target, replaceSubactionsTarget);
	}

	@Override
	public void reset() {
		super.reset();

		action.recycle();
		action = null;
	}

	@Override
	public void restore() {
		super.restore();

		action.restore();
	}

	@Override
	protected void doPin() {
		action.pin();
	}
}
