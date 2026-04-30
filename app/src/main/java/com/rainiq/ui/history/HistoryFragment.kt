package com.rainiq.ui.history

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rainiq.databinding.FragmentHistoryBinding
import com.rainiq.utils.animateCardEntrance

/**
 * HistoryFragment — Rainfall History Log (Screen 4)
 *
 * Phase 3C: Top summary card + RecyclerView with swipe-to-delete, filter bottom sheet.
 */
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HistoryAdapter
    private val mockEntries = mutableListOf(
        HistoryEntry("1", "24", "OCT", 45f, "Concrete", 382),
        HistoryEntry("2", "20", "OCT", 12f, "Concrete", 102),
        HistoryEntry("3", "15", "OCT", 85f, "Concrete", 720),
        HistoryEntry("4", "02", "OCT", 30f, "Concrete", 255)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        updateEmptyState()
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter(mockEntries) { entry, pos ->
            updateEmptyState()
            Snackbar.make(binding.root, "Entry deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {
                    mockEntries.add(pos, entry)
                    adapter.notifyItemInserted(pos)
                    updateEmptyState()
                }
                .show()
        }
        
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter
        
        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                adapter.removeAt(position)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                // Find the foreground view
                val foregroundView = (viewHolder as HistoryAdapter.HistoryViewHolder).binding.cardForeground
                
                // Only move the foreground
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                val foregroundView = (viewHolder as HistoryAdapter.HistoryViewHolder).binding.cardForeground
                getDefaultUIUtil().clearView(foregroundView)
            }
        }
        
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvHistory)
    }

    private fun updateEmptyState() {
        if (mockEntries.isEmpty()) {
            binding.rvHistory.visibility = View.GONE
            binding.layoutEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvHistory.visibility = View.VISIBLE
            binding.layoutEmptyState.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
