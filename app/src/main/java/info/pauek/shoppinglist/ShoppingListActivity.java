package info.pauek.shoppinglist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static info.pauek.shoppinglist.R.menu.list_menu;

public class ShoppingListActivity extends AppCompatActivity {

    // TODO: 1. Afegir un CheckBox a cada ítem, per marcar o desmarcar els ítems (al model també!)
    // TODO: 2. Que es puguin afegir elements (+ treure els inicials)
    // TODO: 3. Afegir un menú amb una opció per esborrar de la llista tots els marcats.
    // TODO: 4. Que es pugui esborrar un element amb LongClick (cal fer OnLongClickListener)

    // Model
    List<ShoppingItem> items;

    // Referències a elements de la pantalla
    private RecyclerView items_view;
    private ImageButton btn_add;
    private EditText edit_box;
    private ShoppingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        items = new ArrayList<>();
        items.add(new ShoppingItem("Potatoes"));
        items.add(new ShoppingItem("Toilet Paper"));

        items_view = findViewById(R.id.items_view);
        btn_add = findViewById(R.id.btn_add);
        edit_box = findViewById(R.id.edit_box);

        adapter = new ShoppingListAdapter(this, items);

        items_view.setLayoutManager(new LinearLayoutManager(this));
        items_view.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        items_view.setAdapter(adapter);

        adapter.setOnClickListener(new ShoppingListAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                if (!items.get(position).isSelected())
                {
                    items.get(position).setSelected(true);
                }
                else
                    items.get(position).setSelected(false);

                adapter.notifyItemChanged(position);
            }
        });

        adapter.setOnLongClickListener(new ShoppingListAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position)
            {
                items.remove(items.get(position));

                adapter.notifyItemRemoved(position);
            }
        });
    }

    // Create options list menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    // Options menu callback
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RecyclerView.Adapter adapter = items_view.getAdapter();

        switch (item.getItemId())
        {
            case R.id.selectAll:
                for (int i=0; i<items.size(); i++)
                {
                    items.get(i).setSelected(true);
                    adapter.notifyItemChanged(i);
                }
                return true;
            case R.id.removeSelected:
                for (int i=0; i<items.size();)
                {
                    if (items.get(i).isSelected())
                    {
                        items.remove(i);
                        adapter.notifyItemRemoved(i);
                    }
                    else i++;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Add button callback
    public void onClickAdd(View view) {
        AddNewItem(false);
    }

    // Add a new item to the shopping list
    private void AddNewItem(boolean hideKB) {
        String name = edit_box.getText().toString();
        if (!name.isEmpty())
            items.add(new ShoppingItem(name));

        else
            Toast.makeText(this, "Introduce an item, please", Toast.LENGTH_SHORT).show();

        UpdateShoppingList(hideKB);
    }

    // Update the shopping list when we add a new item
    private void UpdateShoppingList(boolean hideKB) {

        HideKeyboard(hideKB);

        edit_box.setText("");
        adapter.notifyItemInserted(items.size());
    }


    private void HideKeyboard(boolean hideKB) {
        if (hideKB)
        {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit_box.getWindowToken(), 0);
        }
    }

    // Add a item to the list with the enter
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            AddNewItem(true);
            return true;
        } else
            return super.onKeyUp(keyCode, event);

    }
}