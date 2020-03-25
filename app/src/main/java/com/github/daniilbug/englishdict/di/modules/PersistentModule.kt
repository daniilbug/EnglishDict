package com.github.daniilbug.englishdict.di.modules

import android.content.Context
import androidx.room.Room
import com.github.daniilbug.englishdict.model.CachedDictionaryRepository
import com.github.daniilbug.englishdict.model.DictionaryRepository
import com.github.daniilbug.englishdict.model.SavedDictionaryRepository
import com.github.daniilbug.englishdict.model.persistent.SavedDictionaryDAO
import com.github.daniilbug.englishdict.model.persistent.SavedDictionaryDb
import com.github.daniilbug.englishdict.model.persistent.SavedDictionaryRoomRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@Module(includes = [NetworkModule::class])
class PersistentModule(private val context: Context) {

    @Provides
    fun savedDictionaryDb(): SavedDictionaryDb {
        return Room.databaseBuilder(context, SavedDictionaryDb::class.java, "saved_dict.db").build()
    }

    @Provides
    fun savedDictionaryDAO(db: SavedDictionaryDb): SavedDictionaryDAO {
        return db.savedDictionaryDao()
    }

    @Provides
    fun savedDictionaryRepository(dao: SavedDictionaryDAO, apiRepository: DictionaryRepository): SavedDictionaryRepository {
        return CachedDictionaryRepository(
            persistence = SavedDictionaryRoomRepository(dao),
            api = apiRepository
        )
    }
}