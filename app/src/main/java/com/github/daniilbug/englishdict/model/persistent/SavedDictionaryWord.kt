package com.github.daniilbug.englishdict.model.persistent

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "saved_dictionary",
    indices = [Index("word", name = "word", unique = true)]
)
class SavedDictionaryWord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val word: String,
    val time: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "definitions",
    foreignKeys = [ForeignKey(entity = SavedDictionaryWord::class, parentColumns = ["id"], childColumns = ["wordId"], onDelete = CASCADE)]
)
class SavedDictionaryDefinition(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val wordId: Long,
    val definition: String
)

@Entity(
    tableName = "images",
    foreignKeys = [ForeignKey(entity = SavedDictionaryWord::class, parentColumns = ["id"], childColumns = ["wordId"], onDelete = CASCADE)]
)
class SavedImage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val wordId: Long,
    val imageUrl: String
)



