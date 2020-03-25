package com.github.daniilbug.englishdict.model.persistent

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SavedDictionaryDAO {

    @Query("SELECT * FROM saved_dictionary WHERE LOWER(word) LIKE LOWER(:query) ORDER BY time DESC")
    abstract fun getSavedWords(query: String = ""): Flow<List<SavedDictionaryWord>>

    @Query("SELECT * FROM definitions WHERE wordId = :wordId")
    abstract fun getDefinitionsForWordId(wordId: Long): Flow<List<SavedDictionaryDefinition>>

    @Query("SELECT * FROM images WHERE wordId = :wordId")
    abstract fun getImagesForWordId(wordId: Long): Flow<List<SavedImage>>

    @Query("SELECT * FROM saved_dictionary WHERE LOWER(word) = LOWER(:word)")
    abstract fun getWord(word: String): Flow<SavedDictionaryWord?>

    @Insert
    abstract suspend fun addWord(word: SavedDictionaryWord): Long

    @Insert
    abstract suspend fun addDefinitionForWord(definition: SavedDictionaryDefinition): Long

    @Insert
    abstract suspend fun addImageForWord(image: SavedImage): Long

    @Query("DELETE FROM saved_dictionary WHERE id = :id")
    abstract suspend fun deleteWordById(id: Long)
}