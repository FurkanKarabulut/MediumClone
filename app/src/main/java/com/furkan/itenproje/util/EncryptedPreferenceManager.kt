package com.furkan.itenproje.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.furkan.itenproje.R

class EncryptedPreferenceManager(
    context: Context,
    prefsName: String? = context.getString(R.string.app_name)
) {

    private val masterKey =
        MasterKey.Builder(context.applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

    private val sharedPreference =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            EncryptedSharedPreferences.create(
                context.applicationContext,
                "encrypted_$prefsName",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        } else {
            context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        }


    private val oldSharePref = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    init {
        // Check if the new ESP has been initialised
        if (oldSharePref.all.isNotEmpty()) {
            // Copy each item into the ESP
            oldSharePref.copyTo(sharedPreference)
            oldSharePref.clear()
        }
    }

    fun all(): MutableMap<String, *> = sharedPreference.all

    fun save(KEY_NAME: String, value: Int) = doSave(KEY_NAME, value)
    fun save(KEY_NAME: String, value: Long) = doSave(KEY_NAME, value)
    fun save(KEY_NAME: String, value: Float) = doSave(KEY_NAME, value)
    fun save(KEY_NAME: String, value: String) = doSave(KEY_NAME, value)
    fun save(KEY_NAME: String, status: Boolean) = doSave(KEY_NAME, status)

    private fun doSave(key: String, value: Any) {
        with(sharedPreference.edit()) {
            when (value) {
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
            }
            commit()
        }
    }

    fun putToStringSet(KEY_NAME: String, value: String) {
        val newSet = mutableSetOf(value)
        newSet.addAll(getValueStringSet(KEY_NAME))
        with(sharedPreference.edit()) {
            putStringSet(KEY_NAME, newSet)
            commit()
        }
    }

    fun getValueString(KEY_NAME: String, defValue: String? = null): String? =
        sharedPreference.getString(KEY_NAME, defValue)

    fun getValueStringSet(KEY_NAME: String, defValue: Set<String>? = null): Set<String> =
        sharedPreference.getStringSet(KEY_NAME, defValue) ?: emptySet()

    fun getValueInt(KEY_NAME: String): Int = sharedPreference.getInt(KEY_NAME, 0)
    fun getValueLong(KEY_NAME: String) = sharedPreference.getLong(KEY_NAME, 0)
    fun getValueBoolean(KEY_NAME: String) = sharedPreference.getBoolean(KEY_NAME, false)

    fun removeValue(KEY_NAME: String) {
        sharedPreference.remove(KEY_NAME)
    }

    fun removeValueFromStringSet(KEY_NAME: String, value: String) {
        val newSet = getValueStringSet(KEY_NAME = KEY_NAME, emptySet()).toMutableSet()
        newSet.remove(value)
        with(sharedPreference.edit()) {
            putStringSet(KEY_NAME, newSet)
            commit()
        }
    }

    fun clearSharedPreference() {
        sharedPreference.clear()
    }

    private fun SharedPreferences.copyTo(dest: SharedPreferences) {
        for (entry in all.entries) {
            val key = entry.key
            val value: Any? = entry.value
            dest.set(key, value)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value.toInt()) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value.toFloat()) }
            is Long -> edit { it.putLong(key, value.toLong()) }
            is Set<*> -> edit { it.putStringSet(key, value as Set<String>) }
            else -> {
                Log.e("Error", "SharedPreferences Unsupported Type: $value")
            }
        }
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    private fun SharedPreferences.clear() {
        edit { it.clear() }
    }

    private fun SharedPreferences.remove(key: String) {
        edit { it.remove(key) }
    }
}