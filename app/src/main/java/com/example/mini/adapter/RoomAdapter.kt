package com.example.mini.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mini.model.Room
class RoomAdapter(
    private var rooms: List<Room>, // Changed from MutableList to List since Repository manages it
    private val onClick: (Room) -> Unit,
    private val onLongClick: (Room) -> Unit
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    // Method to update data
    fun updateData(newRooms: List<Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvId: TextView = view.findViewById(R.id.tvRoomId)
        val tvName: TextView = view.findViewById(R.id.tvRoomName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvTenant: TextView = view.findViewById(R.id.tvTenant)
        val layoutTenant: LinearLayout = view.findViewById(R.id.layoutTenant)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        val context = holder.itemView.context

        holder.tvId.text = room.id
        holder.tvName.text = room.name
        holder.tvPrice.text = context.getString(R.string.fmt_price, String.format("%,.0f", room.price))

        if (room.isRented) {
            holder.tvStatus.text = context.getString(R.string.status_rented)
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_red))

            holder.layoutTenant.visibility = View.VISIBLE
            holder.tvTenant.text = "${room.tenantName} - ${room.tenantPhone}"
        } else {
            holder.tvStatus.text = context.getString(R.string.status_empty)
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_green))

            holder.layoutTenant.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { onClick(room) }
        holder.itemView.setOnLongClickListener {
            onLongClick(room)
            true
        }
    }

    // validate if setStatusColor is still needed, if not remove or keep empty
    // Keeping it simple as per new design using text color
    override fun getItemCount() = rooms.size
}