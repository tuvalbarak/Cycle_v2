package com.tdp.cycle.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.tdp.cycle.local.CycleDB
import com.tdp.cycle.remote.ICycleService
import com.tdp.cycle.remote.IMapsService
import com.tdp.cycle.remote.IWeatherService
import com.tdp.cycle.repositories.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideBaseUrlGoogle() = GOOGLE_BASE_URL

    @Provides
    fun provideBaseUrlWeather() = WEATHER_BASE_URL

    @Singleton
    @Provides
    fun provideRetrofitGoogle(client: OkHttpClient): IMapsService {
        return Retrofit.Builder()
            .baseUrl(GOOGLE_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IMapsService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofitWeather(client: OkHttpClient): IWeatherService {
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IWeatherService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofitCycle(client: OkHttpClient): ICycleService {
        return Retrofit.Builder()
            .baseUrl(CYCLE_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ICycleService::class.java)
    }

    @Singleton
    @Provides
    fun provideDefaultOkhttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

//        val authInterceptor = AuthInterceptor(apiSettings, sharedPreferences, tokenApi)

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(logging)
//            .addInterceptor(authInterceptor)
        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideMapsRepository(mapsService: IMapsService): MapsRepository = MapsRepository(mapsService)

    @Singleton
    @Provides
    fun provideWeatherRepository(weatherService: IWeatherService): WeatherRepository = WeatherRepository(weatherService)

    @Singleton
    @Provides
    fun provideCycleRepository(cycleService: ICycleService): CycleRepository = CycleRepository(cycleService)

    @Singleton
    @Provides
    fun provideUserRepository(db: CycleDB, sharedPreferences: SharedPreferences, cycleService: ICycleService): UserRepository =
        UserRepository(db, sharedPreferences, cycleService)

    @Singleton
    @Provides
    fun provideChargingStationsRepository(): ChargingStationsRepository = ChargingStationsRepository()

    @Singleton
    @Provides
    fun provideElectricVehiclesRepository(): ElectricVehiclesRepository = ElectricVehiclesRepository()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "User",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Singleton
    @Provides
    fun provideCycleDB(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        CycleDB::class.java,
        DB_NAME
    ).build()

    companion object {
        private const val CYCLE_BASE_URL = "https://cycle-shenkar-server.herokuapp.com/api/v1/"
        private const val GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/"
        private const val WEATHER_BASE_URL = "https://api.weatherapi.com/v1/"
        private const val DB_NAME = "cycle_db"
        private const val NETWORK_TIMEOUT = 30L
    }

}