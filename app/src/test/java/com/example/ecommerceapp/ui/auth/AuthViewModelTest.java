package com.example.ecommerceapp.ui.auth;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.ecommerceapp.data.model.User;
import com.example.ecommerceapp.data.repository.AuthRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private AuthRepository mockAuthRepository;

    @Mock
    private Observer<LoginResult> loginResultObserver;
    @Mock
    private Observer<RegistrationResult> registrationResultObserver;
    @Mock
    private Observer<User> authenticatedUserObserver;

    @Captor
    private ArgumentCaptor<AuthRepository.AuthCallback<User>> authCallbackCaptor;

    private AuthViewModel authViewModel;

    @Before
    public void setUp() {
        // It's important to use the constructor that allows injecting the mock repository.
        // If AuthViewModel directly instantiates AuthRepository, this test becomes harder.
        // For this test, I'm assuming AuthViewModel can be instantiated or modified
        // to accept a mock repository, or uses a DI framework.
        // Since the current AuthViewModel instantiates its own repo, we'd ideally refactor it.
        // For now, let's proceed as if it could be mocked or use PowerMockito if needed.
        // For simplicity, I'll assume we can pass the mock.
        // If not, the test would need to use PowerMock to mock the constructor of AuthRepository,
        // or we'd refactor AuthViewModel to take AuthRepository as a constructor parameter.

        // Let's simulate that AuthViewModel is refactored to accept AuthRepository
        // For the actual code, AuthViewModel's constructor was: public AuthViewModel() { this.authRepository = new AuthRepository(); }
        // We will proceed as if we can replace this.authRepository for testing.
        // One way without refactoring is to use reflection or a setter if available.
        // The best way is constructor injection.

        authViewModel = new AuthViewModel(); // This will use the real repo.
        // To properly test, AuthViewModel should allow injection of AuthRepository.
        // E.g., authViewModel = new AuthViewModel(mockAuthRepository);
        // Since it doesn't, this test will be limited or require PowerMock.
        // I will write the test as if injection was possible.
        // In a real scenario, I would refactor AuthViewModel for testability.

        // Let's assume for the sake of this test that we *can* inject the mock.
        // If AuthViewModel had a setter or was refactored:
        // authViewModel.setAuthRepository(mockAuthRepository); // Imaginary setter

        // Given the current AuthViewModel structure, this test will run against the *actual* AuthRepository.
        // This makes it more of an integration test for AuthViewModel + AuthRepository's mock logic.
        // To truly unit test AuthViewModel, AuthRepository must be mocked.
        // I will write the asserts based on the *mocked* repository behavior I would *expect* to set up.
    }

    @Test
    public void login_success() {
        authViewModel.getLoginResult().observeForever(loginResultObserver);
        authViewModel.getAuthenticatedUser().observeForever(authenticatedUserObserver);

        User mockUser = new User("testId", "testUser", "test@example.com", "Test User");

        // This is how we would mock if AuthRepository was injected:
        // doAnswer(invocation -> {
        //     AuthRepository.AuthCallback<User> callback = invocation.getArgument(2);
        //     callback.onSuccess(mockUser);
        //     return null;
        // }).when(mockAuthRepository).login(anyString(), anyString(), any(AuthRepository.AuthCallback.class));

        authViewModel.login("test@example.com", "password123"); // This will use the real AuthRepository's logic

        // Verify LiveData changes (this part depends on AuthRepository's actual mock behavior)
        // If AuthRepository.login calls onSuccess with a user:
        // verify(loginResultObserver).onChanged(argThat(result -> result.isSuccess()));
        // verify(authenticatedUserObserver).onChanged(mockUser); // Or whatever user the real mock repo returns

        // For the current AuthRepository (which is a mock itself):
        // It internally uses a static registeredUser. Let's assume registration happened first.
        // To make this test self-contained for login success, we need to ensure `registeredUser`
        // in the actual AuthRepository is set up to allow a successful login.
        // This highlights the issue of not injecting the mock.

        // Let's simulate a successful login by first registering a user through the ViewModel
        // This is a workaround due to the lack of DI in the ViewModel.
        authViewModel.register("testUser", "test@example.com", "password123");
        // Now try to log in
        authViewModel.login("test@example.com", "password123");

        ArgumentCaptor<LoginResult> loginResultCaptor = ArgumentCaptor.forClass(LoginResult.class);
        verify(loginResultObserver, Mockito.atLeastOnce()).onChanged(loginResultCaptor.capture());
        assertTrue(loginResultCaptor.getValue().isSuccess());
        assertNull(loginResultCaptor.getValue().getError());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(authenticatedUserObserver, Mockito.atLeastOnce()).onChanged(userCaptor.capture());
        assertNotNull(userCaptor.getValue());
        assertEquals("test@example.com", userCaptor.getValue().getEmail());
    }

    @Test
    public void login_failure() {
        authViewModel.getLoginResult().observeForever(loginResultObserver);

        // This is how we would mock if AuthRepository was injected:
        // doAnswer(invocation -> {
        //     AuthRepository.AuthCallback<User> callback = invocation.getArgument(2);
        //     callback.onError("Invalid credentials");
        //     return null;
        // }).when(mockAuthRepository).login(anyString(), anyString(), any(AuthRepository.AuthCallback.class));

        // Test with non-existent user in the actual AuthRepository
        authViewModel.login("wrong@example.com", "wrongpassword");

        ArgumentCaptor<LoginResult> captor = ArgumentCaptor.forClass(LoginResult.class);
        verify(loginResultObserver, Mockito.atLeastOnce()).onChanged(captor.capture());
        assertFalse(captor.getValue().isSuccess());
        assertEquals("Invalid email or password.", captor.getValue().getError());
    }

    @Test
    public void register_success() {
        authViewModel.getRegistrationResult().observeForever(registrationResultObserver);

        // This is how we would mock if AuthRepository was injected:
        // doAnswer(invocation -> {
        //     AuthRepository.AuthCallback<User> callback = invocation.getArgument(3);
        //     callback.onSuccess(new User("newId", "newUser", "new@example.com", "New User"));
        //     return null;
        // }).when(mockAuthRepository).register(anyString(), anyString(), anyString(), any(AuthRepository.AuthCallback.class));

        authViewModel.register("newUser", "new@example.com", "password123");

        ArgumentCaptor<RegistrationResult> captor = ArgumentCaptor.forClass(RegistrationResult.class);
        verify(registrationResultObserver, Mockito.atLeastOnce()).onChanged(captor.capture());
        assertTrue(captor.getValue().isSuccess());
        assertNull(captor.getValue().getError());
    }

    @Test
    public void register_failure_emailExists() {
        authViewModel.getRegistrationResult().observeForever(registrationResultObserver);

        // Register a user first to make their email exist
        authViewModel.register("existingUser", "exists@example.com", "password123");

        // Try to register again with the same email
        authViewModel.register("anotherUser", "exists@example.com", "password456");

        ArgumentCaptor<RegistrationResult> captor = ArgumentCaptor.forClass(RegistrationResult.class);
        verify(registrationResultObserver, Mockito.atLeastOnce()).onChanged(captor.capture()); // Called twice

        // We are interested in the *last* emission for the failed attempt
        RegistrationResult lastResult = captor.getAllValues().get(captor.getAllValues().size() -1 );
        assertFalse(lastResult.isSuccess());
        assertEquals("Email already registered.", lastResult.getError());
    }

    @Test
    public void logout_clearsUser() {
        authViewModel.getAuthenticatedUser().observeForever(authenticatedUserObserver);

        // Simulate login first
        authViewModel.register("testUserLogout", "logout@example.com", "password123");
        authViewModel.login("logout@example.com", "password123");

        // Ensure user was set
        ArgumentCaptor<User> userLoginCaptor = ArgumentCaptor.forClass(User.class);
        verify(authenticatedUserObserver, Mockito.atLeastOnce()).onChanged(userLoginCaptor.capture());
        assertNotNull(userLoginCaptor.getValue()); // User should be non-null after login

        authViewModel.logout();

        // Verify authenticatedUser LiveData is updated to null
        ArgumentCaptor<User> userLogoutCaptor = ArgumentCaptor.forClass(User.class);
        verify(authenticatedUserObserver, Mockito.atLeastOnce()).onChanged(userLogoutCaptor.capture());
        assertNull(userLogoutCaptor.getValue()); // User should be null after logout
    }

    // Note: Proper unit testing of AuthViewModel would require it to be refactored
    // to allow injection of AuthRepository. The tests above are written to work
    // with the current structure but are more like integration tests for the ViewModel
    // and the mock AuthRepository's logic.
}
