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

package com.niyaj.domain.expense

import com.niyaj.common.result.ValidationResult
import com.niyaj.common.tags.ExpenseTestTags.EXPENSE_NAME_EMPTY_ERROR
import com.niyaj.common.tags.ExpenseTestTags.EXPENSE_NAME_LENGTH_ERROR
import javax.inject.Inject

class ValidateExpenseNameUseCase @Inject constructor() {
    operator fun invoke(expenseName: String): ValidationResult {
        if (expenseName.isEmpty()) {
            return ValidationResult(
                successful = false,
                errorMessage = EXPENSE_NAME_EMPTY_ERROR,
            )
        }

        if (expenseName.length < 3) {
            return ValidationResult(
                successful = false,
                errorMessage = EXPENSE_NAME_LENGTH_ERROR,
            )
        }

        return ValidationResult(true)
    }
}