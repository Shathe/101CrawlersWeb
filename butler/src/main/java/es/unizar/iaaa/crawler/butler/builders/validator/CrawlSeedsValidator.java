package es.unizar.iaaa.crawler.butler.builders.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.unizar.iaaa.crawler.butler.yalm.Configuration;

public class CrawlSeedsValidator implements Validator {

	@Override
	public ValidationResult validate(Configuration config) {
		if (config.getCrawlSystem().getSeeds()==null || config.getCrawlSystem().getSeeds().size() == 0) 
			return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_SEEDS,
					"There's no seed in the configuration file.");
			
		boolean correcto = true;
		String badSeed = "";
		for (int i = 0; i < config.getCrawlSystem().getSeeds().size(); i++) {
			String seed = config.getCrawlSystem().getSeeds().get(i);
			correcto &= isUrl(seed);
			if(!isUrl(seed))badSeed=seed;
		}
		if (correcto) {
			return new LatestValidationResult();
		}
		else {
			return new LatestValidationResult(Validator.ErroresValidar.ERROR_UNSUPPORTED_CRAWL_SEEDS, badSeed);
		}

	}

	private static boolean isUrl(String s) {
		String regex = "^(https?://)?(([\\w!~*'().&=+$%-]+: )?[\\w!~*'().&=+$%-]+@)?(([0-9]{1,3}\\.){3}[0-9]{1,3}|([\\w!~*'()-]+\\.)*([\\w^-][\\w-]{0,61})?[\\w]\\.[a-z]{2,6})(:[0-9]{1,4})?((/*)|(/+[\\w!~*'().;?:@&=+$,%#-]+)+/*)$";

		try {
			Pattern patt = Pattern.compile(regex);
			Matcher matcher = patt.matcher(s);
			return matcher.matches();

		} catch (RuntimeException e) {
			return false;
		}
	}
}
