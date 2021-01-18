package com.mpos.newthree.obj;

import java.io.Serializable;

/**
 * Created by HP on 17/11/2017.
 */

public class LoginResponse implements Serializable {
    String status,message,account_bal, token,pin;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getAccount_bal() {
        return account_bal;
    }

    public String getToken() {
        return token;
    }

    public String getPin() {
        return pin;
    }
}
