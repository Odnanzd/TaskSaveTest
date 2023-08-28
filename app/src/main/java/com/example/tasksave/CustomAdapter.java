package com.example.tasksave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomAdapter extends BaseAdapter {


    Context context;
    String AgendaTitulo[];
    String AgendaDescricao[];
    String AgendaData[];
    String AgendaHora[];
    static long[] AgendaID;
    boolean[] isReminderSet;
    LayoutInflater inflater;


    public CustomAdapter(Context context,long[] IDAgenda, String[] TituloAgenda, String[] DescricaoAgenda, String[] DataAgenda, String[] HoraAgenda, boolean[] isReminderSet) {
        this.context = context;
        AgendaID = IDAgenda;
        this.AgendaTitulo = TituloAgenda;
        this.AgendaDescricao = DescricaoAgenda;
        this.AgendaData = DataAgenda;
        this.AgendaHora = HoraAgenda;
        this.isReminderSet = isReminderSet;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return AgendaID.length;
    }

    @Override
    public Object getItem(int position) {
        return AgendaID[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public Object getItemTitulo(int position) {
        return AgendaTitulo[position];
    }
    public Object getItemDescricao(int position) {
        return AgendaDescricao[position];
    }
    public Object getItemData(int position) {
        return AgendaData[position];
    }
    public Object getItemHora(int position) {
        return AgendaHora[position];
    }
    public Boolean getItemLembrete(int position) {
        return isReminderSet[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.layout_activity, null);
        TextView text_view_tit_agenda = convertView.findViewById(R.id.text_view_titulo_agenda);
        TextView text_view_desc_agenda = convertView.findViewById(R.id.text_view_descricao_agenda);
        TextView text_view_dat_agenda = convertView.findViewById(R.id.text_view_data_agenda);
        TextView text_view_hr_agenda = convertView.findViewById(R.id.text_view_hora_agenda);

        text_view_tit_agenda.setText(AgendaTitulo[position]);
        text_view_desc_agenda.setText(AgendaDescricao[position]);

        if (isReminderSet[position]) {
            try {
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                Date date = originalFormat.parse(AgendaData[position]);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                // Verifique se a data da agenda é amanhã em relação à data atual
                Calendar currentCalendar = Calendar.getInstance();
                if (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                        calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR) + 1) {
                    text_view_dat_agenda.setText("Amanhã");
                } else {
                    String formattedDate = targetFormat.format(date);
                    text_view_dat_agenda.setText(formattedDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            text_view_hr_agenda.setText(AgendaHora[position]);
        } else {
            text_view_dat_agenda.setVisibility(View.GONE);
            text_view_hr_agenda.setVisibility(View.GONE);
        }

        return convertView;
    }
}

