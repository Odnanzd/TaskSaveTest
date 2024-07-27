package com.example.tasksave.test.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUsuario {

    private Context context;

    public SharedPreferencesUsuario(Context context) {
        this.context = context;
    }

    //SHAREDPREFERENCES Usuario
    public void armazenaSalvarSenhaBL(boolean salvarSenha) {

        SharedPreferences prefs3 = context.getSharedPreferences("arquivoSalvarSenha", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = prefs3.edit();
        editor3.putBoolean("SalvarSenha", salvarSenha);
        editor3.apply();

    }
    public void armazenaPrimeiroAcesso(boolean primeiroAcesso) {
        SharedPreferences prefs3 = context.getSharedPreferences("ArquivoPrimeiroAcesso", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = prefs3.edit();
        editor3.putBoolean("PrimeiroAcesso", primeiroAcesso);
        editor3.apply();
    }
    public void armazenaEmailLogin(String emailLogin) {
        SharedPreferences prefs3 = context.getSharedPreferences("arquivoSalvarLoginEmail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = prefs3.edit();
        editor3.putString("arquivo_Email", emailLogin);
        editor3.apply();
    }
    public void armazenaSenhaLogin(String senhaLogin) {
        SharedPreferences prefs3 = context.getSharedPreferences("arquivoSalvarLoginSenha", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = prefs3.edit();
        editor3.putString("arquivo_Senha", senhaLogin);
        editor3.apply();
    }
    public void armazenaUsuarioLogin(String usuarioLogin) {
        SharedPreferences prefs3 = context.getSharedPreferences("arquivoSalvarUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = prefs3.edit();
        editor3.putString("userString", usuarioLogin);
        editor3.apply();
    }
    public void armazenaUsuarioCargo(int cargoUsuario) {
        SharedPreferences prefs3 = context.getSharedPreferences("ArquivoIDCargoUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = prefs3.edit();
        editor3.putInt("idCargoUsuario", cargoUsuario);
        editor3.apply();
    }
    public void armazenaBiometria(boolean biometriaUsuario) {
        SharedPreferences prefs = context.getSharedPreferences("ArquivoFingerPrint", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("AcessoFingerPrint", biometriaUsuario);
        editor.commit();
    }
    public void armazenaPrimeiroAcessoFingerprint(boolean primeiroacesso) {
        SharedPreferences prefs = context.getSharedPreferences("ArquivoPrimeiroAcessoFingerPrint", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("PrimeiroAcessoFingerPrint", primeiroacesso);
        editor.commit();
    }
    public void armazenaPrimeiroAcessoAgenda(boolean primeiroAcessoAgenda) {

        SharedPreferences prefs = context.getSharedPreferences("ArquivoPrimeiroAcessoAgenda", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("PrimeiroAcessoAgenda", primeiroAcessoAgenda);
        editor.commit();

    }

    // GETTERS


    public boolean getSalvarSenha() {

        SharedPreferences sharedPrefs2 = context.getSharedPreferences("arquivoSalvarSenha", Context.MODE_PRIVATE);
        boolean salvaSenha = sharedPrefs2.getBoolean("SalvarSenha", false);

        return salvaSenha;
    }
    public boolean getPrimeiroAcesso() {

        SharedPreferences sharedPrefs2 = context.getSharedPreferences("ArquivoPrimeiroAcesso", Context.MODE_PRIVATE);
        boolean primeiroAcesso = sharedPrefs2.getBoolean("PrimeiroAcesso", false);

        return primeiroAcesso;
    }
    public String getEmailLogin() {

        SharedPreferences sharedPrefs2 = context.getSharedPreferences("arquivoSalvarLoginEmail", Context.MODE_PRIVATE);
        String emailLogin = sharedPrefs2.getString("arquivo_Email", "");

        return emailLogin;
    }
    public String getSenhaLogin() {

        SharedPreferences sharedPrefs2 = context.getSharedPreferences("arquivoSalvarLoginSenha", Context.MODE_PRIVATE);
        String senhaLogin = sharedPrefs2.getString("arquivo_Senha", "");

        return senhaLogin;
    }
    public String getUsuarioLogin() {

        SharedPreferences sharedPrefs2 = context.getSharedPreferences("arquivoSalvarUser", Context.MODE_PRIVATE);
        String usuarioLogin = sharedPrefs2.getString("userString", "");

        return usuarioLogin;
    }

    public int getUsuarioCargo() {

        SharedPreferences sharedPrefs2 = context.getSharedPreferences("ArquivoIDCargoUsuario", Context.MODE_PRIVATE);
        int usuarioCargo = sharedPrefs2.getInt("idCargoUsuario", -1);

        return usuarioCargo;
    }

    public boolean getBiometriaUsuario() {

        SharedPreferences sharedPrefs2 = context.getSharedPreferences("ArquivoFingerPrint", Context.MODE_PRIVATE);
        boolean biometria = sharedPrefs2.getBoolean("AcessoFingerPrint", false);

        return biometria;
    }
    public boolean getPrimeiroAcessoBiometriaUsuario() {

        SharedPreferences sharedPrefs2 = context.getSharedPreferences("ArquivoPrimeiroAcessoFingerPrint", Context.MODE_PRIVATE);
        boolean biometria = sharedPrefs2.getBoolean("PrimeiroAcessoFingerPrint", false);

        return biometria;
    }
    public boolean getPrimeiroAcessoAgenda() {

        SharedPreferences sharedPrefs2 = context.getSharedPreferences("ArquivoPrimeiroAcessoAgenda", Context.MODE_PRIVATE);
        boolean agenda = sharedPrefs2.getBoolean("PrimeiroAcessoAgenda", false);

        return agenda;

    }


}
