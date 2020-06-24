package com.code93.lectorcedulacolombia_zxing_android;

import java.io.Serializable;

/**
 * **************************************************************
 * Autor:		Carlos Arturo Reyes Romero
 * email:		carr900@gmail.com
 * ****************************************************************
 */
public class InfoTarjeta implements Serializable {

    private String primerApellido;
    private String segundoApellido="";
    private String primerNombre="";
    private String segundoNombre="";
    private String cedula="";
    private String rh="";
    private String fechaNacimiento="";
    private String sexo="";

    @Override
    public String toString() {
        return "com.code93.lectorcedulacolombia_zxing_android.InfoTarjeta{" +
                "primerApellido='" + primerApellido + '\'' +
                ", segundoApellido='" + segundoApellido + '\'' +
                ", primerNombre='" + primerNombre + '\'' +
                ", segundoNombre='" + segundoNombre + '\'' +
                ", cedula='" + cedula + '\'' +
                ", rh='" + rh + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", sexo='" + sexo + '\'' +
                '}';
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getRh() {
        return rh;
    }

    public void setRh(String rh) {
        this.rh = rh;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
