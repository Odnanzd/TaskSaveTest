package com.example.tasksave;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomAdapter extends BaseAdapter {


    Context context;
    String AgendaTitulo[];
    String AgendaDescricao[];
    String AgendaData[];
    String AgendaHora[];
    private ArrayList<Long> AgendaID;
    boolean[] isReminderSet;
    LayoutInflater inflater;


    public CustomAdapter(Context context,ArrayList<Long> IDAgenda, String[] TituloAgenda,
                         String[] DescricaoAgenda, String[] DataAgenda, String[] HoraAgenda,
                         boolean[] isReminderSet) {
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
        return AgendaTitulo.length;
    }


    @Override
    public long getItemId(int position) {
        return AgendaID.get(position);
    }

    @Override
    public Object getItem(int position) {
        return position;
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
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.layout_activity, null);
        TextView text_view_tit_agenda = convertView.findViewById(R.id.text_view_titulo_agenda);
        TextView text_view_desc_agenda = convertView.findViewById(R.id.text_view_descricao_agenda);
        TextView text_view_dat_agenda = convertView.findViewById(R.id.text_view_data_agenda);
        TextView text_view_hr_agenda = convertView.findViewById(R.id.text_view_hora_agenda);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox1);

        text_view_tit_agenda.setText(AgendaTitulo[position]);
        text_view_desc_agenda.setText(AgendaDescricao[position]);
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(position);
                    return true;
                }
                return false;
            }
        });


        if (isReminderSet[position]) {
            try {
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

                Calendar calendarDataAtual = Calendar.getInstance();
                calendarDataAtual.set(Calendar.SECOND, 0);
                calendarDataAtual.set(Calendar.HOUR_OF_DAY, 0);
                calendarDataAtual.set(Calendar.MINUTE, 0);
                Date calendardataAtual2 = calendarDataAtual.getTime();
                Log.d("Verificação","Data sem horario:" + calendardataAtual2);

                Date date = originalFormat.parse(AgendaData[position]);
                Log.d("Verificação","Data da Agenda[position]:" + date);

                @SuppressLint({"NewApi", "LocalSuppress"})
                LocalDate LocaldatedataCombinada = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                @SuppressLint({"NewApi", "LocalSuppress"})
                LocalDate LocaldatedataAtual = LocalDate.now();

                Log.d("Verificação","Data da Agenda[position]:" + LocaldatedataCombinada + "Data atual local date"+LocaldatedataAtual);

                Calendar calendarAtual = Calendar.getInstance();
                calendarAtual.set(Calendar.SECOND, 0);
                Date dataAtual = calendarAtual.getTime();

                String horarioString = AgendaHora[position];
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date horarioDate = sdf.parse(horarioString);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR_OF_DAY, horarioDate.getHours());
                calendar.add(Calendar.MINUTE, horarioDate.getMinutes());
                Date dataHoraCombinada = calendar.getTime();

                Log.d("Verificação","Datas com horarios:" + dataHoraCombinada + dataAtual);

                // Verifique se a data da agenda é amanhã em relação à data atual
                Calendar currentCalendar = Calendar.getInstance();
                if (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                        calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR) + 1) {
                    text_view_dat_agenda.setText("Amanhã");

                }

                else if(dataHoraCombinada.before(dataAtual) && LocaldatedataAtual.isEqual(LocaldatedataCombinada) ) {

                    AgendaDAO agendaDAO = new AgendaDAO(context);
                    String Teste = String.valueOf(AgendaID.get(position));
                    long idTarefa = AgendaID.get(position);
                    Log.d("Hora atrasada", "LocalDate"+Teste);

                    String formattedDate = targetFormat.format(date);
                    text_view_dat_agenda.setText(formattedDate);
                    Drawable seuDrawable1 = ContextCompat.getDrawable(context, R.drawable.baseline_schedule_24_red);
                    text_view_hr_agenda.setCompoundDrawablesWithIntrinsicBounds(seuDrawable1, null, null, null);

                    boolean statusAgenda = agendaDAO.AtualizarStatusAtraso(idTarefa, 1);

                }

                else if(LocaldatedataAtual.isAfter(LocaldatedataCombinada)) {

                    String Teste = String.valueOf(AgendaID.get(position));
                    Log.d("Dia atrasado", "LocalDate"+Teste);
                    String formattedDate = targetFormat.format(date);
                    text_view_dat_agenda.setText(formattedDate);
                    Drawable seuDrawable = ContextCompat.getDrawable(context, R.drawable.baseline_calendar_month_24_red);
                    text_view_dat_agenda.setCompoundDrawablesWithIntrinsicBounds(seuDrawable, null, null, null);
                    Drawable seuDrawable1 = ContextCompat.getDrawable(context, R.drawable.baseline_schedule_24_red2);
                    text_view_hr_agenda.setCompoundDrawablesWithIntrinsicBounds(seuDrawable1, null, null, null);

                } else if(LocaldatedataAtual.isEqual(LocaldatedataCombinada) || LocaldatedataAtual.isBefore(LocaldatedataCombinada)) {

                    Log.d("Verificação IF", "se data atual for antes da agendfada");
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

