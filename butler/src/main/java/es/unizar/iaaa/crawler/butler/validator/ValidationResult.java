package es.unizar.iaaa.crawler.butler.validator;

import es.unizar.iaaa.crawler.butler.validator.Validator.ErroresValidar;

public interface ValidationResult {

    boolean isOk();

    ErroresValidar getFirstErrorCode();

    Object getFirstErrorValue();

}
