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

public final class Easings {

	// ease in     : accelerating from zero velocity
	// ease out    : decelerating to zero velocity
	// ease in/out : accelerating until halfway, then decelerating
	
	public static final Easing linear = new Easing() {
		@Override
		public float get(float t) {
			return t;
		}
		
		@Override
		public Easing reversed() { return linear; }
	};
	
	public static final Easing easeInQuad = new Easing() {
		@Override
		public float get(float t) {
			return t * t;
		}
		
		@Override
		public Easing reversed() { return easeOutQuad; }
	};
	
	public static final Easing easeOutQuad = new Easing() {
		@Override
		public float get(float t) {
			return t * (2 - t);
		}
		
		@Override
		public Easing reversed() { return easeInQuad; }
	};
	
	public static final Easing easeInOutQuad = new Easing() {
		@Override
		public float get(float t) {
			if (t < 0.5f)
				return 2 * t * t;
			else
				return 2 * t * (2 - t) - 1;
		}
		
		@Override
		public Easing reversed() { return easeInOutQuad; }
	};
	
	public static final Easing easeInCubic = new Easing() {
		@Override
		public float get(float t) {
			return t * t * t;
		}
		
		@Override
		public Easing reversed() { return easeOutCubic; }
	};
	
	public static final Easing easeOutCubic = new Easing() {
		@Override
		public float get(float t) {
			t--;
			return 1 + t * t * t;
		}
		
		@Override
		public Easing reversed() { return easeInCubic; }
	};
	
	public static final Easing easeInOutCubic = new Easing() {
		@Override
		public float get(float t) {
			if (t < 0.5f)
				return 4 * t * t * t;
			else {
				t = 2 * t - 2;
				return 0.5f * (2 + t * t * t);
			}
		}
		
		@Override
		public Easing reversed() { return easeInOutCubic; }
	};
	
	public static final Easing easeInQuart = new Easing() {
		@Override
		public float get(float t) {
			return t * t * t * t;
		}
		
		@Override
		public Easing reversed() { return easeOutQuart; }
	};
	
	public static final Easing easeOutQuart = new Easing() {
		@Override
		public float get(float t) {
			t--;
			return 1 - t * t * t * t;
		}
		
		@Override
		public Easing reversed() { return easeInQuart; }
	};
	
	public static final Easing easeInOutQuart = new Easing() {
		@Override
		public float get(float t) {
			if (t < 0.5f)
				return 8 * t * t * t * t;
			else {
				t = 2 * t - 2;
				return 0.5f * (2 - t * t * t * t);
			}
		}
		
		@Override
		public Easing reversed() { return easeInOutQuart; }
	};
	
	public static final Easing easeInQuint = new Easing() {
		@Override
		public float get(float t) {
			return t * t * t * t * t;
		}
		
		@Override
		public Easing reversed() { return easeOutQuint; }
	};
	
	public static final Easing easeOutQuint = new Easing() {
		@Override
		public float get(float t) {
			t--;
			return 1 + t * t * t * t * t;
		}
		
		@Override
		public Easing reversed() { return easeInQuint; }
	};
	
	public static final Easing easeInOutQuint = new Easing() {
		@Override
		public float get(float t) {
			if (t < 0.5f)
				return 16 * t * t * t * t * t;
			else {
				t = 2 * t - 2;
				return 0.5f * (2 + t * t * t * t * t);
			}
		}
		
		@Override
		public Easing reversed() { return easeInOutQuint; }
	};
	
	public static final  Easing easeInSine = new Easing() {
		@Override
		public float get(float t) {
			return 1 - (float) Math.cos(t * 0.5 * Math.PI);
		}
		
		@Override
		public Easing reversed() { return easeOutSine; }
	};
	
	public static final Easing easeOutSine = new Easing() {
		@Override
		public float get(float t) {
			return (float) Math.sin(t * 0.5 * Math.PI);
		}
		
		@Override
		public Easing reversed() { return easeInSine; }
	};
	
	public static final Easing easeInOutSine = new Easing() {
		@Override
		public float get(float t) {
			return 0.5f * (1 - (float) Math.cos(t * Math.PI));
		}
		
		@Override
		public Easing reversed() { return easeInOutSine; }
	};
	
	public static final Easing easeInExpo = new Easing() {
		@Override
		public float get(float t) {
			return (float) Math.pow(2, 10 * (t - 1));
		}
		
		@Override
		public Easing reversed() { return easeOutExpo; }
	};
	
	public static final Easing easeOutExpo = new Easing() {
		@Override
		public float get(float t) {
			return 1 - (float) Math.pow(2, -10 * t);
		}
		
		@Override
		public Easing reversed() { return easeInExpo; }
	};
	
	public static final Easing easeInOutExpo = new Easing() {
		@Override
		public float get(float t) {
			if (t < 0.5f)
				return 0.5f * (float) Math.pow(2, 20 * t - 10);
			else {
				return 0.5f * (2 - (float) Math.pow(2, -20 * t + 10));
			}
		}
		
		@Override
		public Easing reversed() { return easeInOutExpo; }
	};
	
	public static final Easing easeInCirc = new Easing() {
		@Override
		public float get(float t) {
			return 1 - (float) Math.sqrt(1 - t * t);
		}
		
		@Override
		public Easing reversed() { return easeOutCirc; }
	};
	
	public static final Easing easeOutCirc = new Easing() {
		@Override
		public float get(float t) {
			return (float) Math.sqrt(t * (2 - t));
		}
		
		@Override
		public Easing reversed() { return easeInCirc; }
	};
	
	public static final Easing easeInOutCirc = new Easing() {
		@Override
		public float get(float t) {
			if (t < 0.5f)
				return 0.5f * (1 - (float) Math.sqrt(1 - 4 * t * t));
			else {
				return 0.5f * (1 + (float) Math.sqrt(t * (8 - 4 * t) - 3));
			}
		}
		
		@Override
		public Easing reversed() { return easeInOutCirc; }
	};
}
