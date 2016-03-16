package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.builders.validator.Validator.ErroresValidar;

public class LatestValidationResult implements ValidationResult {

    /* Por defectos valores que indiquen que no hay error */
    private ErroresValidar errorCode = Validator.ErroresValidar.OK;
    private Object errorValue;

    public LatestValidationResult() {

    }

    public LatestValidationResult(ErroresValidar errorCode, Object errorValue) {
        this.errorCode = errorCode;
        this.errorValue = errorValue;
    }

    @Override
    public boolean isOk() {
        return errorCode == Validator.ErroresValidar.OK;
    }

    @Override
    public ErroresValidar getFirstErrorCode() {
        return errorCode;
    }

    @Override
    public Object getFirstErrorValue() {
        return errorValue;
    }

}
