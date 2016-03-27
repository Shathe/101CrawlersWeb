package es.unizar.iaaa.crawler.butler.commands;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyBannerProvider extends DefaultBannerProvider {

    public String getBanner() {
        return new StringBuilder().
                append("=======================================").append(OsUtils.LINE_SEPARATOR).
                append("*                                     *").append(OsUtils.LINE_SEPARATOR).
                append("*            101Crawlers              *").append(OsUtils.LINE_SEPARATOR).
                append("*                                     *").append(OsUtils.LINE_SEPARATOR).
                append("=======================================").append(OsUtils.LINE_SEPARATOR).
                append("Version:").append(this.getVersion()).toString();
    }

    public String getVersion() {
        return "0.0.1";
    }

    public String getWelcomeMessage() {
        return "Welcome to 101Crawlers CLI";
    }

    @Override
    public String getProviderName() {
        return "101Crawlers Banner";
    }
}