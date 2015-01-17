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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Predicate;
import com.vmilea.gdx.flare.actor.ActorProperties;
import com.vmilea.gdx.flare.actor.ComplexActorProperty;
import com.vmilea.gdx.flare.actor.FloatActorProperty;
import com.vmilea.gdx.flare.actor.FloatPairActorProperty;
import com.vmilea.gdx.flare.tween.AbstractTweenAction;
import com.vmilea.gdx.flare.tween.DelayAction;
import com.vmilea.gdx.flare.tween.Easing;
import com.vmilea.gdx.flare.tween.IdempotentAction;
import com.vmilea.gdx.flare.tween.PaddingAction;
import com.vmilea.gdx.flare.tween.ScaleByFactorAction;
import com.vmilea.gdx.flare.tween.TweenByAction;
import com.vmilea.gdx.flare.tween.TweenFloatByAction;
import com.vmilea.gdx.flare.tween.TweenFloatPairByAction;
import com.vmilea.gdx.flare.tween.TweenFloatPairToAction;
import com.vmilea.gdx.flare.tween.TweenFloatToAction;
import com.vmilea.gdx.flare.tween.TweenParallelAction;
import com.vmilea.gdx.flare.tween.TweenSequenceAction;
import com.vmilea.gdx.flare.tween.TweenToAction;
import com.vmilea.gdx.pool.PayloadRunnable;

public final class Actions {

	// generic delegate; if Disposable, it will be disposed when the action is recycled
	//
	public interface Delegate {
		void run(Actor actor, Object data);
	}

	// stateless delegate that may be invoked multiple times with the same effect
	//
	public interface IdempotentDelegate {
		void run(Actor actor, Object data);
	}

	public static void addTo(Actor actor, AbstractAction action) {
		actor.addAction(action);
		action.restart();
	}

	//
	// primitives
	//

	public static DelayAction delay(float duration) {
		return DelayAction.obtain(duration);
	}

	public static DelayAction nothing() {
		return delay(0);
	}

	public static <T> DelayUntilAction<T> delayUntil(Predicate<T> predicate, T argument) {
		return DelayUntilAction.obtain(predicate, argument);
	}

	public static DelayUntilAction<Void> delayUntil(Predicate<Void> predicate) {
		return DelayUntilAction.obtain(predicate, null);
	}

	// tween padding
	//

	public static PaddingAction padding(AbstractTweenAction action, float fixedRatio, float duration) {
		return PaddingAction.obtain(action, fixedRatio, duration);
	}

	// tween by
	//

	public static TweenFloatByAction tweenBy(FloatActorProperty property, float delta, float duration) {
		return TweenFloatByAction.obtain(property, delta, duration);
	}

	public static TweenFloatPairByAction tweenPairBy(FloatPairActorProperty property, float aDelta, float bDelta, float duration) {
		return TweenFloatPairByAction.obtain(property, aDelta, bDelta, duration);
	}

	public static TweenByAction tweenBy(ComplexActorProperty property, float[] delta, float duration) {
		return TweenByAction.obtain(property, delta, duration);
	}

	public static TweenFloatByAction moveXBy(float xDelta, float duration) {
		return tweenBy(ActorProperties.x, xDelta, duration);
	}

	public static TweenFloatByAction moveYBy(float yDelta, float duration) {
		return tweenBy(ActorProperties.y, yDelta, duration);
	}

	public static TweenFloatPairByAction moveBy(float xDelta, float yDelta, float duration) {
		return tweenPairBy(ActorProperties.position, xDelta, yDelta, duration);
	}

	public static TweenFloatPairByAction moveBy(Vector2 delta, float duration) {
		return moveBy(delta.x, delta.y, duration);
	}

	public static TweenFloatPairByAction resizeBy(float widthDelta, float heightDelta, float duration) {
		return tweenPairBy(ActorProperties.size, widthDelta, heightDelta, duration);
	}

	public static TweenFloatPairByAction resizeBy(Vector2 delta, float duration) {
		return resizeBy(delta.x, delta.y, duration);
	}

	public static TweenFloatPairByAction scaleBy(float scaleXDelta, float scaleYDelta, float duration) {
		return tweenPairBy(ActorProperties.scale, scaleXDelta, scaleYDelta, duration);
	}

	public static TweenFloatPairByAction scaleBy(float scaleDelta, float duration) {
		return scaleBy(scaleDelta, scaleDelta, duration);
	}

	public static ScaleByFactorAction scaleByFactor(float xFactor, float yFactor, float duration) {
		return ScaleByFactorAction.obtain(xFactor, yFactor, duration);
	}

