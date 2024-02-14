package com.example.tasksave;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;
import static androidx.core.content.ContextCompat.startActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private boolean showCheckboxes = false;

    private int selectedCount = 0;
    private OnItemSelectionChangedListener selectionChangedListener;
    private OnItemActionListener itemActionListener;

    private ArrayList<Long> selectedIds = new ArrayList<>();
    public interface OnItemActionListener {
        void onItemDeleted(int position);
        void onItemUpdated(int position);
    }


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
            convertView = inflater.inflate(R.layout.layout_activity, parent, false);
        }
        TextView text_view_tit_agenda = convertView.findViewById(R.id.text_view_titulo_agenda);
        TextView text_view_desc_agenda = convertView.findViewById(R.id.text_view_descricao_agenda);
        TextView text_view_dat_agenda = convertView.findViewById(R.id.text_view_data_agenda);
        TextView text_view_hr_agenda = convertView.findViewById(R.id.text_view_hora_agenda);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox1);
        ImageView imageViewBarra = convertView.findViewById(R.id.imageViewpalito);

        final View yourView = convertView.findViewById(R.id.your_view);

        checkBox.setVisibility(showCheckboxes ? View.VISIBLE : View.GONE);

        if (checkBox.getVisibility() == View.VISIBLE) {

            ImageView imageViewPalito = convertView.findViewById(R.id.imageViewpalito);
            imageViewPalito.setVisibility(View.GONE);

        } else {
            // Se não estiver visível, define a visibilidade como VISIBLE para exibi-la

            ImageView imageViewPalito = convertView.findViewById(R.id.imageViewpalito);
            imageViewPalito.setVisibility(View.VISIBLE);

            Paint paintTitulo = text_view_tit_agenda.getPaint();
            Paint paintData = text_view_dat_agenda.getPaint();
            Paint paintHora = text_view_hr_agenda.getPaint();
            Paint paintDesc = text_view_desc_agenda.getPaint();

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {

                            selectedCount++;

                            float radiusTitulo = text_view_tit_agenda.getTextSize() / 6;

                            float radiusData = text_view_dat_agenda.getTextSize() / 6;

                            float radiusHora = text_view_hr_agenda.getTextSize() / 6;

                            float radiusDesc = text_view_desc_agenda.getTextSize() / 6;

                            BlurMaskFilter filter = new BlurMaskFilter(radiusTitulo, BlurMaskFilter.Blur.NORMAL);
                            paintTitulo.setMaskFilter(filter);

                            BlurMaskFilter filter2 = new BlurMaskFilter(radiusDesc, BlurMaskFilter.Blur.NORMAL);
                            paintDesc.setMaskFilter(filter2);

                            BlurMaskFilter filter3 = new BlurMaskFilter(radiusData, BlurMaskFilter.Blur.NORMAL);
                            paintData.setMaskFilter(filter3);

                            BlurMaskFilter filter4 = new BlurMaskFilter(radiusHora, BlurMaskFilter.Blur.NORMAL);
                            paintHora.setMaskFilter(filter4);

                            yourView.setVisibility(View.VISIBLE);
                    yourView.post(new Runnable() {
                        @Override
                        public void run() {
                            AnimatorSet animatorSet = new AnimatorSet();
                            ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(context, R.animator.linhacrescente);
                            objectAnimator.setTarget(yourView);
                            animatorSet.play(objectAnimator);
                            animatorSet.start();
                        }
                    });

                        } else {

                            selectedCount--;
                            paintTitulo.setMaskFilter(null);
                            paintDesc.setMaskFilter(null);
                            paintData.setMaskFilter(null);
                            paintHora.setMaskFilter(null);
                    yourView.setVisibility(View.GONE);

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
//                        Intent intent = new Intent(context, activity_item_selected_agenda.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        long idTarefa = AgendaID.get(position);
                        String titulo = getItemTitulo(position).toString();
                        String descricao = getItemDescricao(position).toString();
                        String data = getItemData(position).toString();
                        String hora = getItemHora(position).toString();
                        boolean lembrete = getItemLembrete(position);
//                        AgendaDAO agendaDAO = new AgendaDAO(v.getContext());
//
//                        activity_item_selected_agenda customDialog = new activity_item_selected_agenda(v.getContext(), titulo,
//                                descricao, idTarefa, data, hora, lembrete, agendaDAO );
//
//                        customDialog.show();

                        Dialog dialog = new Dialog(context, R.style.DialogAboveKeyboard2);
                        dialog.setContentView(R.layout.activity_item_selected_agenda); // Defina o layout do diálogo
                        dialog.setCancelable(true); // Permita que o usuário toque fora do diálogo para fechá-lo
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                        TextView tituloTextView = dialog.findViewById(R.id.titulo_text_view);
                        TextView descricaoTextView = dialog.findViewById(R.id.descricao_text_view);
                        TextView dataTextView = dialog.findViewById(R.id.textView11);
                        TextView horaTextView = dialog.findViewById(R.id.textView12);
                        Button button = dialog.findViewById(R.id.button2);
                        Button button2 = dialog.findViewById(R.id.button);
                        EditText editTextTitulo = dialog.findViewById(R.id.titulo_text_view);
                        EditText editTextDescricao = dialog.findViewById(R.id.descricao_text_view);
                        TextView textViewContador = dialog.findViewById(R.id.text_view_contador1);
                        TextView textViewContador2 = dialog.findViewById(R.id.text_view_contador2);
                        CheckBox checkboxConcluido = dialog.findViewById(R.id.checkBoxConcluido);
                        ImageView imageView = dialog.findViewById(R.id.imageView4);
                        TextView textViewLembrete = dialog.findViewById(R.id.textViewLembretenaodefinido);

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
                        LocalDate localdataEscolhida = LocalDate.parse(data);
                        String dataFormatada = localdataEscolhida.format(formatter);

                        int tamanhoTitulo = titulo.length();
                        textViewContador.setText(tamanhoTitulo + "/14");


                        int tamanhoDescricao = descricao.length();
                        textViewContador2.setText(tamanhoDescricao + "/20");

                        tituloTextView.setText(titulo);
                        descricaoTextView.setText(descricao);

                        if(lembrete) {
                            dataTextView.setText(dataFormatada);
                            horaTextView.setText(hora);
                            textViewLembrete.setVisibility(View.GONE);
                        }else {
                            textViewLembrete.setVisibility(View.VISIBLE);
                            textViewLembrete.setText("Lembrete não definido");
                            dataTextView.setVisibility(View.GONE);
                            horaTextView.setVisibility(View.GONE);
                        }

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        checkboxConcluido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {

                                    editTextDescricao.setEnabled(false);
                                    editTextDescricao.setTextColor(ContextCompat.getColor(context, R.color.grey2));
                                    editTextTitulo.setEnabled(false);
                                    editTextTitulo.setTextColor(ContextCompat.getColor(context, R.color.grey2));
                                    dataTextView.setEnabled(false);
                                    horaTextView.setEnabled(false);
                                    tituloTextView.setText(titulo);
                                    descricaoTextView.setText(descricao);
                                    button.setEnabled(true);
                                    button.setText("Concluir");

                                } else {
                                    editTextDescricao.setEnabled(true);
                                    editTextDescricao.setTextColor(ContextCompat.getColor(context, R.color.white));
                                    editTextTitulo.setEnabled(true);
                                    editTextTitulo.setTextColor(ContextCompat.getColor(context, R.color.white));
                                    dataTextView.setEnabled(true);
                                    horaTextView.setEnabled(true);
                                    button.setEnabled(false);
                                    button.setText("Atualizar");
                                }
                            }
                        });

                        editTextTitulo.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                // Não é necessário implementar nada aqui
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // Verifica se o EditText não está vazio
                                String novoTitulo = s.toString();
                                boolean saoIguais = novoTitulo.equals(titulo);

                                if (s.length() > 0 && !saoIguais) {
                                    button.setEnabled(true);
                                } else {
                                    button.setEnabled(false);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                // Não é necessário implementar nada aqui
                            }
                        });
                        editTextDescricao.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                // Não é necessário implementar nada aqui
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // Verifica se o EditText não está vazio
                                String novoDes = s.toString();
                                boolean saoIguais = novoDes.equals(descricao);

                                if (s.length() > 0 && !saoIguais) {
                                    button.setEnabled(true);
                                } else {
                                    button.setEnabled(false);
                                }
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                                // Não é necessário implementar nada aqui
                            }
                        });

                        AgendaDAO agendaDAO = new AgendaDAO(context);
                        button.setOnClickListener(new View.OnClickListener() {


                            @Override
                            public void onClick(View v) {

                                if(checkboxConcluido.isChecked()) {

                                    Calendar calendar = Calendar.getInstance();
                                    int horasFim = calendar.get(Calendar.HOUR_OF_DAY);
                                    int minutosFim = calendar.get(Calendar.MINUTE);
                                    @SuppressLint({"NewApi", "LocalSuppress"})
                                    LocalDate dataAtual = LocalDate.now();

                                    boolean finalizado = agendaDAO.AtualizarStatus(idTarefa, 1, dataAtual, horasFim, minutosFim);

                                    if (finalizado) {

                                        Toast.makeText(context, "Tarefa concluída.", Toast.LENGTH_SHORT).show();
                                        int clickedPosition = position;
                                        updateItem(clickedPosition);
                                        dialog.dismiss();

                                    } else {
                                        // Algo deu errado na atualização
                                        Toast.makeText(context, "Erro ao atualizar a tarefa", Toast.LENGTH_SHORT).show();
                                    }


                                } else {

                                    String novoTitulo = editTextTitulo.getText().toString();
                                    String novaDescricao = editTextDescricao.getText().toString();

                                    // Aqui você deve pegar o ID da tarefa (que você passou como um extra na Intent)

                                    // Atualize os valores no banco de dados

//                    AgendaDAO agendaDAO = new AgendaDAO(activity_item_selected_agenda.this);
                                    boolean atualizado = agendaDAO.Atualizar(idTarefa, novoTitulo, novaDescricao);

                                    if (atualizado) {
                                        // Atualização bem-sucedida
                                        Toast.makeText(context, "Tarefa atualizada.", Toast.LENGTH_SHORT).show();
                                        int clickedPosition = position;
                                        updateItem(clickedPosition);
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(context, "Erro ao atualizar a tarefa", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        button2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder msgbox = new AlertDialog.Builder(context);
                                msgbox.setTitle("Excluir");
                                msgbox.setIcon(android.R.drawable.ic_menu_delete);
                                msgbox.setMessage("Você realmente deseja excluir a tarefa?");
                                msgbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog2, int which) {

                                        boolean excluir = agendaDAO.Excluir(idTarefa);
                                        Toast.makeText(context, "Tarefa Excluida.", Toast.LENGTH_SHORT).show();
                                        int clickedPosition = position;
                                        deleteItem(clickedPosition);
                                        dialog.dismiss();
                                    }
                                });
                                msgbox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                msgbox.show();
                            }
                        });
                        editTextTitulo.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                // Atualizar o contador de caracteres
                                int currentLength = charSequence.length();
                                textViewContador.setText(currentLength + "/14");
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                // Nada a fazer depois da mudança do texto
                            }
                        });
                        editTextDescricao.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                // Nada a fazer antes da mudança do texto
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                // Atualizar o contador de caracteres
                                int currentLength = charSequence.length();
                                textViewContador2.setText(currentLength + "/20");
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                // Nada a fazer depois da mudança do texto
                            }
                        });

                        dialog.show();
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

            if (isReminderSet[position]) {
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

                        boolean statusAgenda = agendaDAO.AtualizarStatusAtraso(idTarefa, 1);

                        if(statusAgenda) {
                            notifyDataSetChanged();
                        }

                    } else if (LocaldatedataAtual.isAfter(LocaldatedataCombinada)) {

                        String formattedDate = targetFormat.format(date);
                        text_view_dat_agenda.setText(formattedDate);
                        Drawable seuDrawable = ContextCompat.getDrawable(context, R.drawable.baseline_calendar_month_24_red);
                        text_view_dat_agenda.setCompoundDrawablesWithIntrinsicBounds(seuDrawable, null, null, null);
                        Drawable seuDrawable1 = ContextCompat.getDrawable(context, R.drawable.baseline_schedule_24_red2);
                        text_view_hr_agenda.setCompoundDrawablesWithIntrinsicBounds(seuDrawable1, null, null, null);
                        imageViewBarra.setImageResource(R.drawable.removered);

                    } else if (LocaldatedataAtual.isEqual(LocaldatedataCombinada) || LocaldatedataAtual.isBefore(LocaldatedataCombinada)) {

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

