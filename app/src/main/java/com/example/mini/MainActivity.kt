package com.example.mini

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mini.adapter.RoomAdapter
import com.example.mini.data.RoomRepository
import com.example.mini.model.Room
import com.example.mini.ui.AddEditRoomActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoomAdapter

    private lateinit var tvTotal: TextView
    private lateinit var tvEmpty: TextView
    private lateinit var tvRented: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Stats Views
        tvTotal = findViewById(R.id.tvTotalCount)
        tvEmpty = findViewById(R.id.tvEmptyCount)
        tvRented = findViewById(R.id.tvRentedCount)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = RoomAdapter(
            rooms = RoomRepository.getAllRooms(),
            onClick = { room ->
                val intent = Intent(this, AddEditRoomActivity::class.java)
                intent.putExtra(AddEditRoomActivity.EXTRA_ROOM_ID, room.id)
                startActivity(intent)
            },
            onLongClick = { room -> showDeleteConfirmation(room) }
        )
        recyclerView.adapter = adapter

        // Setup Floating Action Button
        val fabAdd: ExtendedFloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditRoomActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning from Add/Edit screen
        adapter.updateData(RoomRepository.getAllRooms())
        updateStats()
    }

    private fun showDeleteConfirmation(room: Room) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_room_title))
            .setMessage(getString(R.string.delete_confirm_msg, room.name, room.id))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                RoomRepository.deleteRoom(room)
                adapter.updateData(RoomRepository.getAllRooms())
                updateStats()
                Toast.makeText(this, getString(R.string.msg_deleted), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.status_red, theme))
        }
        dialog.show()
    }

    private fun updateStats() {
        val rooms = RoomRepository.getAllRooms()
        tvTotal.text = rooms.size.toString()
        tvEmpty.text = rooms.count { !it.isRented }.toString()
        tvRented.text = rooms.count { it.isRented }.toString()
    }
}