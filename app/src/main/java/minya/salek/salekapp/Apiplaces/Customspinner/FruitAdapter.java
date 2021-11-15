package minya.salek.salekapp.Apiplaces.Customspinner;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.List;

import minya.salek.salekapp.Apiplaces.Customspinner.inventory.Fruit;
import minya.salek.salekapp.R;

/********************************************
 *     Created by DailyCoding on 15-May-21.  *
 ********************************************/

public class FruitAdapter extends BaseAdapter {
    private Context context;
    private List<Fruit> fruitList;
    private  OnPlaceType onplacetype;

    public void setOnPlaceType(OnPlaceType onPlaceType) {
        this.onplacetype = onPlaceType;
    }

    public FruitAdapter(Context context, List<Fruit> fruitList) {
        this.context = context;
        this.fruitList = fruitList;
    }

    @Override
    public int getCount() {
        return fruitList != null ? fruitList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.item_fruit, viewGroup, false);

        TextView txtName = rootView.findViewById(R.id.name);
        ImageView image = rootView.findViewById(R.id.image);

        txtName.setText(fruitList.get(i).getName());
        image.setImageResource(fruitList.get(i).getImage());

            txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onplacetype.onclickplacetype(fruitList.get(i).getName());
                }
            });

        return rootView;
    }

    public interface OnPlaceType
    {
        void onclickplacetype(String Placename);
    }
}
