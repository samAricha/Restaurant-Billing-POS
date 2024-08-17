/*
 * Copyright 2024 Sk Niyaj Ali
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.niyaj.domain.payment

import com.niyaj.common.tags.PaymentScreenTags.PAYMENT_NOTE_EMPTY
import com.niyaj.testing.util.MainDispatcherRule
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class ValidatePaymentNoteUseCaseTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()
    val useCase = ValidatePaymentNoteUseCase()

    @Test
    fun `given empty note with required return success`() {
        val result = useCase("", true)
        assertFalse(result.successful)
        assertEquals(PAYMENT_NOTE_EMPTY, result.errorMessage)
    }

    @Test
    fun `given empty note with not required return success`() {
        val result = useCase("")
        assert(result.successful)
        assertNull(result.errorMessage)
    }
}