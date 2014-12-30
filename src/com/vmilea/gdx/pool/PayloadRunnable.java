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

package com.vmilea.gdx.pool;

// Poolable runnable with generic data fields
//
public final class PayloadRunnable extends PoolItem.Base implements Runnable {

	public interface Delegate { 
		void run(PayloadRunnable data);
	}
	
	public boolean autoRecycle = true;
	public long long0;
	public long long1;
	public long long2;
	public long long3;
	public Object obj0;
	public Object obj1;
	
	private Delegate delegate; // assume not pooled, not disposable
	
	
	public static final AltPool<PayloadRunnable> pool = new AltPool<PayloadRunnable>(PayloadRunnable.class) {
		protected PayloadRunnable newObject() {
			return new PayloadRunnable();
		}
	};
	
	public static PayloadRunnable obtain(Delegate delegate) {
		PayloadRunnable pr = pool.obtain();
		pr.delegate = delegate;
		return pr;
	}
	
	public static PayloadRunnable obtain(Delegate delegate, long long0) {
		PayloadRunnable pr = obtain(delegate);
		pr.long0 = long0;
		return pr;
	}
	
	public static PayloadRunnable obtain(Delegate delegate, long long0, long long1) {
		PayloadRunnable pr = obtain(delegate);
		pr.long0 = long0;
		pr.long1 = long1;
		return pr;
	}
	
	public static PayloadRunnable obtain(Delegate delegate, long long0, long long1, long long2) {
        PayloadRunnable pr = obtain(delegate);
		pr.long0 = long0;
		pr.long1 = long1;
		pr.long2 = long2;
		return pr;
	}
	
	public static PayloadRunnable obtain(Delegate delegate, long long0, long long1, long long2, long long3) {
        PayloadRunnable pr = obtain(delegate);
		pr.long0 = long0;
		pr.long1 = long1;
		pr.long2 = long2;
		pr.long3 = long3;
		return pr;
	}
	
	@Override
	public void run() {
		delegate.run(this);
		
		if (autoRecycle)
			recycle();
	}

	@Override
	public void reset() {
		long0 = 0;
		long1 = 0;
		long2 = 0;
		long3 = 0;
		obj0 = null;
		obj1 = null;
		delegate = null;
		autoRecycle = true;
	}
	
	private PayloadRunnable() {
	}
}
