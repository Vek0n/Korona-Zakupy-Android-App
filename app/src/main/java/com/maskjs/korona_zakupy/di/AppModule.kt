package com.maskjs.korona_zakupy.di

import com.maskjs.korona_zakupy.data.layoutModels.ConfirmPasswordTextInputLayoutModel
import com.maskjs.korona_zakupy.data.layoutModels.InputTextLayoutModel
import com.maskjs.korona_zakupy.data.layoutModels.PasswordTextInputLayoutModel
import com.maskjs.korona_zakupy.data.layoutModels.PlainTextInputTextLayoutModel
import com.maskjs.korona_zakupy.data.users.UserRepository
import com.maskjs.korona_zakupy.data.users.api_communication.UserDao
import com.maskjs.korona_zakupy.data.users.data_transfer_object.LoginUserDto
import com.maskjs.korona_zakupy.data.users.data_transfer_object.RegisterUserDto
import com.maskjs.korona_zakupy.ui.login.LoginActivity
import com.maskjs.korona_zakupy.ui.login.LoginViewModel
import com.maskjs.korona_zakupy.ui.main.MainActivity
import com.maskjs.korona_zakupy.ui.main.MainActivityViewModel
import com.maskjs.korona_zakupy.ui.register.RegisterActivity
import com.maskjs.korona_zakupy.ui.register.part1.RegisterPart1Fragment
import com.maskjs.korona_zakupy.ui.register.part1.RegisterPart1ViewModel
import com.maskjs.korona_zakupy.ui.register.part2.RegisterPart2ViewModel
import com.maskjs.korona_zakupy.ui.register.part3.RegisterPart3ViewModel
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

abstract class AppModule {
    companion object {
        @JvmStatic
        val appModule = module {
            single { OkHttpClient() }

            single { UserDao(get ()) }

            single(named("loginRepository")) { UserRepository<LoginUserDto>(get()) }

            single(named("registerRepository")){UserRepository<RegisterUserDto>(get())}

            factory<InputTextLayoutModel>(named("plain")) { PlainTextInputTextLayoutModel() }

            factory<InputTextLayoutModel>(named("password")) { PasswordTextInputLayoutModel()  }

            factory<InputTextLayoutModel>(named("confirmPassword")) {ConfirmPasswordTextInputLayoutModel()}

            scope<MainActivity>{
                viewModel { MainActivityViewModel() }
            }

            scope<LoginActivity>{
                viewModel { (errorMessages : Map<String,String>) -> LoginViewModel(errorMessages,get(
                    named("loginRepository")) , get(named("plain")) ,get(named("plain")))}
            }

            scope<RegisterActivity>{
                scoped { RegisterUserDto("","","","","","","","") }

                viewModel { RegisterPart1ViewModel( get()  ) }

                viewModel { (errorMessages : Map<String,String>) -> RegisterPart2ViewModel(errorMessages, get(named("registerRepository")), get() ,get(named("plain")), get(named("plain")), get(
                    named("password")), get(named("confirmPassword"))) }

                viewModel { (errorMessages: Map<String,String>) -> RegisterPart3ViewModel(errorMessages,get(named("registerRepository")), get(),get(named("plain")),get(named("plain")),get(named("plain"))) }
            }

            scope<RegisterPart1Fragment>{

            }
        }
    }
}