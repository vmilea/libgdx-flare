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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public final class ActorProperties {

	//
	// Enum actor properties
	//

	public static final ActorProperty<Touchable> touchable = new ActorProperty<Touchable>() {

		public Touchable get(Actor target) {
			return target.getTouchable();
		}

		public void set(Actor target, Touchable value) {
			target.setTouchable(value);
		}
	};

	public static final ActorProperty<Boolean> visible = new ActorProperty<Boolean>() {

		public Boolean get(Actor target) {
			return target.isVisible() ? Boolean.TRUE : Boolean.FALSE;
		}

		public void set(Actor target, Boolean value) {
			target.setVisible(value == Boolean.TRUE);
		}
	};

	//
	// Float actor properties
	//

	public static final FloatActorProperty x = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getX();
		}

		public void set(Actor target, float value) {
			target.setX(value);
		}
	};

	public static final FloatActorProperty y = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getY();
		}

		public void set(Actor target, float value) {
			target.setY(value);
		}
	};

	public static final FloatActorProperty centerX = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getX(Align.center);
		}

		public void set(Actor target, float value) {
			target.setPosition(value, target.getY(Align.center), Align.center);
		}
	};

	public static final FloatActorProperty centerY = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getY(Align.center);
		}

		public void set(Actor target, float value) {
			target.setPosition(target.getX(Align.center), value, Align.center);
		}
	};

	public static final FloatActorProperty width = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getWidth();
		}

		public void set(Actor target, float value) {
			target.setWidth(value);
		}
	};

	public static final FloatActorProperty height = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getHeight();
		}

		public void set(Actor target, float value) {
			target.setHeight(value);
		}
	};

	public static final FloatActorProperty scaleX = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getScaleX();
		}

		public void set(Actor target, float value) {
			target.setScaleX(value);
		}
	};

	public static final FloatActorProperty scaleY = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getScaleY();
		}

		public void set(Actor target, float value) {
			target.setScaleY(value);
		}
	};

	public static final FloatActorProperty rotation = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getRotation();
		}

		public void set(Actor target, float value) {
			target.setRotation(value);
		}
	};

	public static final FloatActorProperty alpha = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getColor().a;
		}

		public void set(Actor target, float value) {
			target.getColor().a = value;
		}
	};

	public static final FloatActorProperty red = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getColor().r;
		}

		public void set(Actor target, float value) {
			target.getColor().r = value;
		}
	};

	public static final FloatActorProperty green = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getColor().g;
		}

		public void set(Actor target, float value) {
			target.getColor().g = value;
		}
	};

	public static final FloatActorProperty blue = new FloatActorProperty() {

		public float get(Actor target) {
			return target.getColor().b;
		}

		public void set(Actor target, float value) {
			target.getColor().b = value;
		}
	};

	//
	// FloatPair actor properties
	//

	public static final FloatPairActorProperty position = new FloatPairActorProperty() {

		public float getA(Actor target) {
			return target.getX();
		}

		public float getB(Actor target) {
			return target.getY();
		}

		public void set(Actor target, float a, float b) {
			target.setPosition(a, b);
		}
	};

	public static final FloatPairActorProperty centerPosition = new FloatPairActorProperty() {

		public float getA(Actor target) {
			return target.getX(Align.center);
		}

		public float getB(Actor target) {
			return target.getY(Align.center);
		}

		public void set(Actor target, float a, float b) {
			target.setPosition(a, b, Align.center);
		}
	};

	public static final FloatPairActorProperty size = new FloatPairActorProperty() {

		public float getA(Actor target) {
			return target.getWidth();
		}

		public float getB(Actor target) {
			return target.getHeight();
		}

		public void set(Actor target, float a, float b) {
			target.setSize(a, b);
		}
	};

	public static final FloatPairActorProperty scale = new FloatPairActorProperty() {

		public float getA(Actor target) {
			return target.getScaleX();
		}

		public float getB(Actor target) {
			return target.getScaleY();
		}

		public void set(Actor target, float a, float b) {
			target.setScale(a, b);
		}
	};

	//
	// complex actor properties
	//

	public static final class ColorProperty extends ComplexActorProperty {

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public void get(Actor target, float[] output) {
			get(target.getColor(), output);
		}

		@Override
		public void set(Actor target, float[] input) {
			Color color = target.getColor();
			color.r = input[0];
			color.g = input[1];
			color.b = input[2];
			color.a = input[3];
			color.clamp();
		}

		public void get(Color color, float[] output) {
			output[0] = color.r;
			output[1] = color.g;
			output[2] = color.b;
			output[3] = color.a;
		}
	};

	public static final ColorProperty color = new ColorProperty();

	//
	// private members
	//

	private ActorProperties() {
	}
}
