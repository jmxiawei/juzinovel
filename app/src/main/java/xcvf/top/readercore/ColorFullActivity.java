package xcvf.top.readercore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.iscore.freereader.R;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.setter.TextColorSetter;
import top.iscore.freereader.mode.setter.ViewSetter;

public class ColorFullActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.btn_day)
    Button btnDay;
    @BindView(R.id.btn_night)
    Button btnNight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_full);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.btn_day, R.id.btn_night})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_day:
                ViewSetter daySetter = new TextColorSetter(tv, R.attr.text_color);
                new Colorful.Builder(this).setter(daySetter).create().setTheme(R.style.DayTheme);

                break;
            case R.id.btn_night:
                ViewSetter nightSetter = new TextColorSetter(tv, R.attr.text_color);
                new Colorful.Builder(this).setter(nightSetter).create().setTheme(R.style.NightTheme);

                break;
            default:
                break;
        }
    }
}
