package com.baidu.unbiz.fluentvalidator.support;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.baidu.unbiz.fluentvalidator.util.LocaleUtil;
import com.baidu.unbiz.fluentvalidator.util.Preconditions;

/**
 * 国际化使用的错误消息辅助类
 * <p/>
 * 使用{@link MessageSource}来作为delegate，利用Spring容器加载<code>ResourceBundle</code>，进行语言和国家地区的错误消息支持。
 * <p/>
 * 该类的使用方法如下：
 * <p/>
 * 1）新建一个properties文件，例如文件名为error-message.properties，内容为：
 * <pre>
 * car.size.exceed=car size exceeds
 * </pre>
 * 新建中文国际化文件error-message_zh_CN.properties，内容为：
 * <pre>
 *     car.size.exceed=\u6c7d\u8f66\u6570\u91cf\u8d85\u9650
 * </pre>
 * <p/>
 * 2）在{@link com.baidu.unbiz.fluentvalidator.Validator}中的方法可以通过如下API调用获取错误消息：
 * <pre>
 *     MessageSupport.getText("car.size.exceed");
 * </pre>
 * <p/>
 * 3）在Spring的XML配置中加入如下配置即可：
 * <pre>
 * &lt;bean id="messageSource"
 * class="org.springframework.context.support.ResourceBundleMessageSource"&gt;
 * &lt;property name="basenames"&gt;
 * &lt;list&gt;
 * &lt;value&gt;error-message&lt;/value&gt;
 * &lt;/list&gt;
 * &lt;/property&gt;
 * &lt;/bean&gt;
 *
 * &lt;bean id="messageSupport"
 * class="com.baidu.unbiz.fluentvalidator.support.MessageSupport"&gt;
 * &lt;property name="messageSource" ref="messageSource"/&gt;
 * &lt;/bean&gt;
 * </pre>
 *
 * @author zhangxu
 */
public class MessageSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSupport.class);

    /**
     * Spring delegate resource bundle
     */
    private static MessageSource messageSource;

    /**
     * 语言地区设置
     */
    private static String locale;

    /**
     * 语言地区缓存
     */
    private static Locale localeContext;

    /**
     * 如果在Spring容器中初始化，则打印一条消息
     */
    @PostConstruct
    public void prepare() {
        Preconditions.checkNotNull(messageSource, "MessageSource should not be null");
        LOGGER.info(this.getClass().getSimpleName() + " has been initialized properly");
        localeContext = LocaleUtil.parseLocale(locale);
    }

    /**
     * 获取国际化消息
     *
     * @param code 消息key
     *
     * @return 消息
     */
    public static String getText(String code) {
        return getText(code, null);
    }

    /**
     * 获取国际化消息
     *
     * @param code 消息key
     * @param args 参数列表
     *
     * @return 消息
     */
    public static String getText(String code, Object... args) {
        return getText(code, args, localeContext);
    }

    /**
     * 获取国际化消息
     *
     * @param code   消息key
     * @param args   参数列表
     * @param locale 语言地区
     *
     * @return 消息
     */
    public static String getText(String code, Object[] args, Locale locale) {
        Preconditions.checkState(messageSource != null,
                "If i18n is enabled, please make sure MessageSource is properly set as a member in "
                        + "MessageSupport");
        return messageSource.getMessage(code, args, locale);
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

}
