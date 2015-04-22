package hu.exclusive.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the t_function database table.
 * 
 */
@Entity
@Table(name = "T_FUNCTION")
@NamedQuery(name = "Function.findAll", query = "SELECT f FROM Function f ORDER BY f.functionCode, f.functionName")
public class Function extends EntityCommons implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_function")
    private Integer idFunction;

    @Column(name = "function_code")
    private String functionCode;

    @Column(name = "function_name")
    private String functionName;

    @Column(name = "function_url")
    private String functionUrl;

    @Transient
    private boolean selected;

    public Function() {
    }

    @Override
    public Integer getId() {
        return getIdFunction();
    }

    public Integer getIdFunction() {
        return this.idFunction;
    }

    public void setIdFunction(Integer idFunction) {
        this.idFunction = idFunction;
    }

    public String getFunctionCode() {
        return this.functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionUrl() {
        return this.functionUrl;
    }

    public void setFunctionUrl(String functionUrl) {
        this.functionUrl = functionUrl;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}