package com.loftschool.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate: ");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        MainPagesAdapter adapter = new MainPagesAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        tabLayout.setupWithViewPager(viewPager);

                fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentPage = viewPager.getCurrentItem(); // Узнаем текущую открытую страницу, видим что на нашу кнопку теперь можно кликнуть только когда у нас страница в нормальном состоянии
                String type = null;

                if (currentPage == MainPagesAdapter.PAGE_INCOMES) {
                    type = Item.TYPE_INCOMES;
                } else if (currentPage == MainPagesAdapter.PAGE_EXPENSES) {
                    type = Item.TYPE_EXPENSES;
                }



//              // Неявный intent - когда мы точно не знаем какой экран или какое приложение, но мы знаем что хотим сделать. Запуск из другого приложения
//                Intent intent = new Intent(); // Создание интента
//                intent.setAction(Intent.ACTION_VIEW); // Параметр Action (действие), (ACTION_VIEW - для просмотра чего-то)
//                intent.setData(Uri.parse("https://pikabu.ru")); // Вставляем данные (сайт в нашем случае)
//                startActivity(intent); // Запуск экрана без возврата данных

                // Явный intent - сразу в intent говорим какой экран открыть. Используется для того чтобы запустить компонент из нашего приложения
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class); //GetContext() - передача контескта (откуда мы запускаем), после запятой - какой класс
                intent.putExtra(AddItemActivity.TYPE_KEY, type);
                //startActivity(intent); // Запуск экрана без возврата данных
                startActivityForResult(intent, ItemsFragment.ADD_ITEM_REQUEST_CODE); // Запуск экрана с возвратом

            }
        });


    }




    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case MainPagesAdapter.PAGE_INCOMES:  //Делаем так, чтобы кнопка была видна на доходах и расходах
            case MainPagesAdapter.PAGE_EXPENSES:
            fab.show();
            break;
            case MainPagesAdapter.PAGE_BALANCE: //Прячем кнопку на балансе
                fab.hide();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE: //Когда перелистываются страницы пользователь не сможет нажать на кнопку
                fab.setEnabled(true);
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
            case ViewPager.SCROLL_STATE_SETTLING:
                fab.setEnabled(false);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);

        }
    }
}
