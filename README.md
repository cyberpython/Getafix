# Getafix

## Description

A very simple Java application that reads a text file (K12 format) line-by-line, parses packets and transmits their contents as UDP packets.

Input files should be valid K12 files - each packet should be in the form:

    +---------+---------------+----------+
    HH:MM:SS,mmm,nnn   ETHER
    |0   |BB|BB|.........|BB|

where

* `+---------+---------------+----------+` is the packet delimeter
* HH = hours in 24h format, MM = minutes, SS = seconds, mmm  = milliseconds, nnn = thousands of milliseconds
* BB = byte value in hexadecimal format
* Only 2 digit hex values are considered valid byte values(e.g. 3A or c0). All other values are ignored.

You can create files to use as input for Getafix with Wireshark. Just use Wireshark to capture the packets and save the result as a K12 text file.

![Input file selection and transmission options](http://dl.dropbox.com/u/599926/images/screenshot-getafix-01.png)
![Transmission progress window](http://dl.dropbox.com/u/599926/images/screenshot-getafix-02.png)

## Build instructions

    cd <path_where_the_code_is>
    ant clean jar

## Usage

To run the graphical user interface, double click on the JAR file under Windows or Linux. Another way is to run the application with no arguments:

    java -jar Getafix.jar

To run it using the CLI open a terminal and give:

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
