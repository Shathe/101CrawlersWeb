/**
 * Autor: Iñigo Alonso Ruiz Quality supervised by: F.J. Lopez Pellicer
 */

package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.validator.Validator.Status;

public class LatestValidationResult implements ValidationResult {

    /* Por defectos valores que indiquen que no hay error */
    private Status errorCode = Status.OK;
    private Object errorValue;

    public LatestValidationResult() {

    }

    public LatestValidationResult(Status errorCode, Object errorValue) {
        this.errorCode = errorCode;
        this.errorValue = errorValue;
    }

    @Override
    public boolean isOk() {
        return errorCode == Status.OK;
    }

    @Override
    public Status getFirstErrorCode() {
        return errorCode;
    }

    @Override
    public Object getFirstErrorValue() {
        return errorValue;
    }

}
