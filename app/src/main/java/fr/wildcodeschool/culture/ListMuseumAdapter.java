package fr.wildcodeschool.culture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ListMuseumAdapter extends ArrayAdapter<Museum> {

    public ListMuseumAdapter(Context context, List<Museum> museum) {
        super(context, 0, museum);

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Museum museum = getItem(position);

        if (null == convertView) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_list_view_elements, parent, false);
        }

        TextView name = convertView.findViewById(R.id.tvAdresse);
        TextView numero = convertView.findViewById(R.id.tvDescriptif);
        TextView horaires = convertView.findViewById(R.id.tvHoraires);
        TextView site = convertView.findViewById(R.id.tvName);
        TextView metro = convertView.findViewById(R.id.tvMetro);
        Button favorite = convertView.findViewById(R.id.button);

        name.setText(museum.getName());
        numero.setText(museum.getNumero());
        horaires.setText(museum.getHoraires());
        site.setText(museum.getSite());
        metro.setText(museum.getMetro());
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                Museum favorites = new Museum(museum.getName(),museum.getNumero(),museum.getHoraires(),museum.getSite(),museum.getMetro());
                DatabaseReference favoritesRef = database.getReference("favorites");
                favoritesRef.push().setValue(favorites);

            }
        });

        return convertView;
    }
}
