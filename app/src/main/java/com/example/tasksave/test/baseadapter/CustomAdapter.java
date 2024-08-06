package com.example.tasksave.test.baseadapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.tasksave.R;
import com.example.tasksave.test.activities.ActivityItemSelectedAgenda;
import com.example.tasksave.test.dao.AgendaDAO;

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
    private ArrayList<Integer> AgendaID;
    boolean[] isReminderSet;

    boolean[] RepetirLembrete;
    boolean[] notificouTarefa;

    private ArrayList<Integer> RepetirLembreteModo;

    LayoutInflater inflater;
    private Dialog dialog;
    private boolean showCheckboxes = false;

    private int selectedCount = 0;
    private OnItemSelectionChangedListener selectionChangedListener;
    private OnItemActionListener itemActionListener;

    private ArrayList<Long> selectedIds = new ArrayList<>();
    public interface OnItemActionListener {
        void onItemDeleted(int position);
        void onItemUpdated(int position);
    }


    public CustomAdapter(Context context,ArrayList<Integer> IDAgenda, String[] TituloAgenda,
                         String[] DescricaoAgenda, String[] DataAgenda, String[] HoraAgenda,
                         boolean[] isReminderSet, boolean[] RepetirLembrete, ArrayList<Integer> RepetirLembreteModo, boolean[] notificouTarefa ) {
        this.context = context;
        AgendaID = IDAgenda;
        this.AgendaTitulo = TituloAgenda;
        this.AgendaDescricao = DescricaoAgenda;
        this.AgendaData = DataAgenda;
        this.AgendaHora = HoraAgenda;
        this.isReminderSet = isReminderSet;
        this.RepetirLembrete = RepetirLembrete;
        this.RepetirLembreteModo = RepetirLembreteModo;
        this.notificouTarefa = notificouTarefa;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        itemActionListener = listener;
    }

    private void deleteItem(int position) {
        // Implemente a lógica para excluir o item

        if (itemActionListener != null) {
            itemActionListener.onItemDeleted(position);
        }
    }

    private void updateItem(int position) {
        // Implemente a lógica para atualizar o item

        if (itemActionListener != null) {
            itemActionListener.onItemUpdated(position);
        }
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

    public Boolean getRepetirLembrete(int position) {
        return RepetirLembrete[position];
    }

    public int getRepetirModoLembrete(int position) {
        return RepetirLembreteModo.get(position);
    }
    public Boolean getNotificouTarefa(int position) {
        return notificouTarefa[position];
    }


    public void setShowCheckboxes(boolean showCheckboxes) {
        this.showCheckboxes = showCheckboxes;
        notifyDataSetChanged(); // Notifica o adapter sobre a mudança na exibição
    }

    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
    public boolean isShowCheckboxes() {
        return showCheckboxes;
    }
    public void setOnItemSelectionChangedListener(OnItemSelectionChangedListener listener) {
        this.selectionChangedListener = listener;
    }

    // Interface para notificar a atividade sobre a mudança na seleção de itens
    public interface OnItemSelectionChangedListener {
        void onItemSelectionChanged(boolean hasSelection);
    }

    public void toggleItemSelection(long itemId) {
        if (selectedIds.contains(itemId)) {
            selectedIds.remove(itemId);
        } else {
            selectedIds.add(itemId);
        }
        notifyDataSetChanged(); // Notifica o adaptador sobre a mudança na seleção
    }

    public ArrayList<Long> getSelectedIds() {
        return selectedIds;
    }

    // Método para limpar a lista de itens selecionados
    public void clearSelectedIds() {
        selectedIds.clear();
        notifyDataSetChanged(); // Notifica o adaptador sobre a mudança na seleção
    }
    public void selectAllItems(boolean isSelected) {
        selectedIds.clear();
        if (isSelected) {
            for (long id : AgendaID) {
                selectedIds.add(id);
            }
        }
        notifyDataSetChanged();
    }


    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_item_agenda, parent, false);
        }
        TextView text_view_tit_agenda = convertView.findViewById(R.id.text_view_titulo_agenda);
        TextView text_view_desc_agenda = convertView.findViewById(R.id.text_view_descricao_agenda);
        TextView text_view_dat_agenda = convertView.findViewById(R.id.text_view_data_agenda);
        TextView text_view_hr_agenda = convertView.findViewById(R.id.text_view_hora_agenda);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox1);
        ImageView imageViewBarra = convertView.findViewById(R.id.imageViewpalito);



