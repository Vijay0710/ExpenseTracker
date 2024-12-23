package com.eyeshield.expensetracker.data.local.database

import android.content.Context
import androidx.room.Room
import com.eyeshield.expensetracker.data.local.dao.CreditAccountDao
import com.eyeshield.expensetracker.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): ExpenseTrackDatabase {
        return Room.databaseBuilder(
            appContext,
            ExpenseTrackDatabase::class.java,
            "expensetracker.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providesTransactionDao(database: ExpenseTrackDatabase): TransactionDao {
        return database.TransactionDao()
    }

    @Provides
    fun providesCreditAccountDao(database: ExpenseTrackDatabase): CreditAccountDao {
        return database.CreditAccountDao()
    }

}