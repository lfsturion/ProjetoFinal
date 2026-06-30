package com.example.financeapp.di

import com.example.financeapp.data.remote.api.AuthApi
import com.example.financeapp.data.remote.api.TransactionApi
import com.example.financeapp.data.local.datastore.TokenDataStore
import com.example.financeapp.data.remote.interceptor.AuthAuthenticator
import com.example.financeapp.data.remote.interceptor.AuthInterceptor
import com.example.financeapp.data.repository.AuthRepository
import com.example.financeapp.data.repository.AuthRepositoryImpl
import com.example.financeapp.data.repository.TransactionRepositoryImpl
import com.example.financeapp.data.session.SessionManager
import com.example.financeapp.domain.repository.TransactionRepository
import com.example.financeapp.domain.usecase.DeleteTransactionUseCase
import com.example.financeapp.domain.usecase.GetTransactionsUseCase
import com.example.financeapp.domain.usecase.InsertTransactionsUseCase
import com.example.financeapp.domain.usecase.LoginUseCase
import com.example.financeapp.domain.usecase.UpdateTransactionUseCase
import com.example.financeapp.presentation.SplashViewModel
import com.example.financeapp.ui.screens.home.HomeViewModel
import com.example.financeapp.ui.screens.home.LoginViewModel
import com.example.financeapp.ui.screens.settings.SettingsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://10.0.2.2/projetofinal/"

val appModule = module {

    //---------------------------------------
    // Logging
    //---------------------------------------

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    //---------------------------------------
    // Session
    //---------------------------------------

    single { TokenDataStore(androidContext()) }
    single { SessionManager(get()) }

    //---------------------------------------
    // Retrofit SEM autenticação
    //---------------------------------------

    single(named("plainOkHttp")) {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single(named("plainRetrofit")) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get(named("plainOkHttp")))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //---------------------------------------
    // AuthApi usa Retrofit SEM autenticação
    //---------------------------------------

    single<AuthApi> {
        get<Retrofit>(named("plainRetrofit"))
            .create(AuthApi::class.java)
    }

    //---------------------------------------
    // Authenticator
    //---------------------------------------

    single {
        AuthAuthenticator(
            authApi = get(),
            sessionManager = get()
        )
    }

    //---------------------------------------
    // Interceptor
    //---------------------------------------

    single {
        AuthInterceptor(
            sessionManager = get()
        )
    }

    //---------------------------------------
    // OkHttp autenticado
    //---------------------------------------

    single(named("authOkHttp")) {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .authenticator(get<AuthAuthenticator>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    //---------------------------------------
    // Retrofit autenticado
    //---------------------------------------

    single(named("authRetrofit")) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get(named("authOkHttp")))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //---------------------------------------
    // Transaction API
    //---------------------------------------

    single<TransactionApi> {
        get<Retrofit>(named("authRetrofit"))
            .create(TransactionApi::class.java)
    }

    //---------------------------------------
    // Repository
    //---------------------------------------

    single<TransactionRepository> {
        TransactionRepositoryImpl(get())
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            get(),
            get()
        )
    }

    //---------------------------------------
    // UseCases
    //---------------------------------------

    factory { GetTransactionsUseCase(get()) }
    factory { InsertTransactionsUseCase(get()) }
    factory { UpdateTransactionUseCase(get()) }
    factory { DeleteTransactionUseCase(get()) }
    factory { LoginUseCase(get()) }

    //---------------------------------------
    // ViewModels
    //---------------------------------------

    viewModel { HomeViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}