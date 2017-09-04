package com.johnpetitto.orachat

import com.johnpetitto.orachat.data.user.UserModel
import com.johnpetitto.orachat.ui.register.RegisterPresenter
import com.johnpetitto.orachat.ui.register.RegisterView
import io.reactivex.Completable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.InOrder
import org.mockito.Mockito.*

class RegisterPresenterTest {
    private lateinit var view: RegisterView
    private lateinit var inOrder: InOrder

    @Before
    fun setup() {
        view = mock(RegisterView::class.java)
        inOrder = inOrder(view)
    }

    private fun register(completable: Completable) {
        val model = `when`(mock(UserModel::class.java)
                .createUser(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(completable)
                .getMock<UserModel>()

        val presenter = RegisterPresenter(view, model)
        presenter.register("John Smith", "john@orainteractive.com", "secret", "secret")
    }

    @Test
    fun successfulRegistration() {
        register(Completable.complete())

        verify(view, times(1)).showLoading(true)
        verify(view, times(1)).navigateToMain()

        inOrder.verify(view).showLoading(true)
        inOrder.verify(view).navigateToMain()
    }

    @Test
    fun registrationFailure() {
        register(Completable.error(Throwable()))

        verify(view, times(2)).showLoading(anyBoolean())
        verify(view, times(1)).showRegistrationFailure()

        inOrder.verify(view).showLoading(true)
        inOrder.verify(view).showLoading(false)
        inOrder.verify(view).showRegistrationFailure()
    }

    companion object {
        @BeforeClass @JvmStatic
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        }
    }
}
