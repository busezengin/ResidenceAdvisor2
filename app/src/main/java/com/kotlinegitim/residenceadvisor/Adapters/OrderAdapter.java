package com.kotlinegitim.residenceadvisor.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kotlinegitim.residenceadvisor.R;
import com.kotlinegitim.residenceadvisor.classes.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private List<Order> list;
    //private ItemClickListener clickListener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String apartmentID;
    public OrderAdapter(List<Order> list, String apartmentID) {
        this.list = list;
        //this.clickListener  = clickListener;
        this.apartmentID = apartmentID;
    }
    @NonNull
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyViewHolder holder, int position) {
        holder.ownerIDTextView.setText(list.get(position).getOwnerNameSurname());
        holder.productCountTextView.setText("product count: "+list.get(position).getProductCount().toString());
        holder.orderDateTextView.setText(list.get(position).getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clickListener.onItemClick(list.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Products");
                String[] items = new String[list.get(position).getShopList().size()];

                for (int i =0;i<list.get(position).getShopList().size();i++){
                    items[i]=list.get(position).getShopList().get(i).getCount()+" "+list.get(position).getShopList().get(i).getName();
                }

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //listeden birine tıklanırsa diye
                    }
                });
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("Orders"+apartmentID).document(list.get(position).getDate())
                                .update("productStatus","delivered");
                        list.remove(position);
                        notifyItemRemoved(position);
                        //notifyItemRangeChanged(position, mDataSet.size()); ???
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ownerIDTextView;
        TextView productCountTextView;
        TextView orderDateTextView;

        public MyViewHolder(View view) {
            super(view);
            ownerIDTextView = view.findViewById(R.id.ownerIDTextView);
            productCountTextView = view.findViewById(R.id.productCountTextView);
            orderDateTextView = view.findViewById(R.id.orderDateTextView);
        }

    }

    public interface ItemClickListener {

        //public void onItemClick(DataModel dataModel);
    }
}
