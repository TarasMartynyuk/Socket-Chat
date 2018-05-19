package Calc;

import java.io.Serializable;

public class CalcImpl implements Calculable<Integer>, Serializable {

    @Override
    public Integer calc() {
        return 42;
    }
}
