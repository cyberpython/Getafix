# Getafix

## Description

A very simple Java application that reads a text file (K12 format) line-by-line, parses byte values in that line and transmits these bytes in a UDP packet.

Input file rules:

* All lines that do not start with '|' (ignoring whitespace) are ignored.
* The byte delimeter is '|'.
* Only 2 digit hex values are considered valid byte values(e.g. 3A or c0). All other values are ignored.

You can create files to use as input for Getafix with Wireshark. Just use Wireshark to capture the packets and save the result as a K12 text file.

## Build instructions

### Unix & Unix-like systems

    cd <path where the source code is>
    ./build.sh

OR

    cd <path where the source code is>
    rm -f Getafix.jar
    mkdir tmp
    mkdir tmp/getafix
    javac src/getafix/*.java
    mv src/getafix/*.class tmp/getafix/
    cp Manifest.txt tmp/Manifest.txt
    cd tmp
    jar cfm Getafix.jar Manifest.txt getafix/*.class
    mv Getafix.jar ../
    cd ../
    rm -rf tmp

### Windows systems

Refer to the instructions for Unix systems above and use the equivalent Windows commands.

## Usage

    java -jar Getafix.jar <remote_ip_or_host_name> <port> <offset> <delay> <path_to_input_file>
    
Where:

* `<remote_ip_or_host_name>` is the IP address or host name of the receiver
* `<port>` is the target port of the receiver
* `<offset>` is the number of bytes to be ignored for each packet (e.g. to omit captured Ethernet, IP and UDP headers) - A typical offset for UDP packets captured on Ethernet is 42
* `<delay>` is the number of milliseconds to wait between packet transmissions
* `<path_to_input_file>` is the path to the text file containing the byte values to be transmitted

## License

Copyright 2012 Georgios Migdos <cyberpython@gmail.com>
The source code is available under the terms of the MIT license.
