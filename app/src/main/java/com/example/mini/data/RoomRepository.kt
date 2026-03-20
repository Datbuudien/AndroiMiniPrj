package com.example.mini.data

import com.example.mini.model.Room

object RoomRepository {
    private val rooms = mutableListOf<Room>()

    init {
        // Dữ liệu mẫu
        rooms.add(Room("101", "Phòng 101", 1500000.0, false))
        rooms.add(Room("102", "Phòng 102", 2000000.0, true, "Nguyễn Văn A", "0909123456"))
        rooms.add(Room("103", "Phòng 103", 1800000.0, false))
    }

    fun getAllRooms(): List<Room> = rooms

    fun getRoomById(id: String): Room? = rooms.find { it.id == id }

    fun addRoom(room: Room) {
        rooms.add(room)
    }

    fun updateRoom(updatedRoom: Room) {
        val index = rooms.indexOfFirst { it.id == updatedRoom.id }
        if (index != -1) {
            rooms[index] = updatedRoom // Trong thực tế List lưu tham chiếu object nên có thể cập nhật trực tiếp, nhưng hàm này để rõ ràng logic
        }
    }

    fun deleteRoom(room: Room) {
        rooms.remove(room)
    }

    fun isIdExists(id: String): Boolean {
        return rooms.any { it.id == id }
    }
}

