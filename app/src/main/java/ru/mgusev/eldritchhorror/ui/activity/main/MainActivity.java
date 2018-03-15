package ru.mgusev.eldritchhorror.ui.activity.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.presentation.presenter.main.MainPresenter;
import ru.mgusev.eldritchhorror.presentation.view.main.MainView;

public class MainActivity extends MvpAppCompatActivity implements MainView, View.OnClickListener {

    @InjectPresenter
    MainPresenter mainPresenter;

    private FloatingActionButton addGameFAB;
    private TextView startMessageTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addGameFAB = findViewById(R.id.main_activity_add_game);
        addGameFAB.setOnClickListener(this);

        startMessageTV = findViewById(R.id.main_activity_start_message);
    }

    @Override
    public void showMessage(int message) {
        startMessageTV.setText(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_activity_add_game:
                mainPresenter.addGame();
                break;
            default:
                break;
        }
    }
}