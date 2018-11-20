package com.loftschool.moneytracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ItemsFragment extends Fragment {
    private static final String TAG = "ItemsFragment";

    private static final String TYPE_KEY = "type";

    private static final int ADD_ITEM_REQUEST_CODE = 123;

    public static ItemsFragment createItemsFragment(String type) {
        ItemsFragment fragment = new ItemsFragment();

        Bundle bundle = new Bundle(); //С помощью бандалов передаются данные между экранами и так же во фрагмент
        bundle.putString(ItemsFragment.TYPE_KEY, type);

        fragment.setArguments(bundle);
        return fragment;
    }


    private String type;

    private RecyclerView recycler;
    private FloatingActionButton fab;
    private SwipeRefreshLayout refresh;
    private ItemsAdapter adapter;

    private Api api;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemsAdapter();

        Bundle bundle = getArguments();
        type = bundle.getString(TYPE_KEY, Item.TYPE_EXPENSES);

        if (type.equals(Item.TYPE_UNKNOWN)) {
            throw new IllegalArgumentException("Unknown type");
        }

        api = ((App) getActivity().getApplication()).getApi();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = view.findViewById(R.id.list);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              // Неявный intent - когда мы точно не знаем какой экран или какое приложение, но мы знаем что хотим сделать. Запуск из другого приложения
//                Intent intent = new Intent(); // Создание интента
//                intent.setAction(Intent.ACTION_VIEW); // Параметр Action (действие), (ACTION_VIEW - для просмотра чего-то)
//                intent.setData(Uri.parse("https://pikabu.ru")); // Вставляем данные (сайт в нашем случае)
//                startActivity(intent); // Запуск экрана без возврата данных

                // Явный intent - сразу в intent говорим какой экран открыть. Используется для того чтобы запустить компонент из нашего приложения
                Intent intent = new Intent(getContext(), AddItemActivity.class); //GetContext() - передача контескта (откуда мы запускаем), после запятой - какой класс
                intent.putExtra(AddItemActivity.TYPE_KEY, type);
                //startActivity(intent); // Запуск экрана без возврата данных
                startActivityForResult(intent, ADD_ITEM_REQUEST_CODE); // Запуск экрана с возвратом

            }
        });

        refresh = view.findViewById(R.id.refresh);
        refresh.setColorSchemeColors(Color.BLUE, Color.CYAN, Color.GREEN);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // Метод, в котором определяется когда у нас закончится рефреш и когда уберется колесо
            @Override
            public void onRefresh() {
                loadItems(); // Когда мы потянули обновить экран мы вызываем loadItems() у нас заного вызывается метод к api и идет ответ
            }
        });

        loadItems();
    }

    private void loadItems() {
        Call<List<Item>> call = api.getItems(type);

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                adapter.setData(response.body());
                refresh.setRefreshing(false); // чтобы убрать рефреш
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                refresh.setRefreshing(false); // чтобы убрать рефреш
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Item item = data.getParcelableExtra("item");
            adapter.addItem(item);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

































//    private void loadItems() {
//
//       AsyncTask<Void, Void, List<Item>> task = new AsyncTask<Void, Void, List<Item>>() {
//
//
//            @Override
//            protected void onPreExecute() {
//                Log.d(TAG, "onPreExecute: thread name = " + Thread.currentThread().getName());
//            }
//
//            @Override
//            protected List<Item> doInBackground(Void... voids) {
//                Log.d(TAG, "onPreExecute: thread name = " + Thread.currentThread().getName());
//                Call<List<Item>> call = api.getItems(type);
//            try {
//                List<Item> items = call.execute().body();
//                return items;
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//            }
//
//            @Override
//            protected void onPostExecute(List<Item> items) {
//                if (items != null) {
//                    adapter.setData(items);
//                }
//            }
//        };
//
//
//       task.execute();
//    }


    // Thread and Handler

//    private void loadItems() {
//
//        Log.d(TAG, "loadItems: current thread " + Thread.currentThread().getName());
//
//        new LoadItemsTask(new Handler(callback)).start();
//
//
//    }
//
//    private Handler.Callback callback = new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//
//            if (msg.what == DATA_LOADED) {
//                List<Item> items = (List<Item>) msg.obj;
//                adapter.setData(items);
//            }
//            return true;
//        }
//    };
//
//    private final static int DATA_LOADED = 123;
//
//    private class LoadItemsTask implements Runnable {
//
//        private Thread thread;
//        private Handler handler;
//
//        public LoadItemsTask(Handler handler) {
//            thread = new Thread(this);
//            this.handler = handler;
//        }
//
//        public void start() {
//            thread.start();
//        }
//
//        @Override
//        public void run() {
//
//            Log.d(TAG, "run: current thread " + Thread.currentThread().getName());
//
//            Call<List<Item>> call = api.getItems(type);
//            try {
//                List<Item> items = call.execute().body();
//
//                handler.obtainMessage(DATA_LOADED, items).sendToTarget();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


