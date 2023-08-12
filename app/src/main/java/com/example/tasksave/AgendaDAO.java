package com.example.tasksave;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class AgendaDAO {

        private Conexao con;
        private SQLiteDatabase db;

        public AgendaDAO(Context context) {

            con = new Conexao(context);
            db = con.getWritableDatabase();

        }

    public long inserir(Agenda agenda) {

        ContentValues contentValues = new ContentValues();

        contentValues.put("nomeTarefa", agenda.getNomeAgenda());
        contentValues.put("descricaoTarefa", agenda.getDescriçãoAgenda());
        contentValues.put("dataAgenda", agenda.getDataAgendaString());
        contentValues.put("horaAgenda", agenda.getHoraAgenda());
        contentValues.put("minutoAgenda", agenda.getMinutoAgenda());
        contentValues.put("lembretedefinido", agenda.getLembrete());

        return db.insert("agenda", null, contentValues);

    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public List<Agenda> listardadosAgenda() {
//
//        List<Agenda> listaagenda = new ArrayList<Agenda>();
//
//      Cursor cursor = db.rawQuery("SELECT * FROM agenda;", null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range")
//                String titulo = cursor.getString(cursor.getColumnIndex("nomeTarefa"));
//                @SuppressLint("Range")
//                String descricao = cursor.getString(cursor.getColumnIndex("descricaoTarefa"));
//                @SuppressLint("Range")
//                String dataagenda = cursor.getString(cursor.getColumnIndex("dataAgenda"));
//                @SuppressLint("Range")
//                int horaagenda = cursor.getInt(cursor.getColumnIndex("horaAgenda"));
//                @SuppressLint("Range")
//                int minutoagenda = cursor.getInt(cursor.getColumnIndex("minutoAgenda"));
//
//                LocalDate localdataagenda = LocalDate.parse(dataagenda, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//                listaagenda.add(new Agenda(titulo, descricao, localdataagenda, horaagenda, minutoagenda));
//
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//
//        return listaagenda;
//
//    }
}