	public static ScaleByFactorAction scaleByFactor(float factor, float duration) {
		return scaleByFactor(factor, factor, duration);
	}

	public static TweenFloatByAction rotateBy(float rotationDelta, float duration) {
		return tweenBy(ActorProperties.rotation, rotationDelta, duration);
	}

	public static TweenFloatByAction rotateRadBy(float radRotationDelta, float duration) {
		return rotateBy(MathUtils.radiansToDegrees * radRotationDelta, duration);
	}

	public static TweenFloatByAction increaseAlphaBy(float alphaDelta, float duration) {
		return tweenBy(ActorProperties.alpha, alphaDelta, duration);
	}

	public static TweenFloatByAction decreaseAlphaBy(float alphaDelta, float duration) {
		return tweenBy(ActorProperties.alpha, -alphaDelta, duration);
	}

	public static TweenByAction increaseColorBy(Color delta, float duration) {
		ActorProperties.color.get(delta, tmpFloatArray.items);
		return tweenBy(ActorProperties.color, tmpFloatArray.items, duration);
	}

	public static TweenByAction increaseColorBy(float deltaR, float deltaG, float deltaB, float deltaA, float duration) {
		return increaseColorBy(tmpColor.set(deltaR, deltaG, deltaB, deltaA), duration);
	}

	public static TweenByAction decreaseColorBy(Color delta, float duration) {
		ActorProperties.color.get(delta, tmpFloatArray.items);
		for (int i = 0, n = ActorProperties.color.getCount(); i < n; i++) {
			tmpFloatArray.items[i] *= -1;
		}

		return tweenBy(ActorProperties.color, tmpFloatArray.items, duration);
	}

	public static TweenByAction decreaseColorBy(float deltaR, float deltaG, float deltaB, float deltaA, float duration) {
		return decreaseColorBy(tmpColor.set(deltaR, deltaG, deltaB, deltaA), duration);
	}

	// tween to
	//

	public static TweenFloatToAction tweenTo(FloatActorProperty property, float value1, float duration) {
		return TweenFloatToAction.obtain(property, value1, duration);
	}

	public static TweenFloatPairToAction tweenPairTo(FloatPairActorProperty property, float a1, float b1, float duration) {
		return TweenFloatPairToAction.obtain(property, a1, b1, duration);
	}

	public static TweenToAction tweenTo(ComplexActorProperty property, float[] value1, float duration) {
		return TweenToAction.obtain(property, value1, duration);
	}

	public static TweenFloatToAction moveXTo(float x1, float duration) {
		return tweenTo(ActorProperties.x, x1, duration);
	}

	public static TweenFloatToAction moveYTo(float y1, float duration) {
		return tweenTo(ActorProperties.y, y1, duration);
	}

	public static TweenFloatPairToAction moveTo(float x1, float y1, float duration) {
		return tweenPairTo(ActorProperties.position, x1, y1, duration);
	}

	public static TweenFloatPairToAction moveTo(Vector2 position1, float duration) {
		return moveTo(position1.x, position1.y, duration);
	}

	public static TweenFloatToAction moveCenterXTo(float x1, float duration) {
		return tweenTo(ActorProperties.centerX, x1, duration);
	}

	public static TweenFloatToAction moveCenterYTo(float y1, float duration) {
		return tweenTo(ActorProperties.centerY, y1, duration);
	}

	public static TweenFloatPairToAction moveCenterTo(float x1, float y1, float duration) {
		return tweenPairTo(ActorProperties.centerPosition, x1, y1, duration);
	}

	public static TweenFloatPairToAction moveCenterTo(Vector2 position1, float duration) {
		return moveCenterTo(position1.x, position1.y, duration);
	}

	public static TweenFloatPairToAction scaleTo(float scaleX, float scaleY, float duration) {
		return tweenPairTo(ActorProperties.scale, scaleX, scaleY, duration);
	}

	public static TweenFloatPairToAction scaleTo(float scale, float duration) {
		return tweenPairTo(ActorProperties.scale, scale, scale, duration);
	}

	public static TweenFloatPairToAction resizeTo(float width, float height, float duration) {
		return tweenPairTo(ActorProperties.size, width, height, duration);
	}

	public static TweenFloatPairToAction resizeTo(Vector2 size, float duration) {
		return resizeTo(size.x, size.y, duration);
	}

	public static TweenFloatToAction rotateTo(float rotation1, float duration) {
		return tweenTo(ActorProperties.rotation, rotation1, duration);
	}

