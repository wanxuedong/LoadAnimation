package lets.heartworld.loadanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private Button btn1;
    private GradientRotation loading;
    private RotaryLine loading1;
    private TransformationLoading loading2;
    private JumpLoading loading3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        btn1 = (Button) findViewById(R.id.btn1);
        loading = (GradientRotation) findViewById(R.id.loading);
        loading1 = (RotaryLine) findViewById(R.id.loading1);
        loading2 = (TransformationLoading) findViewById(R.id.loading2);
        loading3 = (JumpLoading) findViewById(R.id.loading3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.startAnimate();
                loading1.startAnimate();
                loading2.startAnimate();
                loading3.startAnimate(0);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.stopAnimate();
                loading1.stopAnimate();
                loading2.stopAnimate();
                loading3.stopAnimate();
            }
        });
    }
}
