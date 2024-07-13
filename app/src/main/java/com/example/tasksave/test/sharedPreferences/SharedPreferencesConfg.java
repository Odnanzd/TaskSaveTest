package com.example.tasksave.test.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesConfg {

    private Context context;

    public SharedPreferencesConfg(Context context) {
        this.context = context;
    }

    //Aparência CONFG

    public void armazenaTema(String tema) {

        SharedPreferences prefs0 = context.getSharedPreferences("ArquivoTema", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor0 = prefs0.edit();
        editor0.putString("ArqTemaString", tema);
        editor0.commit();

    }

    public String getTema() {

        SharedPreferences sharedPreferences = context.getSharedPreferences("ArquivoTema", Context.MODE_PRIVATE);
        String temaCarregado = sharedPreferences.getString("ArqTemaString", "Automático");

        return temaCarregado;
    }

    //Notificações CONFG

    public void armazenaNotifica(boolean notifica) {

        SharedPreferences prefs0 = context.getSharedPreferences("ArquivoNotifica", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor0 = prefs0.edit();
        editor0.putBoolean("NaoNotifica", notifica);
        editor0.commit();

    }
    public boolean getNotifica() {

        SharedPreferences sharedPrefs3 = context.getApplicationContext().getSharedPreferences("ArquivoNotifica", Context.MODE_PRIVATE);
        boolean notifica = sharedPrefs3.getBoolean("NaoNotifica", false);

        return notifica;
    }

    //Sobre CONFG

    public void armazenaAtualiza(boolean atualiza) {
        SharedPreferences prefs = context.getSharedPreferences("ArquivoATT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("NaoATT", atualiza);
        editor.commit();
    }

    public boolean getAtualiza() {

        SharedPreferences sharedPrefs3 = context.getApplicationContext().getSharedPreferences("ArquivoATT", Context.MODE_PRIVATE);
        boolean attAuto = sharedPrefs3.getBoolean("NaoATT", false);

        return attAuto;
    }

    public void armazenaTextoAPP(String texto) {
        SharedPreferences prefs0 = context.getSharedPreferences("ArquivoTextoAPP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor0 = prefs0.edit();
        editor0.putString("ATT", texto);
        editor0.commit();
    }

    public String getTextoAPP() {

        SharedPreferences sharedPrefs3 = context.getApplicationContext().getSharedPreferences("ArquivoTextoAPP", Context.MODE_PRIVATE);
        String versaoTextoAPP = sharedPrefs3.getString("ATT", "");

        return versaoTextoAPP;
    }

    public void armazenaTexto1(String texto) {

        SharedPreferences prefs0 = context.getSharedPreferences("ArquivoTexto1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor0 = prefs0.edit();
        editor0.putString("ATT", texto);
        editor0.commit();

    }
    public String getTexto1() {

        SharedPreferences sharedPrefs3 = context.getApplicationContext().getSharedPreferences("ArquivoTexto1", Context.MODE_PRIVATE);
        String versaoTexto1 = sharedPrefs3.getString("ATT", "");

        return versaoTexto1;
    }

    public void armazenaTexto2(String texto) {

        SharedPreferences prefs0 = context.getSharedPreferences("ArquivoTexto2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor0 = prefs0.edit();
        editor0.putString("ATT", texto);
        editor0.commit();

    }
    public String getTexto2() {

        SharedPreferences sharedPrefs3 = context.getApplicationContext().getSharedPreferences("ArquivoTexto2", Context.MODE_PRIVATE);
        String versaoTexto2 = sharedPrefs3.getString("ATT", "");

        return versaoTexto2;
    }
    public void armazenaTexto3(String texto) {

        SharedPreferences prefs0 = context.getSharedPreferences("ArquivoTexto3", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor0 = prefs0.edit();
        editor0.putString("ATT", texto);
        editor0.commit();

    }
    public String getTexto3() {

        SharedPreferences sharedPrefs3 = context.getApplicationContext().getSharedPreferences("ArquivoTexto3", Context.MODE_PRIVATE);
        String versaoTexto3 = sharedPrefs3.getString("ATT", "");

        return versaoTexto3;
    }
    public void armazenaAtualizaDisponivel(boolean atualizadisp) {

        SharedPreferences prefs3 = context.getSharedPreferences("ArquivoAttDisp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = prefs3.edit();
        editor3.putBoolean("Atualizacao", atualizadisp);
        editor3.commit();

    }
    public boolean getAtualizaDisponivel() {

        SharedPreferences sharedPrefs2 = context.getApplicationContext().getSharedPreferences("ArquivoAttDisp", Context.MODE_PRIVATE);
        boolean attDisponivel = sharedPrefs2.getBoolean("Atualizacao", false);

        return attDisponivel;
    }

    //Sair CONFG
    public void clearShareds(){

        String[] sharedPreferencesNames = {"ArquivoTema", "ArquivoNotifica", "ArquivoATT", "ArquivoTextoAPP","ArquivoTexto1",
                "ArquivoTexto2","ArquivoTexto3", "ArquivoAttDisp", "arquivoSalvarSenha", "ArquivoPrimeiroAcesso",
                "arquivoSalvarLoginEmail", "arquivoSalvarLoginSenha", "arquivoSalvarUser", "ArquivoIDCargoUsuario",
                "ArquivoFingerPrint", "ArquivoPrimeiroAcessoFingerPrint"};

        for (String name : sharedPreferencesNames) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply(); // ou editor.commit();
        }

        SharedPreferencesUsuario sharedPreferencesUsuario = new SharedPreferencesUsuario(context);
        Log.d("TESTE CLEAR", "TESTE"+sharedPreferencesUsuario.getUsuarioLogin());
        Log.d("TESTE CLEAR", "TESTE"+sharedPreferencesUsuario.getEmailLogin());

    }
}
