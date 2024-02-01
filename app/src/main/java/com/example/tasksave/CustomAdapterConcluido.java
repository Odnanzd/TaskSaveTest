package com.example.tasksave;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomAdapterConcluido extends BaseAdapter {

    Context context;
    String AgendaTitulo[];
    String AgendaDescricao[];
    String AgendaData[];
    String AgendaHora[];
    String AgendaDatasFim[];
    String AgendaHorasFim[];
    private ArrayList<Long> AgendaID;
    boolean[] isReminderSet;
    LayoutInflater inflater;

    public CustomAdapterConcluido(Context context,ArrayList<Long> IDAgenda, String[] TituloAgenda,
    String[] DescricaoAgenda, String[] DataAgenda, String[] HoraAgenda, boolean[] isReminderSet,
    String[] DatasAgendaFim, String[] HorasAgendaFim ) {
        this.context = context;
        AgendaID = IDAgenda;
        this.AgendaTitulo = TituloAgenda;
        this.AgendaDescricao = DescricaoAgenda;
        this.AgendaData = DataAgenda;
        this.AgendaHora = HoraAgenda;
        this.isReminderSet = isReminderSet;
        this.AgendaDatasFim = DatasAgendaFim;
        this.AgendaHorasFim = HorasAgendaFim;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return AgendaTitulo.length;
    }


    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return AgendaID.get(position);
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
    public Object getItemDataFim(int position) {
        return AgendaDatasFim[position];
    }

    public Object getItemHoraFim(int position) {
        return AgendaHorasFim[position];
    }


    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.activity_layout_agenda_concluido, null);
        TextView text_view_tit_agenda = convertView.findViewById(R.id.text_view_titulo_agenda2);
        TextView text_view_desc_agenda = convertView.findViewById(R.id.text_view_descricao_agenda2);
        TextView text_view_dat_agenda = convertView.findViewById(R.id.text_view_data_agenda2);
        TextView text_view_hr_agenda = convertView.findViewById(R.id.text_view_hora_agenda2);

        text_view_tit_agenda.setText("Tarefa: "+AgendaTitulo[position]);
        text_view_desc_agenda.setText("Descrição: "+AgendaDescricao[position]);

        if (isReminderSet[position]) {
            try {
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                Date date = originalFormat.parse(AgendaData[position]);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                Calendar currentCalendar = Calendar.getInstance();
                if (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                        calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR) + 1) {
                    text_view_dat_agenda.setText("Amanhã");
                } else {
                    String formattedDate = targetFormat.format(date);
                    text_view_dat_agenda.setText("Criado em: "+formattedDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                SimpleDateFormat originalFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat targetFormat2 = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                Date date2 = originalFormat2.parse(AgendaDatasFim[position]);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date2);
                String formattedDate2 = targetFormat2.format(date2);
                text_view_hr_agenda.setText("Concluído em: "+formattedDate2);
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            text_view_hr_agenda.setText("Concluído em: "+AgendaDatasFim[position]);
        } else {
            text_view_dat_agenda.setVisibility(View.GONE);
            text_view_hr_agenda.setVisibility(View.GONE);
        }

        return convertView;
    }
}
