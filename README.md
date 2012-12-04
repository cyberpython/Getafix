# UDPParseAndTx

## Description

A very simple Java application that reads a text file line-by-line, parses byte values in that line and transmits these bytes in a UDP packet.

Input file rules:

* Empty lines and lines starting with '+' are ignored.
* The byte delimeter is '|'.
* Only 2 digit hex values are considered valid (e.g. 3A or c0). All other values are ignored.

## Usage

    java -jar UDPParseAndTx <remote_ip_or_host_name> <port> <path_to_input_file>
    
Where:
* `<remote_ip_or_host_name>` is the IP address or host name of the receiver
* `<port>` is the target port of the receiver
* `<path_to_input_file>` is the path to the text file containing the byte values to be transmitted

## License

Copyright 2012 Georgios Migdos <cyberpython@gmail.com>
The source code is available under the terms of the MIT license.
