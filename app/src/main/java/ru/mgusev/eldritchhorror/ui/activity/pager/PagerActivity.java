package ru.mgusev.eldritchhorror.ui.activity.pager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.presentation.AndroidBug5497Workaround;
import ru.mgusev.eldritchhorror.presentation.presenter.pager.PagerAdapter;
import ru.mgusev.eldritchhorror.presentation.presenter.pager.PagerPresenter;
import ru.mgusev.eldritchhorror.presentation.view.pager.PagerView;

public class PagerActivity extends MvpAppCompatActivity implements PagerView {

    @InjectPresenter
    PagerPresenter pagerPresenter;

    ViewPager pager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_pager);

        AndroidBug5497Workaround.assistActivity(this);
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(this, getSupportFragmentManager());
        //pager.setOffscreenPageLimit(2);
        pager.setAdapter(pagerAdapter);

        /*pager.postDelayed(new Runnable() {
            @Override
            public void run() {
                pager.setCurrentItem(0, false);
                pagerAdapter.notifyDataSetChanged();
            }
        }, 100);*/
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d("PAGER", "onPageSelected, position = " + position);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //AndroidBug5497Workaround.assistActivity(this);
        //https://github.com/chenxiruanhai/AndroidBugFix/blob/master/bug-5497/AndroidBug5497Workaround.java

        //currentPosition = (int) getIntent().getIntExtra("setPosition", 0);

        /*if (savedInstanceState!= null) {
            currentPosition = savedInstanceState.getInt("position", 0);
            game = savedInstanceState.getParcelable("game");
            isAlert = savedInstanceState.getBoolean("DIALOG");
        }*/
        /*((NestedScrollView)findViewById(R.id.pager_scroll)).setFillViewport(true);
        pagerAdapter = new EHFragmentPagerAdapter (this, getSupportFragmentManager(), this);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(pagerAdapter);

        score = (TextView) findViewById(R.id.score_pager);

        if (game == null) game = (Game) getIntent().getParcelableExtra("editParty");
        if (game == null) game = new Game();

        score.setText(String.valueOf(game.score));

        if (game.id == -1) titleResource = R.string.add_new_game;
        else titleResource = R.string.edit_game;
        initToolbar();


        pager.postDelayed(new Runnable() {
            @Override
            public void run() {
                pager.setCurrentItem(currentPosition, false);
                pagerAdapter.notifyDataSetChanged();
                if (isAlert) ((InvestigatorsChoiceFragment)pagerAdapter.getItem(1)).cleanDialog();
            }
        }, 100);

        invalidateOptionsMenu();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
                currentPosition = position;

                if (position == 1 && clearItem != null) clearItem.setVisible(true);
                else if (clearItem != null) clearItem.setVisible(false);

                if (position != 2) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    if (randomItem != null) randomItem.setVisible(true);
                } else if (randomItem != null) randomItem.setVisible(false);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });*/
    }

}