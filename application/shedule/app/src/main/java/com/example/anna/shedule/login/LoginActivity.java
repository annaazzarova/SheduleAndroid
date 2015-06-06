package com.example.anna.shedule.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anna.shedule.R;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.application.user.service.UserService;
import com.example.anna.shedule.utils.ContextUtils;

import static com.example.anna.shedule.R.id.login;
import static com.example.anna.shedule.R.id.password;
import static com.example.anna.shedule.R.layout.activity_login;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity {

    // UI references.
    private EditText mLoginView = null;
    private EditText mPasswordView = null;
    private Button mLoginSignInButton = null;
/*    private View mProgressView;
    private View mLoginFormView;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_login);

        // Set up the login form.
        mLoginView = (EditText) findViewById(login);
        mPasswordView = (EditText) findViewById(password);

        mLoginView.setText("нехорошкова л.г.");
        mPasswordView.setText("NJZR4QB_S");

        ContextUtils.setContext(getApplicationContext());
        final Button mLoginSignInButton = (Button) findViewById(R.id.login_sign_in_button);

        mLoginSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLoginSignInButton.getText().length() < 4 || mPasswordView.getText().length() < 4) {
                    Toast.makeText(getApplicationContext(), "Пара 'Логин-Пароль' не верная!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final ProgressDialog prog1 = new ProgressDialog(LoginActivity.this);
                prog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                prog1.setMessage("wait please");
                prog1.setIndeterminate(true); // выдать значек ожидания
                prog1.setCancelable(true);
                prog1.show();

                final UserService userService = Services.getService(UserService.class);
                userService.login(mLoginView.getText().toString(), mPasswordView.getText().toString(), new UserService.LoginListener() {
                    @Override
                    public void onSuccess(User user) {
                        final Intent intent = new Intent(LoginActivity.this,
                                com.example.anna.shedule.login.GroupSelectionActivity.class);
                        intent.putExtra("user", user.toString());
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int errorCode, String message) {
                        prog1.cancel();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}

