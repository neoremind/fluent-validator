package com.baidu.unbiz.fluentvalidator;

@NotThreadSafe
public class FluentValidator {

    private ValidatorElementList validatorElementList = new ValidatorElementList();

    private boolean isFailFast = true;

    private ValidatorContext context = new ValidatorContext();

    private int lastAddCount = 0;

    public FluentValidator putAttribute2Context(String key, Object value) {
        if (context == null) {
            context = new ValidatorContext();
        }
        context.setAttribute(key, value);
        return this;
    }

    public FluentValidator putClosure2Context(String key, Closure value) {
        if (context == null) {
            context = new ValidatorContext();
        }
        context.setClosure(key, value);
        return this;
    }

    private FluentValidator() {
    }

    public static FluentValidator checkAll() {
        return new FluentValidator();
    }

    public FluentValidator failFast() {
        this.isFailFast = true;
        return this;
    }

    public FluentValidator failOver() {
        this.isFailFast = false;
        return this;
    }

    public <T> FluentValidator on(T t, Validator<T> v) {
        validatorElementList.getList().add(new ValidatorElement(t, v));
        lastAddCount = 1;
        return this;
    }

    public <T> FluentValidator on(T t, ValidatorChain chain) {
        if (chain.getValidators() == null || chain.getValidators().isEmpty()) {
            lastAddCount = 0;
            return this;
        }
        for (Validator v : chain.getValidators()) {
            validatorElementList.getList().add(new ValidatorElement(t, v));
        }
        lastAddCount = chain.getValidators().size();
        return this;
    }

    public FluentValidator when(boolean expression) {
        if (!expression) {
            for (int i = 0; i < lastAddCount; i++) {
                validatorElementList.getList().removeLast();
            }
        }
        return this;
    }

    ValidateCallback defaultCb = new DefaulValidateCallback();

    public Result doValidate() {
        return doValidate(defaultCb);
    }

    public Result doValidate(ValidateCallback cb) {
        Result ret = new Result();
        if (validatorElementList.isEmpty()) {
            return ret;
        }
        context.setResult(ret);

        for (ValidatorElement element : validatorElementList.getList()) {
            Object target = element.getTarget();
            Validator v = element.getValidator();
            try {
                if (v.accept(context, target)) {
                    if (!v.validate(context, target)) {
                        if (isFailFast) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                v.onException(e, context, target);
                try {
                    cb.onUncaughtException(v, e, target);
                } catch (Throwable t) {
                    throw new RuntimeValidateException(t);
                }
                throw new RuntimeValidateException(e);
            }
        }

        if (ret.hasNoError()) {
            cb.onSuccess(validatorElementList);
        } else {
            cb.onFail(validatorElementList, ret.getErrorMsgs());
        }
        return ret;
    }
}