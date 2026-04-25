package com.gash.vocab.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Persists user settings to SharedPreferences.
 */
class SettingsRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("gash_settings", Context.MODE_PRIVATE)
    private val gson = Gson()

    // ── Study limits ──────────────────────────────────────────────

    var newPerDay: Int
        get() = prefs.getInt("new_per_day", 10)
        set(value) = prefs.edit().putInt("new_per_day", value).apply()

    var reviewsPerDay: Int
        get() = prefs.getInt("reviews_per_day", 50)
        set(value) = prefs.edit().putInt("reviews_per_day", value).apply()

    // ── Review settings ───────────────────────────────────────────

    /** Learning steps in minutes, comma-separated (e.g. "1,10") */
    var learningSteps: String
        get() = prefs.getString("learning_steps", "1,10") ?: "1,10"
        set(value) = prefs.edit().putString("learning_steps", value).apply()

    /** Graduating interval in days */
    var graduatingInterval: Int
        get() = prefs.getInt("graduating_interval", 1)
        set(value) = prefs.edit().putInt("graduating_interval", value).apply()

    /** Easy interval in days */
    var easyInterval: Int
        get() = prefs.getInt("easy_interval", 4)
        set(value) = prefs.edit().putInt("easy_interval", value).apply()

    // ── POS category mapping ──────────────────────────────────────

    /**
     * Map from POS value → display category name.
     * Unmapped POS values fall into "Other".
     */
    var posCategoryMap: Map<String, String>
        get() {
            val json = prefs.getString("pos_category_map", null) ?: return defaultPosCategoryMap()
            val type = object : TypeToken<Map<String, String>>() {}.type
            return gson.fromJson(json, type)
        }
        set(value) {
            prefs.edit().putString("pos_category_map", gson.toJson(value)).apply()
        }

    fun getCategoryForPos(pos: String): String {
        return posCategoryMap[pos] ?: "Other"
    }

    companion object {
        fun defaultPosCategoryMap(): Map<String, String> = mapOf(
            "noun (f.)" to "Nouns",
            "noun (m.)" to "Nouns",
            "noun (m. pl.)" to "Nouns",
            "noun phrase (f.)" to "Nouns",
            "noun phrase (m.)" to "Nouns",
            "pronoun / noun (f.)" to "Nouns",
            "verb" to "Verbs",
            "verb form" to "Verbs",
            "verb phrase" to "Verbs",
            "verb phrase (passive)" to "Verbs",
            "verb + conjunction" to "Verbs",
            "adjective" to "Adjectives",
            "adjective / noun" to "Adjectives",
            "adverb" to "Adverbs",
            "adverbial phrase" to "Adverbs",
            "preposition" to "Other",
            "prepositional phrase" to "Other",
            "phrase" to "Phrases"
        )
    }
}
