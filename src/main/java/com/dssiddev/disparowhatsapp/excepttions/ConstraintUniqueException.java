package com.dssiddev.disparowhatsapp.excepttions;

public class ConstraintUniqueException extends Exception {

    private static final long serialVersionUID = -6823705923719082177L;

    public ConstraintUniqueException() {
        super();
    }

    public ConstraintUniqueException(String exception) {
        super(exception);
    }

}
