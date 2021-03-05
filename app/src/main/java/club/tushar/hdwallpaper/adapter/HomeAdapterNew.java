package club.tushar.hdwallpaper.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import club.tushar.hdwallpaper.R;
import club.tushar.hdwallpaper.activity.HomeActivity;
import club.tushar.hdwallpaper.databinding.RowHomeBinding;
import club.tushar.hdwallpaper.dto.pixels.PixelsResponse;
import club.tushar.hdwallpaper.dto.unPlash.HomeResponseDto;

public class HomeAdapterNew extends RecyclerView.Adapter {

    private Context context;
    private PixelsResponse dto;
    private int count = 1;

    public HomeAdapterNew(Context context, PixelsResponse dto) {
        this.context = context;
        this.dto = dto;
        this.count = count;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowHomeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_home, null, true);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, final int i) {
        Holder holder = (Holder) h;
        holder.binding.tvAuthor.setText("by " + dto.getPhotos().get(i).getPhotographer());
        try{
            holder.binding.ll.setBackgroundColor(Color.parseColor(dto.getPhotos().get(i).getAvgColor()));
        }catch (Exception e){
            Log.e("coll,or", dto.getPhotos().get(i).getAvgColor());
        }

        Glide.with(context).load(dto.getPhotos().get(i).getSrc().getLarge()).into(holder.binding.ivPic);


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                HomeActivity.ha.downloadPicture(dto.getPhotos().get(i));
            }
        });

    }

    @Override
    public int getItemCount() {
        return dto.getPhotos().size();
    }

    private class Holder extends RecyclerView.ViewHolder{
        RowHomeBinding binding;

        public Holder(RowHomeBinding rowHomeBinding) {
            super(rowHomeBinding.getRoot());
            binding = rowHomeBinding;
        }
    }
}