	public static TweenFloatToAction rotateRadTo(float radRotation1, float duration) {
		return rotateTo(MathUtils.radiansToDegrees * radRotation1, duration);
	}

	public static TweenFloatToAction tweenAlphaTo(float alpha1, float duration) {
		return tweenTo(ActorProperties.alpha, alpha1, duration);
	}

	public static TweenFloatToAction fadeOut(float duration) {
		return tweenAlphaTo(0, duration);
	}

	public static TweenFloatToAction fadeIn(float duration) {
		return tweenAlphaTo(1, duration);
	}

	public static TweenToAction tweenColorTo(float r, float g, float b, float a, float duration) {
		return tweenColorTo(tmpColor.set(r, g, b, a), duration);
	}

	public static TweenToAction tweenColorTo(Color color, float duration) {
		ActorProperties.color.get(color, tmpFloatArray.items);
		return tweenTo(ActorProperties.color, tmpFloatArray.items, duration);
	}

	// pace to
	//

	public static PaceFloatToAction paceTo(FloatActorProperty property, float value1, float speed) {
		return PaceFloatToAction.obtain(property, value1, speed);
	}

	public static PaceFloatToAction paceXTo(float x1, float speed) {
		return paceTo(ActorProperties.x, x1, speed);
	}

	public static PaceFloatToAction paceYTo(float y1, float speed) {
		return paceTo(ActorProperties.y, y1, speed);
	}

	public static PaceFloatToAction paceWidthTo(float width1, float speed) {
		return paceTo(ActorProperties.width, width1, speed);
	}

	public static PaceFloatToAction paceHeightTo(float height1, float speed) {
		return paceTo(ActorProperties.height, height1, speed);
	}

	public static PaceFloatToAction paceScaleXTo(float scaleX1, float speed) {
		return paceTo(ActorProperties.scaleX, scaleX1, speed);
	}

	public static PaceFloatToAction paceScaleYTo(float scaleY1, float speed) {
		return paceTo(ActorProperties.scaleY, scaleY1, speed);
	}

	public static PaceFloatToAction paceAlphaTo(float alpha1, float speed) {
		return paceTo(ActorProperties.alpha, alpha1, speed);
	}

	public static PaceFloatToAction paceRotationTo(float rotation1, float speed) {
		return paceTo(ActorProperties.rotation, rotation1, speed);
	}

	public static PaceFloatToAction paceRadRotationTo(float radRotation1, float speed) {
		return paceTo(ActorProperties.rotation, MathUtils.radiansToDegrees * radRotation1, speed);
	}

	//
	// combinators
	//

	public static SequenceAction seq(AbstractAction action1, AbstractAction action2) {
		SequenceAction group = SequenceAction.obtain();
		return group.add(action1).add(action2);
	}

	public static SequenceAction seq(AbstractAction action1, AbstractAction action2, AbstractAction action3) {
		SequenceAction group = SequenceAction.obtain();
		return group.add(action1).add(action2).add(action3);
	}

	public static SequenceAction seq(AbstractAction action1, AbstractAction action2, AbstractAction action3, AbstractAction action4) {
		SequenceAction group = SequenceAction.obtain();
		return group.add(action1).add(action2).add(action3).add(action4);
	}

	public static SequenceAction seq(AbstractAction action1, AbstractAction action2, AbstractAction action3, AbstractAction action4, AbstractAction action5) {
		SequenceAction group = SequenceAction.obtain();
		return group.add(action1).add(action2).add(action3).add(action4).add(action5);
	}

	public static SequenceAction seq(AbstractAction action1, AbstractAction action2, AbstractAction action3, AbstractAction action4, AbstractAction action5, AbstractAction action6) {
		SequenceAction group = SequenceAction.obtain();
		return group.add(action1).add(action2).add(action3).add(action4).add(action5).add(action6);
	}

	public static SequenceAction seq(AbstractAction action1, AbstractAction action2, AbstractAction action3, AbstractAction action4, AbstractAction action5, AbstractAction action6, AbstractAction action7) {
		SequenceAction group = SequenceAction.obtain();
		return group.add(action1).add(action2).add(action3).add(action4).add(action5).add(action6).add(action7);
	}

	public static TweenSequenceAction tseq(AbstractTweenAction action1, AbstractTweenAction action2) {
		return TweenSequenceAction.obtain(action1, action2);
	}

	public static TweenSequenceAction tseq(AbstractTweenAction action1, AbstractTweenAction action2, AbstractTweenAction action3) {
		return tseq(tseq(action1, action2), action3);
	}

