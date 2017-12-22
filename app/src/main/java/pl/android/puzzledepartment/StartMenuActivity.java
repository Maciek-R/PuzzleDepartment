package pl.android.puzzledepartment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import pl.android.puzzledepartment.R;

/**
 * Created by Maciek Ruszczyk on 2017-12-21.
 */

public class StartMenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.start_activity);

        /*NewGameButton = (Button) findViewById(R.id.NewGame);
        ExitButton = (Button) findViewById(R.id.Exit);
        BestScoreButton = (Button) findViewById(R.id.BestScore);
        HardModeButton = (Button) findViewById(R.id.HardMode);*/

    }

    public void checkButton(View view){

        Intent intent;
        switch(view.getId()){

            case R.id.NewGame:
                intent = new Intent(this, MainGameActivity.class);
                startActivity(intent);
                break;

       /*     case R.id.LoadLastGame:
                intent = new Intent(this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("mode", 1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
*/
            case R.id.Exit:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}