/*
 * Copyright 2018 Google LLC
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
 */

package com.example.androidthings.endtoend.companion.device

import androidx.lifecycle.ViewModel
import com.example.androidthings.endtoend.companion.auth.AuthProvider
import com.example.androidthings.endtoend.companion.data.ToggleCommand
import com.example.androidthings.endtoend.companion.domain.LoadGizmoUseCase
import com.example.androidthings.endtoend.companion.domain.SendToggleCommandUseCase
import com.example.androidthings.endtoend.shared.data.model.Toggle

class GizmoDetailViewModel(
    private val authProvider: AuthProvider,
    private val loadGizmoUseCase: LoadGizmoUseCase,
    private val sendToggleCommandUseCase: SendToggleCommandUseCase
) : ViewModel() {

    // We can't load until we have a gizmo ID, so use this to check.
    private var gizmoId: String? = null

    val gizmoLiveData = loadGizmoUseCase.observe()

    fun setGizmoId(gizmoId: String) {
        if (this.gizmoId != gizmoId) {
            this.gizmoId = gizmoId
            loadGizmoUseCase.execute(gizmoId)
        }
    }

    fun onToggleClicked(toggle: Toggle) {
        val gizmoId = gizmoId ?: return
        val user = authProvider.userLiveData.value ?: return
        // Send toggle command
        sendToggleCommandUseCase.execute(
            ToggleCommand(
                user.uid, gizmoId, toggle.id, !toggle.on, System.currentTimeMillis()
            )
        )
    }
}
