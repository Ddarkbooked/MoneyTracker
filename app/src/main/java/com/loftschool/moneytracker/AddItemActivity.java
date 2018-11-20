package com.loftschool.moneytracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddItemActivity extends AppCompatActivity {

    private static final String TAG = "AddItemActivity";

    public static final String TYPE_KEY = "type";

    private EditText name;
    private EditText price;
    private Button addBtn;
    private TextView ruble;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.add_item_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Добавляет стрелочку назад


        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        addBtn = findViewById(R.id.add_btn);
        ruble = findViewById(R.id.ruble_symbol);

        type = getIntent().getStringExtra(TYPE_KEY);

//        name.addTextChangedListener(nameTextWatcher);
//        price.addTextChangedListener(nameTextWatcher);


//    private TextWatcher nameTextWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            String nameInput = name.getText().toString().trim();
//            String priceInput = price.getText().toString().trim();
//
//            addBtn.setEnabled(!nameInput.isEmpty() && !priceInput.isEmpty());
//            ruble.setEnabled(!priceInput.isEmpty());
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };

        addBtn.setOnClickListener(new View.OnClickListener() { // По нажатию кнопки addBtn будет браться и передаваться обратно наша запись
            @Override
            public void onClick(View v) {
                String nameValue = name.getText().toString();
                String priceValue = price.getText().toString();

                Item item = new Item(nameValue, priceValue, type);

                Intent intent = new Intent(); // Здесь Intent нужен чтобы вернуть данные
                intent.putExtra("item", item);

                setResult(RESULT_OK, intent); // Результат запуска
                finish(); // Закрытие экрана
            }
        });
    }
}




