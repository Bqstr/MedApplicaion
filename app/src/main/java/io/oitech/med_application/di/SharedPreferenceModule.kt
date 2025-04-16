package io.oitech.med_application.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.oitech.med_application.utils.FirstLaunchManager
import io.oitech.med_application.utils.UIdManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferenceModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun provideFirstLaunchManager(sharedPreferences: SharedPreferences): FirstLaunchManager {
        return FirstLaunchManager(sharedPreferences)
    }


    @Provides
    @Singleton
    fun provideUIdManager(sharedPreferences: SharedPreferences): UIdManager {
        return UIdManager(sharedPreferences)
    }


}