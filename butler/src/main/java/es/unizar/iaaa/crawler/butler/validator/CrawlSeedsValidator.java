/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.unizar.iaaa.crawler.butler.model.CrawlConfiguration;

/**
 * Validates if the Crawl seeds configuration  is well formed
 * in this case it's well formed if they are urls
 */
public class CrawlSeedsValidator implements Validator {

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

    @Override
    public ValidationResult validate(CrawlConfiguration config) {
        if (config.getCrawlSystem().getSeeds() == null || config.getCrawlSystem().getSeeds().size() == 0)
            return new LatestValidationResult(Status.ERROR_UNSUPPORTED_CRAWL_SEEDS, null);

        boolean correcto = true;
        String badSeed = "";
        for (int i = 0; i < config.getCrawlSystem().getSeeds().size(); i++) {
            String seed = config.getCrawlSystem().getSeeds().get(i);
            correcto &= isUrl(seed);
            if (!isUrl(seed)) badSeed = seed;
        }
        if (correcto) {
            return new LatestValidationResult();
        } else {
            return new LatestValidationResult(Status.ERROR_UNSUPPORTED_CRAWL_SEEDS, badSeed);
        }

    }
}
