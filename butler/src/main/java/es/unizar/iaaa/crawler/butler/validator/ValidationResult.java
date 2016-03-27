/**
 * Autor: Iñigo Alonso Ruiz
 * Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.validator.Validator.Status;

public interface ValidationResult {

    boolean isOk();

    Status getFirstErrorCode();

    Object getFirstErrorValue();

}
