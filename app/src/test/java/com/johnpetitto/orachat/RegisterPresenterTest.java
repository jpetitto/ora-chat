package com.johnpetitto.orachat;

import com.johnpetitto.orachat.data.user.UserModel;
import com.johnpetitto.orachat.ui.register.RegisterPresenter;
import com.johnpetitto.orachat.ui.register.RegisterView;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InOrder;

import io.reactivex.Completable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegisterPresenterTest {
    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(c -> Schedulers.trampoline());
    }

    private RegisterView view;
    private InOrder inOrder;

    @Before
    public void setup() {
        view = mock(RegisterView.class);
        inOrder = inOrder(view);
    }

    private void register(Completable completable) {
        UserModel model = when(mock(UserModel.class)
                .createUser(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(completable)
                .getMock();

        RegisterPresenter presenter = new RegisterPresenter(view, model);
        presenter.register("John Smith", "john@orainteractive.com", "secret", "secret");
    }

    @Test
    public void successfulRegistration() {
        register(Completable.complete());

        verify(view, times(1)).showLoading(true);
        verify(view, times(1)).navigateToMain();

        inOrder.verify(view).showLoading(true);
        inOrder.verify(view).navigateToMain();
    }

    @Test
    public void registrationFailure() {
        register(Completable.error(new Throwable()));

        verify(view, times(2)).showLoading(anyBoolean());
        verify(view, times(1)).showRegistrationFailure();

        inOrder.verify(view).showLoading(true);
        inOrder.verify(view).showLoading(false);
        inOrder.verify(view).showRegistrationFailure();
    }
}
