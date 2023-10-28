/*
 * Copyright 2014-2023 Real Logic Limited.
 * Copyright 2016 Gil Tene
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.iamacat.optimizationsandtweaks.utils.agrona.hints;

import static java.lang.invoke.MethodType.methodType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import fr.iamacat.optimizationsandtweaks.utils.agrona.SystemUtil;

/**
 * This class captures possible hints that may be used by some
 * runtimes to improve code performance. It is intended to capture hinting
 * behaviours that are implemented in or anticipated to be spec'ed under the
 * {@link Thread} class in some Java SE versions, but missing in prior
 * versions.
 */
public final class ThreadHints {

    /**
     * Set this system property to true to disable {@link #onSpinWait()}.
     */
    public static final String DISABLE_ON_SPIN_WAIT_PROP_NAME = "org.agrona.hints.disable.onSpinWait";

    private static final MethodHandle ON_SPIN_WAIT_METHOD_HANDLE;

    static {
        MethodHandle methodHandle = null;

        if (!"true".equals(SystemUtil.getProperty(DISABLE_ON_SPIN_WAIT_PROP_NAME))) {
            try {
                final MethodHandles.Lookup lookup = MethodHandles.lookup();
                methodHandle = lookup.findStatic(Thread.class, "onSpinWait", methodType(void.class));
            } catch (final Exception ignore) {}
        }

        ON_SPIN_WAIT_METHOD_HANDLE = methodHandle;
    }

    private ThreadHints() {}

    /**
     * Indicates that the caller is momentarily unable to progress, until the
     * occurrence of one or more actions on the part of other activities. By
     * invoking this method within each iteration of a spin-wait loop construct,
     * the calling thread indicates to the runtime that it is busy-waiting. The runtime
     * may take action to improve the performance of invoking spin-wait loop constructions.
     */
    public static void onSpinWait() {
        // Call java.lang.Thread.onSpinWait() on Java SE versions that support it. Do nothing otherwise.
        // This should optimize away to either nothing or to an inlining of java.lang.Thread.onSpinWait()
        if (null != ON_SPIN_WAIT_METHOD_HANDLE) {
            try {
                ON_SPIN_WAIT_METHOD_HANDLE.invokeExact();
            } catch (final Throwable ignore) {}
        }
    }
}
