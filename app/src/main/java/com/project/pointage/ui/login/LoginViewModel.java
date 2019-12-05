package com.project.pointage.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.project.pointage.data.LoginRepository;
import com.project.pointage.data.Result;
import com.project.pointage.data.model.LoggedInUser;
import com.project.pointage.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    private Pattern digit = Pattern.compile("[0-9]");
    private Pattern LowerCase = Pattern.compile("[a-z]");
    private Pattern UpperCase = Pattern.compile("[A-Z]");
    private Matcher matcher1,matcher2,matcher3;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {

        if (username == null && this.isValid(username) && username.length()>5) {
            return false;
        }
         else {
            return !username.trim().isEmpty();
        }

    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5 && isValid(password);
    }

    private boolean isValid(String value){
        matcher1 = this.digit.matcher(value);
        matcher2 = this.LowerCase.matcher(value);
        matcher3 = this.UpperCase.matcher(value);

        if( matcher3.find() && matcher1.find() && matcher2.find()){
            return true;
        }
        else{
            return false;
        }
    }
}
