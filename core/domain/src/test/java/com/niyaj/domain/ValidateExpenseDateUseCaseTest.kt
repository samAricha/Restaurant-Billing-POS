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

package com.niyaj.domain

import com.niyaj.common.tags.ExpenseTestTags.EXPENSE_DATE_EMPTY_ERROR
import com.niyaj.domain.expense.ValidateExpenseDateUseCase
import com.niyaj.testing.util.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import kotlin.test.Test

class ValidateExpenseDateUseCaseTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()
    val useCase = ValidateExpenseDateUseCase()

    @Test
    fun `validate expense date with empty date`() {
        val result = useCase("")
        assert(result.successful.not())
        assertEquals(EXPENSE_DATE_EMPTY_ERROR, result.errorMessage)
    }

    @Test
    fun `validate expense date with valid date`() {
        val result = useCase("2024-01-01")
        assert(result.successful)
        assertNull(result.errorMessage)
    }
}
