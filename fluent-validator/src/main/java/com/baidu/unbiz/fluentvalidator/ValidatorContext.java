package com.baidu.unbiz.fluentvalidator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxu
 */
public class ValidatorContext {

    private Map<String, Object> attributes;

    private Map<String, Closure> closures;

    public Result result;

    public Object getAttribute(String key) {
        if (attributes != null && !attributes.isEmpty()) {
            return attributes.get(key);
        }
        return null;
    }

    public <T> T getAttribute(String key, Class<T> clazz) {
        return (T) getAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        if (attributes == null) {
            attributes = new HashMap<String, Object>(1 << 3);
        }
        attributes.put(key, value);
    }

    public void addErrorMsg(String msg) {
        result.addErrorMsg(msg);
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Closure getClosure(String key) {
        if (closures != null && !closures.isEmpty()) {
            return closures.get(key);
        }
        return null;
    }

    public void setClosure(String key, Closure closure) {
        if (closures == null) {
            closures = new HashMap<String, Closure>(1 << 3);
        }
        closures.put(key, closure);
    }

}
