package com.baidu.unbiz.fluentvalidator.dto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CompanyBuilder {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Company buildSimple() {
        try {
            Department department101 = new Department(101, "R&D");
            Department department102 = new Department(102, "程序化广告交易工程平台技术部");
            Department department103 = new Department(103, "市场部");
            Department department104 = new Department(104, "Sales");
            List<Department> departmentList1 = new ArrayList<Department>();
            departmentList1.add(department101);
            departmentList1.add(department102);
            departmentList1.add(department103);
            departmentList1.add(department104);

            Company company = new Company(88, "百度时代网络技术有限公司", dateFormat.parse("2000-11-11"),
                    departmentList1, "Robin Li");
            return company;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Company> buildMulti() {
        try {
            List<Company> ret = new ArrayList<Company>();

            Department department101 = new Department(101, "R&D");
            Department department102 = new Department(102, "程序化广告交易工程平台技术部");
            Department department103 = new Department(103, "市场部");
            Department department104 = new Department(104, "Sales");
            List<Department> departmentList1 = new ArrayList<Department>();
            departmentList1.add(department101);
            departmentList1.add(department102);
            departmentList1.add(department103);
            departmentList1.add(department104);

            Department department201 = new Department(201, "行政部");
            Department department202 = new Department(202, "SaaS云");
            List<Department> departmentList2 = new ArrayList<Department>();
            departmentList2.add(department201);
            departmentList2.add(department202);

            Company company1 = new Company(88, "百度时代网络技术有限公司", dateFormat.parse("2000-11-11"),
                    departmentList1, "Robin Li");
            Company company2 = new Company(99, "IBM China", dateFormat.parse("1956-5-6"),
                    departmentList2, "Tom");

            ret.add(company1);
            ret.add(company2);

            return ret;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
