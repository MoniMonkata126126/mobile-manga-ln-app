package com.example.manga_ln_app.presentation.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.manga_ln_app.domain.model.ListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.manga_ln_app.domain.model.Type
import com.example.manga_ln_app.domain.repository.ContentRepository
import com.example.manga_ln_app.domain.repository.CredentialsStorage
import com.example.manga_ln_app.domain.repository.Role
import com.example.manga_ln_app.presentation.ContentController
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    private val contentController: ContentController,
    private val credentialsStorage: CredentialsStorage,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private var cachedManga = emptyList<ListItem.Content>()
    private var cachedLightNovels = emptyList<ListItem.Content>()
    private var searchJob: Job? = null
    private var observeMangaJob: Job? = null
    private var observeLightNovelsJob: Job? = null

    private val _state = MutableStateFlow(HomePageState(userRole = Role.valueOf(savedStateHandle["role"] ?: "READER")))
    val state = _state
        .onStart {
            if(cachedManga.isEmpty()) {
                observeManga()
            }
            observeSearchQuery()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: HomePageAction) {
        when (action) {
            is HomePageAction.OnContentClick -> {
                contentController.changeContent(action.content)
            }

            is HomePageAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }

            is HomePageAction.OnTabSelected -> {
                _state.update {
                    it.copy(
                        selectedTabIndex = action.index,
                        searchResultsM = if(action.index == 0) {
                            if(it.searchQuery.isBlank()) cachedManga else it.searchResultsM
                        } else emptyList(),
                        searchResultsLN = if(action.index == 1) {
                            if(it.searchQuery.isBlank()) cachedLightNovels else it.searchResultsLN
                        } else emptyList(),
                        searchQuery = ""
                    )
                }
                if (action.index == 1 && cachedLightNovels.isEmpty()) {
                    observeLightNovels()
                }
            }

            HomePageAction.OnLogout -> {
                credentialsStorage.saveRole(null)
                credentialsStorage.saveToken(null)
                credentialsStorage.saveUsername(null)
            }
        }
    }

    private fun observeManga() {
        observeMangaJob?.cancel()
        observeMangaJob = contentRepository
            .getContent(Type.MANGA)
            .onEach { manga ->
                cachedManga = manga
                _state.update { it.copy(
                    searchResultsM = manga,
                    isLoading = false
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeLightNovels() {
        observeLightNovelsJob?.cancel()
        observeLightNovelsJob = contentRepository
            .getContent(Type.LN)
            .onEach { ln ->
                cachedLightNovels = ln
                _state.update { it.copy(
                    searchResultsLN = ln,
                    isLoading = false
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeSearchQuery() {
        state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach { query ->
                when {
                    query.isBlank() -> {
                        _state.update {
                            it.copy(
                                errorMessage = null,
                                searchResultsM = if(it.selectedTabIndex == 0) cachedManga else emptyList(),
                                searchResultsLN = if(it.selectedTabIndex == 1) cachedLightNovels else emptyList()
                            )
                        }
                    }

                    query.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = searchContent(query)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchContent(query: String) = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        contentRepository
            .searchContent(query, Type.fromInt(state.value.selectedTabIndex) ?: Type.MANGA)
            .onSuccess { searchResults ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        searchResultsM = if(state.value.selectedTabIndex == 0) searchResults else emptyList(),
                        searchResultsLN = if(state.value.selectedTabIndex == 1) searchResults else emptyList()
                    )
                }
            }
            .onFailure { error ->
                _state.update {
                    it.copy(
                        searchResultsM = if(state.value.selectedTabIndex == 0) emptyList() else it.searchResultsM,
                        searchResultsLN = if(state.value.selectedTabIndex == 1) emptyList() else it.searchResultsLN,
                        isLoading = false,
                        errorMessage = error.toString()
                    )
                }
            }
    }
}