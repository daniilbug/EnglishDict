package com.github.daniilbug.englishdict.model.persistent

import com.github.daniilbug.englishdict.model.DefinitionNotFoundError
import com.github.daniilbug.englishdict.model.DictionaryDefinition
import com.github.daniilbug.englishdict.model.SavedDictionaryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
@FlowPreview
class SavedDictionaryRoomRepository(private val dao: SavedDictionaryDAO): SavedDictionaryRepository {
    override fun getDefinitionsFor(word: String): Flow<DictionaryDefinition> {
        val wordFlow = dao.getWord(word)
            .map { savedWord -> savedWord?.id ?: throw DefinitionNotFoundError() }
        val definitionsFlow = wordFlow
            .flatMapConcat { id -> dao.getDefinitionsForWordId(id) }
            .map { definitions -> definitions.map(SavedDictionaryDefinition::definition) }
        val imagesFlow = wordFlow
            .flatMapConcat { id -> dao.getImagesForWordId(id) }
            .map { images -> images.map(SavedImage::imageUrl)}
        return definitionsFlow.zip(imagesFlow) { definitions, images ->
            DictionaryDefinition(word, definitions, images)
        }
    }

    override fun getSavedDefinitions(query: String) = dao.getSavedWords("${query.trim()}%").convert()

    override suspend fun addDefinition(word: DictionaryDefinition) {
        val wordId = dao.addWord(SavedDictionaryWord(word = word.word))
        val definitions =
            word.definitions.map { SavedDictionaryDefinition(wordId = wordId, definition = it) }
        for (definition in definitions) {
            dao.addDefinitionForWord(definition)
        }
        val images = word.imageUrls.map { SavedImage(wordId = wordId, imageUrl = it) }
        for (image in images) {
            dao.addImageForWord(image)
        }
    }

    override suspend fun deleteDefinition(word: String) {
        dao.getWord(word).collect { if (it != null) dao.deleteWordById(it.id) }
    }

    private fun Flow<List<SavedDictionaryWord>>.convert(): Flow<List<DictionaryDefinition>>  = map { words ->
        words.map { word ->
            val definitions = dao.getDefinitionsForWordId(word.id).first()
                .map(SavedDictionaryDefinition::definition)
            val images = dao.getImagesForWordId(word.id).first().map(SavedImage::imageUrl)
            DictionaryDefinition(word.word, definitions, images)
        }
    }
}