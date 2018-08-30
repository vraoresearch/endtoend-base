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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.androidthings.endtoend.companion.R
import com.example.androidthings.endtoend.companion.ViewModelFactory
import com.example.androidthings.endtoend.companion.data.Gizmo
import com.example.androidthings.endtoend.shared.domain.Result
import com.example.androidthings.endtoend.shared.domain.Result.Loading
import com.example.androidthings.endtoend.shared.domain.successOr
import kotlinx.android.synthetic.main.fragment_gizmo_detail.gizmo_name
import kotlinx.android.synthetic.main.fragment_gizmo_detail.gizmo_toggles
import kotlinx.android.synthetic.main.fragment_gizmo_detail.progress

/** Displays information about a Gizmo and provides ways to interact with the Gizmo's state. */
class GizmoDetailFragment : Fragment() {

    private lateinit var gizmoDetailViewModel: GizmoDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gizmo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gizmoDetailViewModel = ViewModelProviders.of(this, ViewModelFactory.instance)
            .get(GizmoDetailViewModel::class.java)
            .apply {
                setGizmoId(GizmoDetailFragmentArgs.fromBundle(arguments).gizmoId)
            }

        gizmoDetailViewModel.gizmoLiveData.observe(this, Observer { result -> bindGizmo(result) })
    }

    private fun bindGizmo(result: Result<Gizmo?>) {
        val gizmo = result.successOr(null)
        for (v in listOf(gizmo_name, gizmo_toggles)) {
            v.visibility = if (gizmo == null) View.GONE else View.VISIBLE
        }
        gizmo?.let {
            gizmo_name.text = it.displayName
            gizmo_toggles.text = it.toggles.joinToString(separator = "\n") { toggle ->
                "${toggle.displayName} is ${if (toggle.isOn) "ON" else "OFF"}"
            }
        }

        progress.visibility = if (result is Loading) View.VISIBLE else View.GONE
    }
}