	public static TweenSequenceAction tseq(AbstractTweenAction action1, AbstractTweenAction action2, AbstractTweenAction action3, AbstractTweenAction action4) {
		return tseq(tseq(action1, action2, action3), action4);
	}

	public static TweenSequenceAction tseq(AbstractTweenAction action1, AbstractTweenAction action2, AbstractTweenAction action3, AbstractTweenAction action4, AbstractTweenAction action5) {
		return tseq(tseq(action1, action2, action3, action4), action5);
	}

	public static TweenSequenceAction tseq(AbstractTweenAction action1, AbstractTweenAction action2, AbstractTweenAction action3, AbstractTweenAction action4, AbstractTweenAction action5, AbstractTweenAction action6) {
		return tseq(tseq(action1, action2, action3, action4, action5), action6);
	}

	public static TweenSequenceAction tseq(AbstractTweenAction action1, AbstractTweenAction action2, AbstractTweenAction action3, AbstractTweenAction action4, AbstractTweenAction action5, AbstractTweenAction action6, AbstractTweenAction action7) {
		return tseq(tseq(action1, action2, action3, action4, action5, action6), action7);
	}

	public static ParallelAction prl(AbstractAction action1, AbstractAction action2) {
		ParallelAction group = ParallelAction.obtain();
		return group.add(action1).add(action2);
	}

	public static ParallelAction prl(AbstractAction action1, AbstractAction action2, AbstractAction action3) {
		ParallelAction group = ParallelAction.obtain();
		return group.add(action1).add(action2).add(action3);
	}

	public static ParallelAction prl(AbstractAction action1, AbstractAction action2, AbstractAction action3, AbstractAction action4) {
		ParallelAction group = ParallelAction.obtain();
		return group.add(action1).add(action2).add(action3).add(action4);
	}

	public static ParallelAction prl(AbstractAction action1, AbstractAction action2, AbstractAction action3, AbstractAction action4, AbstractAction action5) {
		ParallelAction group = ParallelAction.obtain();
		return group.add(action1).add(action2).add(action3).add(action4).add(action5);
	}

	public static ParallelAction prl(AbstractAction action1, AbstractAction action2, AbstractAction action3, AbstractAction action4, AbstractAction action5, AbstractAction action6) {
		ParallelAction group = ParallelAction.obtain();
		return group.add(action1).add(action2).add(action3).add(action4).add(action5).add(action6);
	}

	public static ParallelAction prl(AbstractAction action1, AbstractAction action2, AbstractAction action3, AbstractAction action4, AbstractAction action5, AbstractAction action6, AbstractAction action7) {
		ParallelAction group = ParallelAction.obtain();
		return group.add(action1).add(action2).add(action3).add(action4).add(action5).add(action6).add(action7);
	}

	public static TweenParallelAction tprl(AbstractTweenAction action1, AbstractTweenAction action2) {
		return TweenParallelAction.obtain(action1, action2);
	}

	public static TweenParallelAction tprl(AbstractTweenAction action1, AbstractTweenAction action2, AbstractTweenAction action3) {
		return tprl(tprl(action1, action2), action3);
	}

	public static TweenParallelAction tprl(AbstractTweenAction action1, AbstractTweenAction action2, AbstractTweenAction action3, AbstractTweenAction action4) {
		return tprl(tprl(action1, action2, action3), action4);
	}

	public static TweenParallelAction tprl(AbstractTweenAction action1, AbstractTweenAction action2, AbstractTweenAction action3, AbstractTweenAction action4, AbstractTweenAction action5) {
		return tprl(tprl(action1, action2, action3, action4), action5);
	}

	public static TweenParallelAction tprl(AbstractTweenAction action1, AbstractTweenAction action2, AbstractTweenAction action3, AbstractTweenAction action4, AbstractTweenAction action5, AbstractTweenAction action6) {
		return tprl(tprl(action1, action2, action3, action4, action5), action6);
	}

	public static TweenParallelAction tprl(AbstractTweenAction action1, AbstractTweenAction action2, AbstractTweenAction action3, AbstractTweenAction action4, AbstractTweenAction action5, AbstractTweenAction action6, AbstractTweenAction action7) {
		return tprl(tprl(action1, action2, action3, action4, action5, action6), action7);
	}

	public static AbstractAction delayed(AbstractAction action, float duration) {
		return seq(delay(duration), action);
	}

	public static AbstractTweenAction tdelayed(AbstractTweenAction action, float duration) {
		return tseq(delay(duration), action);
	}

	public static AbstractTweenAction tpaddedRight(AbstractTweenAction action, float duration) {
		return tseq(action, padding(action, 1, duration));
	}