//        final View yourView = convertView.findViewById(R.id.your_view);

        checkBox.setVisibility(showCheckboxes ? View.VISIBLE : View.GONE);

        if (checkBox.getVisibility() == View.VISIBLE) {

            ImageView imageViewPalito = convertView.findViewById(R.id.imageViewpalito);
            imageViewPalito.setVisibility(View.GONE);

        } else {
            // Se não estiver visível, define a visibilidade como VISIBLE para exibi-la

            ImageView imageViewPalito = convertView.findViewById(R.id.imageViewpalito);
            imageViewPalito.setVisibility(View.VISIBLE);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {

                            selectedCount++;



                        } else {

                            selectedCount--;

                        }

                if (selectionChangedListener != null) {
                        selectionChangedListener.onItemSelectionChanged(selectedCount > 0);
                    }

                        text_view_tit_agenda.invalidate();
                        text_view_desc_agenda.invalidate();
                        text_view_dat_agenda.invalidate();
                        text_view_hr_agenda.invalidate();

                    }

            });
            if (checkBox.getVisibility() == View.GONE) {
                    checkBox.setChecked(false);
                }

            text_view_tit_agenda.setText(AgendaTitulo[position]);
            text_view_desc_agenda.setText(AgendaDescricao[position]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CheckBox checkBox = v.findViewById(R.id.checkbox1);
                    if (checkBox.getVisibility() == View.VISIBLE) {
                        // Execute a lógica do CheckBox
                        checkBox.setChecked(!checkBox.isChecked());
                        toggleItemSelection(AgendaID.get(position));

                    } else {
                        // Execute a lógica do item

                        int idTarefa = AgendaID.get(position);
                        String titulo = getItemTitulo(position).toString();
                        String descricao = getItemDescricao(position).toString();
                        String data = getItemData(position).toString();
                        String hora = getItemHora(position).toString();
                        boolean lembrete = getItemLembrete(position);
                        boolean repetirLembrete = getRepetirLembrete(position);
                        int repetirModoLembrete = getRepetirModoLembrete(position);

                        Intent intent = new Intent(context, ActivityItemSelectedAgenda.class);
                        intent.putExtra("idTarefa", idTarefa);
                        intent.putExtra("tituloIntent", titulo);
                        intent.putExtra("descIntent", descricao);
                        intent.putExtra("dataIntent", data);
                        intent.putExtra("horaIntent", hora);
                        intent.putExtra("lembreteIntent", lembrete);
                        intent.putExtra("repetirLembreteIntent", repetirLembrete);
                        intent.putExtra("repetirModoIntent", repetirModoLembrete);
                        Log.d("lembrete", "lembrete: "+lembrete);
                        context.startActivity(intent);

                    }
                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        return onItemLongClickListener.onItemLongClick(position);
                    }
                    return false;
                }
            });

        }

            if (!isReminderSet[position]) {

                text_view_dat_agenda.setVisibility(View.GONE);
                text_view_hr_agenda.setVisibility(View.GONE);


            } else if(!RepetirLembrete[position]) {

                try {

                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

                    Calendar calendarDataAtual = Calendar.getInstance();
                    calendarDataAtual.set(Calendar.SECOND, 0);
                    calendarDataAtual.set(Calendar.HOUR_OF_DAY, 0);
                    calendarDataAtual.set(Calendar.MINUTE, 0);
                    Date calendardataAtual2 = calendarDataAtual.getTime();

                    Date date = originalFormat.parse(AgendaData[position]);

                    @SuppressLint({"NewApi", "LocalSuppress"})
                    LocalDate LocaldatedataCombinada = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    @SuppressLint({"NewApi", "LocalSuppress"})
                    LocalDate LocaldatedataAtual = LocalDate.now();


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

//                    boolean repetirLembreteBL = getRepetirLembrete(position);

                    // Verifique se a data da agenda é amanhã em relação à data atual
                    Calendar currentCalendar = Calendar.getInstance();
                    if (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                            calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR) + 1) {
                        text_view_dat_agenda.setText("Amanhã");


                    } else if (dataHoraCombinada.before(dataAtual) && LocaldatedataAtual.isEqual(LocaldatedataCombinada)) {

                        AgendaDAO agendaDAO = new AgendaDAO(context);
                        long idTarefa = AgendaID.get(position);

                        String formattedDate = targetFormat.format(date);
                        text_view_dat_agenda.setText(formattedDate);
                        Drawable DrawableClock = ContextCompat.getDrawable(context, R.drawable.baseline_schedule_24_red);
                        text_view_hr_agenda.setCompoundDrawablesWithIntrinsicBounds(DrawableClock, null, null, null);
                        imageViewBarra.setImageResource(R.drawable.removeorange);


                        boolean statusAgenda = agendaDAO.atualizarTarefaPendente(idTarefa, 1);

                    } else if (LocaldatedataAtual.isAfter(LocaldatedataCombinada)) {

                        AgendaDAO agendaDAO = new AgendaDAO(context);
                        long idTarefa = AgendaID.get(position);

                        String formattedDate = targetFormat.format(date);
                        text_view_dat_agenda.setText(formattedDate);
                        Drawable seuDrawable = ContextCompat.getDrawable(context, R.drawable.baseline_calendar_month_24_red);
                        text_view_dat_agenda.setCompoundDrawablesWithIntrinsicBounds(seuDrawable, null, null, null);
                        Drawable seuDrawable1 = ContextCompat.getDrawable(context, R.drawable.baseline_schedule_24_red2);
                        text_view_hr_agenda.setCompoundDrawablesWithIntrinsicBounds(seuDrawable1, null, null, null);
                        imageViewBarra.setImageResource(R.drawable.removered);


                        boolean statusAgenda = agendaDAO.atualizarTarefaPendente(idTarefa, 2);


                    } else if (LocaldatedataAtual.isEqual(LocaldatedataCombinada) || LocaldatedataAtual.isBefore(LocaldatedataCombinada)) {

                        String formattedDate = targetFormat.format(date);
                        text_view_dat_agenda.setText(formattedDate);
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                text_view_hr_agenda.setText(AgendaHora[position]);

            } else if(RepetirLembrete[position]) {

                try {

                    SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = originalFormat.parse(AgendaData[position]);
                    String formattedDate = targetFormat.format(date);
                    text_view_dat_agenda.setText(formattedDate);
                    text_view_hr_agenda.setText(AgendaHora[position]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return convertView;
        }




}

