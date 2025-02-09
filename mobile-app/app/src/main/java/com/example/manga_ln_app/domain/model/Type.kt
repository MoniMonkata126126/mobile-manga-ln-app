package com.example.manga_ln_app.domain.model

enum class Type {
    MANGA,
    LN;

    companion object {
        fun fromInt(value: Int): Type? {
            return entries.firstOrNull { it.ordinal == value }
        }
    }
}