	public static AbstractTweenAction tpaddedLeft(AbstractTweenAction action, float duration) {
		return tseq(padding(action, 0, duration), action);
	}

	//
	// idempotent instant actions
	//

	public static IdempotentAction show() {
		return IdempotentAction.obtain(visibleSetter, Boolean.TRUE);
	}

	public static IdempotentAction hide() {
		return IdempotentAction.obtain(visibleSetter, Boolean.FALSE);
	}

	public static IdempotentAction touchableTo(Touchable touchable) {
		return IdempotentAction.obtain(touchableSetter, touchable);
	}

	public static IdempotentAction normalizeRotation() {
		return IdempotentAction.obtain(rotationNormalizer, null);
	}

	public static IdempotentAction runIdempotent(IdempotentDelegate delegate, Object data) {
		return IdempotentAction.obtain(delegate, data);
	}

	public static IdempotentAction runIdempotent(IdempotentDelegate delegate) {
		return runIdempotent(delegate, null);
	}

	public static IdempotentAction runIdempotent(Runnable delegate) {
		return runIdempotent(idempotentRunner, delegate);
	}

	//
	// one-shot instant actions
	//

	public static OneShotAction removeActor(Actor actorToRemove) {
		return OneShotAction.obtain(actorRemover, actorToRemove);
	}

	public static OneShotAction removeActor() {
		return removeActor(null);
	}

	public static OneShotAction run(Delegate delegate, Object data) {
		return OneShotAction.obtain(delegate, data);
	}

	public static OneShotAction run(Delegate delegate) {
		return run(delegate, null);
	}

	public static OneShotAction run(Runnable delegate) {
		return run(oneShotRunner, delegate);
	}

	//
	// wrappers
	//

	public static LazyAction lazy(LazyAction.Generator generator, Object data) {
		return LazyAction.obtain(generator, data);
	}

	public static LazyAction lazy(LazyAction.Generator generator) {
		return lazy(generator, null);
	}

	public static RepeatAction repeat(AbstractAction action, int repeatLimit) {
		return RepeatAction.obtain(action, repeatLimit);
	}

	public static RepeatAction repeatForever(AbstractAction action) {
		return repeat(action, Integer.MAX_VALUE);
	}

	public static TimeScaleAction timeScale(AbstractAction action, float scale) {
		return TimeScaleAction.obtain(action, scale);
	}

	public static EaseAction ease(AbstractAction action, Easing easing) {
		return EaseAction.obtain(action, easing);
	}

	//
	// private members
	//

	private Actions() { // sealed
	}

	public static final Color tmpColor = new Color();
	public static final FloatArray tmpFloatArray = new FloatArray(4);

	// idempotent delegates

	private static final IdempotentDelegate idempotentRunner = new IdempotentDelegate() {
		@Override
		public void run(Actor actor, Object data) {
			Runnable runnable = (Runnable) data;
			runnable.run();
		}
	};

	private static final IdempotentDelegate rotationNormalizer = new IdempotentDelegate() {
		@Override
		public void run(Actor actor, Object data) {
			float angle = actor.getRotation();

			while (angle <= -180)
				angle += 360;
			while (angle >= 180)
				angle -= 360;

			actor.setRotation(angle);
		}
	};

	private static final IdempotentDelegate visibleSetter = new IdempotentDelegate() {
		@Override
		public void run(Actor actor, Object data) {
			actor.setVisible(data == Boolean.TRUE);
		}
	};

	private static final IdempotentDelegate touchableSetter = new IdempotentDelegate() {
		@Override
		public void run(Actor actor, Object data) {
			actor.setTouchable((Touchable) data);
		}
	};

	//
	// stateful action delegates
	//

	private static final Delegate oneShotRunner = new Delegate() {
		@Override
		public void run(Actor actor, Object data) {
			Runnable runnable = (Runnable) data;
			runnable.run();
		}
	};

	private static final Delegate actorRemover = new Delegate() {
		@Override
		public void run(Actor actor, Object data) {
			PayloadRunnable pr = PayloadRunnable.obtain(actorRemoverHelper);
			pr.obj0 = (data != null ? ((Actor) data) : actor);
			Gdx.app.postRunnable(pr);
		}
	};

	private static final PayloadRunnable.Delegate actorRemoverHelper = new PayloadRunnable.Delegate() {
		@Override
		public void run(PayloadRunnable data) {
			Actor actorToRemove = (Actor) data.obj0;
			actorToRemove.remove();
		}
	};
}
