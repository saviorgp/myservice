package com.myservice.model.component;

import java.io.Serializable;

public class FilterVO implements Serializable{

    private Integer categoriaID;
    private String localizacao;
    private Integer valor;
    private Boolean precoOrder;
    private Boolean dataOrder;
    private Boolean relevanciaOrder;

    public Integer getCategoriaID() {
        return categoriaID;
    }

    public void setCategoriaID(Integer categoriaID) {
        this.categoriaID = categoriaID;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Boolean getPrecoOrder() {
        return precoOrder;
    }

    public void setPrecoOrder(Boolean precoOrder) {
        this.precoOrder = precoOrder;
    }

    public Boolean getDataOrder() {
        return dataOrder;
    }

    public void setDataOrder(Boolean dataOrder) {
        this.dataOrder = dataOrder;
    }

    public Boolean getRelevanciaOrder() {
        return relevanciaOrder;
    }

    public void setRelevanciaOrder(Boolean relevanciaOrder) {
        this.relevanciaOrder = relevanciaOrder;
    }
}
