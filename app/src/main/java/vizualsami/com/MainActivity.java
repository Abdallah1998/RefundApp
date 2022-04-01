package vizualsami.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button loginbtn;

    private FirebaseAuth mAuth;


    private static String TAG = "MainActivity";
    static {
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "Opencv installed");

        } else {
            Log.d(TAG, "Opencv not installed");
        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.loginbtn:
                loginbtn();
                break;
        }

    }

    private void loginbtn() {

            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if(email.isEmpty()){
                editTextEmail.setError("Email is required!");
                editTextEmail.requestFocus();
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editTextEmail.setError("Please enter a valid email");
                editTextEmail.requestFocus();
                return;
            }

            if(password.isEmpty()){
                editTextPassword.setError("Password is required");
                editTextPassword.requestFocus();
                return;
            }

            if(password.length() < 6){
                editTextPassword.setError("Minimum password length is 6 characters!");
                editTextPassword.requestFocus();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        //redirect
                        Intent i = new Intent(MainActivity.this, AccountActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

}