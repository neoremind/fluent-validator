package com.baidu.unbiz.fluentvalidator;

/**
 * 验证结果收集器
 * <p/>
 * 在<code>FluentValidator.on(..).on(..).doValidate()</code>这一连串“<a href="https://en.wikipedia
 * .org/wiki/Lazy_evaluation">惰性求值</a>”计算后的“及时求值”收殓出口，
 * 支持自定义的对外结果数据结构，泛型&lt;T&gt;代表结果类型
 * <p/>
 * 其思路类似于Java8中的<code>java.util.stream.Collector</code>，用于结合框架操作后的结果生成。
 * <pre>
 *     List<String> res = Stream.of("abc", "xyz", "hh").map(str -> str.toUpperCase()).collect(toList());
 * </pre>
 *
 * @author zhangxu
 */
public interface ResultCollector<T> {

    /**
     * 转换为对外结果
     *
     * @param result 框架内部验证结果
     *
     * @return 对外验证结果对象
     */
    T toResult(ValidationResult result);

}
