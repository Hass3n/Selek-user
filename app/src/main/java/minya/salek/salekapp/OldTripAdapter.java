package minya.salek.salekapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import minya.salek.salekapp.Model.OldTripModel;
import minya.salek.salekapp.Model.userTrip;


public class OldTripAdapter extends RecyclerView.Adapter<OldTripAdapter.Holder> {

    Context context;
    List<userTrip> oldTrip;

    public OldTripAdapter(Context context, List<userTrip> oldTrip) {
        this.context = context;
        this.oldTrip = oldTrip;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_old_trip, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        userTrip currentOldTrip = oldTrip.get(position);

        holder.tvDate.setText(currentOldTrip.getTripDate());
        holder.tvPlace.setText(currentOldTrip.getEndTrip());
        holder.tv_price.setText(currentOldTrip.getTripPrice()+"جنيه");

    }

    @Override
    public int getItemCount() {
        return oldTrip.size();
    }


    class Holder extends RecyclerView.ViewHolder{
        TextView tvDate, tvPlace,tv_price;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.old_trip_date_txt);
            tvPlace = itemView.findViewById(R.id.old_trip_place_txt);
            tv_price=itemView.findViewById(R.id.old_trip_price_txt);

        }
    }
}
