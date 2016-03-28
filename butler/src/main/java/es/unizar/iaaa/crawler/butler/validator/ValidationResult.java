/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.validator.Validator.Status;

// TODO @Iñigo Documentar como Javadoc (recuerda /**)
public interface ValidationResult {

    boolean isOk();

    Status getFirstErrorCode();

    Object getFirstErrorValue();

}
