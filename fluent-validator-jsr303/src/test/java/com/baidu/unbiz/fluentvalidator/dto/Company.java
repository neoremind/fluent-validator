package com.baidu.unbiz.fluentvalidator.dto;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.baidu.unbiz.fluentvalidator.grouping.AddCompany;

/**
 * 公司DTO
 *
 * @author zhangxu
 */
public class Company extends BaseObject {

    @NotEmpty
    @Pattern(regexp = "[0-9a-zA-Z\4e00-\u9fa5]+")
    private String name;

    @NotNull
    private Date establishTime;

    @NotNull
    @Size(min = 0, max = 10)
    @Valid
    private List<Department> departmentList;

    @Length(message = "Company CEO is not valid", min = 10, groups = {AddCompany.class})
    private String ceo;

    public Company() {
        super();
    }

    public Company(int id, String name, Date establishTime, List<Department> departmentList, String ceo) {
        super();
        this.setId(id);
        this.name = name;
        this.establishTime = establishTime;
        this.departmentList = departmentList;
        this.ceo = ceo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public Date getEstablishTime() {
        return establishTime;
    }

    public void setEstablishTime(Date establishTime) {
        this.establishTime = establishTime;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", this.getId())
                .append("name", name).append("establishTime", establishTime)
                .append("departmentList", departmentList).toString();
    }

    public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = ceo;
    }
}
