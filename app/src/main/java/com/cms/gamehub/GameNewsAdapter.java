package com.cms.gamehub;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GameNewsAdapter extends ArrayAdapter<GameNews>
{
    public GameNewsAdapter(Context context, List<GameNews> gameNewsList)
    {
        super(context, 0, gameNewsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItemView = convertView;

        if (listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        GameNews currentGN = getItem(position);

        TextView titleTV = listItemView.findViewById(R.id.title_tv);
        titleTV.setText(currentGN.getTitle());

        TextView sectionTV = listItemView.findViewById(R.id.section_tv);
        sectionTV.setText(currentGN.getSectionName());

        TextView timeTV = listItemView.findViewById(R.id.time_tv);
        Date date = new Date(currentGN.getPublicationTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        timeTV.setText(sdf.format(date));


        TextView authorTV = listItemView.findViewById(R.id.author_tv);
        if(currentGN.getAuthor() != "")
        {
            authorTV.setText(currentGN.getAuthor());
        }
        else
        {
            authorTV.setVisibility(View.GONE);
        }

        return listItemView;
    }
}
