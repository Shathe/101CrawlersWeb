package es.unizar.iaaa.crawler.butler.commands;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyBannerProvider extends DefaultBannerProvider  {

	public String getBanner() {
		StringBuffer buf = new StringBuffer();
		buf.append("=======================================" + OsUtils.LINE_SEPARATOR);
		buf.append("*                                     *"+ OsUtils.LINE_SEPARATOR);
		buf.append("*            HelloWorld               *" +OsUtils.LINE_SEPARATOR);
		buf.append("*                                     *"+ OsUtils.LINE_SEPARATOR);
		buf.append("=======================================" + OsUtils.LINE_SEPARATOR);
		buf.append("Version:" + this.getVersion());
		return buf.toString();
	}

	public String getVersion() {
		return "1.2.3";
	}

	public String getWelcomeMessage() {
		return "Welcome to HelloWorld CLI";
	}
	
	@Override
	public String getProviderName() {
		return "Hello World Banner";
	}
}