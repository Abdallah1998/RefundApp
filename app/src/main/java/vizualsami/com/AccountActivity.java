package vizualsami.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import vizualsami.com.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import org.opencv.android.OpenCVLoader;

public class AccountActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private Button logout;
    private Button openTeachersActivityBtn,openUploadActivityBtn, validateReadingsBtn;
    private String userID;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        openTeachersActivityBtn = findViewById ( R.id.openTeachersActivityBtn );
        openUploadActivityBtn = findViewById ( R.id.openUploadActivityBtn );
        validateReadingsBtn = findViewById( R.id.validateReadingsBtn);
        logout = (Button) findViewById(R.id.signout);

        textView = (TextView)findViewById(R.id.textView);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView greetingTextView = (TextView) findViewById(R.id.greeting);
        final TextView fullnameTextView = (TextView) findViewById(R.id.fullname);
        final TextView emailTextView = (TextView) findViewById(R.id.email);
        final TextView dobTextView = (TextView) findViewById(R.id.dob);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);

                        if(userprofile != null){
                            String fullname = userprofile.fullname;
                            String email = userprofile.email;
                            String dob = userprofile.dob;

                            greetingTextView.setText("Welcome, " + fullname + "!");
                            fullnameTextView.setText(fullname);
                            emailTextView.setText(email);
                            dobTextView.setText(dob);

                        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountActivity.this, "Something has went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        openTeachersActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountActivity.this, ItemsActivity.class);
                startActivity(i);
            }
        });
        openUploadActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountActivity.this, UploadActivity.class);
                startActivity(i);
            }
        });
        validateReadingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountActivity.this, ValidateActivity.class);
                startActivity(i);
            }
        });


    }
}