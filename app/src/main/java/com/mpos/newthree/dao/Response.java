/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpos.newthree.dao;

public class Response {
    String[] res = new String[100];

    public Response(){
        //res[00] = "Transaction completed successfully";
        res[00] = "Completed Successfully";
        res[01] = "Refer to card issuer";
        res[02] = "Refer to card issuer’s special conditions";
        res[03] = "Invalid merchant";
        res[04] = "Pick-up P POS";
        res[05] = "Do not honour";
        res[07] = "Pick-up, Special condition";
        res[Integer.parseInt("08")] = "Honour with identification";
        res[Integer.parseInt("09")] = "Request in progress";
        res[10] = "Approved for partial amount";
        res[11] = "Approved (VIP)";
        res[12] = "Invalid transaction";
        res[13] = "Invalid Amount";
        res[14] = "Invalid Card Number (No such number)";
        res[16] = "Approved, update track 3";
        res[17] = "Customer cancellation";
        res[19] = "Re-enter transaction";
        res[20] = "Invalid response";
        res[21] = "No action taken";
        res[22] = "Suspected malfunction";
        res[23] = "Unacceptable transaction fee";
        res[24] = "File update not suported by receiver - FILE";
        res[25] = "Unable to locate record on file - FILE";
        res[33] = "Expired card";
        res[34] = "Suspected fraud";
        res[35] = "Card acceptor contact acquirer";
        res[36] = "Restricted card";
        res[37] = "Card acceptor call acquirer security";
        res[38] = "Allowable PIN retries succeeded";
        res[39] = "No credit account";
        res[40] = "Requested function not supported";
        res[41] = "Lost card";
        res[42] = "No universal account";
        res[43] = "Stolen card";
        res[44] = "No investment account";
        res[51] = "Not sufficient funds";
        res[52] = "No chequing account";
        res[53] = "No savings account";
        res[54] = "Expired card";
        res[55] = "Incorrect PIN";
        res[56] = "No card record";
        res[57] = "Transaction not permitted to cardholder";
        res[58] = "Transaction not permitted to terminal";
        res[59] = "Suspected fraud";
        res[60] = "Card acceptor contact acquirer";
        res[61] = "Exceeds withdrawal amount limit";
        res[62] = "Restricted card";
        res[63] = "security violation";
        res[64] = "Original amount incorrect";
        res[65] = "Exceeds withdrawal frquency limit";
        res[66] = "Card acceptor call acquirer’s security";
        res[67] = "Hard capture(by ATM)";
        res[68] = "Response received too late";
        res[75] = "Allowable number of PINs exceeded";
        res[91] = "Issuer or switch is inoperative";
        res[93] = "Transaction cannot be completed, violation of law";
        res[94] = "Duplicate transaction";
        res[96] = "System Malfunction";
    }

    public String getResponse(int index){
        return res[index];
    }

}
