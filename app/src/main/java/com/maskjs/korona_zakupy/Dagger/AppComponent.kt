package com.maskjs.korona_zakupy.Dagger

import com.maskjs.korona_zakupy.RegisterActivity
import com.maskjs.korona_zakupy.RegisterPart1Fragment
import com.maskjs.korona_zakupy.RegisterPart2Fragment
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
   // fun inject(frag: RegisterPart1Fragment)
   // fun inject(frag: RegisterPart2Fragment)
   // fun inject(activity: RegisterActivity)

}