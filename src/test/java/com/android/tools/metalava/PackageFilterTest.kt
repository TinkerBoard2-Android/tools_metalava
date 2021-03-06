/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tools.metalava

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PackageFilterTest {
    @Test
    fun testExact() {
        val filter = PackageFilter()
        filter.addPackages("foo.bar:bar.baz")
        assertThat(filter.matches("foo.bar")).isTrue()
        assertThat(filter.matches("bar.baz")).isTrue()
        assertThat(filter.matches("foo.bar.baz")).isFalse()
        assertThat(filter.matches("foo")).isFalse()
        assertThat(filter.matches("foo.barf")).isFalse()
    }

    @Test
    fun testWildcard() {
        val filter = PackageFilter()
        filter.addPackages("foo.bar*:bar.baz.*")
        assertThat(filter.matches("foo.bar")).isTrue()
        assertThat(filter.matches("foo.bars")).isTrue()
        assertThat(filter.matches("foo.bar.baz")).isTrue()
        assertThat(filter.matches("bar.baz")).isTrue() // different from doclava behavior
        assertThat(filter.matches("bar.bazz")).isFalse()
        assertThat(filter.matches("foo.bar.baz")).isTrue()
        assertThat(filter.matches("foo")).isFalse()
    }

    @Test
    fun testBoth() {
        val filter = PackageFilter()
        filter.addPackages("foo.bar:foo.bar.*")
        assertThat(filter.matches("foo.bar")).isTrue()
        assertThat(filter.matches("foo.bar.sub")).isTrue()
        assertThat(filter.matches("foo.bar.sub.sub")).isTrue()
        assertThat(filter.matches("foo.bars")).isFalse()
        assertThat(filter.matches("foo")).isFalse()
    }

    @Test
    fun testImplicit() {
        val filter = PackageFilter()
        filter.addPackages("foo.bar.*")
        assertThat(filter.matches("foo.bar")).isTrue()
        assertThat(filter.matches("foo.bar.sub")).isTrue()
        assertThat(filter.matches("foo.bar.sub.sub")).isTrue()
        assertThat(filter.matches("foo.bars")).isFalse()
        assertThat(filter.matches("foo")).isFalse()
    }
}