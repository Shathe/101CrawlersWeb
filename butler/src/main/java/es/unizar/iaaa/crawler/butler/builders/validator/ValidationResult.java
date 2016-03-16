package es.unizar.iaaa.crawler.butler.builders.validator;

import es.unizar.iaaa.crawler.butler.builders.validator.Validator.ErroresValidar;

public interface ValidationResult {

    boolean isOk();

    ErroresValidar getFirstErrorCode();

    Object getFirstErrorValue();

}
