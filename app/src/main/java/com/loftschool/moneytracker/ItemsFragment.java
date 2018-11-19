package com.loftschool.moneytracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ItemsFragment extends Fragment {

    private static final int TYPE_UNKNOWN = -1;

    public static final int TYPE_EXPENSES = 1;
    public static final int TYPE_INCOMES = 2;
    public static final int TYPE_BALANCE = 3;


    private static final String TYPE_KEY = "type";


    public static ItemsFragment createItemsFragment(int type) {
        ItemsFragment fragment = new ItemsFragment();

        Bundle bundle = new Bundle(); //С помощью бандалов передаются данные между экранами и так же во фрагмент
        bundle.putInt(ItemsFragment.TYPE_KEY, ItemsFragment.TYPE_EXPENSES);

        fragment.setArguments(bundle);
        return fragment;
    }


    private int type;

    private RecyclerView recycler;
    private ItemsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemsAdapter();

        Bundle bundle = getArguments();
        type = bundle.getInt(TYPE_KEY, TYPE_UNKNOWN);

        if (type == TYPE_UNKNOWN) {
            throw new IllegalArgumentException("Unknown type");
        }
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


        loadItems();
    }

    private void loadItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Молоко",35));
        items.add(new Item("Жизнь",1));
        items.add(new Item("Курсы",50));

        adapter.setData(items);

    }
}
