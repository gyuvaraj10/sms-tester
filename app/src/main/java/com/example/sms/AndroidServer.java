package com.example.sms;

import java.io.IOException;
import fi.iki.elonen.NanoHTTPD;

public class AndroidServer extends NanoHTTPD {

    public AndroidServer() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse("Hello World");
    }
}
