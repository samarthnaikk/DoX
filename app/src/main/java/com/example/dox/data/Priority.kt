package com.example.dox.data

enum class Priority(val displayName: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");
    
    companion object {
        fun fromString(value: String?): Priority? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}
