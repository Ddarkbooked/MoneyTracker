package com.loftschool.moneytracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loftschool.moneytracker.api.Api;
import com.loftschool.moneytracker.api.AuthResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 321;

    private static final String TAG = "AuthActivity"; // logt, для создания логов

    private GoogleSignInClient googleSignInClient;

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        api = ((App) getApplication()).getApi();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton button = findViewById(R.id.sign_in_button); // Создаем кнопку и подключаем к кнопке в активити
        button.setOnClickListener(new View.OnClickListener() { // Вешаем на нее обработчик клика
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: Check for an existing signed-in user

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) { // Если аккаунт не равен null
            updateUI(account); // То мы будем обновлять наш экран, вызывать метод
        }
        updateUI(account);

    }

    
    private void signIn() { // Создаем свой метод, настраиваем вход
        Log.i(TAG, "signIn: ");
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN); //Если вызываем активити для результата, чтобы получить результат, мы должны получить request code, поэтому сверху кода создаем private static final int RC_SIGN_IN = 321;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // Получаем результат
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) { // Метод вызывается когды мы получили аккаунт
        if (account == null) {
            showError("Account is null");
            return; // Если ошибка то прекратим выполнение
        }

        String id = account.getId(); // Получаем id пользователя
        // Log.i(TAG, "id = " + id); // Логируем id, будем использовать для авторизации на нашем сервере

        api.auth(id).enqueue(new Callback<AuthResult>() { // Результат входа
            @Override
            public void onResponse(Call<AuthResult> call, Response<AuthResult> response) {
                AuthResult result = response.body();
                ((App) getApplication()).saveAuthToken(result.token);
                finish();
            }

            @Override
            public void onFailure(Call<AuthResult> call, Throwable t) {
                showError("Auth failed " + t.getMessage());
            }
        });
    }

    private void showSuccess() { // Создание метода для удачного входа
        Toast.makeText(this, "Account successfully obtained", Toast.LENGTH_SHORT).show();

    }

    private void showError(String error) { // Создание метода для ошибок
        // Toast - выскакивающее уведомление снизу, старое.
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}
