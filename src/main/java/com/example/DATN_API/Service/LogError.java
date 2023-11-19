package com.example.DATN_API.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
public  class LogError {
    public static void saveToLog(Exception e) {
        try {
            FileWriter New_File = new FileWriter("Error-log.txt", true);
            BufferedWriter Buff_File = new BufferedWriter(New_File);
            PrintWriter Print_File = new PrintWriter(Buff_File, true);
            e.printStackTrace(Print_File);
        }
        catch (Exception ie) {
            throw new RuntimeException("Cannot write the Exception to file", ie);
        }
    }
}



