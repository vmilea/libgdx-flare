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

package com.vmilea.gdx.flare.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface InterpolatableActorProperty<T> extends ActorProperty<T> {

	void setBetween(Actor actor, T value0, T value1, float ratio);

	void setRelative(Actor actor, T value0, T delta, float ratio);

	T obtain(T prototype);

	void recycle(T value);
}
