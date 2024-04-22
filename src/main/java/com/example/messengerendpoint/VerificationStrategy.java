package com.example.messengerendpoint;

import java.io.IOException;

public interface VerificationStrategy {
    boolean isStrategy(String string) throws IOException;
    String getResponse();
}
