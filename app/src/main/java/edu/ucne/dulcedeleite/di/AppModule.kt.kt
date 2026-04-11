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

    @Provides
    @Singleton
    fun provideApi(moshi: Moshi): DulceDeleiteApi {
        return Retrofit.Builder()
            .baseUrl("http://DulceDeleite.somee.com/") // Tu URL de Somee
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DulceDeleiteApi::class.java)
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
}