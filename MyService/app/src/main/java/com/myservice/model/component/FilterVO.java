package com.myservice.model.component;

import java.io.Serializable;

public class FilterVO implements Serializable{

    private Integer categoriaID;
    private Integer subCategoriaID;
    private String localizacao;
    private Integer minValor;
    private Integer maxValor;
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

    public Integer getMinValor() {
        return minValor;
    }

    public void setMinValor(Integer minValor) {
        this.minValor = minValor;
    }

    public Integer getMaxValor() {
        return maxValor;
    }

    public void setMaxValor(Integer maxValor) {
        this.maxValor = maxValor;
    }

    public Integer getSubCategoriaID() {
        return subCategoriaID;
    }

    public void setSubCategoriaID(Integer subCategoriaID) {
        this.subCategoriaID = subCategoriaID;
    }
}
