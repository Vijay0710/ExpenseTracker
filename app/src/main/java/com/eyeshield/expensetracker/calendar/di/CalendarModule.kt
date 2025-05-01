package com.eyeshield.expensetracker.calendar.di

import com.eyeshield.expensetracker.calendar.CustomCalendar
import com.eyeshield.expensetracker.calendar.CustomCalendarImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Calendar
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalendarModule {

    @Provides
    @Singleton
    fun provideCustomCalendar(calendar: Calendar): CustomCalendar {
        return CustomCalendarImpl(calendar)
    }

    @Provides
    @Singleton
    fun provideActualCalendarInstance(): Calendar {
        return Calendar.getInstance()
    }
}