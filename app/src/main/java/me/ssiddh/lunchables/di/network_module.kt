package me.ssiddh.lunchables.di

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import me.ssiddh.lunchables.R
import me.ssiddh.lunchables.data.repository.RestaurantSearchRepository
import me.ssiddh.lunchables.network.GooglePlacesApiService
import me.ssiddh.lunchables.utils.AuthenticationInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

val apiModule = module {
    fun provideGooglePlacesApi(retrofit: Retrofit): GooglePlacesApiService =
        retrofit.create(GooglePlacesApiService::class.java)

    single { provideGooglePlacesApi(get()) }
}

val repositoryModule = module {
    fun provideSearchRepository(googlePlacesApiService: GooglePlacesApiService) =
        RestaurantSearchRepository(googlePlacesApiService)

    single { provideSearchRepository(get()) }
}

val networkModule = module {
    fun provideCache(application: Application): Cache {
        return Cache(File(application.cacheDir, "http"), (10 * 1024 * 1024).toLong())
    }

    fun provideAuthenticationInterceptor(application: Application): AuthenticationInterceptor {
        val apiKey = application.resources.getString(R.string.google_maps_key)
        return AuthenticationInterceptor(apiKey)
    }

    fun provideHttpClient(
        cache: Cache,
        authenticationInterceptor: AuthenticationInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(authenticationInterceptor)
            .build()
    }

    fun providerMoshi(): Moshi =
        Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    fun provideRetrofit(moshi: Moshi, httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/api/place/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(httpClient)
        .build()

    single { provideCache(androidApplication()) }
    single { provideAuthenticationInterceptor(androidApplication()) }
    single { provideHttpClient(get(), get()) }
    single { providerMoshi() }
    single { provideRetrofit(get(), get()) }
}
