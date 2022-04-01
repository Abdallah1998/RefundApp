package vizualsami.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextfullname, editTextpassword, editTextdob, editTextemail;
    Button signupbtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editTextfullname = (EditText) findViewById(R.id.fullname);
        editTextpassword = (EditText) findViewById(R.id.password);
        editTextemail = (EditText) findViewById(R.id.email);
        editTextdob = (EditText) findViewById(R.id.dob);
        signupbtn = (Button) findViewById(R.id.signupbtn);



        signupbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = editTextemail.getText().toString();
                String password = editTextpassword.getText().toString();
                String dob = editTextdob.getText().toString();
                String fullname = editTextfullname.getText().toString();
                
                if(fullname.isEmpty()){
                    editTextfullname.setError("Full name is required!");
                    editTextfullname.requestFocus();
                    return;
                }

                if(dob.isEmpty()){
                    editTextdob.setError("Date of Birth is required!");
                    editTextdob.requestFocus();
                    return;
                }


                if(email.isEmpty()){
                    editTextemail.setError("Email is required!");
                    editTextemail.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextemail.setError("Please provide valid email!");
                    editTextemail.requestFocus();
                    return;
                }

                if(password.isEmpty()){
                    editTextpassword.setError("Password is required!");
                    editTextpassword.requestFocus();
                    return;
                }

                if(password.length() < 6){
                    editTextpassword.setError("Minimum password length should be 6 characters!");
                    editTextpassword.requestFocus();
                    return;
                }
                
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    User user = new User(fullname, dob, email);

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "User has been registered succesfully!", Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                     });
                                }else{
                                    Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }





        });





    }
}