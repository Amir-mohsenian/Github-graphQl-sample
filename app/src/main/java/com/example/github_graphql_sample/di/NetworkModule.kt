package com.example.github_graphql_sample.di

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCache
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.example.github_graphql_sample.data.GithubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideApolloClient(
        okHttpClient: OkHttpClient,
        normalizedCache: NormalizedCacheFactory<LruNormalizedCache>
    ): ApolloClient {
        return ApolloClient.builder()
            .serverUrl("https://api.github.com/graphql")
            .okHttpClient(okHttpClient)
            .normalizedCache(normalizedCache).build()
    }

    @Singleton
    @Provides
    fun provideLruCache(@ApplicationContext context: Context)
            : NormalizedCacheFactory<LruNormalizedCache> {
        return LruNormalizedCacheFactory(
            EvictionPolicy.builder().maxSizeBytes(10485760).build()
        ).chain(SqlNormalizedCacheFactory(context))
    }

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader(
                "Authorization", "bearer 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"
            )
                .build()
            chain.proceed(request)
        }).addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
    }

    @Singleton
    @Provides
    fun provideRepositoryDataSource(apolloClient: ApolloClient): GithubRepository {
        return GithubRepository(apolloClient)
    }
}

