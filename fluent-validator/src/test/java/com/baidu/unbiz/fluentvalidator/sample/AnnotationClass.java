/**
 *
 */
package com.baidu.unbiz.fluentvalidator.sample;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

public class AnnotationClass {

    private int x;
    @TestAnnotation(value = "y")
    private int y;

    private String z;
    @TestAnnotation(value = "d")
    private Date d;

    public int getX() {
        return x;
    }

    @TestAnnotation(value = "setX")
    public void setX(int x) {
        this.x = x;
    }

    @TestAnnotation(value = "getY")
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @TestAnnotation(value = "getZ")
    public String getZ() {
        return z;
    }

    @TestAnnotation(value = "setZ")
    public void setZ(String z) {
        this.z = z;
    }

    @TestAnnotation(value = "getD")
    public Date getD() {
        return d;
    }

    @TestAnnotation(value = "setD")
    public void setD(Date d) {
        this.d = d;
    }

    @TestAnnotation(value = "toString")
    public String toString() {
        return super.toString();
    }

    @TestAnnotation(value = "clone")
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
        String value();
    }

}
