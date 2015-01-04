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

import com.vmilea.gdx.flare.ActionPool;
import com.vmilea.gdx.pool.AltPool;
import com.vmilea.util.ArgCheck;

public final class ScaleByFactorAction extends AbstractTweenAction {

	private float xFactor, yFactor;
	private float scaleX0 = Float.NaN, scaleY0 = Float.NaN;
	private float deltaScaleX, deltaScaleY;

	public static final AltPool<ScaleByFactorAction> pool = ActionPool.make(ScaleByFactorAction.class);

	ScaleByFactorAction() { } // internal

	public static ScaleByFactorAction obtain(float xFactor, float yFactor, float duration) {
		ArgCheck.check(xFactor >= 0 && yFactor >= 0, "Factor may not be negative");

		ScaleByFactorAction obj = pool.obtain();
		obj.xFactor = xFactor;
		obj.yFactor = yFactor;
		obj.duration = duration;
		return obj;
	}

	@Override
	public void reset() {
		super.reset();
		xFactor = yFactor = 0;
		scaleX0 = Float.NaN;
		scaleY0 = Float.NaN;
		deltaScaleX = deltaScaleY = 0;
	}

	@Override
	public void restore() {
		super.restore();
		scaleX0 = Float.NaN;
		scaleY0 = Float.NaN;
	}

	@Override
	public boolean isReversible() {
		return true;
	}

	@Override
	public ScaleByFactorAction reversed() {
		ScaleByFactorAction reversed = obtain(1 / xFactor, 1 / yFactor, duration);

		reversed.target = target;
		reversed.ease(easing.reversed());
		return reversed;
	}

	@Override
	protected void doPin() {
		scaleX0 = target.getScaleX();
		scaleY0 = target.getScaleY();
		deltaScaleX = scaleX0 * (xFactor - 1);
		deltaScaleY = scaleY0 * (yFactor - 1);
	}

	@Override
	protected void applyRatio(float ratio) {
		target.setScale(
				scaleX0 + ratio * deltaScaleX,
				scaleY0 + ratio * deltaScaleY);
	}
}
