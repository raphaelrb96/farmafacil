package revolution.ph.developer.remediofacilmanaus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ConclusaoActivity extends AppCompatActivity {
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusao);
        uid = getIntent().getStringExtra("uid");
    }

    public void verMinhasCompras(View view) {
        startActivity(new Intent(this, MinhasComprasActivity.class).putExtra("uid", uid));
        finish();
    }
}
