package club.tushar.hdwallpaper.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import club.tushar.hdwallpaper.R;
import club.tushar.hdwallpaper.activity.HomeActivity;
import club.tushar.hdwallpaper.databinding.RowHomeBinding;
import club.tushar.hdwallpaper.dto.unPlash.HomeResponseDto;

public class HomeAdapterNew extends RecyclerView.Adapter {

    private Context context;
    private List<HomeResponseDto.Hits> dto;
    private int count = 1;

    public HomeAdapterNew(Context context, List<HomeResponseDto.Hits> dto) {
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
        holder.binding.tvDownload.setText(dto.get(i).getLikes() + "");
        //Picasso.get().load(dto.get(i).getUrl()).into(holder.binding.ivPic);
        holder.binding.tvAuthor.setText("by " + dto.get(i).getUser());
        Glide.with(context).load(dto.get(i).getWebformatURL()).into(holder.binding.ivPic);
        //Log.e("url", dto.get(i).getWebformatURL());

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                HomeActivity.ha.downloadPicture(dto.get(i).getLargeImageURL(), dto.get(i).getId() + "", dto.get(i).getLargeImageURL());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dto.size();
    }

    private class Holder extends RecyclerView.ViewHolder{
        RowHomeBinding binding;

        public Holder(RowHomeBinding rowHomeBinding) {
            super(rowHomeBinding.getRoot());
            binding = rowHomeBinding;
        }
    }
}
