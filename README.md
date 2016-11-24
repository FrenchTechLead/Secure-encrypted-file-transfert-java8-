# secure file transfert program:
This is a mini-project for secure file transfert using java8.
Files are encrypted by DES after starting a session between the server and the client.

# Main classes: 
  - Client: src/client/Main.java
  - Server: src/server/Main.java

# Dependencies:
  - Apache commons-io-2.4.jar
  - Apache commons-lang3-3.4.jar
  - BouncyCastle bcprov-ext-jdk15on-153.jar

**Note:** all of theise jars are already included in this repository

# About: 
this is a very basic project about security in java in wich we use :
  - Asymetric encryption DES
  - Symetric encryption RSA
  - Autosigned Certificates

# Possible improvements:
  - Implementing a keyStore and a certification authority to store certificates
  - Cutting very large files before sending them to prevent memory overflow exception
  - Showing the transfert progress
