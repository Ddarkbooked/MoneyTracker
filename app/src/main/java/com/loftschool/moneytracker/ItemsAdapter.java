package com.loftschool.moneytracker;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    public ActionMode actionMode;
    private List<Item> data = new ArrayList<>();
    private ItemsAdapterListener listener = null; // Создаем переменную которая будет хранить наш listener

    public void setListener(ItemsAdapterListener listener) {  // Возможность задать listener
        this.listener = listener;
    }


    public void setData(List<Item> data) { // Метод, задает нашему адаптеру список айтемов из вне и передаем массив
        this.data = data;
        notifyDataSetChanged(); // Так как набор данных изменился, то notifyDataSetChanged() все удаляет и заного по всем элементам дата пробигается и перерисовывает все данные на экране
    }

    public void addItem(Item item) {
        data.add(0, item);
        notifyItemInserted(0); // Не перерисовывает, а добавит новый элемент; notifyItemInserted - добавляет наверх позицию
    }

    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.ItemViewHolder holder, int position) {
        Item record = data.get(position);
        holder.applyData(record, position, listener, selections.get(position, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private SparseBooleanArray selections = new SparseBooleanArray(); // Замена HashMap,

    public void toggleSelection(int position) {
        if (selections.get(position, false)) {
            selections.delete(position);
        } else {
            selections.put(position, true);
        }
        notifyItemChanged(position); // Говорим адаптеру что у нас что-то поменялось
    }

    void clearSelections() { // Убираем выделения после закрытия ActionMode
        selections.clear(); // Очищает состояния
        notifyDataSetChanged(); // Перерисовывает
    }

    int getSelectedItemCount() { // Получить колличство выделенных элементов
        return selections.size();
    }

    List<Integer> getSelectedItems() { // Получить позиции выделенных элементов
        List<Integer> items = new ArrayList<>(selections.size()); // Создаем массив позиций
        for (int i = 0; i < selections.size(); i++) { // Проходимся по selections
            items.add(selections.keyAt(i));
        }
        return items;
    }

    Item remove(int pos) { // Удаляем элементы нашего списка
        final Item item = data.remove(pos);
        notifyItemRemoved(pos);
        return item;
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView price;
        private final String ruble;

        public ItemViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            ruble = itemView.getContext().getResources().getString(R.string.ruble_symbol);

        }

        public void applyData(final Item item, final int position, final ItemsAdapterListener listener, boolean selected) {
            title.setText(item.name);
            price.setText(String.format(ruble, item.price)); //Форматирование текста, рубль

            itemView.setOnClickListener(new View.OnClickListener() { // Нажатие
                @Override
                public void onClick(View v) {
                    if (listener != null) { // Если мы передали нашему адаптеру listener то
                        listener.onItemClick(item, position); // Вызываем onItemClick
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() { // Долгое нажатие
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) { // Если мы передали нашему адаптеру listener то
                        listener.onItemLongClick(item, position); // Вызываем onItemLongClick
                    }
                    return true;
                }
            });

            itemView.setActivated(selected);
        }

    }
}
