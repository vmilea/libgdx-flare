/*******************************************************************************
 * Copyright 2014 Valentin Milea
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

package com.vmilea.util;

// Library consistency checks. No recovery expected.
//
public final class Assert {

	public static class AssertFailed extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public AssertFailed() {
		}

		public AssertFailed(String message) {
			super(message);
		}
	}

	public static void check(boolean condition) {
		if (!condition) {
			throw new AssertFailed();
		}
	}

	public static void fail(String format, Object... args) {
		throw new AssertFailed(String.format(format, args));
	}

	private Assert() { // sealed
	}
}
