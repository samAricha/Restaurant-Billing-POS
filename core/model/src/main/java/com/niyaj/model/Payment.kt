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

package com.niyaj.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Payment(
    val paymentId: Int,

    val employeeId: Int,

    val paymentAmount: String = "",

    val paymentDate: String = "",

    val paymentType: PaymentType = PaymentType.Advanced,

    val paymentMode: PaymentMode = PaymentMode.Cash,

    val paymentNote: String = "",

    val createdAt: Long = System.currentTimeMillis(),

    val updatedAt: Long? = null,
)

fun List<Payment>.searchPayment(searchText: String): List<Payment> {
    return if (searchText.isNotEmpty()) {
        this.filter {
            it.paymentAmount.contains(searchText, true) ||
                it.paymentType.name.contains(searchText, true) ||
                it.paymentDate.contains(searchText, true) ||
                it.paymentMode.name.contains(searchText, true) ||
                it.paymentNote.contains(searchText, true)
        }
    } else {
        this
    }
}

fun Payment.filterPayment(searchText: String): Boolean {
    return if (searchText.isNotEmpty()) {
        this.paymentAmount.contains(searchText, true) ||
            this.paymentType.name.contains(searchText, true) ||
            this.paymentDate.contains(searchText, true) ||
            this.paymentMode.name.contains(searchText, true) ||
            this.paymentNote.contains(searchText, true)
    } else {
        true
    }
}
