package com.github.daniilbug.englishdict.model.persistent

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedDictionaryWord::class, SavedDictionaryDefinition::class, SavedImage::class], version = 1)
abstract class SavedDictionaryDb: RoomDatabase() {
    abstract fun savedDictionaryDao(): SavedDictionaryDAO
}