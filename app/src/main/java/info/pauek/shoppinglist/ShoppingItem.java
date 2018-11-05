package info.pauek.shoppinglist;

import android.widget.CheckBox;

public class ShoppingItem {
    private String name;
    private boolean isSelected;

    public ShoppingItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
