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

@SuppressWarnings("rawtypes")
public interface PoolItem extends AltPool.Poolable {

	void setPool(com.badlogic.gdx.utils.Pool pool);
	
	com.badlogic.gdx.utils.Pool getPool();
	
	void recycle();
	
	@Override
	void reset();
	
	public static abstract class Base implements PoolItem {

		protected com.badlogic.gdx.utils.Pool pool;
		
		@Override
		public com.badlogic.gdx.utils.Pool getPool() {
			return pool;
		}
		
		@Override
		public void setPool(com.badlogic.gdx.utils.Pool pool) {
			this.pool = pool;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void recycle() {
			if (pool != null)
				pool.free(this);
		}
		
		public abstract void reset();
	}
}
