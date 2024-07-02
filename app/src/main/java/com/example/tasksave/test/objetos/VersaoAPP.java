package com.example.tasksave.test.objetos;

public class VersaoAPP {
    private String versionDB;
    private String versionTextApp;
    private String versionText1;
    private String versionText2;
    private String versionText3;

    // Construtor
    public VersaoAPP(String versionDB, String versionTextApp, String versionText1, String versionText2, String versionText3) {

        this.versionDB = versionDB;
        this.versionTextApp = versionTextApp;
        this.versionText1 = versionText1;
        this.versionText2 = versionText2;
        this.versionText3 = versionText3;

    }

    // Getters e Setters
    public String getVersionDB() { return versionDB; }
    public String getVersionTextApp() { return versionTextApp; }
    public String getVersionText1() { return versionText1; }
    public String getVersionText2() { return versionText2; }
    public String getVersionText3() { return versionText3; }
}