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

// Check if user has requested an operation while in an illegal state. No recovery expected.
//
public final class StateCheck {

	public static void check(boolean condition, String message) {
		if (!condition) {
			throw new IllegalStateException(message);
		}
	}

	public static void fail(String format, Object... args) {
		throw new IllegalArgumentException(String.format(format, args));
	}

	private StateCheck() { // sealed
	}
}
