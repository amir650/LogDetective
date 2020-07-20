package com.amir.analyzer;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

public class BasicAuthenticator
    extends Authenticator {

    private final URL url;
    private final String username;
    private final String password;

    BasicAuthenticator(final URL url, final String username, final String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        Authenticator.setDefault(this);
    }

    URLConnection establishConnection() {

        URLConnection connection = null;

        try {
            connection = this.url.openConnection();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }

        return connection;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        // Return the information
        return new PasswordAuthentication(this.username, this.password.toCharArray());
    }
}