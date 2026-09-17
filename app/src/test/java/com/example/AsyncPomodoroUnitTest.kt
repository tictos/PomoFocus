package com.example

import com.example.ui.viewmodel.ProductivityInsight
import com.example.ui.viewmodel.SyncState
import com.example.ui.viewmodel.UiEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AsyncPomodoroUnitTest {

    @Test
    fun testSyncStateHierarchy() {
        val idleState: SyncState = SyncState.Idle
        val syncingState: SyncState = SyncState.Syncing("Chiffrement...", 0.45f)
        val successState: SyncState = SyncState.Success("Succès", 123456789L)
        val errorState: SyncState = SyncState.Error("Erreur réseau")

        assertTrue(idleState is SyncState.Idle)
        assertTrue(syncingState is SyncState.Syncing)
        assertEquals(0.45f, (syncingState as SyncState.Syncing).progress, 0.001f)
        assertTrue(successState is SyncState.Success)
        assertTrue(errorState is SyncState.Error)
    }

    @Test
    fun testProductivityInsightModel() {
        val insight = ProductivityInsight(
            id = "ins_test",
            title = "Pic de clarté matinale",
            description = "Taux de complétion optimal à 98%",
            impact = "+35% de focus",
            category = "Neuro-Focus",
            confidenceScore = 95
        )

        assertEquals("ins_test", insight.id)
        assertEquals(95, insight.confidenceScore)
        assertNotNull(insight.impact)
    }

    @Test
    fun testUiEvents() {
        val toastEvent = UiEvent.ShowToast("Mission créée")
        val snackbarEvent = UiEvent.ShowSnackbar("Synchronisation Cloud réussie", "OK")

        assertEquals("Mission créée", toastEvent.message)
        assertEquals("Synchronisation Cloud réussie", snackbarEvent.message)
        assertEquals("OK", snackbarEvent.actionLabel)
    }
}
