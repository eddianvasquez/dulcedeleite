package edu.ucne.dulcedeleite.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.dulcedeleite.data.local.dao.DulceDeleiteDao
import edu.ucne.dulcedeleite.data.local.database.DulceDeleiteDatabase
import edu.ucne.dulcedeleite.data.remote.api.DulceDeleiteApi
import edu.ucne.dulcedeleite.data.remote.remotedatasource.DulceDeleiteRemoteDataSource
import edu.ucne.dulcedeleite.data.repository.PedidoRepositoryImpl
import edu.ucne.dulcedeleite.data.repository.ProductoRepositoryImpl
import edu.ucne.dulcedeleite.domain.repository.PedidoRepository
import edu.ucne.dulcedeleite.domain.repository.ProductoRepository

import edu.ucne.dulcedeleite.data.remote.AuthApi
import edu.ucne.dulcedeleite.data.remote.AuthRemoteDataSource
import edu.ucne.dulcedeleite.data.repository.AuthRepositoryImpl
import edu.ucne.dulcedeleite.domain.repository.AuthRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- 1. BASE DE DATOS LOCAL (ROOM) ---
    @Provides
    @Singleton
    fun provideDulceDeleiteDatabase(
        @ApplicationContext context: Context
    ): DulceDeleiteDatabase {
        return Room.databaseBuilder(
            context,
            DulceDeleiteDatabase::class.java,
            "dulce_deleite_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDulceDeleiteDao(database: DulceDeleiteDatabase): DulceDeleiteDao {
        return database.dulceDeleiteDao()
    }

    // --- 2. CONEXIÓN A INTERNET (RETROFIT & MOSHI) ---
    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    
    // --- AUTENTICACIÓN (TOKENS) ---
    @Provides
    @Singleton
    fun provideAuthTokenManager(@ApplicationContext context: Context): edu.ucne.dulcedeleite.data.local.datastore.AuthTokenManager {
        return edu.ucne.dulcedeleite.data.local.datastore.AuthTokenManager(context)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: edu.ucne.dulcedeleite.data.local.datastore.AuthTokenManager): edu.ucne.dulcedeleite.data.remote.AuthInterceptor {
        return edu.ucne.dulcedeleite.data.remote.AuthInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: edu.ucne.dulcedeleite.data.remote.AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://DulceDeleite.somee.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): DulceDeleiteApi {
        return retrofit.create(DulceDeleiteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    // --- 3. REPOSITORIOS ---
    @Provides
    @Singleton
    fun provideProductoRepository(
        localDataSource: DulceDeleiteDao,
        remoteDataSource: DulceDeleiteRemoteDataSource
    ): ProductoRepository {
        return ProductoRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Provides
    @Singleton
    fun providePedidoRepository(
        localDataSource: DulceDeleiteDao,
        remoteDataSource: DulceDeleiteRemoteDataSource
    ): PedidoRepository {
        return PedidoRepositoryImpl(localDataSource, remoteDataSource)
    }
    
    @Provides
    @Singleton
    fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository {
        return authRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideDireccionRepository(
        remoteDataSource: DulceDeleiteRemoteDataSource
    ): edu.ucne.dulcedeleite.domain.repository.DireccionRepository {
        return edu.ucne.dulcedeleite.data.repository.DireccionRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideMetodoPagoRepository(
        remoteDataSource: DulceDeleiteRemoteDataSource
    ): edu.ucne.dulcedeleite.domain.repository.MetodoPagoRepository {
        return edu.ucne.dulcedeleite.data.repository.MetodoPagoRepositoryImpl(remoteDataSource)
    }
}