package com.trimax.vts.interfaces;

import java.util.ArrayList;

public interface PositionClickListener {

    void itemClicked(int id, ArrayList<Integer> arraylist_position,Boolean flag);
    void itemClickedType(int id, ArrayList<Integer> arraylist_position_type);
    void  itemselectAll(boolean value);

}
