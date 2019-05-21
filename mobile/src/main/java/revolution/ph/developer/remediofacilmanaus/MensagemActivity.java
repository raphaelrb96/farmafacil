package revolution.ph.developer.remediofacilmanaus;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class MensagemActivity extends AppCompatActivity {

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_msg);
//        setSupportActionBar(toolbar);
    }

}
