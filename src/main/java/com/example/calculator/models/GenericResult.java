package com.example.calculator.models;

import java.io.Serializable;

public class GenericResult implements Serializable {
    public Serializable result;
    public GenericResult(Serializable _result) {
        result = _result;
    }
}
