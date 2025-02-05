package com.example.manga_ln_app.di

import com.example.manga_ln_app.data.local.InMemoryCredentialsStorage
import com.example.manga_ln_app.domain.repository.CredentialsStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CredentialsStorageModule {

    @Binds
    @Singleton
    abstract fun bindCredentialsStorage(
        inMemoryCredentialsStorage: InMemoryCredentialsStorage
    ): CredentialsStorage
}