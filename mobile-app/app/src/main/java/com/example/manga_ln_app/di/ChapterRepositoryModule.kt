package com.example.manga_ln_app.di

import com.example.manga_ln_app.data.repository.ChapterRepositoryImpl
import com.example.manga_ln_app.domain.repository.ChapterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ChapterRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChapterRepository(
        chapterRepositoryImpl: ChapterRepositoryImpl
    ): ChapterRepository
}