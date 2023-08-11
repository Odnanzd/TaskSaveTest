package com.example.tasksave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {


    Context context;
    String AgendaTitulo[];
    String AgendaDescricao[];
    String AgendaData[];
    String AgendaHora[];
    LayoutInflater inflater;


    public CustomAdapter(Context context, String[] TituloAgenda, String[] DescricaoAgenda, String[] DataAgenda, String[] HoraAgenda) {
        this.context = context;
        this.AgendaTitulo = TituloAgenda;
        this.AgendaDescricao = DescricaoAgenda;
        this.AgendaData = DataAgenda;
        this.AgendaHora = HoraAgenda;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return AgendaTitulo.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.layout_activity, null);
        TextView text_view_tit_agenda = (TextView) convertView.findViewById(R.id.text_view_titulo_agenda);
        TextView text_view_desc_agenda = (TextView) convertView.findViewById(R.id.text_view_descricao_agenda);
        TextView text_view_dat_agenda = (TextView) convertView.findViewById(R.id.text_view_data_agenda);
        TextView text_view_hr_agenda = (TextView) convertView.findViewById(R.id.text_view_hora_agenda);

        text_view_tit_agenda.setText(AgendaTitulo[position]);
        text_view_desc_agenda.setText(AgendaDescricao[position]);
        text_view_dat_agenda.setText(AgendaData[position]);
        text_view_hr_agenda.setText(AgendaHora[position]);

        return convertView;

    }
}

